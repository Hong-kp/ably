package com.ably.project.customer.domain.service

import com.ably.project.customer.domain.entity.Customer
import com.ably.project.customer.domain.entity.MobileAuth
import com.ably.project.customer.infrastructure.CustomerRepository
import com.ably.project.customer.infrastructure.MobileAuthRepository
import com.ably.project.customer.presentation.dto.CustomerDTO
import com.ably.project.customer.presentation.enums.CustomerErrorCode
import com.ably.project.global.infrastructure.exception.ApiException
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import kotlin.math.log

@Service
class CustomerPersistenceService(
    val customerRepository: CustomerRepository,
    val mobileAuthRepository: MobileAuthRepository
) {
    /**
     * 발급 인증번호 저장
     */
    fun saveIssueMobileAuthCode(mobile: String, authCode: String, expiredAt: LocalDateTime) {
        mobileAuthRepository.save(MobileAuth().createMobileAuth(mobile, authCode, expiredAt))
    }

    /**
     * 모바일 인증번호 검증
     */
    fun inspectMobileAuthCode(mobile: String, authCode: String): MobileAuth? {
        return mobileAuthRepository.findByMobileAndAuthCodeAndExpiredAtIsAfter(mobile, authCode, LocalDateTime.now())
    }

    /**
     * 모바일 인증번호 삭제
     */
    fun deleteMobileAuthCode(mobile: String, authCode: String) {
        mobileAuthRepository.deleteMobileAuthCode(mobile, authCode)
    }

    /**
     * 전화번호나 이메일로 회원정보 확인
     */
    fun findCustomer(dto: CustomerDTO): Customer? {
        return customerRepository.findByMobileOrEmail(dto.mobile, dto.email)
    }

    /**
     * 회원가입
     */
    fun signUp(dto: CustomerDTO) {
        customerRepository.save(Customer().createCustomer(dto))
    }

    /**
     * 로그인
     */
    fun signIn(dto: CustomerDTO.LoginDTO): Customer? {
        return customerRepository.findByMobileOrEmail(dto.mobile, dto.email)
    }

    /**
     * 이메일로 정보확인
     */
    fun findByEmail(email: String): CustomerDTO? {
        return customerRepository.findByEmail(email)?.let { CustomerDTO().ofDTO(it) } ?: throw ApiException(
            CustomerErrorCode.NOT_EXISTS_CUSTOMER_INFO
        )
    }
}