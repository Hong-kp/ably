package com.ably.project.global.utils

import com.ably.project.customer.presentation.dto.CustomerDTO
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

/**
 * !! 토큰 AccessToken 유효기간과 별도의 Refresh 토큰을 가지지 않음 (AccessToken 갱신과정 제외)
 */
@Component
class JWTUtil {
    private val log = LoggerFactory.getLogger(javaClass)

    @Value("\${ably_auth.auth_system.secret-key}")
    lateinit var secretKey: String

    @Value("\${ably_auth.auth_system.issuer}")
    lateinit var issuer: String

    /**
     * 모바일 인증 토큰생성
     */
    fun createByMobileVerify(): String? {
        return JWT.create()
            .withIssuer(issuer)
            .withSubject("MOBILE_VERIFY_TOKEN") // 전화번호인증 토큰
            .withPayload(mapOf("RESULT" to "SUCCESS"))
            .sign(Algorithm.HMAC256(secretKey))
    }

    /**
     * 모바일 인증 토큰 검증
     */
    fun claimByMobileToken(token: String): String {
        val parseToken = JWT.require(Algorithm.HMAC256(secretKey))
            .withIssuer(issuer)
            .withSubject("MOBILE_VERIFY_TOKEN")
            .build()
            .verify(token)

        return parseToken.claims["RESULT"]!!.asString()
    }

    /**
     * 로그인 인증 토큰 생성
     */
    fun createByLoginVerify(dto: CustomerDTO.LoginDTO): String? {
        return JWT.create()
            .withIssuer(issuer)
            .withSubject("CUSTOMER_VERIFY_TOKEN") // 회원인증완료 토큰
            .withPayload(jacksonObjectMapper().convertValue(dto,Map::class.java) as MutableMap<String, *>?)
            .sign(Algorithm.HMAC256(secretKey))
    }

    /**
     * 로그인 인증 토큰 검증
     */
    fun claimByLoginToken(token: String): String {
        val parseToken = JWT.require(Algorithm.HMAC256(secretKey))
            .withIssuer(issuer)
            .withSubject("CUSTOMER_VERIFY_TOKEN")
            .build()
            .verify(token)

        return parseToken.claims["email"]!!.asString()
    }
}