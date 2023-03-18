package com.ably.project.customer.presentation.controller

import com.ably.project.customer.application.usecase.CustomerUseCase
import com.ably.project.customer.presentation.dto.CustomerDTO
import com.ably.project.global.infrastructure.aop.annotation.AcceptService
import com.ably.project.global.presentation.ApiFormat
import com.ably.project.global.presentation.enums.ServiceType
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

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
    @AcceptService([ServiceType.ABLY])
    @ResponseBody
    @PostMapping("/v1/mobile-verify")
    fun mobileToken(
        @RequestBody mobileVerifyDTO: ApiFormat<CustomerDTO.MobileVerifyDTO>,
    ): ResponseEntity<ApiFormat<String>> {
        val mobileVerifyToken: String = customerUseCase.getMobileToken(mobileVerifyDTO.content!!)

        return ResponseEntity.ok(ApiFormat(mobileVerifyToken).ok("전화번호 인증이 완료 되었습니다."))
    }
}