package com.example.hospitalproject.repository;

import com.example.hospitalproject.domain.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, Long> {
}
