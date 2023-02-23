# 전국 병원 데이터 분석 프로젝트

- 전국 병원 데이터 약 12만개 데이터 수집
- 데이터 파싱 후 DB에 삽입
- 전체 데이터 리스트 조회 기능 + 페이징 처리 구현
- 리뷰 작성 기능 구현
- 검색 및 정렬 기능 구현
  - 검색(키워드로 입력) : 병원명
  - 검색(리스트에서 선택) : 지역, 영업상태, 업태구분명
  - 정렬 : 리뷰 많은 순, 별점 높은 순

## 약 12만개 데이터 삽입 속도 비교 및 성능 개선

- JDBC 사용 (Driver Manager, 쿼리 실행)
  - insert문 12만개 : 약 30초
  - insert문 1개 : 약 3초
- Jdbc Template 사용
  - 데이터 파싱 후 하나씩 삽입 : 약 23초
  - Bulk Insert(Batch Size 조정) : 약 15초
  - @Transactional 적용 + Bulk Insert : 약 7초
- Spring Jpa 사용
  - 데이터 파싱 후 하나씩 삽입 : 약 3시간 이상
  - @Transactional 적용 : 약 35초
  - saveAll 적용 : 약 25초
  - Batch Size 조정 : 약 30초