package com.example.hospitalproject.controller;

import com.example.hospitalproject.domain.ExtractDto;
import com.example.hospitalproject.service.HospitalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/hospitals")
@RequiredArgsConstructor
public class HospitalController {

    private final HospitalService hospitalService;

    @GetMapping("/all")
    public String getAll() {
        return "hospitals/list";
    }

    /**
     * JPA를 사용하여 전체 데이터 하나씩 파싱 후 삽입 => 12만개 30초
     */
    @ResponseBody
    @PostMapping("/jpa/all")
    public String insertAllByJpa() {
        try {
            long startTime = System.currentTimeMillis();
            int successCnt = hospitalService.insertAllData("./original_data/hospital_data.csv");
            long endTime = System.currentTimeMillis();

            log.info("JPA를 사용한 데이터 파싱 + 삽입 시간 : {}초", (endTime - startTime) / 1000.0);
            return successCnt + "개 데이터 삽입 성공";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 삽입된 데이터에서 statusCode, region, type 추출 결과
     * statusCode(상세 영업 코드) => 13(영업중), 2(휴업), 3(폐업), 24(직권폐업)
     * type(업태구분명) => 의원, 치과의원, 보건의료원, 보건진료소, 보건소, 조산원, 보건지소, 한의원
     * region(지역, 17개) => 서울특별시, 대전광역시, 울산광역시, 광주광역시, 부산광역시, 대구광역시, 인천광역시, 세종특별자치시
     *                경기도, 전라남도, 전라북도, 경상남도, 경상북도, 충청남도, 충청북도, 제주특별자치도, 강원도
     */
    @ResponseBody
    @GetMapping("/extract")
    public ExtractDto extract() {
        return hospitalService.extract();
    }

}
