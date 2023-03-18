package com.ably.project.customer.application.usecase

import com.ably.project.customer.presentation.dto.CustomerDTO
import org.springframework.transaction.annotation.Transactional

interface CustomerUseCase {
    /**
     * 전화번호 인증번호 받기
     */
    fun getMobileAuthCode(mobile: String): String

    /**
     * 전화번호 인증 토큰 발급
     */
    fun getMobileToken(dto: CustomerDTO.MobileVerifyDTO): String

    /**
     * 회원가입 (with MobileVerifyToekn)
     */
    @Transactional
    fun signUp(dto: CustomerDTO)

    /**
     * 로그인 (issue Token)
     */
    fun signIn(dto: CustomerDTO)

    /**
     * 내 정보 보기 (with LoginToken)
     */
    fun myinfo(dto: CustomerDTO)

    /**
     * 비밀번호 수정 (with LoginToken)
     */
    fun passwordModify(dto: CustomerDTO)

}