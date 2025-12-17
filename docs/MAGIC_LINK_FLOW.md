# Magic Link Login 구현 가이드

## 전체 플로우

```
┌──────────────┐
│  프론트엔드   │
│  (로그인창)  │
└──────┬───────┘
       │ 1. POST /api/auth/request-login
       │    { "email": "user@example.com" }
       ↓
┌──────────────┐
│   백엔드     │ → 이메일 발송 (매직 링크)
└──────┬───────┘
       │ Response: { "loginTokenId": "abc-123", "message": "이메일 확인" }
       ↓
┌──────────────┐
│  프론트엔드   │ → Polling 시작 (2초마다)
│  (로그인창)  │    GET /api/auth/login-status/abc-123
└──────┬───────┘
       │
       │ ┌─────────────────────────────────┐
       │ │  사용자가 이메일의 링크 클릭    │
       │ │  GET /api/auth/verify-login?token=xyz │
       │ └─────────────┬───────────────────┘
       │               ↓
       │         ┌──────────────┐
       │         │   백엔드     │ → JWT 생성 및 저장
       │         └──────────────┘
       │               ↓
       ↓         Response: { "success": true }
┌──────────────┐
│  프론트엔드   │ → Polling이 SUCCESS 받음 (JWT는 쿠키로 자동 전달)
│  (로그인창)  │    { "status": "SUCCESS" }
└──────┬───────┘
       │
       ↓
   자동 로그인 완료!
```

## API 명세

### 1. 로그인 요청
```http
POST /api/auth/request-login
Content-Type: application/json

{
  "email": "user@example.com"
}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "loginTokenId": "uuid-abc-123"
  }
}
```

### 2. 로그인 상태 확인 (Polling)
```http
GET /api/auth/login-status/{loginTokenId}
```

**Response (대기 중):**
```json
{
  "success": true,
  "data": {
    "status": "PENDING"
  }
}
```

**Response (완료):**
HttpOnly 쿠키로 토큰 전달. 
```json
{
  "success": true,
  "data": {
    "status": "SUCCESS",
  }
}
```

### 3. 이메일 링크 검증
```http
GET /api/auth/verify-login?token={loginTokenId}
```

**Response:**
```json
{
  "success": true,
  "data":null
}
```

## 프론트엔드 구현 예시

### React 예시
```javascript
import { useState, useEffect } from 'react';

function LoginPage() {
  const [email, setEmail] = useState('');
  const [loginTokenId, setLoginTokenId] = useState(null);
  const [isPolling, setIsPolling] = useState(false);

  // Step 1: 로그인 요청
  const handleLogin = async () => {
    const response = await fetch('/api/auth/request-login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email })
    });

    const data = await response.json();
    setLoginTokenId(data.data.loginTokenId);
    setIsPolling(true);

    alert('이메일을 확인해주세요!');
  };

  // Step 2: Polling (2초마다)
  useEffect(() => {
    if (!isPolling || !loginTokenId) return;

    const interval = setInterval(async () => {
      const response = await fetch(`/api/auth/login-status/${loginTokenId}`, {
        credentials: 'include'  // 쿠키 포함
      });
      const data = await response.json();

      if (data.data.status === 'SUCCESS') {
        // JWT는 HttpOnly 쿠키로 자동 저장됨
        // 로그인 완료!
        setIsPolling(false);
        window.location.href = '/dashboard';
      }
    }, 2000);  // 2초마다

    // Cleanup
    return () => clearInterval(interval);
  }, [isPolling, loginTokenId]);

  return (
    <div>
      <h1>로그인</h1>
      <input
        type="email"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
        placeholder="이메일 입력"
      />
      <button onClick={handleLogin}>로그인</button>

      {isPolling && (
        <p>이메일을 확인해주세요... ⏳</p>
      )}
    </div>
  );
}
```

