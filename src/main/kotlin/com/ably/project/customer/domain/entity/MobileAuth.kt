package com.ably.project.customer.domain.entity

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "MOBILE_AUTH")
class MobileAuth {
    @Id
    @Column(name = "mobile", length = 20)
    lateinit var mobile: String

    @Column(name = "auth_code", length = 4)
    lateinit var authCode: String

    @Column(name = "expired_at")
    var expiredAt: LocalDateTime? = null

    /**
     * 인증코드 엔티티 객체 생성
     */
    fun createMobileAuth(mobile: String, authCode: String, expiredAt: LocalDateTime): MobileAuth {
        return MobileAuth().apply {
            this.mobile = mobile
            this.authCode = authCode
            this.expiredAt = expiredAt
        }
    }
}