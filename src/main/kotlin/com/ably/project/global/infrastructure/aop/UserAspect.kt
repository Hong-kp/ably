package com.ably.project.global.infrastructure.aop

import com.ably.project.global.domain.enums.HttpStatusCode
import com.ably.project.global.infrastructure.aop.annotation.User
import com.ably.project.global.infrastructure.exception.ApiException
import com.ably.project.global.infrastructure.exception.GlobalErrorCode
import com.ably.project.global.presentation.enums.AuthorityType
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.reflect.MethodSignature
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.lang.reflect.Method
import java.util.*

@Component
@Aspect
class UserAspect {
    private val log = LoggerFactory.getLogger(javaClass)

    @Before("@annotation(com.ably.project.global.infrastructure.aop.annotation.User)")
    fun roleCheck(joinPoint: JoinPoint) {
        val methodRoles = getAuthsFromAnnotation(joinPoint)
        val authorityType = getRequestHeader("Authorization-Type")

        checkUserType(methodRoles, authorityType).onSuccess {
            log.info("(@User@Type) 검증성공 {}",it)
        }.onFailure {
            throw ApiException(
                "(@User@Type) 요청 권한이 없습니다.",
                GlobalErrorCode.INTERNAL_SERVER_ERROR,
                HttpStatusCode.INTERNAL_SERVER_ERROR
            )
        }
    }

    /**
     * 요청자 타입 검증
     */
    private fun checkUserType(authorityType: Array<AuthorityType>, token: String): Result<AuthorityType> {
        return runCatching {
            authorityType.find {
                it.name == token
            }?:throw ApiException()
        }
    }

    private fun getRequestHeader(header: String): String {
        val requestAttributes = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes
        val request = requestAttributes.request

        return request.getHeader(header) ?: throw ApiException(GlobalErrorCode.HTTP_HEADER_REQUIRED)
    }

    /*메소드에 선언된 필요권한 목록 가져오기*/
    private fun getAuthsFromAnnotation(joinPoint: JoinPoint): Array<AuthorityType> {
        val signature: MethodSignature = joinPoint.signature as MethodSignature
        val method: Method = signature.method
        val user: User = method.getAnnotation(User::class.java)
        return user.authorityType
    }
}