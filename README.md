### AWS Budget 설정
AWS Budget 설정을 통해 1달에 최대 한도를 100달러로 설정하였고 비용이 일정치가 되면 경고 이메일이 오도록 하였다.
![budget 설정](img1.png)

---
### 네트워크 구축 및 핵심 기능 배포
#### VPC 설계
VPC는 퍼블릭 클라우드 서비스 내에 논리적으로 격리된 고객 전용 사설 네트워크 공간으로 서브넷 구성, IP 주소 대역 설정,
라우팅 테이블 및 네트워크 게이트웨이 등을 직접 제어하여 고도화된 보안 및 맞춤형 인프라를 구축할 수 있는 서비스이다.

VPC를 설정하여 외부에서 직접 접근이 가능한 네트워크 영역인 public subnet과 외부에서 다이렉트로 접근이 불가능한 private subnet으로 나누었다.
![vpc 설정](img2.png)
![vpc 설정](img3.png)

#### EC2 생성
앞에서 설정한 VPC의 public subnet과 연결된 ec2 인스턴스를 생성하였다. 그후 생성된 ec2를 ssh를 사용하여 연결하였다.
![ec2 생성](img4.png)
![ec2 생성](img5.png)

#### 팀원 정보 저장 및 조회 API 개발
티원의 이름, 나이, MBTI를 JSON으로 받아 저장하는 API를 개발하였고
저장된 팀원 정보를 조회하는 API를 개발하였다.

#### Profile 분리
로컬환경에선 H2를 쓰고 운영환경에선 MySQL을 쓰도록 하기 위해 application-local.yml, appliaction-prod.yml을 만들었다.

![profile 분리](img6.png)
![local 환경 결과](img7.png)
![prod 환경 결과](img8.png)

#### 로깅
Controller 단에 요청이 들어오면 INFO 레벨로 로그를 남기도록 하였고
예외발생시 RestControllerAdvice를 사용하여 예외처리를 하도록 하였으며 ERROR 레벨로 스택 트레이스를 남기도록 하였다.

#### 상태 모니터링
spring-boot-starter-actuator 의존성을 추가하여 연결된 서버의 상태를 모니터링 하였다.
```
management.endpoints.web.exposure.include=health
```
/actuator/health라는 url로 서버의 다운 유무를 알수 있다.
![actuator 결과](img9.png)

#### 배포
위의 boot application을 scp를 사용하여 ec2에 올려놓았다.
ec2의 public ip는 43.203.200.229 이다.

---
### DB 분리 및 보안 연결하기
#### Actuator Info 엔드포인트
parameter store에 team-name 값을 저장하였고 /actuator/info 접속시 이 저장된 팀 이름이 출력되도록 하였다.
![acutator info](img11.png)
http://15.165.75.9:8080/actuator/info

#### RDS 보안 그룹 
RDS의 보안 그룹을 EC2의 보안 그룹과 같게 설정하여 EC2와 RDS의 연결을 보장하였다.
![](img10.png)

---
### 프로필 사진 기능 추가와 권한 관리
배포의 환경은 ec2로 ec2에서는 iam access key가 필요가 없고 권한 만으로 s3에 접근이 가능하다.
따라서 iam role을 통해 s3에 접근하고 다운로드/업로드 할 수 있는 권한을 주었고 prod profile에서 iam access key를 저장하는 부분을 삭제하였다.

- [POST]/api/members/{id}/profile-image 함수를 통해 MultiFile로 이미지를 받아 S3 버킷에 업로드하고, 이미지 URL을 DB에 업데이트하는 기능을 만들었다.
- [GET] /api/members/{id}/profile-image 함수를 통해 Presigned URL을 생성하여 반환하도록 하였다.

