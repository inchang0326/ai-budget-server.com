package com.teady.budgetserver.global.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val code: String,
    val message: String,
    val status: HttpStatus
) {
    RESOURCE_NOT_FOUND(
        code = "RESOURCE_NOT_FOUND",
        message = "요청한 리소스를 찾을 수 없습니다",
        status = HttpStatus.NOT_FOUND
    ),
    INVALID_REQUEST(
        code = "INVALID_REQUEST",
        message = "잘못된 요청입니다",
        status = HttpStatus.BAD_REQUEST
    ),
    UNAUTHORIZED(
        code = "UNAUTHORIZED",
        message = "인증이 필요합니다",
        status = HttpStatus.UNAUTHORIZED
    ),
    VALIDATION_ERROR(
        code = "VALIDATION_ERROR",
        message = "입력값 검증에 실패했습니다",
        status = HttpStatus.BAD_REQUEST
    ),
    INTERNAL_SERVER_ERROR(
        code = "INTERNAL_SERVER_ERROR",
        message = "서버 내부 오류가 발생했습니다",
        status = HttpStatus.INTERNAL_SERVER_ERROR
    ),
    FEIGN_CLIENT_ERROR(
        code = "FEIGN_CLIENT_ERROR",
        message = "외부 API 호출 중 오류가 발생했습니다",
        status = HttpStatus.INTERNAL_SERVER_ERROR
    ),
    FEIGN_BAD_REQUEST(
        code = "FEIGN_BAD_REQUEST",
        message = "외부 API에 잘못된 요청을 보냈습니다",
        status = HttpStatus.BAD_REQUEST
    ),
    FEIGN_UNAUTHORIZED(
        code = "FEIGN_UNAUTHORIZED",
        message = "외부 API 인증이 필요합니다",
        status = HttpStatus.UNAUTHORIZED
    ),
    FEIGN_FORBIDDEN(
        code = "FEIGN_FORBIDDEN",
        message = "외부 API 접근 권한이 없습니다",
        status = HttpStatus.FORBIDDEN
    ),
    FEIGN_RESOURCE_NOT_FOUND(
        code = "FEIGN_RESOURCE_NOT_FOUND",
        message = "외부 API 리소스를 찾을 수 없습니다",
        status = HttpStatus.NOT_FOUND
    ),
    FEIGN_INTERNAL_SERVER_ERROR(
        code = "FEIGN_INTERNAL_SERVER_ERROR",
        message = "외부 API 서버 오류가 발생했습니다",
        status = HttpStatus.INTERNAL_SERVER_ERROR
    ),
    FEIGN_SERVICE_UNAVAILABLE(
        code = "FEIGN_SERVICE_UNAVAILABLE",
        message = "외부 API 서비스를 일시적으로 사용할 수 없습니다",
        status = HttpStatus.SERVICE_UNAVAILABLE
    );

    companion object {
        fun fromCode(code: String): ErrorCode {
            return entries.find { it.code == code } ?: INTERNAL_SERVER_ERROR
        }
    }
}
