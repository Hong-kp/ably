package com.ably.project.global.domain.enums

enum class HttpStatusCode(var code: Int) {
    INTERNAL_SERVER_ERROR(500),
    BAD_REQUEST_BODY(400),
    EXISTS_PRODUCT(400),
    NOT_FOUND(404);
}