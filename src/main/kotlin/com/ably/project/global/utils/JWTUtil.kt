package com.ably.project.global.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
object JWTUtil {
    private val log = LoggerFactory.getLogger(javaClass)

    @Value("\${ably_auth.auth_system.secret-key}")
    lateinit var secretKey: String

    @Value("\${ably_auth.auth_system.issuer}")
    lateinit var issuer: String

    /**
     * 전화번호 인증 토큰생송
     */
    fun createByMobilVerify(): String? {
        return JWT.create()
            .withIssuer(issuer)
            .withSubject("MOBILE_VERIFY_TOKEN") // 전화번호인증 토큰
            .withPayload(mapOf("RESULT" to "SUCCESS"))
            .sign(Algorithm.HMAC256(secretKey))
    }

    /**
     * 전화번호 인증 토큰 검증
     */
    fun claimByMobileToken(token: String): String? {
        val parseToken = JWT.require(Algorithm.HMAC256(secretKey))
            .withIssuer(issuer)
            .withSubject("MOBILE_VERIFY_TOKEN")
            .build()
            .verify(token)

        return parseToken.claims["RESULT"]!!.asString()
    }

    /**
     * 로그인 토큰 생성
     */
    fun createByLoginVerify(): String? {
        return JWT.create()
            .withIssuer(issuer)
            .withSubject("CUSTOMER_VERIFY_TOKEN") // 회원인증완료 토큰
            .withPayload(mapOf("USER_ID" to "k2jeans","USER_NAME" to "홍광표"))
            .sign(Algorithm.HMAC256(secretKey))
    }

    /**
     * 로그인 토큰 검증
     */
    fun claimByLoginToken(token: String): String {
        val parseToken = JWT.require(Algorithm.HMAC256(secretKey))
            .withIssuer(issuer)
            .withSubject("CUSTOMER_VERIFY_TOKEN")
            .build()
            .verify(token)

        return parseToken.claims["USER_ID"]!!.asString()
    }
}