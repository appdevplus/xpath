mvn spring-boot:run

# อ่านจากไฟล์ใน classpath
curl http://localhost:8080/api/balances

# อัปโหลดไฟล์ XML
curl -F "file=@03030364.xml" http://localhost:8080/api/balances
