package com.example.hospitalproject.dao;

import com.example.hospitalproject.domain.Hospital;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HospitalDao2 {

    private final JdbcTemplate jdbcTemplate;

    public void insert(Hospital hospital) {
        String query = "INSERT INTO `likelion-db`.`hospital` (id, name, status_code, phone, road_name_address," +
                "type, employees_cnt, has_inpatient_room, area)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);" ;  // 9개

        jdbcTemplate.update(query,
                hospital.getId(), hospital.getName(), hospital.getStatusCode(), hospital.getPhone(),
                hospital.getRoadNameAddress(), hospital.getType(), hospital.getEmployeesCnt(),
                hospital.getHasInpatientRoom(), hospital.getArea());
    }
}
