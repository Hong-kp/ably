package com.ably.project.global.infrastructure.exception

import com.ably.project.global.domain.enums.HttpStatusCode

/**
 * throw ApiException(PaymentErrorCode.FAILED_PAYMENT)
 */
class ApiException : RuntimeException {
    private var userErrorCode: ErrorCode? = null
    private var httpStatus: HttpStatusCode? = null

    constructor()

    constructor(message: String?) : super(message)

    constructor(userErrorCode: ErrorCode) : super(userErrorCode.message) {
        this.userErrorCode = userErrorCode
    }

    constructor(message: String?, userErrorCode: ErrorCode?) : super(message) {
        this.userErrorCode = userErrorCode
    }

    constructor(message: String?, userErrorCode: ErrorCode?, httpStatus: HttpStatusCode?) : super(message) {
        this.userErrorCode = userErrorCode
        this.httpStatus = httpStatus
    }
}