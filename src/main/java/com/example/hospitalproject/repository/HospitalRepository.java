package com.example.hospitalproject.repository;

import com.example.hospitalproject.domain.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HospitalRepository extends JpaRepository<Hospital, Long> {
}
