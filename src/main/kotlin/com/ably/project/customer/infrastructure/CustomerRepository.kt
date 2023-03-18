package com.ably.project.customer.infrastructure

import com.ably.project.customer.domain.entity.Customer
import com.ably.project.customer.domain.entity.MobileAuth
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

interface CustomerRepository: JpaRepository<Customer, Long> {
    /**
     * 전화번호 or 이메일로 고객정보 확인 (회원가입 중복 시)
     */
    fun findByMobileOrEmail(mobile: String?, email: String?): Customer?

    /**
     * 이메일로 고객정보 확인
     */
    fun findByEmail(email: String): Customer?
}

interface MobileAuthRepository: JpaRepository<MobileAuth, String>{
    /**
     * 유효기간이 남아있는 전화번호+인증번호 데이터 확인
     */
    fun findByMobileAndAuthCodeAndExpiredAtIsAfter(mobile: String, authCode: String, expiredAt: LocalDateTime): MobileAuth?

    /**
     * 모바일 인증번호 삭제
     */
    @Modifying
    @Query("delete from MobileAuth as a where a.mobile=:mobile and a.authCode=:authCode")
    fun deleteMobileAuthCode(mobile: String, authCode: String)
}