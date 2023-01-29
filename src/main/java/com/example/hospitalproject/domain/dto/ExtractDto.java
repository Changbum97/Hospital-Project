package com.example.hospitalproject.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;

@Getter
@Setter
@AllArgsConstructor
public class ExtractDto {
    HashSet<Integer> statusCodes;   // 상세 영업 코드
    HashSet<String> regions;        // 지역
    HashSet<String> types;          // 업태 구분 명
}
