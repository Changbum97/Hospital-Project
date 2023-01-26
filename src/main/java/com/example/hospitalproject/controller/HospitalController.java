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
     * JPA를 사용하여 전체 데이터 파싱 후 삽입 => 12만개 30초
     */
    @ResponseBody
    @PostMapping("/all")
    public String insertAll() {
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
     * CSV 파일을 읽어와 한 줄 씩 직접 쿼리로 변환 => 12만개 1.5초
     */
    @ResponseBody
    @GetMapping("/make-queries-v1")
    public String makeQueriesV1() throws IOException {
        long startTime = System.currentTimeMillis();

        MakeSqlFile makeSqlFile = new MakeSqlFile("queryV1.sql");
        makeSqlFile.writeV1("./original_data/hospital_data.csv");

        long endTime = System.currentTimeMillis();

        log.info("쿼리 생성 시간 : {}초", (endTime - startTime) / 1000.0);

        return "쿼리 생성 완료";
    }

    /**
     * 위에서 만든 sql 파일을 한 줄 씩 읽고 쿼리를 실행시켜 DB에 데이터 삽입 => 12만개 35초
     */
    @ResponseBody
    @GetMapping("/insert-query")
    public String insertQuery() throws SQLException, IOException {
        long startTime = System.currentTimeMillis();

        HospitalDao hospitalDao = new HospitalDao();
        hospitalDao.insert("./extract_data/queryV1.sql");

        long endTime = System.currentTimeMillis();

        log.info("쿼리 입력 시간 : {}초", (endTime - startTime) / 1000.0);
        return "쿼리 입력 완료";
    }
}
