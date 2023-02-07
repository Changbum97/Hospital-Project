package com.example.hospitalproject.domain.dto;

import com.example.hospitalproject.domain.entity.Hospital;
import com.example.hospitalproject.domain.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCreateDto {

    private String writer;
    private String texts;

    public Review toEntity(Hospital hospital) {
        return Review.builder()
                .writer(writer)
                .texts(texts)
                .hospital(hospital)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
