package com.example.hospitalproject.service;

import com.example.hospitalproject.domain.Hospital;
import com.example.hospitalproject.parser.HospitalParser;
import com.example.hospitalproject.repository.HospitalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class HospitalService {

    private final HospitalRepository hospitalRepository;
    private final HospitalParser hospitalParser;

    //@Transactional
    public String insertAllData(String filename) throws IOException {
        String result = "";

        BufferedReader br = new BufferedReader(new FileReader(filename));
        br.readLine();      // 첫 줄은 머리말이기 때문에 제외

        HashSet<String> types = new HashSet<>();
        HashSet<Integer> status = new HashSet<>();

        int cnt = 0;
        String line;

        long startTime = System.currentTimeMillis();

        while((line = br.readLine()) != null) {
            try {
                Hospital hospital = hospitalParser.parse(line);
                hospitalRepository.save(hospital);
                types.add(hospital.getType());
                status.add(hospital.getStatus());
            } catch (Exception e) {
                cnt --;
            }
            cnt ++;
        }

        long endTime = System.currentTimeMillis();

        result += "입력 개수 : " + cnt + "\n";
        result += "걸린 시간 : " + (endTime - startTime) / 1000.0 + "초\n";
        result += "types : " + types.toString() + "\n";
        result += "status : " + status.toString() + "\n";

        return result;
    }
}
