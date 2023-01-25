package com.example.hospitalproject.controller;

import com.example.hospitalproject.domain.ExtractDto;
import com.example.hospitalproject.service.HospitalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/hospitals")
@RequiredArgsConstructor
public class HospitalController {

    private final HospitalService hospitalService;

    @GetMapping("/all")
    public String getAll() {
        return "hospitals/list";
    }

    @ResponseBody
    @PostMapping("/all")
    public String insertAll() {
        try {
            return hospitalService.insertAllData("./original_data/hospital_data.csv");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ResponseBody
    @GetMapping("/extract")
    public ExtractDto extract() {
        return hospitalService.extract();
    }
}
