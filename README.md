## 요구사항!
- PDF 내용

## 제공  API 목록 (branch - main / rest.http 참조)!
1. 모바일 인증번호 발급
   - 휴대번호를 GET 파라미터로 인증번호를 발급 (인증시간 3분)
2. 모바일 토큰 발급
   - 휴대번호+인증번호로 모바일인증 완료 토큰 발급 (AccessToken 발급(유효기간 0) - RefreshToken 제외)
3. 회원가입 (모바일 토큰 사용)
    - 모바일인증 토큰을 header에 추가하여 회원가입 진행
4. 로그인 (인증토큰 발급) /*개인식별정보는 이메일 or 휴대번호 + 비밀번호의 일치로 함*/
    - 회원정보 및 후속 개발에 필요한 회원인증 토큰(로그인) 발급 (AccessToken 발급 - RefreshToken 제외)
5. 회원정보 가져오기 (인증 토큰 사용)
    - 회원인증 토큰으로 회원정보 가져오기
6. 비밀번호 변경하기 (모바일 토큰 사용)
    - 모바일인증 토큰 및 body(이메일-식별위함,기존비밀번호,변경비밀번호)를 통하여 비밀번호 변경

## 개발사용 프레임워크 및 기술! - fork
- kotlin 1.7
- springboot2
- jpa
- h2
- aop
- jwt

## 미구현 부분
- JWT의 refresh토큰 기능 필요 (짧은 라이프사이클 테스트를 고려해서 리프레시 토큰 부분은 제외함)
- JWT의 보안처리 필요
- Aggregate를 'Customer' 도메인으로 묶음 (분리정도의 범위를 지니지 않는다고 판단됨)
- Aggregate가 묶임으로 내부 FeignCall은 사용되지 않음

## 특이사항!
- 없음

## API 포맷!
성공시
  ```json
    {
      "content": {
       "data": "결과값"
      },
      "meta": {
        "result": "ok",
        "code": 200,
        "resultMsg": ""
       }
    }
  ```
실패시
  ```json
    {
      "content": {},
      "meta": {
          "result": "fail",
          "code": 500,
          "resultMsg": "오류 메시지"
      }
    }
  ```

## 실행!
- 프로젝트 루트에서 gradlew 실행
- ./gradlew bootRun --args='--spring.profiles.active=local'
