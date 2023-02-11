package com.example.hospitalproject.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class HospitalJdbcServiceTest {

    @Autowired
    HospitalJdbcService hospitalJdbcService;

    @BeforeEach
    public void deleteAll() throws SQLException {
        hospitalJdbcService.deleteAll();
    }

    @Test
    @DisplayName("삽입 V1 테스트 => 111942개 데이터 삽입 성공 확인")
    void insertAllDataV1() throws SQLException, IOException {
        assertEquals(0, hospitalJdbcService.findAll().size());
        assertEquals(111942,
                hospitalJdbcService.insertAllDataV1("./extract_data/queryV1.sql", "./original_data/hospital_data.csv"));
    }

    @Test
    @DisplayName("삽입 V2 테스트 => 111942개 데이터 삽입 성공 확인")
    void insertAllDataV2() throws SQLException, IOException {
        assertEquals(0, hospitalJdbcService.findAll().size());
        hospitalJdbcService.insertAllDataV2("./extract_data/queryV1.sql", "./original_data/hospital_data.csv");
        assertEquals(111942, hospitalJdbcService.findAll().size());
    }

}