# Fantry Backend

Fantry 백엔드 프로젝트입니다.

## 🚀 프로젝트 구조

- `.github/workflows`: CI/CD를 위한 GitHub Actions 워크플로우 파일이 위치합니다.
- `src/main`: 애플리케이션 소스 코드가 위치합니다.
- `build.gradle`: 프로젝트의 의존성 및 빌드 설정 파일입니다.

## ⚙️ CI/CD

이 프로젝트는 GitHub Actions를 사용하여 CI/CD 파이프라인을 구축했습니다.

- **Develop Branch**: `develop` 브랜치에 코드가 푸시되면 개발(dev) 서버에 자동으로 배포됩니다. (`.github/workflows/deploy-dev.yml`)
- **Main Branch**: `main` 브랜치에 코드가 푸시되면 운영(prod) 서버에 자동으로 배포됩니다. (`.github/workflows/deploy-prod.yml`)

## 🌿 브랜치 전략 및 기여 방법

- **Default Branch**: `develop`
- **Contribution Workflow**:
    1.  기능 개발을 위한 새로운 브랜치를 생성합니다. (e.g., `feature/my-new-feature`)
    2.  해당 브랜치에서 작업을 완료하고 코드를 푸시합니다.
    3.  `develop` 브랜치로 Pull Request(PR)를 생성합니다.
    4.  코드 리뷰 후 `develop` 브랜치에 머지됩니다.

## 🔑 시크릿 키 관리

API 키와 같은 민감한 정보는 GitHub Repository의 `Settings > Secrets and variables > Actions`에 등록하여 사용합니다. 코드에 직접 키를 하드코딩하지 않도록 주의해주세요.

## ⚙️ 설정 파일 (`.properties`)

프로젝트는 다양한 환경에 대한 설정을 `.properties` 파일을 통해 관리합니다.

- `application.properties`: 모든 환경에서 공통으로 사용되는 기본 설정 파일입니다.
- `application-dev.properties`: 개발(dev) 환경을 위한 설정 파일입니다.
- `application-prod.properties`: 운영(prod) 환경을 위한 설정 파일입니다.
- `application-local.properties.example`: 로컬 개발 환경 설정 예시 파일입니다. `application-local.properties` 파일을 생성하여 사용하세요. (이 파일은 git에 의해 추적되지 않습니다.)
- `application-local-dev.properties.example`: 로컬에서 개발 서버 환경을 테스트하기 위한 설정 예시 파일입니다.

## 📚 주요 의존성

이 프로젝트는 다음과 같은 주요 라이브러리 및 프레임워크를 사용합니다.

- **Spring Boot**:
    - `spring-boot-starter-web`: 웹 애플리케이션 개발을 위한 기본 의존성.
    - `spring-boot-starter-data-jpa`: 데이터베이스 연동을 위한 JPA 관련 의존성.
    - `spring-boot-starter-security`: 인증 및 권한 부여를 위한 Spring Security.
    - `spring-boot-starter-validation`: 데이터 유효성 검증.
    - `spring-boot-starter-actuator`: 애플리케이션 모니터링 및 관리.
- **Database**:
    - `mysql-connector-j`: MySQL 데이터베이스 드라이버.
    - `h2`: 테스트용 인메모리 데이터베이스.
- **Lombok**: `Getter`, `Setter`, `Constructor` 등 반복적인 코드를 줄여주는 라이브러리.
- **Testing**:
    - `spring-boot-starter-test`: Spring Boot 애플리케이션 테스트를 위한 기본 의존성.
    - `spring-security-test`: Spring Security 관련 테스트 지원.
