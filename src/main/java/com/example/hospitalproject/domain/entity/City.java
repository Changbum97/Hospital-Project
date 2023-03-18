package com.example.hospitalproject.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class City {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 큰 도시 이름 => 서울특별시, 경기도, 강원도, ...
    private String region;
    // 작은 도시(시,군,구) 이름 => 강남구, 수원시, 평창군
    private String state;
}
