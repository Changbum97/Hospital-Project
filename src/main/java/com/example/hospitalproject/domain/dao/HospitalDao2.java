package com.example.hospitalproject.domain.dao;

import com.example.hospitalproject.domain.entity.Hospital;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class HospitalDao2 {

    private final JdbcTemplate jdbcTemplate;

    public void insertOne(Hospital hospital) {
        String query = "INSERT INTO `hospitals`.`hospital` (id, name, status_code, phone, road_name_address," +
                "type, employees_cnt, has_inpatient_room, area)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);" ;  // 9개 컬럼

        jdbcTemplate.update(query,
                hospital.getId(), hospital.getName(), hospital.getStatusCode(), hospital.getPhone(),
                hospital.getRoadNameAddress(), hospital.getType(), hospital.getEmployeesCnt(),
                hospital.getHasInpatientRoom(), hospital.getArea());
    }

    //@Transactional
    public void insertAll(List<Hospital> hospitals) {
        String query = "INSERT INTO `hospitals`.`hospital` (id, name, status_code, phone, road_name_address," +
                "type, employees_cnt, has_inpatient_room, area)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)" ;  // 9개 컬럼

        jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Hospital hospital = hospitals.get(i);
                ps.setLong(1, hospital.getId());
                ps.setString(2, hospital.getName());
                ps.setInt(3, hospital.getStatusCode());
                ps.setString(4, hospital.getPhone());
                ps.setString(5, hospital.getRoadNameAddress());
                ps.setString(6, hospital.getType());
                ps.setInt(7, hospital.getEmployeesCnt());
                ps.setBoolean(8, hospital.getHasInpatientRoom());
                ps.setDouble(9, hospital.getArea());
            }

            @Override
            public int getBatchSize() {
                return hospitals.size();
            }
        });
    }

    public List<Hospital> findAll() {
        String query = "SELECT * FROM `hospitals`.`hospital`;";
        List<Hospital> hospitals = jdbcTemplate.query(query, hospitalMapper);
        return hospitals;
    }

    public List<Hospital> findByAddress(String keyword) {
        String query = "SELECT * FROM `hospitals`.`hospital` WHERE road_name_address LIKE ?;";
        List<Hospital> hospitals = jdbcTemplate.query(query, hospitalMapper, '%'+keyword+'%');
        return hospitals;

    }

    public void deleteAll() {
        String query = "DELETE FROM `hospitals`.`hospital`";
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
