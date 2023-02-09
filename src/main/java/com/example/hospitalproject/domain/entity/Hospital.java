package com.example.hospitalproject.domain.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Hospital {

    @Id
    private Long id;                    // 번호 (PK)

    private String name;                // 사업장 명
    private Integer statusCode;         // 상세 영업 코드 (13:영업중, 3:폐업, 2:휴업, 24:직권폐업)
    private String phone;               // 소재지 전화
    private String roadNameAddress;     // 도로명 주소
    private String type;                // 업태 구분 명
    private Integer employeesCnt;       // 의료인 수
    private Boolean hasInpatientRoom;   // 입원실 여부
    private Double area;                // 총 면적

    @OneToMany(mappedBy = "hospital")
    private List<Review> reviews;

    @ColumnDefault(value = "0")
    private Integer reviewCnt;

    @ColumnDefault(value = "0")
    private Integer starSum;

    public void addReview(Integer star) {
        reviewCnt ++;
        starSum += star;
    }
}
