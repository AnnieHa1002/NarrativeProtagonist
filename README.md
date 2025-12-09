# NarrativeProtagonist

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9+-purple.svg)](https://kotlinlang.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green.svg)](https://spring.io/projects/spring-boot)

**인터랙티브 스토리 제작을 위한 오픈소스 백엔드 엔진**

복잡한 분기형 스토리를 구조화하고 관리할 수 있는 API 기반 플랫폼입니다.

---

## 💡 Why This Project?

게임 시나리오, 특히 분기형 스토리를 작성하다 보면:

- 선택지가 늘어날수록 **어떤 루트가 어디로 연결되는지 추적하기 어렵다**
- 엑셀이나 텍스트 파일로는 **복잡한 분기 구조를 관리할 수 없다**
- 기존 비주얼 노벨 툴들은 **게임 제작에만 집중**되어 있고, 순수하게 **인터랙티브 스토리 작성**에는 과도하다
- 조건부 분기, 아이템, 변수 시스템을 **체계적으로 설계하고 테스트할 도구**가 부족하다

**NarrativeProtagonist**는 작가 중심의 인터랙티브 스토리 제작 백엔드입니다.

프론트엔드와 분리된 API 기반 설계로, 다양한 클라이언트(웹, 모바일, CLI)에서 활용 가능하며,
복잡한 분기 로직을 구조화하고 버전 관리할 수 있도록 설계되었습니다.

### 기존 솔루션과의 차별점

- ✅ **백엔드 API로 분리** - 어떤 UI든 자유롭게 붙일 수 있음
- ✅ **Variable/Item 시스템 분리** - 스토리 상태 관리가 명확함
- ✅ **Random Event 체계화** - 조건 + 확률 기반 이벤트 관리
- ✅ **버전 관리** - 독자 세션과 작가 수정 분리
- ✅ **다양한 Export 형식** - JSON, HTML, Ren'Py, Ink 지원 예정
- ✅ **오픈소스 & 무료** - Self-hosted 가능

---

## ✨ Key Features

### 🏠 Multi-tenant Architecture
- 유저별 Sandbox(작업 공간) 자동 생성
- Private/Public 프로젝트 관리
- JWT 기반 인증 시스템

### 🧩 Flexible Node System
- **ENTRY** - 스토리 시작점
- **SCENE** - 단일 장면 (자동 진행)
- **BRANCH** - 선택지 분기
- **ENDING** - 스토리 종착점

### 🎒 Rich State Management
- **Variable** - 수치/문자열 기반 상태 (health, age, respect 등)
- **Item** - 아이템 소지 및 효과 시스템
- **Effects** - 노드 방문 시 자동 실행되는 상태 변경

### 🎲 Random Event System
- 조건(Conditions) + 확률(Probability) 기반
- SCENE 노드에서 다음 노드로 넘어가기 전 발동
- Tag 기반 이벤트 트리거

### 🔖 Version Management
- Publish 시점에 전체 스토리 스냅샷 저장
- 독자가 읽는 동안 작가의 수정사항 영향 없음
- 독자별 진행 상태(Session) 독립 관리

### 📦 Multiple Export Formats
- **JSON** - 범용 데이터 형식
- **Standalone HTML** - 브라우저에서 바로 플레이 가능
- **Ren'Py Script** - 비주얼 노벨 제작용 (예정)
- **Ink Script** - Unity 게임 개발용 (예정)

---

## 🎯 Use Cases

### Self-Hosted Story Platform
팀이나 조직 내부에서 자체 인터랙티브 픽션 플랫폼 운영

### Public Service
누구나 가입 가능한 스토리 작성 플랫폼으로 운영

### Game Development Backend
- 비주얼 노벨, 텍스트 어드벤처 게임의 스토리 엔진
- Unity + Ink 워크플로우
- Ren'Py 프로젝트 통합

### Writing Tool
복잡한 분기형 시나리오 작성 및 실험

---

## 🚀 Quick Start

### Prerequisites
- JDK 17+
- Docker & Docker Compose
- Gradle 8+

### Installation
```bash
# Clone repository
git clone https://github.com/yourusername/NarrativeProtagonist.git
cd NarrativeProtagonist

# Start infrastructure (PostgreSQL, Redis)
docker-compose up -d

# Run application
./gradlew bootRun
```

### First API Call
```bash
# Health check
curl http://localhost:8080/actuator/health

# Create user (signup)
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{"username":"writer","email":"writer@example.com","password":"password123"}'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"writer@example.com","password":"password123"}'
```

---

## 🏗️ Tech Stack

| Category | Technology |
|----------|-----------|
| **Language** | Kotlin |
| **Framework** | Spring Boot 3 |
| **Database** | PostgreSQL |
| **ORM** | JPA / Exposed |
| **Cache** | Redis (예정) |
| **Auth** | JWT |
| **Build** | Gradle (KTS) |
| **Containerization** | Docker |

---

## 📖 Documentation

- [Architecture Guide](docs/ARCHITECTURE.md) - 시스템 구조 및 ERD
- [API Documentation](docs/API.md) - 엔드포인트 명세 (Swagger 예정)
- [Setup Guide](docs/SETUP.md) - 상세 설치 및 개발 환경 구축
- [Contributing Guide](CONTRIBUTING.md) - 기여 방법

---

## 📁 Project Structure
```
narrative-protagonist/
 ├─ src/
 │   └─ main/kotlin/com/narrativeprotagonist/
 │       ├─ domain/          # 도메인 로직
 │       │   ├─ user/
 │       │   ├─ sandbox/
 │       │   ├─ project/
 │       │   ├─ node/
 │       │   ├─ item/
 │       │   ├─ variable/
 │       │   ├─ condition/
 │       │   ├─ effect/
 │       │   └─ random/
 │       ├─ application/     # 유스케이스
 │       ├─ api/             # REST API
 │       └─ infra/           # 인프라 구현
 ├─ docs/                    # 문서
 ├─ docker-compose.yml
 ├─ build.gradle.kts
 └─ README.md
```

---

## 🗺️ Roadmap

### v0.1 — Core Domain Setup (🚧 In Progress)
- [x] Sandbox / User / Project 기본 구조
- [ ] Entry / Scene / Branch / Ending 타입 설계
- [ ] JWT 인증 시스템

### v0.2 — Item / Variable / Effect System
- [ ] Effect engine 구현
- [ ] Conditions 엔진 기본 버전

### v0.3 — Random Event System
- [ ] SCENE 기반 확률 분기
- [ ] Condition + Probability 조합

### v0.4 — Publish & Versioning
- [ ] NodeSet 버전 스냅샷 저장
- [ ] Reader 세션 구조 구축

### v0.5 — Export System (Phase 1)
- [ ] JSON Export
- [ ] Standalone HTML Export

### v0.6 — Export System (Phase 2)
- [ ] Ren'Py Script Export
- [ ] Ink Script Export (Unity)

### v1.0 — Full Release
- [ ] API 문서화 (Swagger/OpenAPI)
- [ ] 샘플 스토리 제공
- [ ] Docker 이미지 배포 (Docker Hub)
- [ ] 통합 테스트 & CI/CD

---

## 🤝 Contributing

기여는 언제나 환영합니다!

1. 이 저장소를 Fork 하세요
2. Feature 브랜치를 생성하세요 (`git checkout -b feature/AmazingFeature`)
3. 변경사항을 커밋하세요 (`git commit -m 'Add some AmazingFeature'`)
4. 브랜치에 Push 하세요 (`git push origin feature/AmazingFeature`)
5. Pull Request를 열어주세요

자세한 내용은 [CONTRIBUTING.md](CONTRIBUTING.md)를 참고해주세요.

### Good First Issues
- 라벨이 `good first issue`인 이슈들을 확인해보세요
- 궁금한 점은 GitHub Discussions에서 질문해주세요

---

## 📞 Contact & Community

- **Author**: Annie Ha
- **Blog**: [dev-annieha.tistory.com](https://dev-annieha.tistory.com)
- **Issues**: [GitHub Issues](https://github.com/yourusername/NarrativeProtagonist/issues)
- **Discussions**: [GitHub Discussions](https://github.com/yourusername/NarrativeProtagonist/discussions)

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 🙏 Acknowledgments

인터랙티브 픽션과 게임 개발 커뮤니티에 감사드립니다.

---

**⚠️ 현재 상태: Work In Progress**

이 프로젝트는 활발히 개발 중입니다.
Production 환경에서 사용하기 전에 충분한 테스트를 거쳐주세요.