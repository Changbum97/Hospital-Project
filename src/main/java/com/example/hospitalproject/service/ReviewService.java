package com.example.hospitalproject.service;

import com.example.hospitalproject.domain.dto.ReviewCreateDto;
import com.example.hospitalproject.domain.entity.Hospital;
import com.example.hospitalproject.domain.entity.Review;
import com.example.hospitalproject.repository.HospitalRepository;
import com.example.hospitalproject.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final HospitalRepository hospitalRepository;

    public void save(Long hospitalId, ReviewCreateDto dto) {
        Hospital hospital = hospitalRepository.findById(hospitalId).get();
        hospital.addReview(dto.getStar());
        hospitalRepository.save(hospital);
        reviewRepository.save(dto.toEntity(hospital));
    }

    public List<Review> getReviewList(Long hospitalId) {
        return reviewRepository.findByHospitalId(hospitalId);
    }
}
