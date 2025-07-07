# JWT 인증 기반 Spring Boot API 프로젝트


## 📌 프로젝트 소개


이 프로젝트는 JWT(Json Web Token)를 이용한 인증/인가 시스템을 구현한 Spring Boot 기반의 REST API입니다.  
사용자는 회원가입, 로그인 후 토큰을 발급받고, 이를 통해 인증된 요청을 수행할 수 있습니다.  
또한 관리자는 사용자에게 권한을 부여할 수 있으며, Swagger UI를 통해 API를 테스트할 수 있습니다.


---


## 🔎배포 (GitHub Actions + ECR + EC2 )


GitAction 및 ECR 과 EC2를 활용하여 자동 빌드 및 배포 수행


---


## 📮 API 명세


### 주요 엔드 포인트
| 메서드   | URL                           | 설명          | 권한         |
| ----- | ----------------------------- | ----------- | ---------- |
| POST  | `/signup`                     | 회원가입        | -          |
| POST  | `/login`                      | 로그인 및 토큰 발급 | -          |
| POST  | `/admin`                      | 어드민 회원가입    | -          |
| PATCH | `/admin/users/{userId}/roles` | 사용자 권한 부여   | 관리자(Admin) |


---


## 테스트
- JUnit + MockMvc 기반의 단위 테스트 제공합니다.
- GitHub Actions에서 테스트 자동 수행합니다.
```
./gradlew test
```


---


## ☁ 배포 정보
- EC2 서버 주소: http://3.36.60.173:8080
- Swagger 주소: http://3.36.60.173:8080/swagger-ui/index.html


---


## 🛠 기술 스택
- Spring Boot 3.x
- Spring Security + JWT
- JUnit 5, Mockito
- Swagger (springdoc-openapi)
- Docker / ECR / EC2
- GitHub Actions (CI/CD)