### Vue 예시
```vue
<template>
  <div>
    <h1>로그인</h1>
    <input v-model="email" type="email" placeholder="이메일 입력" />
    <button @click="handleLogin">로그인</button>
    <p v-if="isPolling">이메일을 확인해주세요... ⏳</p>
  </div>
</template>

<script>
export default {
  data() {
    return {
      email: '',
      loginTokenId: null,
      isPolling: false,
      pollInterval: null
    }
  },
  methods: {
    async handleLogin() {
      const response = await fetch('/api/auth/request-login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email: this.email })
      });

      const data = await response.json();
      this.loginTokenId = data.data.loginTokenId;
      this.isPolling = true;

      this.startPolling();
      alert('이메일을 확인해주세요!');
    },

    startPolling() {
      this.pollInterval = setInterval(async () => {
        const response = await fetch(`/api/auth/login-status/${this.loginTokenId}`, {
          credentials: 'include'  // 쿠키 포함
        });
        const data = await response.json();

        if (data.data.status === 'SUCCESS') {
          // JWT는 HttpOnly 쿠키로 자동 저장됨
          this.isPolling = false;
          clearInterval(this.pollInterval);
          this.$router.push('/dashboard');
        }
      }, 2000);
    }
  },
  beforeUnmount() {
    if (this.pollInterval) {
      clearInterval(this.pollInterval);
    }
  }
}
</script>
```

## 주요 특징

### 장점
✅ **별도 탭/창에서도 작동**: 이메일을 다른 기기에서 열어도 OK
✅ **구현 간단**: WebSocket 불필요
✅ **안정적**: Polling은 네트워크 문제에 강함
✅ **보안**: JWT는 이메일 링크 클릭 시에만 생성

### 단점
⚠️ **약간의 지연**: 최대 2초 (Polling 주기)
⚠️ **서버 부하**: 많은 사용자 시 Polling 요청 증가

### 최적화 방법
1. **Exponential Backoff**: 시간이 지날수록 Polling 간격 증가
   ```javascript
   let interval = 2000;  // 시작: 2초
   // 30초 후: 5초
   // 1분 후: 10초
   ```

2. **최대 시도 횟수**: 10분 후 자동 중단
   ```javascript
   const MAX_ATTEMPTS = 300;  // 10분 (2초 * 300)
   ```

3. **WebSocket 대안**: 많은 사용자 예상 시 SSE 고려

## 보안 고려사항

1. **토큰 만료**: LoginToken 10분 후 자동 만료
2. **1회 사용**: 한 번 사용한 토큰은 재사용 불가 (used 플래그)
3. **HTTPS 필수**: 프로덕션에서는 HTTPS만 사용 (Secure 쿠키)
4. **HttpOnly 쿠키**: JWT를 HttpOnly 쿠키로 전달하여 XSS 공격 방지
5. **SHA-256 해시**: Refresh Token을 데이터베이스에 저장 시 SHA-256으로 해싱
6. **분리된 시크릿**: Access Token과 Refresh Token에 각각 다른 시크릿 키 사용
7. **이메일 재전송 제한**: 1분 이내 재전송 불가 (Brute Force 방어)

### 향후 보안 강화 계획
- **Rate Limiting**: 로그인 시도 횟수 제한 추가 예정 (Brute Force 방어 강화)

## 데이터베이스 스키마

```sql
CREATE TABLE login_token (
    id VARCHAR(255) PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL,
    expires_at BIGINT NOT NULL,
    verified BOOLEAN DEFAULT FALSE,  -- 이메일 링크 클릭 여부
    used BOOLEAN DEFAULT FALSE,       -- 토큰 사용 여부
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE refresh_token (
    id VARCHAR(255) PRIMARY KEY,
    session_id VARCHAR(255) NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    token_hash VARCHAR(64) NOT NULL,  -- SHA-256 해시 (64자)
    expires_at TIMESTAMP NOT NULL,
    revoked BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
```

## 테스트 시나리오

1. ✅ 정상 로그인 플로우
2. ✅ 토큰 만료 (10분 후)
3. ✅ 토큰 재사용 방지
4. ✅ 존재하지 않는 이메일
5. ✅ 인증되지 않은 사용자
6. ✅ Polling 타임아웃
