# MAT.ZIP

- 배포 사이트 >> [http://mat-zip.site/](http://mat-zip.site/) </li>
- PPT >> [https://www.miricanvas.com/v/12yn3gi](https://www.miricanvas.com/v/12yn3gi) </li>
---
# 🍽️ **서비스 소개**
맛집 공유 서비스 MAT.ZIP ([실 사이트](http://mat-zip.site))입니다.
맛집 조회와 리뷰 작성, 어드민 사이트 등을 구현하였습니다.
- 프로젝트
    
    
    | 기간 : 2024. 1. 23 ~ 2024. 3. 11 | 총 4인 (심채원, 유은겸, 김연수, 박선희) |
    | --- | --- |
## 👤 유저
1. 플랫폼이나 카카오를 통해 로그인/회원가입을 할 수 있다.
2. 마이페이지에서 유저 정보를 수정할 수 있다.
3. 사이트에 저장되어 있는 맛집들을 map 위의 마커나 리스트에서 조회한다.
4. 자신이 방문한 맛집의 리뷰를 작성한다.
5. 관심있는 맛집을 찜하여 이후에 해당 맛집 정보를 조회할 수 있다.
6. 사이트에 저장되어 있는 맛집들의 정보 수정 요청을 한다.
## 🧑‍💼 어드민
1. 유저에 어드민 권한을 부여할 수 있다.
2. 사이트에 등록된 맛집들을 등록하거나 정보(상태, 위치, 메뉴, 영업시간)를 수정, 삭제할 수 있다.
3. 사이트에 회원가입한 회원들의 정보를 조회하거나 관련 리뷰, 이미지에 대한 삭제할 수 있다.
4. 유저가 전송한 정보 수정 요청을 처리할 수 있다.
5. matzip 사이트의 홈 화면에 보여질 해시태그들을 설정할 수 있다.
## **🛠 사용기술**
### OS
- Windows
### IDE
- Eclipse, Oracle SQL Developer
### **Frontend**
- Thymeleaf, HTML, CSS, Javascript, Axios
### **Backend**
- Spring boot, Spring security, JPA/Hibernate, Querydsl
### **Infra**
- Server: AWS EC2(ubuntu, Nginx), Route53
- DB: AWS RDS (Oracle)
- Storage: AWS S3
### API
- Kakao map API
- Excel api (Apache POI)
## **✍️ 담당 업무**
### 1️⃣지도 페이지
![image](https://github.com/user-attachments/assets/fb10bf94-a832-4443-b75b-d7a17f8bf1aa)

1. Kakao Map API를 이용하여 해당 음식점들 마커 구현
2. 게시물의 마우스를 올릴 시 지도 부분의 해당 마커의 중심 좌표로 이동
3. 필터링, 검생 기능 구현
</br>

### 2️⃣네이게이션 바
![스크린샷 2024-03-11 095136](https://github.com/user-attachments/assets/11c72a3e-8610-42cc-9ea1-d2d91515f336)

1. 검색 기능
2. 로그인, 로그아웃 시 그 전의 페이지로 리다이렉트

</br>

### 3️⃣검색 페이지
![image](https://github.com/user-attachments/assets/c07df15d-0c45-417a-8df7-0a52d0ea4631)

1. 가게명, 해시태그 카테고리로 필터링
2. 리뷰가 있는 음식점부터 차례대로 배열
</br>

### 4️⃣음식점 디테일 페이지
![Untitled](https://github.com/user-attachments/assets/f21fb639-6ed6-4ee5-808e-e788cbf266db)

1. 음식점 사진 이미지 배열
2. 좋아요, 평가하기 기능
3. 음식점 기본 정보 
4. 지도보기 버튼을 누를 시 모달창으로 표시
5. 영업시간(영업시간 정보가 없을 시 이미지로 대체)
6. 메뉴정보(메뉴정보가 없을 시 이미지로 대체)

   </br>
### 🔗 Output
- AWS 구조도
![aws구조도matzip](https://github.com/JAVA-GIRLS/matzip/assets/114975208/0ed2b2c4-e5dd-4555-b38d-e78157aefc5d)
- github : [GitHub - JAVA-GIRLS/matzip](https://github.com/JAVA-GIRLS/matzip)
- 맛집 사이트
    - [사이트 바로가기](http://mat-zip.site)
    - 어드민 계정 이용
        - 아이디 : admin123
        - 비밀번호 : admin123!
- 발표 PPT 보기 : [matzip](https://www.miricanvas.com/v/12yn3gi)
    
---

