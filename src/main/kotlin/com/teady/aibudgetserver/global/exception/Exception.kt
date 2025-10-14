package com.teady.aibudgetserver.global.exception
sealed class BusinessException(
    message: String,
    val errorCode: String
) : RuntimeException(message)

class ResourceNotFoundException(
    message: String = "요청한 리소스를 찾을 수 없습니다",
    errorCode: String = "RESOURCE_NOT_FOUND"
) : BusinessException(message, errorCode)

class InvalidRequestException(
    message: String = "잘못된 요청입니다",
    errorCode: String = "INVALID_REQUEST"
) : BusinessException(message, errorCode)

class UnauthorizedException(
    message: String = "인증이 필요합니다",
    errorCode: String = "UNAUTHORIZED"
) : BusinessException(message, errorCode)