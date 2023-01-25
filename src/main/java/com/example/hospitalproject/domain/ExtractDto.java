package com.example.hospitalproject.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;

@Getter
@Setter
@AllArgsConstructor
public class ExtractDto {
    HashSet<Integer> statusCodes;   // 상세 영업 코드 추출
    HashSet<String> regions;        // 지역(~~도 ~~시) 추출
    HashSet<String> types;          // 업태 구분 명 추출
    Double extractTime;               // 소요 시간
}
