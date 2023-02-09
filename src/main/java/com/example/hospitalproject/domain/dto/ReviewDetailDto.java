package com.example.hospitalproject.domain.dto;

import com.example.hospitalproject.domain.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@AllArgsConstructor
public class ReviewDetailDto {

    private String writer;
    private String texts;
    private String createdAt;
    private Integer star;

    public static ReviewDetailDto of(Review review) {
        return new ReviewDetailDto(review.getWriter(), review.getTexts(),
                review.getCreatedAt().format(DateTimeFormatter.ofPattern("yy/MM/dd")),
                review.getStar());
    }
}
