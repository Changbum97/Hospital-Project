package com.example.hospitalproject.service;

import com.example.hospitalproject.domain.dao.HospitalDao;
import com.example.hospitalproject.domain.entity.Hospital;
import com.example.hospitalproject.parser.MakeSqlFile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HospitalJdbcService {

    private final HospitalDao hospitalDao;

   public int insertAllDataV1(String outputFileName, String inputFileName) throws IOException, SQLException {

       MakeSqlFile makeSqlFile = new MakeSqlFile(outputFileName);
       makeSqlFile.writeV1(inputFileName);

       return hospitalDao.insertV1(outputFileName);
   }

   public void insertAllDataV2(String outputFileName, String inputFileName) throws SQLException, IOException {

       MakeSqlFile makeSqlFile = new MakeSqlFile(outputFileName);
       makeSqlFile.writeV2(inputFileName);

       hospitalDao.insertV2(outputFileName);
   }

   public List<Hospital> findAll() throws SQLException {
       return hospitalDao.findAll();
   }

   public List<Hospital> findByAddress(String keyword) throws SQLException {
       return hospitalDao.findByAddress(keyword);
   }

   public void deleteAll() throws SQLException {
        hospitalDao.deleteAll();
   }
}
