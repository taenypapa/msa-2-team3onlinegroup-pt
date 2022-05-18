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
#### CQRS Pattern
#### Correlation / Compensation(Unique Key)
#### Request / Response (Feign Client / Sync.Async)
#### Gateway
#### Deploy / Pipeline
#### Circuit Breaker
#### Autoscale(HPA)
#### Self-Healing(Liveness Probe)
#### Zero-Downtime Deploy(Readiness Probe)
#### Config Map / Persistence Volume
#### Polyglot
