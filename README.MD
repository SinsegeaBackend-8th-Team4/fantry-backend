# Fantry

<p align="center">
  <!-- 프로젝트 로고가 있다면 여기에 추가하세요 -->
  <!-- <img src="URL_TO_YOUR_LOGO" alt="Fantry Logo" width="200"/> -->
</p>

<h3 align="center">아티스트 굿즈 C2B2C 이커머스 플랫폼</h3>

<p align="center">
  <img src="https://img.shields.io/badge/build-passing-brightgreen" alt="Build Status">
  <img src="https://img.shields.io/badge/Java-21-orange" alt="Java Version">
  <img src="https://img.shields.io/badge/Spring%20Boot-3.x-green" alt="Spring Boot Version">
  <img src="https://img.shields.io/badge/license-MIT-lightgrey" alt="License">
</p>

---

## 📖 프로젝트 소개 (About)

기존 개인간 중고 굿즈 거래 시장에는 **가격 책정의 어려움**, **진위 여부 판별의 불확실성**, **상품 상태에 대한 주관적 평가**, **거래 과정의 불편함** 등 판매자와 구매자 모두를 지치게 하는 여러 문제점들이 존재합니다.

**Fantry**는 이러한 문제점을 해결하기 위해, **회사가 직접 상품을 매입하여 전문적으로 검수하고 판매**하는 C2B2C 모델의 통합 거래 플랫폼입니다.

전문적인 검수 과정과 표준화된 상품 관리를 통해, 판매자에게는 **즉시 현금화의 편의성**을, 구매자에게는 **검증된 상품과 합리적인 가격**을 제공합니다. Fantry는 이를 통해 양측 모두의 거래 스트레스를 최소화하고, 신뢰할 수 있는 새로운 굿즈 중고거래 생태계를 구축하는 것을 목표로 합니다.


## ✨ 주요 기능 (Features)

- **전문 검수 시스템**: 회사가 직접 상품을 검수하여 진위 여부와 상태를 보증합니다.
- **빠른 정산 시스템**: 판매자는 상품 판매 후 즉시 현금화가 가능하여 거래 편의성이 높습니다.
- **표준화된 상품 정보**: 모든 상품은 표준화된 기준에 따라 관리되어, 구매자는 객관적인 정보로 구매를 결정할 수 있습니다.
- **다양한 거래 방식**: 일반 판매뿐만 아니라, 희소성 있는 굿즈를 위한 경매 기능도 제공합니다.
- **통합 고객 지원**: 공지사항, FAQ, 1:1 문의 등 원활한 서비스 이용을 위한 고객 지원 채널을 운영합니다.


## 🛠️ 기술 스택 (Tech Stack)

### Backend
- **Language**: `Java 21`
- **Framework**: `Spring Boot 3.x`
- **ORM**: `JPA (Hibernate)`
- **Build Tool**: `Gradle`

### Database
- **RDBMS**: `MySQL`
- **In-Memory DB**: `Redis`
- **Migration**: `Flyway`

### DevOps & Tools
- **VCS**: `Git`, `GitHub`
- **IDE**: `IntelliJ IDEA`, `Visual Studio Code`


## 🚀 시작하기 (Getting Started)

### 사전 요구사항

- Java 21
- Gradle
- MySQL / Redis

### 설치 및 실행

1. **프로젝트 클론**
   ```bash
   git clone [YOUR_GIT_REPOSITORY_URL]
   cd fantry
   ```

2. **데이터베이스 설정**
   - `src/main/resources/application.properties` 또는 `application.yml` 파일에 자신의 MySQL 및 Redis 데이터베이스 정보를 입력합니다.
   - Flyway가 애플리케이션 실행 시 `src/main/resources/db/migration` 경로의 SQL 파일을 읽어 자동으로 DB 마이그레이션을 수행합니다.

3. **애플리케이션 실행**
   ```bash
   ./gradlew bootRun
   ```
   이제 `http://localhost:8080`에서 애플리케이션이 실행됩니다.


## 📁 프로젝트 구조 (Project Structure)

```
src
└── main
    └── java
        └── com
            └── eneifour
                └── fantry
                    ├── common          # 📦 공통 모듈 (Exception, Util 등)
                    │   ├── exception   # 🛡️ 전역 예외 처리
                    │   └── util        # 🛠️ 유틸리티 클래스 (파일 관리 등)
                    ├── cs              # 📞 고객 지원 도메인
                    ├── member          # 👤 회원 도메인
                    ├── auction         # ⚖️ 경매/거래 도메인
                    └── ...             # ➕ 기타 비즈니스 도메인
```

## 🛡️ 예외 처리 전략 (Exception Handling)

본 프로젝트는 일관성 있는 에러 응답을 위해 체계적인 예외 처리 전략을 사용합니다.

1.  **Custom Exception**: 각 도메인 또는 기능에 맞는 커스텀 예외(`FileException` 등)를 정의합니다.
2.  **Error Code Enum**: HTTP 상태, 고유 코드, 메시지를 담은 `Enum`(`FileErrorCode` 등)을 통해 에러를 체계적으로 관리합니다.
3.  **@RestControllerAdvice**: `GlobalExceptionHandler`가 애플리케이션 전역에서 발생하는 특정 예외를 감지하여 중앙 처리합니다.
4.  **ErrorResponse**: 모든 에러는 아래와 같은 일관된 `ErrorResponse` JSON 형식으로 클라이언트에게 반환됩니다.

```json
{
    "timestamp": "2023-10-27T10:30:00.123456",
    "status": 404,
    "error": "Not Found",
    "code": "F001",
    "message": "파일을 찾을 수 없습니다."
}
```

## 🤝 기여자 (Contributors)

<!-- 팀원들의 GitHub 프로필을 추가하여 기여자를 표시할 수 있습니다. -->
<!--
<a href="https://github.com/your-github-username">
  <img src="https://contrib.rocks/image?repo=your-repo/fantry" />
</a>
-->

## 📜 라이선스 (License)

본 프로젝트는 [MIT License](LICENSE)를 따릅니다.
