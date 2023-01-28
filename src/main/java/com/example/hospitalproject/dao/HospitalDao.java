package com.example.hospitalproject.dao;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

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

    public void insertV1(String fileName) throws SQLException, IOException {

        BufferedReader br = new BufferedReader(new FileReader(fileName));

        String line;
        PreparedStatement ps = null;
        while((line = br.readLine()) != null) {
            // 쿼리 입력
            ps = conn.prepareStatement(line);

            // 쿼리 실행
            ps.executeUpdate();
        }

        ps.close();
    }

    public void insertV2(String fileName) throws SQLException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        PreparedStatement ps = conn.prepareStatement(br.readLine());
        ps.executeUpdate();
        ps.close();
    }
}
