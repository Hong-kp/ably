package com.ably.project.global.infrastructure.exception

import com.ably.project.global.presentation.ApiFormat
import feign.FeignException
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.convert.ConversionFailedException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.BindingResult
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
internal class ExceptionAdvice {
    @Value("\${application.environment}")
    lateinit var env:String

    @ExceptionHandler(ApiException::class)
    protected fun apiHandleException(e: ApiException): ResponseEntity<ApiFormat<Unit>> {
        return ApiFormat.exception(HttpStatus.BAD_REQUEST.value(), e.message ?: "")
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(bindingResult: BindingResult): ResponseEntity<ApiFormat<Unit>> {
        val fieldErrors = bindingResult.allErrors.mapNotNull {it.defaultMessage}.joinToString(
            transform = {"[${it}]"},
            separator = ",",
        )

        return ApiFormat.exception(HttpStatus.BAD_REQUEST.value(), fieldErrors)
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun httpRequestMethodNotSupportedExceptionHandler(e: HttpRequestMethodNotSupportedException): ResponseEntity<ApiFormat<Unit>> {
        return ApiFormat.exception(HttpStatus.BAD_REQUEST.value(), e.message ?: "")
    }

    @ExceptionHandler(FeignException::class)
    fun feignHandleException(e: FeignException): ResponseEntity<ApiFormat<Unit>> {
        return ApiFormat.exception(HttpStatus.BAD_REQUEST.value(), "기타 API통신 중 오류가 발생하였습니다. (결과오류 : ..${e.message?.takeLast(51)})")
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun messageConverterHandleException(e: HttpMessageNotReadableException): ResponseEntity<ApiFormat<Unit>> {
        return ApiFormat.exception(HttpStatus.BAD_REQUEST.value(), e.message ?: "")
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun illegalArgumentHandleException(e: IllegalArgumentException): ResponseEntity<ApiFormat<Unit>> {
        return ApiFormat.exception(HttpStatus.BAD_REQUEST.value(), e.message ?: "")
    }

    @ExceptionHandler(ConversionFailedException::class)
    fun conversionFailedHandleError(e: ConversionFailedException): ResponseEntity<ApiFormat<Unit>> {
        return ApiFormat.exception(HttpStatus.BAD_REQUEST.value(), e.message ?: "")
    }

    @ExceptionHandler(IllegalStateException::class,UninitializedPropertyAccessException::class,NullPointerException::class,MissingServletRequestParameterException::class)
    fun internalServerHandleException(e: Exception): ResponseEntity<ApiFormat<Unit>> {
        e.printStackTrace()
        return ApiFormat.exception(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.message ?: "")
    }
}

