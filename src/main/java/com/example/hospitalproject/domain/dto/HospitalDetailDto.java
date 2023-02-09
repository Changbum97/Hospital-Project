package com.example.hospitalproject.domain.dto;

import com.example.hospitalproject.domain.entity.Hospital;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HospitalDetailDto {
    private Long id;                    // 번호 (PK)
    private String name;                // 사업장 명
    private String status;              // 상세 영업 코드 (13:영업중, 3:폐업, 2:휴업, 24:직권폐업)
    private String phone;               // 소재지 전화
    private String roadNameAddress;     // 도로명 주소
    private String type;                // 업태 구분 명
    private Integer employeesCnt;       // 의료인 수
    private String hasInpatientRoom;    // 입원실 여부
    private Double area;                // 총 면적
    private Integer reviewCnt;          // 리뷰 개수
    private String starAvg;             // 별점 평균

    public static HospitalDetailDto of(Hospital hospital) {
        String status = "";
        if(hospital.getStatusCode() == 13) {
            status = "영업중";
        } else if(hospital.getStatusCode() == 3) {
            status = "폐업";
        } else if(hospital.getStatusCode() == 2) {
            status = "휴업";
        } else {
            status = "직권폐업";
        }

        return new HospitalDetailDto(hospital.getId(), hospital.getName(), status,
                hospital.getPhone(), hospital.getRoadNameAddress(), hospital.getType(),
                hospital.getEmployeesCnt(), hospital.getHasInpatientRoom() == true?"보유":"미보유", hospital.getArea(),
                hospital.getReviewCnt(), String.format("%.2f", (double)hospital.getStarSum() / hospital.getReviewCnt()));
    }
}
