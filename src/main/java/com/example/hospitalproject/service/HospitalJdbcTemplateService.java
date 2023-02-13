package com.example.hospitalproject.service;

import com.example.hospitalproject.dao.HospitalDao2;
import com.example.hospitalproject.domain.entity.Hospital;
import com.example.hospitalproject.parser.HospitalParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HospitalJdbcTemplateService {

    private final HospitalDao2 hospitalDao;
    private final HospitalParser hospitalParser;

    public int insertAll(String inputFileName) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(inputFileName));
        br.readLine();      // 첫 줄은 머리말이기 때문에 제외

        String line;
        int successCnt = 0;
        while((line = br.readLine()) != null) {
            try {
                Hospital hospital = hospitalParser.parse(line);
                hospitalDao.insertOne(hospital);
                successCnt ++;
            }
            catch (Exception e) { }
        }

        return successCnt;
    }

    public int batchInsertAll(String inputFileName) throws IOException {
        Long time1 = System.currentTimeMillis();
        BufferedReader br = new BufferedReader(new FileReader(inputFileName));
        br.readLine();      // 첫 줄은 머리말이기 때문에 제외

        String line;
        List<Hospital> hospitals = new ArrayList<>();

        while((line = br.readLine()) != null) {
            try {
                Hospital hospital = hospitalParser.parse(line);
                hospitals.add(hospital);
            }
            catch (Exception e) { }
        }

        Long time2 = System.currentTimeMillis();

        hospitalDao.insertAll(hospitals);

        Long time3 = System.currentTimeMillis();

        System.out.println("1 -> 2 : " + (time2 - time1)/1000.0);
        System.out.println("2 -> 3 : " + (time3 - time2)/1000.0);
        return hospitals.size();
    }

    public List<Hospital> findAll() {
        return hospitalDao.findAll();
    }

    public List<Hospital> findByAddress(String keyword) {
        return hospitalDao.findByAddress(keyword);
    }

    public void deleteAll() {
        hospitalDao.deleteAll();
    }
}
