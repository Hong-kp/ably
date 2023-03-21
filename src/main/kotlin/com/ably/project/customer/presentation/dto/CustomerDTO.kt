package com.ably.project.customer.presentation.dto

import com.ably.project.customer.domain.entity.Customer
import com.ably.project.global.infrastructure.exception.ApiException
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.Email
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

/**
 * 고객기본 DTO
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
data class CustomerDTO(
    @field:NotNull(message = "이름은 필수 입력 입니다.") @get:JsonProperty("name") var name: String? = null,

    @field:NotNull(message = "이메일은 필수 입력 입니다.")
    @field:Email
    @get:JsonProperty("email") var email: String? = null,

    @field:Pattern(regexp = "^01(?:0|1|0)[.-]?(\\d{4})[.-]?(\\d{4})$", message = "올바른 형식의 전화번호여야 합니다. (010XXXXXXXX)")
    @field:NotNull(message = "전화번호는 필수 입력 입니다.")
    @get:JsonProperty("mobile") var mobile: String? = null,

    @get:JsonProperty("nick_name")
    var nickName: String? = null,

    @field:NotNull(message = "비밀번호는 필수 입력 입니다.") @get:JsonProperty("password") var password: String? = null
){
    /**
     * 모바일 인증용 DTO
     */
    data class MobileVerifyDTO(
        @field:Email
        @get:JsonProperty("email")
        var email: String? = null,

        @field:Pattern(regexp = "^01(?:0|1|0)[.-]?(\\d{4})[.-]?(\\d{4})$", message = "올바른 형식의 전화번호여야 합니다. (010XXXXXXXX)")
        @field:NotNull(message = "전화번호는 필수 입력 입니다.")
        @get:JsonProperty("mobile")
        var mobile: String? = null,

        @field:NotNull(message = "인증코드는 필수 입력 입니다.")
        @get:JsonProperty("auth_code")
        var authCode: String? = null
    )

    /**
     * 로그인 DTO (전화번호 , 이메일 , 비밀번호)
     */
    data class LoginDTO(
        @field:Pattern(regexp = "^01(?:0|1|0)[.-]?(\\d{4})[.-]?(\\d{4})$", message = "올바른 형식의 전화번호여야 합니다. (010XXXXXXXX)")
        @get:JsonProperty("mobile") var mobile: String? = null,

        @field:Email
        @get:JsonProperty("email")
        var email: String? = null,

        @field:NotNull(message = "비밀번호는 필수 입력 입니다.") @get:JsonProperty("password") var password: String? = null
    ){
        init {
            if(mobile==null && email==null) throw ApiException("전화번호와 이메일중 하나는 필수 입력입니다.")
        }
    }

    /**
     * 비밀번호 변경 DTO
     */
    data class ModifyPasswordDTO(
        @field:Pattern(regexp = "^01(?:0|1|0)[.-]?(\\d{4})[.-]?(\\d{4})$", message = "올바른 형식의 전화번호여야 합니다. (010XXXXXXXX)")
        @get:JsonProperty("mobile") var mobile: String? = null,

        @field:NotNull(message = "비밀번호는 필수 입력 입니다.")
        @get:JsonProperty("password")
        var password: String? = null
    )

    /**
     * entity to dto
     */
    fun ofDTO(customer: Customer): CustomerDTO {
        return CustomerDTO().apply {
            this.email = customer.email
            this.mobile = customer.mobile
            this.name = customer.name
            this.nickName = customer.nickName
        }
    }
}