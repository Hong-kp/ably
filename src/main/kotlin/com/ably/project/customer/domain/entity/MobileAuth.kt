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

    @Column(name = "expired_at", updatable = false)
    var expiredAt: LocalDateTime? = null
}