package com.example.hospitalproject.repository;

import com.example.hospitalproject.domain.entity.Hospital;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HospitalRepository extends JpaRepository<Hospital, Long> {
    Page<Hospital> findAll(Pageable pageable);
    Page<Hospital> findByRoadNameAddressContainsAndRoadNameAddressContainsAndStatusCodeAndTypeAndNameContains(
            String region, String state, Integer statusCode, String type, String name, Pageable pageable);
    Page<Hospital> findByRoadNameAddressContainsAndRoadNameAddressContainsAndTypeAndNameContains(
            String region, String state, String type, String name, Pageable pageable);
    Page<Hospital> findByRoadNameAddressContainsAndRoadNameAddressContainsAndStatusCodeAndNameContains(
            String region, String state, Integer statusCode, String name, Pageable pageable);
    Page<Hospital> findByRoadNameAddressContainsAndRoadNameAddressContainsAndNameContains(
            String region, String state, String name, Pageable pageable);
}
