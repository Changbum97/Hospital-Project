package com.example.hospitalproject.controller;

import com.example.hospitalproject.service.HospitalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/hospitals")
@RequiredArgsConstructor
public class HospitalController {

    private final HospitalService hospitalService;

    @ResponseBody
    @GetMapping("/size")
    public String count() {
        try {
            return hospitalService.insertAllData("./original_data/hospital_data.csv");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
