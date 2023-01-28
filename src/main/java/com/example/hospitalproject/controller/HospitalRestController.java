package com.example.hospitalproject.controller;

import com.example.hospitalproject.domain.Hospital;
import com.example.hospitalproject.service.HospitalJdbcService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/hospitals")
@RequiredArgsConstructor
public class HospitalRestController {

    private final HospitalJdbcService hospitalJdbcService;

    /**
     * CSV 파일을 읽어와 한 줄 씩 쿼리로 변환 => 12만개 1.8초
     * JDBC를 사용하여 12만개의 삽입 쿼리 실행 => 12만개 35초
     */
    @PostMapping("/jdbc/all/v1")
    public String insertAllV1() throws IOException, SQLException {

        int successCnt = hospitalJdbcService.insertAllDataV1(
                "./extract_data/queryV1.sql",
                "./original_data/hospital_data.csv");
        return successCnt + "개 데이터 삽입 성공";
    }

    /**
     * CSV 파일을 읽어와 한 줄 씩 쿼리로 변환 => 12만개 1.8초
     * JDBC를 사용하여 1개의 삽입 쿼리 실행으로 12만개 데이터 한번에 삽입 => 12만개 2.5초
     */
    @PostMapping("/jdbc/all/v2")
    public String insertAllV2() throws IOException, SQLException {
        hospitalJdbcService.insertAllDataV2(
                "./extract_data/queryV2.sql",
                "./original_data/hospital_data.csv");

        return "데이터 삽입 성공";
    }

    /**
     * JDBC를 사용하여 전체 병원 리스트 조회
     */
    @GetMapping("/jdbc/all")
    public String getAll() throws SQLException {
        List<Hospital> hospitals = hospitalJdbcService.findAll();
        return hospitals.size() + "개 조회 성공";
    }

    /**
     * JDBC를 사용하여 조건에 맞는(주소로 검색) 병원 조회
     */
    @GetMapping("/jdbc/{keyword}")
    public String findByAddress(@PathVariable String keyword) throws SQLException {
        List<Hospital> hospitals = hospitalJdbcService.findByAddress(keyword);
        return "주소에 '" + keyword + "'이(가) 포함된 병원 " + hospitals.size() + "개 조회 성공";
    }

    /**
     * JDBC를 사용하여 전체 삭제
     */
    @DeleteMapping("/jdbc/all")
    public String deleteAll() throws SQLException {
        hospitalJdbcService.deleteAll();
        return "전체 삭제 성공";
    }
}
