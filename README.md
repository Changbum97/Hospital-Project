# 전국 병의원 및 약국 정보 제공 서비스 (V2 제작중)

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
- Thymeleaf, Bootstrap 5.2.3

## 원본 데이터 정리

- 데이터 출처 : [HIRA 빅데이터 개방포털](https://opendata.hira.or.kr/op/opc/selectOpenData.do?sno=11925&publDataTpCd=&searchCnd=&searchWrd=%EC%A0%84%EA%B5%AD&pageIndex=1)
1. hospital_info (76604개의 데이터)
    - 암호화 요양 기호(ID, 중복 X)
    - 요양기관명
    - 종별코드, 종별코드명
    - 시도코드, 시도코드명, 시군구코드, 시군구코드명, 읍면동
    - 우편번호, 주소, 전화번호, 병원 홈페이지
    - 개설일자
    - 총 의사 수
    - 의과, 치과, 한방 각각 일반의, 인턴, 레지던트, 전문의 인원수, 조산사 인원수
    - 좌표(x), 좌표(y)
2. pharmacy_info (24503개의 데이터)
    - 암호화 요양 기호(ID, 중복 X)
    - 요양기관명
    - 종별코드(81), 종별코드명(약국)
    - 시도코드, 시도코드명, 시군구코드, 시군구코드, 읍면동
    - 우편번호, 주소, 전화번호, 개설일자
    - 좌표(x), 좌표(y)
3. facility_info (101106개의 데이터)
    - 암호화 요양 기호(ID) (병원 + 약국, 중복 X)
    - 요양기관명
    - 종별코드, 종별코드명
    - 설립구분코드, 설립구분코드명
    - 시도코드, 시도코드명, 시군구코드, 시군구코드, 읍면동
    - 우편번호, 주소, 전화번호, 병원링크, 개설일자
    - 일반입원실상급병상수, 일반입원실일반병상수, 성인중환자병상, 소아중환자병상수, 분만실병상수, 수술실병상수, 응급실병상수, 물리치료실병상수, 정신과폐쇄상급병상수, 정신과폐쇄일반병상수, 격리병실병상수, 무균치료실병상수
4. detail_info (21034개의 데이터)
    - 암호화 요양 기호(ID) (병원 + 약국, 중복 X)
    - 요양기관명
    - 위치_공공건물(장소)명, 위치_방향, 위치_거리
    - 주차가능대수, 주차비용부담여부, 주차_기타안내사항
    - 일요일 휴진안내, 공휴일 휴진안내
    - 응급실 주간운영여부, 응급실 주간전화번호1, 응급실 주간전화번호2
    - 응급실 야간운영여부, 응급실 야간전화번호1, 응급실 야간전화번호2
    - 점심시간_평일, 점심시간_토요일
    - 접수시간_평일, 접수시간_토요일
    - 진료시작시간, 진료종료시간 각각 일~토
5. hospital_medical_department_info (370867개의 데이터)
    - 암호화 요양 기호(ID) (병원, 중복 O => 하나의 병원에서 여러개의 진료과목이 존재하는 경우)
    - 요양기관명
    - 진료과목코드, 진료과목코드명
    - 과목별 전문의수, 선택진료 의사수(0)
6. hospital_medical_equipment_info (59192개의 데이터)
    - 암호화 요양 기호(ID) (병원, 중복 O => 하나의 병원에서 여러종류의 의료장비를 사용하는 경우)
    - 장비코드, 장비코드명, 장비대수
7. hospital_special_care_info (9008개의 데이터)
    - 암호화 요양 기호(ID) (병원 + 약국, 중복 O => 하나의 병원/약국에서 여러 종류의 특수 진료를 하는 경우)
    - 요양기관명
    - 검색코드, 검색코드명
8. hospital_speciality_designated_field_info (108개의 데이터)
    - 암호화 요양 기호(ID) (병원, 중복 X, 전문 병원 지정 분야)
    - 요양기관명
    - 검색코드, 검색코드명
9. hospital_other_manpower_info (45886개의 데이터)
    - 암호화 요양 기호(ID) (병원 + 약국, 중복 O => 하나의 병원/약국에서 여러 종류의 기타 인력을 보유한 경우)
    - 요양기관명
    - 기타인력코드, 기타인력코드명, 기타인력수

## 설계

1. 필요한 데이터 선정 (Table 설계)
   - 병원 => ID, 이름, 분류(종합병원, 치과의원, 한의원 등), 시도, 시군구, 우편번호, 주소, 전화번호, 홈페이지 주소, 개설일자, 전체 의사 수, 위도, 경도
   - 약국 => ID, 이름, 시도, 시군구, 우편번호, 주소, 전화번호, 개설일자, 위도, 경도
   - 시설 정보 => 병원의 ID, 설립 구분(개인, 공립, 의료법인 등), 전체 병상수, 일반입원실 상급병상수, 일반입원실 일반병상수, 성인중환자 병상수, 소아중환자 병상수, 신생아중환자 병상수, 분만실 병상수, 수술실 병상수, 응급실 병상수, 물리치료실 병상수, 정신과폐쇄 병상수, 격리병실 병상수, 무균치료실 병상수
   - 상세 정보 => 병원/약국의 ID, 주차 가능 여부, 공휴일 휴진 안내, 식사 시간, 평일 진료 시간, 토요일 진료 시간, 일요일 진료 시간, 응급실 주간운영여부, 응급실 주간전화번호, 응급실 야간운영여부, 응급실 야간전화번호
   - 진료 과목 => 병원의 ID, 진료과목명, 과목별 전문의수
   - 의료 장비 => 병원의 ID, 장비명, 장비대수
   - 특수 진료 => 병원의 ID, 특수진료명
   - 전문 병원 지정 분야 => 병원의 ID, 지정분야명
   - 기타 인력 => 병원/약국의 ID, 기타인력명, 기타인력수
2. 기능 설계
   - 병원 리스트 출력
   - 약국 리스트 출력
   - 병원 상세 조회 시 시설 정보, 상세 정보, 진료 과목, 의료 장비, 특수 진료, 전문 병원 지정 분야, 기타 인력이 존재하면 출력
   - 약국 상세 조회 시 시설 정보, 상세 정보, 기타 인력이 존재하면 출력
   - 병원 상세 조회 시 가까운 약국(100m 이내) 리스트 출력
3. 리뷰 기능 설계
   - 병원, 약국 상세 조회 페이지에서 리뷰 작성 가능
   - 작성자명, 평점(5점 만점), 상세 리뷰 작성 가능
4. 검색 기능 설계
   - 병원 검색 => 이름, 주소, 설립 구분, 병상 존재 여부, 주차 가능 여부, 공휴일, 토요일, 일요일 진료 여부, 응급실 주간 운영 여부, 응급실 야간 운영 여부, 진료과목명, 의료 장비 보유 여부, 특수진료명, 전문 병원 지정 여부, 전문 병원 지정 분야명
   - 약국 검색 => 이름, 주소, 공휴일, 토요일, 일요일 진료 여부
5. 정렬 기능 설계
   - 검색과 중복 가능
   - 평점 높은 순, 리뷰 많은 순

## 구현 기능

- 전국 병원 데이터 약 7.6만개, 약국 데이터 약 2.4만개 및 추가 데이터 약 60만개 수집 (CSV 파일)
- 데이터 파싱 후 필요한 데이터 DB에 삽입
- 추가 데이터들과 Join 기능 구현
- 전체 데이터 리스트 조회 기능 + 페이징 처리 구현
- 리뷰 작성 기능 + 별점 기능 구현
- 검색 및 정렬 기능 구현
- Interceptor을 활용하여 각각의 요청이 걸린 시간을 log로 출력

## 결과

- http://ec2-52-79-82-151.ap-northeast-2.compute.amazonaws.com:8081/hospitals

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
    - +application.yml에서 batch size 조정 => 약 22초
  - GET /hospitals : 전체 조회 리스트 페이지, 페이징 추가, 검색 기능 추가
  - GET /hospitals/{hospitalId} : 병원 한 개 조회 페이지, 해당 병원에 달린 리뷰 조회 가능, 리뷰 추가 가능
  - POST /hospitals/{hospitalId}/reviews : 해당 병원에 리뷰 추가
  - GET /hospitals/extract
    - 원본 파일에서 statusCode, city, type 추출
    - city는 큰 지역(서울, 경기도, 강원도, ...)과 작은 지역(강남구, 수원시, 평창군, ...)으로 구분 후 DB에 저장
    - DB에 저장된 city는 지역 검색에 사용
    - 