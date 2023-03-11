package com.example.hospitalproject.parser;

import com.example.hospitalproject.domain.entity.Hospital;

import java.io.*;

public class MakeSqlFile {

    private String fileName;

    public MakeSqlFile(String fileName) {
        this.fileName = fileName;
        createFile();
    }

    public void createFile() {
        File file = new File(fileName);
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

        BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));

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

    public void writeV2(String inputFileName) throws IOException {
        HospitalParser hospitalParser = new HospitalParser();

        BufferedReader br = new BufferedReader(new FileReader(inputFileName));
        br.readLine();      // 첫 줄은 머리말이기 때문에 제외

        BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
        bw.write("INSERT INTO `likelion-db`.`hospital` (`id`,`name`,`status_code`,`phone`,`road_name_address`,`type`,`employees_cnt`, `has_inpatient_room`, `area`) VALUES ");

        String nowLine = br.readLine();
        String nextLine = br.readLine();
        while(nextLine != null) {
            try {
                Hospital hospital = hospitalParser.parse(nowLine);
                bw.write(makeQueryV2(hospital));

                // 마지막 라인은 ;로 끝내고 마지막이 아닌 라인은 ,로 끝내는 작업
                bw.write(", ");
            }
            catch (Exception e) {
            }
            nowLine = nextLine;
            nextLine = br.readLine();
        }

        Hospital hospital = hospitalParser.parse(nowLine);
        bw.write(makeQueryV2(hospital));
        bw.write(";");

        bw.flush();
        bw.close();
    }

    /*
    INSERT INTO `hospitals`.`hospital` (`id`,`name`,`status_code`,`phone`,`road_name_address`,`type`,`employees_cnt`, `has_inpatient_room`, `area`)
    VALUES (1,'효치과의원', 13, '062-515-2875', '광주광역시 북구 동문대로 24, 3층 (풍향동)', '치과의원', 1, 'true', 52.29);
    이런 식의 쿼리를 만들어야 함
    */
    public String makeQueryV1(Hospital hospital) {
        String query = "INSERT INTO `hospitals`.`hospital` (`id`,`name`,`status_code`,`phone`,`road_name_address`,`type`,`employees_cnt`, `has_inpatient_room`, `area`) VALUES ";
        query += String.format("(%d, '%s', %d, '%s', '%s', '%s', %d, %b, %f);\n",
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
        return query;
    }

    // 맨 처음에 Insert문을 맨 처음 한 번만 써주고
    // (1,'효치과의원', 13, '062-515-2875', '광주광역시 북구 동문대로 24, 3층 (풍향동)', '치과의원', 1, 'true', 52.29),
    // 이런 식으로 Tuple String 만 뽑아서 write => Insert문 한 번만으로 모든 데이터가 삽입되기 때문에 속도가 매우 빠름
    public String makeQueryV2(Hospital hospital) {
        String query = String.format("(%d, '%s', %d, '%s', '%s', '%s', %d, %b, %f)",
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
        return query;
    }
}
