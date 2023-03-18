package com.example.hospitalproject.service;

import com.example.hospitalproject.domain.entity.City;
import com.example.hospitalproject.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CityService {

    private final CityRepository cityRepository;

    public List<City> findAllRegion() {
        List<City> cities = cityRepository.findAll();
        List<City> regions = new ArrayList<>();
        for (City city : cities) {
            if (city.getState().equals("작은 도시") || city.getState().equals("전체")) {
                regions.add(city);
            }
        }
        return regions;
    }
    public List<City> findAllState() {
        return cityRepository.findAll();
    }

    public City findById(Long id) {
        return cityRepository.findById(id).get();
    }
}
