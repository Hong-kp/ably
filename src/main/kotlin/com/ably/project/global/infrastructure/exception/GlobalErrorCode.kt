package com.ably.project.global.infrastructure.exception

enum class GlobalErrorCode(
    override val code: String,
    override val message: String
) : ErrorCode {
    BAD_REQUEST_BODY("BAD_REQUEST_BODY", "입력값을 확인해주세요."),

    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "서버 오류입니다."),

    // AUTH
    ACCESS_DENIED("ACCESS_DENIED", "(ErrorCode) 요청 권한이 없습니다."),

    // HEADER
    HTTP_HEADER_REQUIRED("HTTP_HEADER_REQUIRED", "REQUEST HEADER REQUIRED"),
}
