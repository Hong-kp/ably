package com.ably.project.customer.infrastructure

import com.ably.project.customer.domain.entity.Customer
import com.ably.project.customer.domain.entity.MobileAuth
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface CustomerRepository: JpaRepository<Customer, Long>

interface MobileAuthRepository: JpaRepository<MobileAuth, String>{
    /**
     * 유효기간이 남아있는 전화번호+인증번호 데이터 확인
     */
    fun findByMobileAndAuthCodeAndExpiredAtIsAfter(mobile: String, authCode: String, expiredAt: LocalDateTime): MobileAuth?
}