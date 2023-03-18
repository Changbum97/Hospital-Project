package com.example.hospitalproject.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ExtractDto {
    HashSet<Integer> statusCodes;   // 상세 영업 코드
    List<String> regions;           // 큰 도시
    HashSet<String> states;         // 작은 도시
    HashSet<String> types;          // 업태 구분 명
}
