package com.ably.project.customer.presentation.dto

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * 고객기본 DTO
 */
data class CustomerDTO(
    @get:JsonProperty("name") var name: String? = null,
    @get:JsonProperty("email") var email: String? = null,
    @get:JsonProperty("mobile") var mobile: String? = null,
    @get:JsonProperty("nick_name") var nickName: String? = null,
    @get:JsonProperty("password") var password: String? = null
){
    /**
     * 모바일 인증용 DTO
     */
    data class MobileVerifyDTO(
        @get:JsonProperty("mobile") var mobile: String? = null,
        @get:JsonProperty("auth_code") var authCode: String? = null
    )
}