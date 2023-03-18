package com.ably.project.customer.presentation.enums

import com.ably.project.global.infrastructure.exception.ErrorCode

enum class CustomerErrorCode(
    override val code: String,
    override val message: String
) : ErrorCode {
    FAIL_ISSUE_MOBILE_TOKEN("FAIL_ISSUE_MOBILE_TOKEN","전화번호 인증이 실패하였습니다."),
    NOT_EXISTS_CUSTOMER_INFO("NOT_EXISTS_CUSTOMER_INFO","고객 정보를 찾을 수 없습니다."),
    EXISTS_CUSTOMER("EXISTS_CUSTOMER", "이미 가입된 정보가 있습니다."),
    PASSWORD_INVALID("PASSWORD_INVALID", "비밀번호가 잘못 입력 되었습니다."),;
}
