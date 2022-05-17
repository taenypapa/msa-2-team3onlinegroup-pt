package hw.systems.msa2.team3.group.pt.reservation.openfeign.member;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
