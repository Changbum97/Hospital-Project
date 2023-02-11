package com.example.hospitalproject.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class HospitalJdbcTemplateServiceTest {

    @Autowired
    HospitalJdbcTemplateService hospitalJdbcTemplateService;

    @BeforeEach
    public void deleteAll() {
        hospitalJdbcTemplateService.deleteAll();
    }

    @Test
    @DisplayName("삽입 테스트 => 111942개 데이터 삽입 성공 확인")
    void insertAll() throws IOException {
        assertEquals(0, hospitalJdbcTemplateService.findAll().size());
        assertEquals(111942, hospitalJdbcTemplateService.insertAll("./original_data/hospital_data.csv"));
    }
}