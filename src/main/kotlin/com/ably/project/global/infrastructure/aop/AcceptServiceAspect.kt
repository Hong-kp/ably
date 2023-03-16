package com.ably.project.global.infrastructure.aop

import com.ably.project.global.infrastructure.exception.ApiException
import com.ably.project.global.infrastructure.exception.GlobalErrorCode
import com.ably.project.global.domain.enums.HttpStatusCode
import com.ably.project.global.presentation.enums.ServiceType
import com.ably.project.global.infrastructure.aop.annotation.AcceptService
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.reflect.MethodSignature
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.lang.reflect.Method

@Component
@Aspect
class AcceptServiceAspect {
    private val log = LoggerFactory.getLogger(javaClass)

    @Before("@annotation(com.ably.project.global.infrastructure.aop.annotation.AcceptService)")
    fun roleCheck(joinPoint: JoinPoint) {
        val methodRoles = getAuthsFromAnnotation(joinPoint)
        val token = getRequestHeader()

        checkService(methodRoles,token).onSuccess {
            log.info("(@RequestService) 검증성공 {}",it)
            /**
             * after token parse
             */
        }.onFailure {
            // TransactionAspectSupport.currentTransactionStatus().setRollbackOnly() // 강제롤백처리

            throw ApiException(
                "(@RequestService) 요청 권한이 없습니다.",
                GlobalErrorCode.INTERNAL_SERVER_ERROR,
                HttpStatusCode.INTERNAL_SERVER_ERROR
            )
        }
    }

    private fun checkService(serviceType: Array<ServiceType>, token: String): Result<ServiceType> {
        return runCatching {
            serviceType.find {
                it.name == token
            } ?: throw ApiException()
        }
    }

    private fun getRequestHeader(): String {
        val requestAttributes = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes
        val request = requestAttributes.request

        return request.getHeader("Site-Code")?: throw ApiException(GlobalErrorCode.HTTP_HEADER_REQUIRED)
    }

    /*메소드에 선언된 필요권한 목록 가져오기*/
    private fun getAuthsFromAnnotation(joinPoint: JoinPoint): Array<ServiceType> {
        val signature: MethodSignature = joinPoint.signature as MethodSignature
        val method: Method = signature.method
        val auth: AcceptService = method.getAnnotation(AcceptService::class.java)
        return auth.serviceType
    }
}