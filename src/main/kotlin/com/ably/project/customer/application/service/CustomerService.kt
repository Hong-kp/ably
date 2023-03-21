package com.ably.project.customer.application.service

import com.ably.project.customer.application.usecase.CustomerUseCase
import com.ably.project.customer.domain.service.CustomerPersistenceService
import com.ably.project.customer.presentation.dto.CustomerDTO
import com.ably.project.customer.presentation.enums.CustomerErrorCode
import com.ably.project.global.infrastructure.aop.LoginTokenAspect
import com.ably.project.global.infrastructure.exception.ApiException
import com.ably.project.global.utils.EncProc
import com.ably.project.global.utils.JWTUtil
import com.ably.project.global.utils.Storage
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class CustomerService(
    val customerPersistenceService: CustomerPersistenceService,
    val jwtUtil: JWTUtil
): CustomerUseCase {
    override fun getMobileAuthCode(mobile: String): String {
        // 인증번호 1000~9999 4자리
        val authCode = (1000..9999).random().toString()
        // 인증번호 유효기간 설정
        val expiredAt:LocalDateTime = LocalDateTime.now().plusMinutes(3)

        /**
         * 모바일 인증번호 저장
         */
        customerPersistenceService.saveIssueMobileAuthCode(mobile,authCode,expiredAt)

        return authCode
    }

    /**
     * 전화번호 인증 및 토큰발급
     */
    override fun getMobileToken(dto: CustomerDTO.MobileVerifyDTO): String {
        /**
         * 모바일 인증번호 검증 완료 후 토큰 발행
         */
        return customerPersistenceService.inspectMobileAuthCode(dto.mobile!!,dto.authCode!!)?.let {
            customerPersistenceService.deleteMobileAuthCode(dto.mobile!!,dto.authCode!!)
            jwtUtil.createByMobileVerify()
        }?: throw ApiException(CustomerErrorCode.FAIL_ISSUE_MOBILE_TOKEN)
    }

    /**
     * 회원가입
     *
     * 이메일 혹은 전화번호로 가입된 아이디가 있으면 실패 없으면 가입
     */
    override fun signUp(dto: CustomerDTO) {
        customerPersistenceService.findCustomer(dto)?.let {
            throw ApiException(CustomerErrorCode.EXISTS_CUSTOMER)
        }?:customerPersistenceService.signUp(dto)
    }

    /**
     * 로그인
     */
    override fun signIn(dto: CustomerDTO.LoginDTO): String {
        /**
         * 로그인 완료 후 토큰 발행
         */
        return customerPersistenceService.signIn(dto)?.let {
            // 로그인 인증 성공 시 entity 값을 dto에 주입
            if(it.password == EncProc.getHash(dto.password!!)) {
                dto.apply {
                    this.mobile = it.mobile
                    this.email = it.email
                }

                jwtUtil.createByLoginVerify(dto)
            }else{
                throw ApiException(CustomerErrorCode.PASSWORD_INVALID)
            }
        }?:throw ApiException(CustomerErrorCode.NOT_EXISTS_CUSTOMER_INFO)
    }

    /**
     * 내 정보 보기
     */
    override fun myInfo(): CustomerDTO {
        return Storage.customer.get()
    }

    /**
     * 비밀번호수정
     */
    override fun passwordModify(dto: CustomerDTO.ModifyPasswordDTO) {
        customerPersistenceService.findCustomer(CustomerDTO(
            mobile = dto.mobile,
            password = dto.password
        ))?.let {
            it.setMyPassword(dto.password!!)
        }?:throw ApiException(CustomerErrorCode.NOT_EXISTS_CUSTOMER_INFO)
    }
}