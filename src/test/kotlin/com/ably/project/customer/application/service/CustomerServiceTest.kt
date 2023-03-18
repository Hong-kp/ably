package com.ably.project.customer.application.service

import com.ably.project.customer.application.usecase.CustomerUseCase
import com.ably.project.customer.presentation.dto.CustomerDTO
import com.ably.project.customer.presentation.enums.CustomerErrorCode
import com.ably.project.global.infrastructure.exception.ApiException
import com.ably.project.global.utils.JWTUtil
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("local")
@DisplayName("CUSTOMER 테스트")
@SpringBootTest
class CustomerServiceTest @Autowired constructor(
    private val customerUseCase: CustomerUseCase,
    private val jwtUtil: JWTUtil
) {
    @BeforeEach
    fun setup() {
        customerUseCase.signUp(
            CustomerDTO().apply {
                this.name = "홍광표"
                this.nickName = "두둥이"
                this.mobile = "01044443333"
                this.email = "google@gmail.com"
                this.password = "1234"
            }
        )
    }

    @Test
    fun 로그인토큰발급후_토큰검증() {
        val signInToken = customerUseCase.signIn(CustomerDTO.LoginDTO(
                mobile = "01044443333",
                password = "1234"
        ))

        assertThat(jwtUtil.claimByLoginToken(signInToken)).isEqualTo("google@gmail.com")
    }

    @Test
    fun 비밀번호_변경하기_성공() {
        customerUseCase.passwordModify(
            CustomerDTO.ModifyPasswordDTO(
                email = "google@gmail.com", password = "1234", newPassword = "3144"
        ))
    }

    @Test
    fun 비밀번호_변경하기_실패_기존패스워드_오입력() {
        assertThatThrownBy {
            customerUseCase.passwordModify(
                CustomerDTO.ModifyPasswordDTO(
                    email = "google@gmail.com", password = "12345", newPassword = "3144"
                ))
        }.isInstanceOf(ApiException::class.java)
            .hasMessage(CustomerErrorCode.PASSWORD_INVALID.message)
    }
}