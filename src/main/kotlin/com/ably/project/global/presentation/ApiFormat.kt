package com.ably.project.global.presentation

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity

/**
 * API 송수신 포맷
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
class ApiFormat<T>(
    @get:JsonProperty("content")
    val content: T? = null, /*FeignClient의 수신데이터로 ApiFormat을 받을 때는 Jackson을 사용하는데 JackSon은 기본생성자가 없으면 변환이 되지 않으므로 content를 nullable처리함*/
    @get:JsonProperty("meta")
    var meta: Meta = Meta.ok()
) {
    companion object {
        /**
         * 공통 헤더
         */
        val header = mapOf(
            "Site-Code" to "ABLY",
            "Authorization-Type" to "SYSTEM"
        )

        fun exception(statusCode: Int, message: String): ResponseEntity<ApiFormat<Unit>> {
            val header = HttpHeaders().apply {
                this.accept = mutableListOf(MediaType.APPLICATION_JSON)
                this.contentType = MediaType.valueOf("application/json;charset=UTF-8")
            }

            return ResponseEntity
                .status(statusCode)
                .headers(header)
                .body(
                    ApiFormat(Unit).fail(statusCode,message)
                )
        }
    }

    fun ok(): ApiFormat<T> {
        this.meta = Meta.ok()
        return this
    }

    fun ok(resultMsg: String): ApiFormat<T> {
        this.meta = Meta.ok(resultMsg)
        return this
    }

    fun ok(code: Int, resultMsg: String): ApiFormat<T> {
        this.meta = Meta.ok(code, resultMsg)
        return this
    }

    fun fail(code: Int, message: String): ApiFormat<T> {
        this.meta = Meta.fail(
            code = code,
            message = message
        )
        return this
    }
}

/**
 * Default Output Status
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
class Meta(
    val result: MetaResult? = null,
    val code: Int? = null,
    val resultMsg: String? = null
) {
    companion object {
        fun ok() = Meta(result = MetaResult.OK, code = 200, resultMsg = "")
        fun ok(resultMsg:String) = Meta(result = MetaResult.OK, code = 200, resultMsg = resultMsg)
        fun ok(code: Int, resultMsg:String) = Meta(result = MetaResult.OK, code = code, resultMsg = resultMsg)
        fun fail(code: Int,message: String) = Meta(result = MetaResult.FAIL, code = code, resultMsg = message)
    }
}

/**
 * Meta에서 사용되는 공통코드
 */
enum class MetaResult { OK, FAIL }