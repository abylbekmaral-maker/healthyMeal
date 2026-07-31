# Healthy Meal

🌐 **언어**

- [English](README.md)
- [Русский](README_RU.md)
- 한국어

Healthy Meal은 사용자의 건강 정보를 바탕으로 맞춤형 식단을 추천하여 건강한 식습관을 도와주는 웹 애플리케이션입니다.

##  주요 기능

- 개인 맞춤형 주간 식단 제공
- 건강 상태와 목표를 고려한 식단 추천
- 장보기 목록 자동 생성
- 일일 건강 상태 기록
- 다국어 지원 (한국어, 영어, 러시아어)
- 모바일 반응형 인터페이스

## ⚙️ 사용 기술

- **Frontend:** HTML5, CSS3, JavaScript (ES6)
- **Backend:** Java 17, Spring Boot 3.2.0, Spring Data JPA
- **Database:** MySQL (Aiven Cloud)
- **REST API:** Spring Web
- **Validation:** Spring Validation
- **Build Tool:** Gradle
- **Deployment:** Render
- **Version Control:** Git, GitHub

##  시스템 구조

Browser → Spring Boot (REST API) → Spring Data JPA → MySQL (Aiven Cloud)
애플리케이션은 Render에 배포되었습니다.

## 💬 테스트 방법

> ⚠️ 처음 접속 시 서버가 시작되는 데 2~3분 정도 걸릴 수 있습니다. 페이지가 모두 로드될 때까지 기다려 주세요.

1. 아래 링크에서 애플리케이션을 실행합니다.
2. 로그인하거나 회원가입을 합니다.
3. 프로필 정보를 입력합니다.
4. **Planner**에서 주간 식단을 생성합니다.
5. **Home**에서 맞춤형 식단 추천을 확인합니다.

🔗 **사용자 매뉴얼(PDF):**  
[사용자 매뉴얼(PDF)](demo2/docs/healthyMeal_사용자_설명서.pdf)

## 프로젝트 링크

- **Live Demo:** https://healthymeal.onrender.com
- **GitHub Repository:** https://github.com/abylbekmaral-maker/healthyMeal