# 예외 처리 가이드

## 비교표: 자바 vs 코틀린 예외 처리

| 특징 | 자바 스타일 (Enum) | 코틀린 스타일 (Sealed Class) |
|------|-------------------|----------------------------|
| **타입 안정성** | ❌ 런타임에만 확인 | ✅ 컴파일 타임에 확인 |
| **when 완전성 체크** | ❌ 불가능 | ✅ 가능 (exhaustive) |
| **파라미터 전달** | ❌ 제한적 | ✅ 각 예외마다 다른 파라미터 |
| **IDE 지원** | ⚠️ 보통 | ✅ 뛰어남 (자동완성, 리팩토링) |
| **확장성** | ⚠️ Enum에 추가만 | ✅ 자유롭게 상속 구조 |
| **data class 활용** | ❌ 불가능 | ✅ 가능 |

## 코틀린 Sealed Class를 사용하는 이유

### 1. 타입 세이프
```kotlin
// ❌ 자바 스타일 - 런타임에만 체크
throw BusinessException(ExceptionCode.USER_NOT_FOUND)
// userId를 어떻게 전달? 생성자에 추가 파라미터?

// ✅ 코틀린 스타일 - 컴파일 타임에 체크
throw BusinessException.UserNotFound("user-123")
// userId가 타입에 포함되어 있음!
```

### 2. when 표현식의 완전성 체크 (Exhaustive)
```kotlin
// ✅ 새 예외 추가하면 컴파일 에러!
fun handleException(e: BusinessException) = when (e) {
    is BusinessException.UserNotFound -> "사용자 없음"
    is BusinessException.DuplicateEmail -> "이메일 중복"
    // ProjectNotFound를 추가했는데 여기 안 쓰면 컴파일 에러!
}
```

### 3. 각 예외마다 다른 데이터
```kotlin
// ✅ 예외마다 필요한 정보만 담음
data class UserNotFound(val userId: String)
data class UnauthorizedAccess(val userId: String, val resourceId: String)
data object TokenExpired  // 파라미터 없는 예외
```

### 4. 계층 구조
```kotlin
sealed class BusinessException {
    // User 도메인
    sealed class UserException : BusinessException()
    data class UserNotFound(...) : UserException()

    // Auth 도메인
    sealed class AuthException : BusinessException()
    data class InvalidToken(...) : AuthException()
}

// 도메인별로 묶어서 처리 가능!
```

## 사용 가이드

### 예외 발생
```kotlin
@Service
class UserService {
    fun getUser(id: String): User =
        userRepository.findById(id)
            ?: throw BusinessException.UserNotFound(id)

    fun createUser(email: String) {
        if (userRepository.existsByEmail(email)) {
            throw BusinessException.DuplicateEmail(email)
        }
    }
}
```

### 예외 응답 (자동)
```json
{
  "success": false,
  "message": "사용자를 찾을 수 없습니다",
  "data": {
    "code": "USER_001",
    "message": "사용자를 찾을 수 없습니다",
    "details": {
      "userId": "user-123"
    }
  },
  "timestamp": 1234567890
}
```

### 새로운 예외 추가 (3단계)

#### 1. BusinessException에 추가
```kotlin
sealed class BusinessException {
    data class SandboxLimitExceeded(
        val userId: String,
        val currentCount: Int,
        val maxCount: Int
    ) : BusinessException(
        code = "SANDBOX_002",
        status = HttpStatus.FORBIDDEN,
        message = "Sandbox limit exceeded: $currentCount/$maxCount",
        messageKey = "error.sandbox.limitExceeded"
    )
}
```

#### 2. messages.properties에 추가
```properties
error.sandbox.limitExceeded=샌드박스 생성 한도를 초과했습니다
```

#### 3. 사용
```kotlin
if (sandboxCount >= MAX_SANDBOXES) {
    throw BusinessException.SandboxLimitExceeded(userId, sandboxCount, MAX_SANDBOXES)
}
```

끝! GlobalExceptionHandler가 자동으로 처리합니다.

## 다국어 지원

### 한국어 (기본)
```
GET /api/users/invalid-id
Accept-Language: ko

→ "사용자를 찾을 수 없습니다"
```

### 영어
```
GET /api/users/invalid-id
Accept-Language: en

→ "User not found"
```

## 로깅

BusinessException은 자동으로 로그에 기록됩니다:
```
WARN  c.n._global.exception.GlobalExceptionHandler - BusinessException: [USER_001] User not found: user-123
```

## 모니터링

에러 코드로 집계 가능:
- `USER_001`: 사용자 not found 빈도
- `AUTH_002`: 토큰 만료 빈도
- `PROJECT_002`: 권한 없음 시도 빈도

## 결론

✅ **코틀린 Sealed Class 방식 사용 권장!**

- 타입 안정성 ↑
- 컴파일 타임 체크 ↑
- IDE 지원 ↑
- 유지보수성 ↑
- 확장성 ↑
