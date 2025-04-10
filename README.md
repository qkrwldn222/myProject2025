# Restaurant Reservation Program

## 1. 프로젝트 개요

### 개발 목표
- 테이블링 앱을 벤치마킹하여 맛집 예약 및 줄서기(대기) 기능을 구현
- 동시성을 고려한 안정적인 예약 시스템 구축
- JWT 기반 인증 및 권한 관리 (Spring Security 적용)
- Redis를 활용한 실시간 대기열 관리 및 캐싱
- 관리자 및 사용자 전용 프론트엔드 분리
- 결제 시스템 연동 및 예약 취소/환불 로직 구현
- CI/CD 자동화를 통한 배포 및 운영 최적화

### 개발 환경

#### 1) 백엔드
- **언어**: Java 17
- **프레임워크**: Spring Boot 3.x
- **DB**: mariadb
- **ORM**: JPA + MyBatis
- **캐시**: Redis(대기열, 토큰 관리) , Spring Cache  (코드, 유저 등)
- **API 문서화**: Swagger 3.x (OpenAPI)
- **로그 관리**: Logback + ELK(Stack)
- **인증/권한 관리**: Spring Security + JWT
- **메시지 브로커**: Kafka (예약 알림 및 상태 업데이트)

#### 2) 프론트엔드
- **프레임워크**: React 18 (TypeScript 적용)
- **상태 관리**: Zustand
- **라우팅**: React Router
- **HTTP 통신**: Axios

#### 3) 배포 및 CI/CD
- **CI/CD**: Jenkins
- **컨테이너화**: Docker 
---

## 2. 주요 기능 (시나리오 별도 문서화 예정)

### 사용자 기능
- 회원가입 / 로그인 (JWT 기반 인증)
- 소셜 로그인 지원 (Google, Kakao, Naver / 안 할지도 모름)
- 레스토랑 검색 및 예약
- 대기열 등록 및 실시간 상태 확인 (SSE 활용)
- 예약 확인 및 취소
- 리뷰 및 평점 작성

### 운영자(가게) 기능
- 가게 정보 관리 (운영 시간, 좌석 수 설정 등)
- 실시간 예약 및 대기열 확인
- 예약 승인 및 거절 (자동 승인 옵션 지원)
- 방문 완료 처리 및 노쇼 관리

### 관리자 기능
- 전체 예약 및 대기열 모니터링
- 사용자 관리 (차단, 권한 부여 등)
- 통계 대시보드 (예약 현황 분석, 인기 맛집 데이터 시각화)
- 운영자 계정 등록 및 관리

---

## 3. 서버 아키텍처

### 구성 요소
- **Redis**: JWT 세션 관리, 대기열 캐싱
- **Backend (Spring Boot)**: REST API 제공, 예약 및 사용자 로직 처리, 비즈니스 로직 실행
- **MySQL (DB)**: 사용자, 예약, 가게 데이터 관리 (최적화된 인덱싱 적용)
- **Jenkins**: CI/CD 자동화 (자동 빌드 및 배포)
- **Frontend (User/Admin)**: React 기반 웹 애플리케이션
- **SSE**: 실시간 대기열 및 예약 상태 업데이트

---

## 4. 기타
- **API 명세서**: OpenAPI 기반 자동 문서화 (Swagger UI 제공)
- **사용자 시나리오 문서**: 예약, 대기, 결제 흐름을 상세 정의한 플로우 차트
- **데이터 모델 정의서**: MySQL 테이블 및 관계 정의 (ERD 포함)
- **배포 가이드**: Jenkins + Docker 배포과정 문서화
  
## 5. 데이터 베이스
https://github.com/users/qkrwldn222/projects/1/views/3?pane=issue&itemId=101279944&issue=qkrwldn222%7CmyProject2025%7C16
