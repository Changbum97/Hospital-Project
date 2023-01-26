package com.example.hospitalproject.parser;

import com.example.hospitalproject.domain.Hospital;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;

public class MakeSqlFile {

    private String path;
    private String fullPath;
    private String fileName;

    public MakeSqlFile(String fileName) {
        this.path = "./extract_data/";
        this.fileName = fileName;
        this.fullPath = path + fileName;
        createFile();
    }

    public void createFile() {
        File file = new File(fullPath);
        try {
            if(file.exists()) {
                file.delete();
            }
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeV1(String inputFileName) throws IOException {

        HospitalParser hospitalParser = new HospitalParser();

        BufferedReader br = new BufferedReader(new FileReader(inputFileName));
        br.readLine();      // 첫 줄은 머리말이기 때문에 제외

        BufferedWriter bw = new BufferedWriter(new FileWriter(fullPath));

        String line;
        while((line = br.readLine()) != null) {
            try {
                Hospital hospital = hospitalParser.parse(line);
                bw.write(makeQueryV1(hospital));
            }
            catch (Exception e) { }
        }

        bw.flush();
        bw.close();
    }

    public void writeV2(List<Hospital> hospitals) {
        //BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));


    }

    /*
    INSERT INTO `likelion-db`.`hospital` (`id`,`name`,`statusCode`,`phone`,`roadNameAddress`,`type`,`employeesCnt`, `hasInpatientRoom`, `area`)
    VALUES (1,'효치과의원', 13, '062-515-2875', '광주광역시 북구 동문대로 24, 3층 (풍향동)', '치과의원', 1, 'true', 52.29);
    이런 식의 쿼리를 만들어야 함
    */
    public String makeQueryV1(Hospital hospital) {
        String query = "INSERT INTO `likelion-db`.`hospital` (`id`,`name`,`status_code`,`phone`,`road_name_address`,`type`,`employees_cnt`, `has_inpatient_room`, `area`) VALUES ";
        query += String.format("(%d, '%s', %d, '%s', '%s', '%s', %d, %b, %f",
                hospital.getId(),
                hospital.getName(),
                hospital.getStatusCode(),
                hospital.getPhone(),
                hospital.getRoadNameAddress().replace("'", ""),
                // 도로명주소에 '가 들어가면 쿼리문과 구분되지 않아 발생하는 에러 해결
                hospital.getType(),
                hospital.getEmployeesCnt(),
                hospital.getHasInpatientRoom(),
                hospital.getArea() );
        query += ");\n";
        return query;
    }

    public String makeQueryV2(Hospital hospital) {
        return null;
    }
}
