package com.teady.budgetserver.global.exception

import org.springframework.http.HttpStatus

sealed class BusinessException(
    val errorCode: ErrorCode,
    override val message: String = errorCode.message,
    val status: HttpStatus = errorCode.status
) : RuntimeException(message)

class ResourceNotFoundException(
    errorCode: ErrorCode = ErrorCode.RESOURCE_NOT_FOUND,
    message: String = errorCode.message,
    status: HttpStatus = errorCode.status
) : BusinessException(errorCode, message, status)

class InvalidRequestException(
    errorCode: ErrorCode = ErrorCode.INVALID_REQUEST,
    message: String = errorCode.message,
    status: HttpStatus = errorCode.status
) : BusinessException(errorCode, message, status)

class UnauthorizedException(
    errorCode: ErrorCode = ErrorCode.UNAUTHORIZED,
    message: String = errorCode.message,
    status: HttpStatus = errorCode.status
) : BusinessException(errorCode, message, status)

sealed class FeignClientException(
    val errorCode: ErrorCode,
    override val message: String = errorCode.message,
    val status: HttpStatus = errorCode.status
) : RuntimeException(message)

class FeignBadRequestException(
    errorCode: ErrorCode = ErrorCode.FEIGN_BAD_REQUEST,
    message: String = errorCode.message,
    status: HttpStatus = errorCode.status
) : FeignClientException(errorCode, message, status)

class FeignUnauthorizedException(
    errorCode: ErrorCode = ErrorCode.FEIGN_UNAUTHORIZED,
    message: String = errorCode.message,
    status: HttpStatus = errorCode.status
) : FeignClientException(errorCode, message, status)

class FeignForbiddenException(
    errorCode: ErrorCode = ErrorCode.FEIGN_FORBIDDEN,
    message: String = errorCode.message,
    status: HttpStatus = errorCode.status
) : FeignClientException(errorCode, message, status)

class FeignResourceNotFoundException(
    errorCode: ErrorCode = ErrorCode.FEIGN_RESOURCE_NOT_FOUND,
    message: String = errorCode.message,
    status: HttpStatus = errorCode.status
) : FeignClientException(errorCode, message, status)

class FeignInternalServerException(
    errorCode: ErrorCode = ErrorCode.FEIGN_INTERNAL_SERVER_ERROR,
    message: String = errorCode.message,
    status: HttpStatus = errorCode.status
) : FeignClientException(errorCode, message, status)

class FeignServiceUnavailableException(
    errorCode: ErrorCode = ErrorCode.FEIGN_SERVICE_UNAVAILABLE,
    message: String = errorCode.message,
    status: HttpStatus = errorCode.status
) : FeignClientException(errorCode, message, status)