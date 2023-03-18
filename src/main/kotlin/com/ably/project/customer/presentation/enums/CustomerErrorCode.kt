package com.ably.project.customer.presentation.enums

import com.ably.project.global.infrastructure.exception.ErrorCode

enum class CustomerErrorCode(
    override val code: String,
    override val message: String
) : ErrorCode {
    FAIL_ISSUE_MOBILE_TOKEN("FAIL_ISSUE_MOBILE_TOKEN","전화번호 인증이 실패하였습니다."),
    NOT_EXISTS_CUSTOMER_INFO("NOT_EXISTS_CUSTOMER_INFO","고객 정보를 찾을 수 없습니다."),
}
