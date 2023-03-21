package com.ably.project.customer.presentation.controller

import com.ably.project.customer.application.usecase.CustomerUseCase
import com.ably.project.customer.presentation.dto.CustomerDTO
import com.ably.project.global.infrastructure.aop.annotation.AcceptService
import com.ably.project.global.infrastructure.aop.annotation.RequiredLoginToken
import com.ably.project.global.infrastructure.aop.annotation.RequiredMobileToken
import com.ably.project.global.presentation.ApiFormat
import com.ably.project.global.presentation.enums.ServiceType
import org.hibernate.validator.*
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@Validated
@RequestMapping("/customer")
@RestController
class CustomerController(
    val customerUseCase: CustomerUseCase
) {
    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * 전화번호 인증번호 발급
     */
    @ResponseBody
    @GetMapping("/v1/mobile-authcode/{mobile}")
    fun mobileVerify(
        @PathVariable mobile: String
    ): ResponseEntity<ApiFormat<String>> {
        return ResponseEntity.ok(ApiFormat(
            customerUseCase.getMobileAuthCode(mobile)
        ).ok("인증번호가 발급되었습니다."))
    }

    /**
     * 전화번호 인증 토큰 발급
     */
    @AcceptService([ServiceType.ABLY_APP])
    @ResponseBody
    @PostMapping("/v1/mobile-verify")
    fun mobileToken(
        @Valid @RequestBody mobileVerifyDTO: CustomerDTO.MobileVerifyDTO,
    ): ResponseEntity<ApiFormat<String>> {
        val mobileVerifyToken: String = customerUseCase.getMobileToken(mobileVerifyDTO)

        return ResponseEntity.ok(ApiFormat(mobileVerifyToken).ok("전화번호 인증이 완료 되었습니다."))
    }

    /**
     * 회원 가입 (모바일인증 완료 토큰 보유자)
     */
    @RequiredMobileToken
    @AcceptService([ServiceType.ABLY_APP])
    @ResponseBody
    @PostMapping("/v1/sign-up")
    fun signUp(
        @Valid @RequestBody customerDTO: CustomerDTO
    ): ResponseEntity<ApiFormat<Unit>> = ResponseEntity.ok(ApiFormat(customerUseCase.signUp(customerDTO)).ok("회원 가입이 완료 되었습니다."))

    /**
     * 로그인 인증 토큰 발급
     */
    @AcceptService([ServiceType.ABLY_APP])
    @ResponseBody
    @PostMapping("/v1/sign-in")
    fun signIn(
        @Valid @RequestBody loginDTO: CustomerDTO.LoginDTO
    ): ResponseEntity<ApiFormat<String>> = ResponseEntity.ok(ApiFormat(customerUseCase.signIn(loginDTO)).ok("인증토큰이 발급 되었습니다."))

    /**
     * 내정보 가져오기 (로그인 완료 토큰 보유자)
     */
    @RequiredLoginToken
    @AcceptService([ServiceType.ABLY_APP,ServiceType.ABLY_ADMIN])
    @ResponseBody
    @PostMapping("/v1/myinfo")
    fun myInfo(): ResponseEntity<ApiFormat<CustomerDTO>> = ResponseEntity.ok(ApiFormat(customerUseCase.myInfo()))

    /**
     * 비밀번호 수정 (모바일인증 완료 토큰 보유자)
     */
    @RequiredMobileToken
    @AcceptService([ServiceType.ABLY_APP,ServiceType.ABLY_ADMIN])
    @ResponseBody
    @PostMapping("/v1/reset-password")
    fun passwordReset(
        @Valid @RequestBody modifyPasswordDTO: CustomerDTO.ModifyPasswordDTO
    ): ResponseEntity<ApiFormat<Unit>> = ResponseEntity.ok(ApiFormat(customerUseCase.passwordModify(modifyPasswordDTO)).ok("비밀번호 변경이 완료 되었습니다."))
}