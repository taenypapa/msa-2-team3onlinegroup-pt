# MSA 2차수 Capstone team3 - 그룹pt 예약 서비스
##### 팀원
- SharedService3팀 이성호 과장(팀장)
- 서비스운영2팀 박수지 사원

## 서비스 시나리오
1. 매니저가 회원을 생성/수정/삭제 한다.
2. 매니저가 수업을 생성/수정/삭제 한다.
3. 회원은 수업을 예약/예약취소 한다.
4. 수업의 예약 최대 인원이 모두 찼을때,
   - 예약시 해당 회원은 대기 예약 상대가 된다.
   - 예약된 인원이 취소할 경우 대기 예약된 회원이 예약이 된다.

## 체크포인트
SAGA Pattern
CQRS Pattern
Correlation / Compensation(Unique Key)
Request / Response (Feign Client / Sync.Async)
Gateway
Deploy / Pipeline
Circuit Breaker
Autoscale(HPA)
Self-Healing(Liveness Probe)
Zero-Downtime Deploy(Readiness Probe)
Config Map / Persistence Volume
Polyglot

## Event Storming 결과
![원본](https://user-images.githubusercontent.com/95392386/168939002-aa5430c2-2119-4660-bc35-8f118619061f.PNG)
```
1. 매니저가 회원을 생성/수정/삭제 한다.(ok)
2. 매니저가 수업을 생성/수정/삭제 한다.(ok)
3. 회원은 수업을 예약/예약취소 한다.(ok)
4. 수업의 예약 최대 인원이 모두 찼을때,
   - 예약시 해당 회원은 대기 예약 상대가 된다.(ok)
   - 예약된 인원이 취소할 경우 대기 예약된 회원이 예약이 된다.(ok)
```

## 구현
#### SAGA Pattern
1. producer: sendAddReservation(최초 요청일 경우 현재 예약 수를 확인해서 성공 또는 대기 처리 및 예약 수 1 증가 , 취소 요청 시 현재 예약 수 1 감소)
```java
@KafkaListener(id = "updatedReservationListener", topics = "UPDATED-RESERVATION", containerFactory = "kafkaListenerContainerFactory")
    public void updatedReservationListener(String in, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                     @Header(KafkaHeaders.OFFSET) long offset) {
    	
    	ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    	
    	try {
            ReservationEntity result = objectMapper.readValue(in, ReservationEntity.class);

            if(result != null) {
                Optional<MyclassEntity> optionalMyclassEntity = myclassReadService.findMyclass(result.getMyclassId());
                if(optionalMyclassEntity.isPresent()) {
                	MyclassEntity myclassEntity = optionalMyclassEntity.get();
                	int maxCnt = myclassEntity.getMaxCnt();
                    int reserveCnt = myclassEntity.getReserveCnt();
                    
                    if(result.getStatus().equals(ReservationStatus.REQUESTED) || result.getStatus().equals(ReservationStatus.WAITED)) {
                    	if(reserveCnt < maxCnt) {
                        	myclassEntity.setReserveCnt(reserveCnt+1);
                        	result.setStatus(ReservationStatus.DONE);
                        }else {
                        	result.setStatus(ReservationStatus.WAITED);
                        }
                	}
                	else if(result.getStatus().equals(ReservationStatus.CANCELED)){
                		myclassEntity.setReserveCnt(reserveCnt-1);
                	} else {
                        result.setStatus(ReservationStatus.WARN);
                    }
                    
                    myclassService.save(myclassEntity);

                    producerService.sendCallbackReservation(result);
                }

            }

        } catch (Exception e) {
            log.error("callBackUpdatedClassListener error: {}", e);
        }
        

        log.info(" #### Received: {} from {} @ {} ",in, topic, offset);
    }
```

3. 결과 callback: callBackUpdatedClassListener(예약 상태 업데이트: 대기/성공, 취소 시 대기자가 있으면 대기자 1명 다시 예약 요청)
```java
@KafkaListener(id = "callBackUpdatedClassListener", topics = "CALL-BACK-UPDATED-CLASS", containerFactory = "kafkaListenerContainerFactory")
    public void callBackUpdatedClassListener(String in, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                     @Header(KafkaHeaders.OFFSET) long offset) {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        try {
            ReservationEntity result = objectMapper.readValue(in, ReservationEntity.class);

            if(result != null) {
                Optional<ReservationEntity> optionalReservationEntity = reservationReadService.findReservation(result.getId());
                if(optionalReservationEntity.isPresent()) {
                    ReservationEntity reservationEntity = optionalReservationEntity.get();
                    reservationEntity.setStatus(result.getStatus());

                    if(!ReservationStatus.CANCELED.equals(result.getStatus())
                            && !ReservationStatus.REQUESTED.equals(result.getStatus())) {
                        reservationService.save(reservationEntity);
                    } else if(ReservationStatus.CANCELED.equals(result.getStatus())) {
                        Optional<ReservationEntity> addReservationEntity = reservationReadService.findByMyclassIdAndStatus(result.getMyclassId(), ReservationStatus.WAITED);

                        /** 대기자가 있으면 수강신청 */
                        if(addReservationEntity.isPresent()) {
                            producerService.sendAddReservation(addReservationEntity.get());
                        }
                    }
                }
            }

        } catch (Exception e) {
            log.error("callBackUpdatedClassListener error: {}", e);
        }
        log.info(" #### Received: {} from {} @ {} ",in, topic, offset);
    }
```
#### CQRS Pattern
ReadService 분리

```
@Service
@AllArgsConstructor
@Slf4j
public class ReservationReadService {
    private ReservationRepository reservationRepository;

    public Optional<ReservationEntity> findByMyclassIdAndStatus(Long myclassId, ReservationStatus status){
        return reservationRepository.findFirstByMyclassIdAndStatusOrderByRegisteredAt(myclassId, status);
    }

    public Optional<ReservationEntity> findByMemberIdAndMyclassId(Long memberId, Long myclassId){
        return reservationRepository.findByMemberIdAndMyclassId(memberId, myclassId);
    }


    //@Cacheable("reservation")
    public List<ReservationEntity> findAll(){
        return reservationRepository.findAll();
    }

    //@Cacheable(value = "reservation", key = "#myclassId")
    public List<ReservationEntity> findByMyclassId(Long myclassId){
        return reservationRepository.findByMyclassId(myclassId);
    }

    //@Cacheable(value = "reservation", key = "#memberId")
    public List<ReservationEntity> findByMemberId(Long memberId){
        return reservationRepository.findByMemberId(memberId);
    }

    //@Cacheable(value = "reservation", key = "#id")
    public Optional<ReservationEntity> findReservation(Long id){
        return reservationRepository.findById(id);
    }
}

```

#### Correlation / Compensation(Unique Key)
#### Request / Response (Feign Client / Sync.Async)

예약 시 회원 정보 유효성 검증
```java
@FeignClient(name = "memberOpenfeignClient", url = "http://localhost:8080", configuration = MemberOpenfeignConfig.class
        , fallbackFactory = MemberOpenfeignFallbackFactory.class)
public interface MemberOpenfeignClient {

    @RequestMapping(method = RequestMethod.GET, value = "/members/{id}")
    String getMember(@PathVariable("id") Long id);
}

@Slf4j
@Component
public class MemberOpenfeignFallbackFactory implements FallbackFactory<MemberOpenfeignClient> {
    @Override
    public MemberOpenfeignClient create(Throwable cause) {
        log.error(cause.getLocalizedMessage(), cause);
        return id -> "";
    }
}

@Service
@AllArgsConstructor
@Slf4j
public class MemberOpenfeignService {
    private final MemberOpenfeignClient memberOpenfeignClient;

    public MemberResponse getMember(Long id) {
        return getObject(memberOpenfeignClient.getMember(id));
    }

    private MemberResponse getObject(String response) {
        MemberResponse memberResponse = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JSONObject jsonObject = objectMapper.readValue(response, JSONObject.class);

            log.info("jsonObject2: {}", jsonObject.get("memberEntity"));

            ModelMapper modelMapper = new ModelMapper();
            memberResponse = modelMapper.map(jsonObject.get("memberEntity"), MemberResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return memberResponse;
    }
}

MemberResponse memberResponse = memberOpenfeignService.getMember(dto.getMemberId());

        if(memberResponse == null) {
            return ResponseEntity.badRequest().build();
        }
```

#### Gateway

ingress 추가
```
apiVersion: "extensions/v1beta1"
kind: "Ingress"
metadata:
  name: "group-pt-ingress"
  annotations:
    kubernetes.io/ingress.class: "nginx"
spec:
  rules:
    -
      http:
        paths:
          -
            path: /member
            pathType: Prefix
            backend:
              serviceName: group-pt-member
              servicePort: 8080
          -
            path: /myclass
            pathType: Prefix
            backend:
              serviceName: group-pt-myclass
              servicePort: 8081
          -
            path: /reservation
            pathType: Prefix
            backend:
              serviceName: group-pt-reservation
              servicePort: 8082
```

![image](https://user-images.githubusercontent.com/88877798/168971427-e8d969e5-60a7-4be3-8506-efc0a19bfa79.png)


#### Deploy / Pipeline

github action ci/cd
```
name: Docker Image CI

on:
  push:
    branches: [ master ]
  pull_request:
    branches: [ master ]

jobs:

  build:

    runs-on: ubuntu-latest

    steps:
    - uses: actions/checkout@v3
    - name: Set up JDK 8
      uses: actions/setup-java@v3
      with:
        java-version: '8'
        distribution: 'adopt'
      
    - name: Build with Maven
      run: mvn --batch-mode --update-snapshots package
      
    - name: login docker hub
      uses: docker/login-action@v1
      with:
        username: ${{ secrets.DOCKERHUB_USERNAME }}
        password: ${{ secrets.DOCKERHUB_TOKEN }}
        
    - name: Build with docker - member
      run: docker build . --file ./group-pt-member/Dockerfile --tag cylsh3452/group-pt-member:latest
        
    - name: Push with docker - member
      run: docker push cylsh3452/group-pt-member:latest
      
    - name: Build with docker - myclass
      run: docker build . --file ./group-pt-myclass/Dockerfile --tag cylsh3452/group-pt-myclass:latest
        
    - name: Push with docker - myclass
      run: docker push cylsh3452/group-pt-myclass:latest
      
    - name: Build with docker - reservation
      run: docker build . --file ./group-pt-reservation/Dockerfile --tag cylsh3452/group-pt-reservation:latest
        
    - name: Push with docker - reservation
      run: docker push cylsh3452/group-pt-reservation:latest
```
이미지 repository: docker hub

![image](https://user-images.githubusercontent.com/88877798/168972870-1f831835-5929-4059-83fd-0de83f887b8b.png)

eks 디플로이

![image](https://user-images.githubusercontent.com/88877798/168972937-241a8a53-fb2d-4781-9cfb-459ae1ed29d7.png)


#### Circuit Breaker
#### Autoscale(HPA)
#### Self-Healing(Liveness Probe)
#### Zero-Downtime Deploy(Readiness Probe)
#### Config Map / Persistence Volume
#### Polyglot
