package com.example.hospitalproject.service;

import com.example.hospitalproject.domain.ExtractDto;
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
import java.util.List;

@Service
@RequiredArgsConstructor
public class HospitalService {

    private final HospitalRepository hospitalRepository;
    private final HospitalParser hospitalParser;

    // 약 11000초(3시간) 이상 -> @Transcational 사용 후 20초 이내로 시간 단축
    @Transactional
    public int insertAllData(String filename) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(filename));
        br.readLine();      // 첫 줄은 머리말이기 때문에 제외

        int successCnt = 0;
        String line;

        while((line = br.readLine()) != null) {
            try {
                Hospital hospital = hospitalParser.parse(line);
                hospitalRepository.save(hospital);
            } catch (Exception e) {
                successCnt --;
            }
            successCnt ++;
        }

        return successCnt;
    }

    public ExtractDto extract() {
        List<Hospital> hospitals = hospitalRepository.findAll();

        HashSet<Integer> statusCodes = new HashSet<>();   // 상세 영업 코드 추출
        HashSet<String> regions = new HashSet<>();         // 지역 추출
        HashSet<String> types = new HashSet<>();           // 업태 구분 명 추출

        for(Hospital hospital : hospitals) {
            statusCodes.add(hospital.getStatusCode());
            types.add(hospital.getType());

            String[] split = hospital.getRoadNameAddress().split(" ");
            if(split.length >= 1) {
                regions.add(split[0]);
            }
        }
        return new ExtractDto(statusCodes, regions, types);
    }

    public Hospital findById(Long id) {
        return hospitalRepository.findById(id).get();
    }

    public List<Hospital> findAll() {
        return hospitalRepository.findAll();
    }
}
