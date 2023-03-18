package com.ably.project.customer.application.service

import com.ably.project.customer.application.usecase.CustomerUseCase
import com.ably.project.customer.domain.service.CustomerPersistenceService
import com.ably.project.customer.presentation.dto.CustomerDTO
import com.ably.project.customer.presentation.enums.CustomerErrorCode
import com.ably.project.global.infrastructure.exception.ApiException
import com.ably.project.global.utils.JWTUtil
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class CustomerService(
    val customerPersistenceService: CustomerPersistenceService,
    val jwtUtil: JWTUtil
): CustomerUseCase {
    override fun getMobileAuthCode(mobile: String): String {
        // 인증번호 1000~9999 4자리
        val authCode = (1000..9999).random().toString()
        // 인증번호 유효기간 설정
        val expiredAt:LocalDateTime = LocalDateTime.now().plusMinutes(3)

        /**
         * 모바일 인증번호 저장
         */
        customerPersistenceService.saveIssueMobileAuthCode(mobile,authCode,expiredAt)

        return authCode
    }

    /**
     * 전화번호 인증 및 토큰발급
     */
    override fun getMobileToken(dto: CustomerDTO.MobileVerifyDTO): String {
        /**
         * 모바일 인증번호 검증 완료 후 토큰 발행
         */
        return customerPersistenceService.inspectMobileAuthCode(dto.mobile!!,dto.authCode!!)?.let {
            jwtUtil.createByMobileVerify()
        }?: throw ApiException(CustomerErrorCode.FAIL_ISSUE_MOBILE_TOKEN)
    }

    /**
     * 회원가입
     */
    override fun signUp(dto: CustomerDTO) {
        TODO("Not yet implemented")
    }

    /**
     * 로그인
     */
    override fun signIn(dto: CustomerDTO) {
        TODO("Not yet implemented")
    }

    /**
     * 내정보보기
     */
    override fun myinfo(dto: CustomerDTO) {
        TODO("Not yet implemented")
    }

    /**
     * 비밀번호수정
     */
    override fun passwordModify(dto: CustomerDTO) {
        TODO("Not yet implemented")
    }
}