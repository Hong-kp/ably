package com.ably.project.global.infrastructure.aop

import com.ably.project.customer.domain.service.CustomerPersistenceService
import com.ably.project.global.domain.enums.HttpStatusCode
import com.ably.project.global.infrastructure.exception.ApiException
import com.ably.project.global.infrastructure.exception.GlobalErrorCode
import com.ably.project.global.utils.JWTUtil
import com.ably.project.global.utils.Storage
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.util.*

@Component
@Aspect
class LoginTokenAspect(
    private val jwtUtil: JWTUtil,
    private val customerPersistenceService: CustomerPersistenceService
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Value("\${ably_auth.auth_system.secret-key}")
    lateinit var secretKey: String

    @Value("\${ably_auth.auth_system.issuer}")
    lateinit var issuer: String

    @Before("@annotation(com.ably.project.global.infrastructure.aop.annotation.RequiredLoginToken)")
    fun roleCheck(joinPoint: JoinPoint) {
        val bearer = getRequestHeader("Authorization")

        checkUserToken(bearer).onSuccess {
            log.info("(@User@Token) 검증성공 권한소유자 {}",it)

            Storage.customer.set(customerPersistenceService.findByEmail(it))
        }.onFailure {
            throw ApiException(
                "(@User@Token) 요청 권한이 없습니다. (토큰 검증에 실패하였습니다.)",
                GlobalErrorCode.INTERNAL_SERVER_ERROR,
                HttpStatusCode.INTERNAL_SERVER_ERROR
            )
        }
    }

    /**
     * 토큰 검증
     */
    private fun checkUserToken(token: String): Result<String> {
        return runCatching {
            jwtUtil.claimByLoginToken(token)
        }
    }

    private fun getRequestHeader(header: String): String {
        val requestAttributes = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes
        val request = requestAttributes.request

        return request.getHeader(header) ?: throw ApiException(GlobalErrorCode.HTTP_HEADER_REQUIRED)
    }
}