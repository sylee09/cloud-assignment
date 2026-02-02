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
http://43.203.200.229:8080/actuator/info

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
https://sparta-assignment-sy9999-files.s3.ap-northeast-2.amazonaws.com/uploads/0fc90d33-a887-4b1f-a50d-36bdffac4894_IMG_8709.jpg?X-Amz-Security-Token=IQoJb3JpZ2luX2VjEBsaDmFwLW5vcnRoZWFzdC0yIkcwRQIhALUjA4ueyywm9gAUWOK%2FpNLGAjdv57QIf1MVo2LfLP%2BSAiB%2B%2F3unREIt3p0o64AdQlS2heDyufRqUu%2F25yo53Co6cyrSBQjk%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F8BEAAaDDIyNjYyODUxMDc2NCIMXEpOnEeUrcC4z7KtKqYFva8u20Gozd8NOcmoiDNw%2BtU9wtQ1iHvtfQAWrWWvErSVvy1i2C18euMrMnigjwLfrgq60pcf%2BtTGPFdlE8%2FTaHnsV4KcEDkyhdzCMA2P%2BObTUNhEJCl1R8qNJ5TF265%2F4yQz%2BBhnPOMEFLM2i%2BTLxbrzc11VS%2Buxt3ygbtPqealjtCZwUvu8xpGBKSjfgao4F%2BbVfTp0EADtOnkPa95zE9WltzB7JNpkq69%2BJ2F134%2BU5JvBD14G9DU8AV8nVQz3BFJTgdHjpmSoxtfJ4wgBTyHBDHEiuZrASm%2B2syXEktVDfSj5NtipCPJ6%2FPU4wpRJ5a41Vo1uwviA1gh7OHeoHcsOarqO7WEaVO4mFNKuX2mdi%2Bf617A4R0HGZ%2FiQy4nP3J8fL4NMzutFwepWymC72Io6zLs5TWF7JT8FEjmN70vcVTlBlDG2wMJZJzrj9MW55Bd%2BiEAuf946dxWg6glax5XaYBEXdck2sG77CHed4xOwAP1DMPl2IHp3a3h8b7IbCkC78%2Bdg6Q9HTMcwBOHLwVrzOAfJuPJwPVcmhIgsg8f3XMUjo6P6G6sa784%2B1cPDiNTvSTCgZaZzQh4%2BDnb%2FtWy6BFUsmXKpJVRz6ryWuwYHD1JLDzsdtJm012W0SzjZWOg1B2j%2BcuDMesLXaaj3WFyDvVY0nU0F6xBLDViV7WsX0OlRmO0qaTLTJU%2FfS7a9L03Lf%2FQdL1JkS7Bq%2B1A5k88YxVbhW4lSc7E3vCWEsPwGQhssT1ochHMTr1b8qRfBTYZjQtw%2BzmpSOVwai5kDiHWZmTwODh2l30jTeSAvH5yqD5utzFovuq%2BHOnOgX%2FvlEYysE%2FpN56Wz6mgpHLTh0Hjf5Y2OnQJNIpULTQoPX3avOhhj7YTCMNn%2FdHYsO7PqUletldOIMJOLgswGOrEB4aQnLGYDMsDho1SxGOigtw8vCuacSuHcvToxDZhtRgYOjAmg%2FnJ3%2BNLxkKwEABUizWARLqpl9temOk0QUmJ0CVNggBYE9p8a5QCc4W18ut1nJHUgisZYTI2k69L7CFhWDbK%2F%2BunIE2SaDAGWXTlQE7w7HBmZ3catwLSELzrsd6cwUUqLKlpObS%2Fkc7HJI7l6k2VTM2Xi%2BGVOJP7Ygf%2BsWZzT7x1XEYNonaZe8DEQu5Hi&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20260202T113129Z&X-Amz-SignedHeaders=host&X-Amz-Credential=ASIATJRA4UQWDC7IP6JC%2F20260202%2Fap-northeast-2%2Fs3%2Faws4_request&X-Amz-Expires=604800&X-Amz-Signature=08254db50ae754e1123d551a5365f158468cf64fbb1f0a8c2ce896b560dfb13a

위 URL은 2/9일 까지 유효하다.


