package com.ably.project.customer.application.service

import com.ably.project.customer.application.usecase.CustomerUseCase
import com.ably.project.customer.domain.entity.MobileAuth
import com.ably.project.customer.infrastructure.MobileAuthRepository
import com.ably.project.customer.presentation.dto.CustomerDTO
import com.ably.project.global.utils.JWTUtil
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime

@ActiveProfiles("local")
@DisplayName("모바일인증 관련 테스트")
@SpringBootTest
class MobileAuthServiceTest @Autowired constructor(
    private val customerUseCase: CustomerUseCase,
    private val mobileAuthRepository: MobileAuthRepository,
    private val jwtUtil: JWTUtil
){
    @BeforeEach
    fun setup() {
        mobileAuthRepository.saveAll(
            mutableListOf(
                MobileAuth().apply {
                    this.mobile = "01012341231"
                    this.authCode = "0001"
                    this.expiredAt = LocalDateTime.now().plusMinutes(3)
                },
                MobileAuth().apply {
                    this.mobile = "01012341232"
                    this.authCode = "0002"
                    this.expiredAt = LocalDateTime.now().plusMinutes(3)
                },
                MobileAuth().apply {
                    this.mobile = "01012341233"
                    this.authCode = "0003"
                    this.expiredAt = LocalDateTime.now().plusMinutes(3)
                }
            )
        )
    }

    @Test
    fun 모바일_인증코드_신규_발급() {
        val mobileAuthCode = customerUseCase.getMobileAuthCode("01012341234")
        assertThat(mobileAuthCode).isNotEmpty
    }

    @Test
    fun 모바일_토큰_발급() {
        val mobileToken = customerUseCase.getMobileToken(
            CustomerDTO.MobileVerifyDTO().apply {
                this.mobile = "01012341231"
                this.authCode = "0001"
            }
        )

        assertThat(jwtUtil.claimByMobileToken(mobileToken)).isEqualTo("SUCCESS")
    }
}