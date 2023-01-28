package com.example.hospitalproject.dao;

import com.example.hospitalproject.domain.Hospital;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class HospitalDao2 {

    private final JdbcTemplate jdbcTemplate;

    public void insertOne(Hospital hospital) {
        String query = "INSERT INTO `likelion-db`.`hospital` (id, name, status_code, phone, road_name_address," +
                "type, employees_cnt, has_inpatient_room, area)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);" ;  // 9개 컬럼

        jdbcTemplate.update(query,
                hospital.getId(), hospital.getName(), hospital.getStatusCode(), hospital.getPhone(),
                hospital.getRoadNameAddress(), hospital.getType(), hospital.getEmployeesCnt(),
                hospital.getHasInpatientRoom(), hospital.getArea());
    }

    public List<Hospital> findAll() {
        String query = "SELECT * FROM `likelion-db`.`hospital`;";
        List<Hospital> hospitals = jdbcTemplate.query(query, hospitalMapper);
        return hospitals;
    }

    public List<Hospital> findByAddress(String keyword) {
        String query = "SELECT * FROM `likelion-db`.`hospital` WHERE road_name_address LIKE ?;";
        List<Hospital> hospitals = jdbcTemplate.query(query, hospitalMapper, '%'+keyword+'%');
        return hospitals;

    }

    public void deleteAll() {
        String query = "DELETE FROM `likelion-db`.`hospital`";
        jdbcTemplate.update(query);
    }

    private static RowMapper<Hospital> hospitalMapper = ((rs, rowNum) ->
            Hospital.builder()
                    .id(rs.getLong("id"))
                    .name(rs.getString("name"))
                    .statusCode(rs.getInt("status_code"))
                    .phone(rs.getString("phone"))
                    .roadNameAddress(rs.getString("road_name_address"))
                    .type(rs.getString("type"))
                    .employeesCnt(rs.getInt("employees_cnt"))
                    .hasInpatientRoom(rs.getBoolean("has_inpatient_room"))
                    .area(rs.getDouble("area"))
                    .build());
}
