package com.example.hospitalproject.controller;

import com.example.hospitalproject.dao.HospitalDao;
import com.example.hospitalproject.domain.ExtractDto;
import com.example.hospitalproject.domain.Hospital;
import com.example.hospitalproject.parser.MakeSqlFile;
import com.example.hospitalproject.service.HospitalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;

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

            log.info("JPA를 사용한 데이터 삽입 시간 : {}초", (endTime - startTime) / 1000.0);
            return successCnt + "개 데이터 삽입 성공";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ResponseBody
    @GetMapping("/extract")
    public ExtractDto extract() {
        return hospitalService.extract();
    }

    /**
     * CSV 파일을 읽어와 한 줄 씩 쿼리로 변환 => 12만개 1.8초
     * 12만개의 삽입 쿼리 실행 => 12만개 35초
     */
    @ResponseBody
    @PostMapping("/driver-manager-v1/all")
    public String insertAllByDriverMangerV1() throws IOException, SQLException {
        long startTime = System.currentTimeMillis();

        MakeSqlFile makeSqlFile = new MakeSqlFile("queryV1.sql");
        makeSqlFile.writeV1("./original_data/hospital_data.csv");

        long endTime = System.currentTimeMillis();
        log.info("쿼리 생성 시간 : {} 초", (endTime - startTime) / 1000.0);

        startTime = System.currentTimeMillis();

        HospitalDao hospitalDao = new HospitalDao();
        hospitalDao.insertV1("./extract_data/queryV1.sql");

        endTime = System.currentTimeMillis();
        log.info("쿼리 삽입 시간 : {}초", (endTime - startTime) / 1000.0);

        return "삽입 완료";
    }

    @ResponseBody
    @PostMapping("/driver-manager-v2/all")
    public String insertAllByDriverMangerV2() throws IOException, SQLException {
        long startTime = System.currentTimeMillis();

        MakeSqlFile makeSqlFile = new MakeSqlFile("queryV2.sql");
        makeSqlFile.writeV2("./original_data/hospital_data.csv");

        long endTime = System.currentTimeMillis();
        log.info("쿼리 생성 시간 : {}초", (endTime - startTime) / 1000.0);

        startTime = System.currentTimeMillis();

        HospitalDao hospitalDao = new HospitalDao();
        hospitalDao.insertV2("./extract_data/queryV2.sql");

        endTime = System.currentTimeMillis();
        log.info("쿼리 삽입 시간 : {}초", (endTime - startTime) / 1000.0);

        return "삽입 완료";
    }

}
