package com.example.hospitalproject.controller;

import com.example.hospitalproject.domain.entity.Hospital;
import com.example.hospitalproject.service.HospitalJdbcService;
import com.example.hospitalproject.service.HospitalJdbcTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/hospitals")
@RequiredArgsConstructor
public class HospitalRestController {

    private final HospitalJdbcService hospitalJdbcService;
    private final HospitalJdbcTemplateService hospitalJdbcTemplateService;

    /**
     * CSV 파일을 읽어와 한 줄 씩 쿼리로 변환 후 queryV1에 저장
     * JDBC를 사용하여 12만개의 삽입 쿼리 실행
     */
    @PostMapping("/jdbc/all/v1")
    public String insertAllV1() throws IOException, SQLException {
        int successCnt = hospitalJdbcService.insertAllDataV1(
                "./extract_data/queryV1.sql",
                "./original_data/hospital_data.csv");

        return successCnt + "개 데이터 삽입 성공";
    }

    /**
     * CSV 파일을 읽어와 한 줄 씩 쿼리로 변환 후 queryV2에 저장
     * JDBC를 사용하여 1개의 삽입 쿼리로 12만개 데이터 한번에 삽입
     */
    @PostMapping("/jdbc/all/v2")
    public String insertAllV2() throws IOException, SQLException {
        hospitalJdbcService.insertAllDataV2(
                "./extract_data/queryV2.sql",
                "./original_data/hospital_data.csv");

        return "전체 데이터 삽입 성공";
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


    /**
     * CSV 파일을 읽어와 한 줄 씩 파싱 후 JDBC Template을 사용하여 하나씩 전체 데이터 삽입
     */
    @PostMapping("/jdbc-template/all")
    public String insertAllByJdbcTemplate() throws IOException {
        int successCnt = hospitalJdbcTemplateService.insertAll("./original_data/hospital_data.csv");
        return successCnt + "개 데이터 삽입 성공";
    }

    /**
     * CSV 파일을 읽어와 한 줄 씩 파싱 후 JDBC Template을 사용하여 한번에 전체 데이터 삽입
     */
    @PostMapping("/jdbc-template/all/v2")
    public String insertAllByJdbcTemplateV2() throws IOException {
        int successCnt = hospitalJdbcTemplateService.batchInsertAll("./original_data/hospital_data.csv");
        return successCnt + "개 데이터 삽입 성공";
    }

    /**
     * JDBC Template을 사용하여 전체 병원 리스트 조회
     */
    @GetMapping("/jdbc-template/all")
    public String getAllByJdbcTemplate() {
        List<Hospital> hospitals = hospitalJdbcTemplateService.findAll();
        return hospitals.size() + "개 조회 성공";
    }

    /**
     * JDBC Template을 사용하여 조건에 맞는(주소로 검색) 병원 조회
     */
    @GetMapping("/jdbc-template/{keyword}")
    public String findByAddressByJdbcTemplate(@PathVariable String keyword) throws SQLException {
        List<Hospital> hospitals = hospitalJdbcTemplateService.findByAddress(keyword);
        return "주소에 '" + keyword + "'이(가) 포함된 병원 " + hospitals.size() + "개 조회 성공";
    }

    /**
     * JDBC Template을 사용하여 전체 삭제
     */
    @DeleteMapping("/jdbc-template/all")
    public String deleteAllByJdbcTemplate() throws SQLException {
        hospitalJdbcTemplateService.deleteAll();
        return "전체 삭제 성공";
    }
}
