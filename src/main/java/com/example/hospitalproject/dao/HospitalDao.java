package com.example.hospitalproject.dao;

import com.example.hospitalproject.domain.entity.Hospital;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class HospitalDao {

    private Map<String, String> env;
    private String dbHost, dbUser, dbPassword;

    private Connection conn;

    public HospitalDao() throws SQLException {
        // 환경변수에서 DB 정보 가져오기
        env = System.getenv();
        dbHost = env.get("DB_HOST");
        dbUser = env.get("DB_USER");
        dbPassword = env.get("DB_PASSWORD");

        // DB 연결
        conn = DriverManager.getConnection(dbHost, dbUser, dbPassword);
    }

    public int insertV1(String fileName) throws SQLException, IOException {

        BufferedReader br = new BufferedReader(new FileReader(fileName));

        String line;
        int successCnt = 0;
        PreparedStatement ps = null;
        while((line = br.readLine()) != null) {
            // 쿼리 입력
            ps = conn.prepareStatement(line);

            // 쿼리 실행
            ps.executeUpdate();

            successCnt ++;
        }

        ps.close();
        return successCnt;
    }

    public void insertV2(String fileName) throws SQLException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        PreparedStatement ps = conn.prepareStatement(br.readLine());
        ps.executeUpdate();
        ps.close();
    }

    public List<Hospital> findAll() throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM `likelion-db`.hospital;");
        ResultSet rs = ps.executeQuery();
        return resultSetToList(rs);
    }

    public List<Hospital> findByAddress(String keyword) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM `likelion-db`.hospital WHERE road_name_address LIKE ?;");
        ps.setString(1, '%'+keyword+'%');
        ResultSet rs = ps.executeQuery();
        return resultSetToList(rs);
    }

    public void deleteAll() throws SQLException {
        PreparedStatement ps = conn.prepareStatement("DELETE FROM `likelion-db`.hospital");
        ps.executeUpdate();
    }

    private List<Hospital> resultSetToList(ResultSet rs) throws SQLException {
        List<Hospital> hospitals = new ArrayList<>();
        while(rs.next()) {
            Hospital hospital = Hospital.builder()
                    .id(rs.getLong("id"))
                    .name(rs.getString("name"))
                    .statusCode(rs.getInt("status_code"))
                    .phone(rs.getString("phone"))
                    .roadNameAddress(rs.getString("road_name_address"))
                    .type(rs.getString("type"))
                    .employeesCnt(rs.getInt("employees_cnt"))
                    .hasInpatientRoom(rs.getBoolean("has_inpatient_room"))
                    .area(rs.getDouble("area"))
                    .build();
            hospitals.add(hospital);
        }
        return hospitals;
    }
}