#### Presigned URL
https://sparta-assignment-sy9999-files.s3.ap-northeast-2.amazonaws.com/uploads/39950b73-6_IMG_8709.jpg?X-Amz-Security-Token=IQoJb3JpZ2luX2VjECkaDmFwLW5vcnRoZWFzdC0yIkgwRgIhAK5vzw2a2qKKhYxNNvcWckz5tHLs7Nfzfx3ZmqwfysJ2AiEAh4JU2cjmKnfj3M7Nhz3K5FxStesvwbwLu4t787MXrTUq0gUI8v%2F%2F%2F%2F%2F%2F%2F%2F%2F%2FARAAGgwyMjY2Mjg1MTA3NjQiDM6ickvqD20URzysuSqmBYCzQny5Epvy2uVA3E9Boj9Xt%2B9BVdo9sxDQsHtB7h41L%2FHsAKJaC5daah5ghoN4807oUlnXNVsTXg%2BbIDMfhJzxNfTSpRXGfkUBtmtZE4e9c785mpUeaIuGl0SY6a4LJNHhL5je1tie5a2IX9lncu0%2Ff%2FTyjy386ncvJ9WBK%2B0YcybPubkCZcgmI6QwwLaGV7LnN6qa5Veqtv1Zv5PBTXqHbER5Fmf3IXi0q3xTTn52ICqOGn5TDX7iG9ExRSeA2CS7LKGZVLsDcIVZIQkks4F37HMAtGtf%2FZAHmlaWPUQN4qaKQIFspcTBj7AW0Wh7dZD4A9ZMn0dcyr%2FfLjNbmm%2B3B3iJR9%2F4tz9RTmsyIoQ1TwH3WGxJsNhWGnjNgN%2BfJXJup%2B837VhiHkgCVH%2B9%2Bb%2BmE4%2FcQTvGy%2B1WWI2NnAO0%2FUy%2B%2B0vjgWPeXmZIkZWDHr0eVmVmOms8rxJFV5nZQitPvnpMFvHSyn2I5roUZUCo%2FhqpUCGFDhMcUFR3tVAUNrXqyj5hw3Pr40NI5T2OOsW%2Fr7gUF3j2EhRt%2Bi5NWjoSjxTR%2BjElLXnfoL07GXG1FJh5GtIMOfdpxRYQmXHa9SNShfpt54IfyelJjXvffMGbf2baqLi7kzlwh1sBfQcE%2BqGvFRvkTv0rXwFR%2F%2FRGajaBYsH%2FxzOItTJdWOAhVrqTdbRA%2BUUNxyUiGQoshoCb2eMazc%2BmKwak%2Ba5oodVNHGkWlFnpOaIw0u%2BFuszIuuxN%2BUvUl9rcc5PFH9fnRLipg%2F9k%2F%2FHx7%2B4gSz02m4x7t4IBZgdwJNYX7mOUzhh12DcJX0Yf8wzw%2Fq2ugG7zaxoOFjRFIv6ks6dG8y6CnN5gpzipgGXvATxbg87VMS11UPuKGiw0CcvOEoMU5pLl11r%2FUqnlCg9FEzCikoXMBjqwAbsgbqrBiV94Xw60M3VoErmb7%2BM9Fi4bcqUDJtMBVsHJa%2FoDZdTSHwx6go2nKESduP2My3kxgIgeuJM%2FmQUUVuDMKcpT7CPPGXOnPbio2E%2BgRNsX2H%2BqDA83yHPkLX%2ByZmy42FcVKBVF7GczIczaqFyEDzjGNGYS1TwXhkW8U%2BittEvjX%2FdZi1v6rwor3oDf58tqtLeyVPlM88W6nSMGvy8cwCEmGiyM9TBasJ63s8or&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20260203T011054Z&X-Amz-SignedHeaders=host&X-Amz-Credential=ASIATJRA4UQWP2KGZGG5%2F20260203%2Fap-northeast-2%2Fs3%2Faws4_request&X-Amz-Expires=604800&X-Amz-Signature=9d502e4dc842bf3b0fe152643235c0d98fe3ab407e1bb9d557f9d051bf054d03
위 URL은 2/9일 까지 유효하다.


