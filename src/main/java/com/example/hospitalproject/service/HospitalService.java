package com.example.hospitalproject.service;

import com.example.hospitalproject.domain.dto.ExtractDto;
import com.example.hospitalproject.domain.entity.City;
import com.example.hospitalproject.domain.entity.Hospital;
import com.example.hospitalproject.parser.HospitalParser;
import com.example.hospitalproject.repository.CityRepository;
import com.example.hospitalproject.repository.HospitalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class HospitalService {

    private final HospitalRepository hospitalRepository;
    private final CityRepository cityRepository;
    private final HospitalParser hospitalParser;

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

    @Transactional
    public int insertAllDataWithTransaction(String filename) throws IOException {

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

    @Transactional
    public int insertAllDataV3(String filename) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(filename));
        br.readLine();      // 첫 줄은 머리말이기 때문에 제외

        int successCnt = 0;
        String line;

        List<Hospital> hospitals = new ArrayList<>();

        while((line = br.readLine()) != null) {
            try {
                hospitals.add(hospitalParser.parse(line));
            } catch (Exception e) {
                successCnt --;
            }
            successCnt ++;
        }
        hospitalRepository.saveAll(hospitals);

        return successCnt;
    }

    public ExtractDto extract() {
        List<Hospital> hospitals = hospitalRepository.findAll();

        HashSet<Integer> statusCodes = new HashSet<>();     // 상세 영업 코드 추출
        HashMap<String, Integer> regions = new HashMap<>(); // 큰 도시 추출
        List<String> sortedRegions = new ArrayList<>();     // 추출한 큰 도시를 병원수 기준으로 내림차순 정렬
        HashSet<String> states = new HashSet<>();           // 작은 도시 추출
        HashSet<String> types = new HashSet<>();            // 업태 구분 명 추출

        for(Hospital hospital : hospitals) {
            statusCodes.add(hospital.getStatusCode());
            types.add(hospital.getType());

            String[] split = hospital.getRoadNameAddress().split(" ");
            if(split.length >= 2) {
                char last1 = split[0].charAt(split[0].length() - 1);
                char last2 = split[1].charAt(split[1].length() - 1);

                if (last1 == '시' || last1 == '도') {
                    if (regions.containsKey(split[0])) {
                        regions.put(split[0], regions.get(split[0]) + 1);
                    } else {
                        regions.put(split[0], 1);
                    }
                    if (last2 == '시' || last2 == '군' || last2 == '구') {
                        states.add(split[0] + " " + split[1]);
                    }
                }
            }
        }

        // 큰 지역은 병원 개수가 많은 순으로 정렬
        List<String> keys = new ArrayList<>(regions.keySet());

        Collections.sort(keys, (v2, v1) -> (regions.get(v1).compareTo(regions.get(v2))));

        for (String key : keys) {
            sortedRegions.add(key);
        }

        // sortedRegions, states를 DB에 저장
        cityRepository.save(City.builder().region("큰 도시").state("작은 도시").build());
        for (String region : sortedRegions) {
            cityRepository.save(City.builder().region(region).state("전체").build());

            List<String> regionsStates = new ArrayList<>();
            for (String state : states) {
                if (state.split(" ")[0].equals(region)) {
                    regionsStates.add(state.split(" ")[1]);
                }
            }

            Collections.sort(regionsStates);
            for (String regionState : regionsStates) {
                cityRepository.save(City.builder().region(region).state(regionState).build());
            }
        }

        return new ExtractDto(statusCodes, sortedRegions, states, types);
    }

    public Hospital findById(Long id) {
        return hospitalRepository.findById(id).get();
    }

    public Page<Hospital> search(String region, String state, Integer statusCode, String type, String keyword, Pageable pageable) {
        // statusCode, type은 contains가 아니기 때문에 null이 들어오면 조회 안 됨 => 별도의 처리 필요
        if(statusCode != null && !type.equals("")) {
            return hospitalRepository.findByRoadNameAddressContainsAndRoadNameAddressContainsAndStatusCodeAndTypeAndNameContains(
                    region, state, statusCode, type, keyword, pageable);
        } else if(statusCode == null && !type.equals("")) {
            return hospitalRepository.findByRoadNameAddressContainsAndRoadNameAddressContainsAndTypeAndNameContains(
                    region, state, type, keyword, pageable);
        } else if(statusCode != null && type.equals("")) {
            return hospitalRepository.findByRoadNameAddressContainsAndRoadNameAddressContainsAndStatusCodeAndNameContains(
                    region, state, statusCode, keyword, pageable);
        } else {
            return hospitalRepository.findByRoadNameAddressContainsAndRoadNameAddressContainsAndNameContains(
                    region, state, keyword, pageable);
        }
    }
}
