# 전국 병원 정보 프로젝트

## 구현 기능

- 전국 병원 데이터 약 12만개 데이터 수집 (CSV 파일)
- 데이터 파싱 후 DB에 삽입
- 전체 데이터 리스트 조회 기능 + 페이징 처리 구현
- 리뷰 작성 기능 + 별점 기능 구현
- 검색 및 정렬 기능 구현 (중복 검색 가능)
  - 검색(키워드로 입력) : 병원명
  - 검색(리스트에서 선택) : 지역, 영업상태, 업태구분명
  - 정렬 : 리뷰 많은 순, 별점 높은 순
- Interceptor를 통해 각각의 요청이 걸린 시간을 log로 출력

## 개발 환경

- Language: JAVA 11
- Framework: Springboot 2.7.8
- DB: MySQL 8.0
- Build: Gradle 7.6
- Server: AWS EC2
- Deploy: Docker

## 라이브러리

- Spring Boot Web
- Spring Boot DevTools
- Spring Data Jpa, MySQL
- Lombok
- Mustache, Bootstrap 5.2.3

## 결과

- http://ec2-43-200-173-234.ap-northeast-2.compute.amazonaws.com:8081/hospitals

## 약 12만개 데이터 삽입 속도 비교 및 성능 개선 (End Point)

- JDBC 사용
  - Driver Manager로 DB와 연결, 쿼리를 직접 실행하는 방식
  - HospitalRestController, HospitalJdbcService, HospitalDao 사용
  - End Point
    - POST /api/hospitals/jdbc/all/v1
      - csv -> sql (12만개의 입력 쿼리로 변환) -> sql 실행
      - 약 37초
    - POST /api/hospitals/jdbc/all/v2
      - csv -> sql (한 개의 입력 쿼리로 변환) -> sql 실행 (한 개의 쿼리로 12만개 데이터 삽입)
      - 약 3.8초
    - GET /api/hospitals/jdbc/all : 전체 조회
    - GET /api/hospitals/jdbc/{keyword} : 주소에 keyword가 들어간 병원 조회
    - DELETE /api/hospitals/jdbc/all : 전체 삭제
- Jdbc Template 사용
  - JdbcTemplate을 사용하여 데이터 처리
  - HospitalRestController, HospitalJdbcTemplateService, HospitalDao2 사용
  - End Point
    - POST /api/hospitals/jdbc-template/all/v1
      - csv -> 한 줄씩 파싱 -> 하나씩 삽입
      - 약 23초
    - POST /api/hospitals/jdbc-template/all/v2
      - Bulk Insert(Batch Size 조정) 
      - csv -> 한 줄씩 파싱 -> 한번에 삽입
      - 약 17초
    - POST /api/hospitals/jdbc-template/all/v3
      - Bulk Insert + @Transactional 적용
      - 약 7초
    - POST /api/hospitals/jdbc-template/all/v4
      - Bulk Insert + @Transactional 적용 + 병렬 처리 적용
      - 약 6초
    - GET /api/hospitals/jdbc-template/all : 전체 조회
    - GET /api/hospitals/jdbc-template/{keyword} : 주소에 keyword가 들어간 병원 조회
    - DELETE /api/hospitals/jdbc-template/all : 전체 삭제
- Spring Jpa 사용
  - Spring Jpa를 사용하여 데이터 처리 + 화면 출력
  - HospitalController, HospitalService, HospitalRepository 사용
  - POST /hospitals/jpa/all/v1
    - csv 파일을 한 줄 씩 읽고 파싱 -> 하나씩 DB에 삽입
    - 3시간 이상 
  - POST /hospitals/jpa/all/v2
    - csv 파일을 한 줄 씩 읽고 파싱 -> 하나씩 DB에 삽입 + @Transactional 적용
    - 약 34초
  - POST /hospitals/jpa/all/v3
    - csv 파일을 파싱 -> saveAll()로 한번에 DB에 삽입 + @Transactional 적용
    - 약 32초
    - + application.yml에서 batch size 조정 => 약 22초
  - GET /hospitals : 전체 조회 리스트 페이지, 페이징 추가, 검색 기능 추가
  - GET /hospitals/{hospitalId} : 병원 한 개 조회 페이지, 해당 병원에 달린 리뷰 조회 가능, 리뷰 추가 가능
  - POST /hospitals/{hospitalId}/reviews : 해당 병원에 리뷰 추가
  - GET /hospitals/extract
    - 원본 파일에서 statusCode, city, type 추출
    - city는 큰 지역(서울, 경기도, 강원도, ...)과 작은 지역(강남구, 수원시, 평창군, ...)으로 구분 후 DB에 저장
    - DB에 저장된 city는 지역 검색에 사용