# my_2024_2025_project
# Restaurant Reservation Program

## 프로젝트 개요

### 개발 환경
- **IDE**:
    - IntelliJ: 백엔드 개발과 클린 아키텍처 구조 설계에 사용
    - VSCode: 프론트엔드 개발 및 TypeScript 기반 React 프로젝트 작업에 최적화
- **서버 환경**:
    - React와 Node.js를 기반으로 프론트엔드 서버 구성
    - Spring Boot로 빌드된 백엔드 JAR 파일 하나로 API와 비즈니스 로직 처리
    - 데이터베이스: 사용자 정보, 공통 코드, 예약 정보를 관리하는 3개의 스키마로 구성된 Local DB
    - Jenkins를 활용한 CI/CD 서버로 배포 자동화 및 모니터링
    - Docker를 활용해 모든 서버를 컨테이너화하여 효율적인 배포 환경 구축

### 백엔드
- **기술 스택**:
    - Java 17: 최신 버전의 안정성과 성능을 활용
    - Spring Boot: RESTful API 구현과 대규모 애플리케이션 아키텍처 설계를 위한 프레임워크
    - HeidSQL: 데이터베이스 관리 도구로, 사용자 DB, 공통 코드 DB, 예약 DB를 로컬에서 관리
    - **데이터베이스 설계**:
        - 인사 DB: 사용자 로그인 및 역할 관리
        - 공통 코드 DB: 다양한 공통 데이터를 효율적으로 제공
        - 예약 관련 DB: 예약 정보와 상태를 저장 및 관리
    - **아키텍처**:
        - 클린 아키텍처 기반으로 모듈화된 코드 구조
        - 도메인 중심 설계로 유지보수성과 확장성을 강화
    - **주요 기능**:
        - JWT를 사용하여 사용자 인증 및 권한 부여
        - MyBatis와 JPA를 조합하여 복잡한 쿼리와 ORM 기반 데이터 처리를 효율적으로 수행
        - MapStruct를 사용하여 DTO와 엔티티 간의 변환 로직 자동화
        - Redis 캐시를 활용해 공통 코드 데이터를 저장 및 빠르게 제공
    - **품질 관리 도구**:
        - SonarLint: 코드 품질 분석 및 잠재적 버그 식별

### 프론트엔드
- **기술 스택**:
    - Node.js (최신 버전): 서버사이드 렌더링과 의존성 관리를 위해 사용
    - React 5.x: 사용자 인터페이스를 위한 라이브러리로 최신 기능 사용
    - TypeScript: 정적 타입 검사를 통해 오류를 사전에 방지
    - Zustand: React 상태 관리를 단순화하고 효율적으로 수행
    - React Router Outlet: 라우팅과 페이지 구성을 효율적으로 처리
    - **프로젝트 구조**:
        - 모노레포로 구성하여 관리자와 사용자 페이지를 분리
        - 공통 컴포넌트 및 유틸리티를 별도 패키지로 관리
    - **특징**:
        - 관리자 페이지: 예약 관리, 사용자 관리 및 시스템 설정
        - 사용자 페이지: 레스토랑 예약 및 상태 확인

### CI/CD
- **도구**:
    - Jenkins: 지속적 통합 및 배포 자동화 구현
    - Docker: 컨테이너화된 애플리케이션을 빌드 및 배포
- **배포 파이프라인**:
    - Git 커밋 시 자동으로 빌드 및 테스트 실행
    - Docker 이미지를 생성하여 프론트엔드와 백엔드를 각각 컨테이너로 패키징
    - Jenkins를 통해 프로덕션 서버에 자동 배포 및 모니터링