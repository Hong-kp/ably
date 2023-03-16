package com.ably.project.global.infrastructure.exception

enum class GlobalErrorCode(
    override val code: String,
    override val message: String
) : ErrorCode {
    BAD_REQUEST_BODY("BAD_REQUEST_BODY", "입력값을 확인해주세요."),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "서버 오류입니다."),

    // STOCK
    PRODUCT_NOT_EXISTS("PRODUCT_NOT_EXISTS", "제품 정보가 없습니다"),

    // AUTH
    ACCESS_DENIED("ACCESS_DENIED", "(ErrorCode) 요청 권한이 없습니다."),

    // HEADER
    HTTP_HEADER_REQUIRED("HTTP_HEADER_REQUIRED", "REQUEST HEADER REQUIRED"),

    // ORDER
    NOT_EXISTS_ORDER("NOT_EXISTS_ORDER", "존재하지 않는 거래입니다."),

    // FINGER PRINT
    FINGER_PRINT_INVALID("FINGER_PRINT_INVALID","암호화 지문오류")
}
