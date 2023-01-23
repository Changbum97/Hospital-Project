package com.example.hospitalproject.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Hospital {

    @Id
    private Long id;                    // 번호 (PK)

    private String name;                // 사업장 명
    private Integer status;             // 상세 영업 코드
    private String phone;               // 소재지 전화
    private String roadNameAddress;     // 도로명 주소
    private String type;                // 업태 구분 명
    private Integer employeesCnt;       // 의료인 수
    private Boolean hasInpatientRoom;   // 입원실 여부
    private Double area;                // 총 면적
}
