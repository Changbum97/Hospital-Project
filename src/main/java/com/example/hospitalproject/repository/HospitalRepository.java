package com.example.hospitalproject.repository;

import com.example.hospitalproject.domain.Hospital;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HospitalRepository extends JpaRepository<Hospital, Long> {
    Page<Hospital> findAll(Pageable pageable);
    Page<Hospital> findByRoadNameAddressContainsAndStatusCodeAndTypeAndNameContains(
            String roadNameAddress, Integer statusCode, String type, String name, Pageable pageable);
    Page<Hospital> findByRoadNameAddressContainsAndTypeAndNameContains(
            String roadNameAddress, String type, String name, Pageable pageable);
    Page<Hospital> findByRoadNameAddressContainsAndStatusCodeAndNameContains(
            String roadNameAddress, Integer statusCode, String name, Pageable pageable);
    Page<Hospital> findByRoadNameAddressContainsAndNameContains(
            String roadNameAddress, String name, Pageable pageable);
}
