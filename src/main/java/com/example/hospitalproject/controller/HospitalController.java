package com.example.hospitalproject.controller;

import com.example.hospitalproject.domain.dto.*;
import com.example.hospitalproject.domain.entity.Hospital;
import com.example.hospitalproject.service.HospitalService;
import com.example.hospitalproject.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/hospitals")
@RequiredArgsConstructor
public class HospitalController {

    private final HospitalService hospitalService;
    private final ReviewService reviewService;

    @GetMapping(value = {"", "/"})
    public String getAll(Model model,
                         @RequestParam(required = false, defaultValue = "1") Integer page,
                         @RequestParam(defaultValue = "") String region,
                         @RequestParam(required = false) Integer statusCode,
                         @RequestParam(defaultValue = "") String type,
                         @RequestParam(defaultValue = "") String keyword,
                         @RequestParam(required = false) String sortBy) {

        PageRequest pageable = PageRequest.of(page - 1, 10, Sort.by("id").ascending());
        if(sortBy != null) {
            if(sortBy.equals("reviewCnt")) {
                pageable = PageRequest.of(page - 1, 10, Sort.by("reviewCnt").descending());
            } else {
                pageable = PageRequest.of(page - 1, 10, Sort.by("starAvg").descending());
            }
            model.addAttribute("sortBy", sortBy);
        }
        Page<Hospital> hospitals = hospitalService.search(region, statusCode, type, keyword, pageable);
        model.addAttribute("cnt", hospitals.getTotalElements());

        // 검색 옵션 유지를 위해 전송
        if(!region.equals(""))  model.addAttribute("region", region);
        if(statusCode != null)  model.addAttribute("statusCode", statusCode);
        if(!type.equals(""))    model.addAttribute("type", type);
        if(!keyword.equals("")) model.addAttribute("keyword", keyword);

        // 페이징 작업을 위해 전송
        if (hospitals.getNumber() == 0) {
            model.addAttribute("isFirstPage", true);
        }else if (hospitals.getNumber() == hospitals.getTotalPages() - 1) {
            model.addAttribute("isLastPage", true);
        }
        model.addAttribute("nowPage", hospitals.getNumber() + 1);
        model.addAttribute("lastPage", hospitals.getTotalPages());

        // Hospitals -> HospitalListDto
        model.addAttribute("hospitals",
                hospitals.stream()
                .map(hospital -> HospitalListDto.of(hospital))
                .collect(Collectors.toList()));

        return "hospitals/list";
    }

    @GetMapping("/{hospitalId}")
    public String showDetail(@PathVariable Long hospitalId, Model model) {
        Hospital hospital = hospitalService.findById(hospitalId);
        model.addAttribute("dto", HospitalDetailDto.of(hospital));
        model.addAttribute("reviewCreateDto", new ReviewCreateDto());
        model.addAttribute("reviewList", reviewService.getReviewList(hospitalId).stream()
                .map(review -> ReviewDetailDto.of(review))
                .collect(Collectors.toList()));
        return "hospitals/detail";
    }

    @PostMapping("/{hospitalId}/reviews")
    public String postReview(@PathVariable Long hospitalId, ReviewCreateDto dto) {
        reviewService.save(hospitalId, dto);
        return "redirect:/hospitals/" + hospitalId;
    }

    /**
     * JPA를 사용하여 전체 데이터 하나씩 파싱 후 삽입 => 12만개 30초
     */
    @ResponseBody
    @PostMapping("/jpa/all")
    public String insertAllByJpa() {
        try {
            long startTime = System.currentTimeMillis();
            int successCnt = hospitalService.insertAllData("./original_data/hospital_data.csv");
            long endTime = System.currentTimeMillis();

            log.info("JPA를 사용한 데이터 파싱 + 삽입 시간 : {}초", (endTime - startTime) / 1000.0);
            return successCnt + "개 데이터 삽입 성공";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * JPA를 사용하여 전체 데이터 하나씩 파싱 후 삽입 => 12만개
     */
    @ResponseBody
    @PostMapping("/jpa/all/v2")
    public String insertAllByJpaV2() {
        try {
            long startTime = System.currentTimeMillis();
            int successCnt = hospitalService.insertAllDataV2("./original_data/hospital_data.csv");
            long endTime = System.currentTimeMillis();

            log.info("JPA를 사용한 데이터 파싱 + 삽입 시간 : {}초", (endTime - startTime) / 1000.0);
            return successCnt + "개 데이터 삽입 성공";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 삽입된 데이터에서 statusCode, region, type 추출 결과
     * statusCode(상세 영업 코드) => 13(영업중), 2(휴업), 3(폐업), 24(직권폐업)
     * type(업태구분명) => 의원, 치과의원, 보건의료원, 보건진료소, 보건소, 조산원, 보건지소, 한의원
     * region(지역, 17개) => 서울특별시, 대전광역시, 울산광역시, 광주광역시, 부산광역시, 대구광역시, 인천광역시, 세종특별자치시
     *                경기도, 전라남도, 전라북도, 경상남도, 경상북도, 충청남도, 충청북도, 제주특별자치도, 강원도
     */
    @ResponseBody
    @GetMapping("/extract")
    public ExtractDto extract() {
        return hospitalService.extract();
    }

}
