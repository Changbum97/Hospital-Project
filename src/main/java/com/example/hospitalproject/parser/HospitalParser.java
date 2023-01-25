package com.example.hospitalproject.parser;

import com.example.hospitalproject.domain.Hospital;
import org.springframework.stereotype.Component;

import javax.persistence.Id;

@Component
public class HospitalParser {

    public Hospital parse(String input) {

        // CSV에서는 값이 있으면 ""로 감싸고, 값이 없으면 ""가 없기 때문에 맞춰주는 작업
        while(input.contains(",,")) {
            input = input.replace(",,", ",\"\",");
        }
        // 첫 번째 값과 마지막 값 형식 맞춰주는 작업
        input = "\"," + input + "\"";

        String[] split = input.split("\",\"");

        return Hospital.builder()
                .id(Long.valueOf(split[1]))             // 번호 (PK)
                .name(split[22])                        // 사업장 명
                .statusCode(Integer.valueOf(split[10]))     // 상세 영업 코드
                .phone(split[16])                       // 소재지 전화
                .roadNameAddress(split[20])             // 도로명 주소
                .type(split[26])                        // 업태 구분 명
                .employeesCnt(Integer.valueOf(split[30]))       // 의료인 수
                .hasInpatientRoom( (split[32].equals("0")) ? false : true )
                // 입원실이 0개이면 false, 0이 아니면 true
                .area(Double.valueOf(split[33]))        // 총 면적
                .build();
    }
}
