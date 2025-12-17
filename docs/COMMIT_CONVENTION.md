# Commit Convention

본 프로젝트의 커밋 메시지 규칙입니다.

## 커밋 메시지 형식

```
[type] commit message
```

## 커밋 타입

| 타입 | 설명 | 예시 |
|------|------|------|
| `[feature]` | 새로운 기능 추가 | `[feature] define common API response structure` |
| `[add]` | 파일, 엔티티, 의존성 추가 | `[add] entities` |
| `[update]` | 기존 기능 개선 및 수정 | `[update] improve error handling` |
| `[fix]` | 버그 수정 | `[fix] resolve null pointer exception` |
| `[refactor]` | 코드 리팩토링 (기능 변경 없음) | `[refactor] simplify service logic` |
| `[doc]` | 문서 추가 및 수정 | `[doc] Schema added` |
| `[test]` | 테스트 코드 추가 및 수정 | `[test] add user service unit tests` |
| `[config]` | 설정 파일 변경 | `[config] add Swagger configuration` |
| `[chore]` | 빌드, 패키지 관리 등 기타 작업 | `[chore] update dependencies` |

## 커밋 메시지 작성 가이드

### 기본 원칙
- 메시지는 영어로 작성
- 동사 원형으로 시작
- 간결하고 명확하게 작성
- 한 커밋에는 하나의 논리적 변경사항만 포함

### 좋은 예시
```
[feature] add magic link authentication
[add] JWT and email configuration
[config] add Swagger OpenAPI documentation
[fix] resolve cookie secure attribute issue
[refactor] separate domain models from entities
```

### 나쁜 예시
```
[feature] Added new feature
[add] add
[update] changed some files
fixed bug
```

## 커밋 단위

- **기능 단위**: 하나의 완결된 기능을 구현한 경우
- **파일 타입 단위**: 관련된 설정/문서 파일들을 추가한 경우
- **버그 수정 단위**: 특정 버그를 해결한 경우

## 주의사항

- 보안 관련 파일(`.env`, 시크릿 키 등)은 절대 커밋하지 않습니다
- 여러 타입의 변경사항이 있다면 커밋을 분리합니다
- 작업 중인 코드는 커밋하지 않습니다 (빌드 및 테스트 통과 확인)