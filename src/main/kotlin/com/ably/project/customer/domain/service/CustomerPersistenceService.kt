package com.ably.project.customer.domain.service

import com.ably.project.customer.domain.entity.MobileAuth
import com.ably.project.customer.infrastructure.CustomerRepository
import com.ably.project.customer.infrastructure.MobileAuthRepository
import com.ably.project.customer.presentation.enums.CustomerErrorCode
import com.ably.project.global.infrastructure.exception.ApiException
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class CustomerPersistenceService(
    val customerRepository: CustomerRepository,
    val mobileAuthRepository: MobileAuthRepository
) {
    /**
     * 발급 인증번호 저장
     */
    fun saveIssueMobileAuthCode(mobile: String, authCode: String, expiredAt: LocalDateTime) {
        mobileAuthRepository.save(
            MobileAuth().apply {
                this.mobile = mobile
                this.authCode = authCode
                this.expiredAt = expiredAt
            }
        )
    }

    /**
     * 모바일 인증번호 검증
     */
    fun inspectMobileAuthCode(mobile: String, authCode: String): MobileAuth? {
        return mobileAuthRepository.findByMobileAndAuthCodeAndExpiredAtIsAfter(
            mobile, authCode, LocalDateTime.now()
        )
    }
}