docker build -t hospital-project

docker run -p 8081:8081 \
-e SPRING_DATASOURCE_URL=jdbc:mysql://jdbc:mysql://aws-freetier.cjjpwgrapvvh.ap-northeast-2.rds.amazonaws.com:3306/hospitals \
-e SPRING_DATASOURCE_USERNAME=root \
-e SPRING_DATASOURCE_PASSWORD=root4568# \
-d hospital-project