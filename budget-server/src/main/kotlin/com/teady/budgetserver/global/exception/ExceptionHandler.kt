package com.teady.budgetserver.global.exception

import ApiResponse
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import java.util.concurrent.ConcurrentHashMap
import javax.validation.ConstraintViolationException

@RestControllerAdvice
class GlobalExceptionHandler(
    private val meterRegistry: MeterRegistry
) {

    private val logger = LoggerFactory.getLogger(javaClass)
    private val errorCounters = ConcurrentHashMap<String, Counter>()

    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(
        ex: BusinessException,
    ): ResponseEntity<ApiResponse<Nothing>> {

        logger.error("Business Exception: {}", ex.message, ex)

        val errCode = ex.errorCode.code
        val errMessage = ex.message
        val errStatus = ex.errorCode.status.value()

        incrementErrorCounter(errCode, errStatus)

        return ResponseEntity.status(errStatus)
            .body(
                ApiResponse.failure(
                    error = errCode,
                    message = errMessage
                )
            )
    }

    @ExceptionHandler(Exception::class)
    fun handleFeignClientException(
        ex: FeignClientException,
    ): ResponseEntity<ApiResponse<Nothing>> {

        logger.error("FeignClient Exception: {}", ex.message, ex)

        val errCode = ex.errorCode.code
        val errMessage = ex.message
        val errStatus = ex.errorCode.status.value()

        incrementErrorCounter(errCode, errStatus)

        return ResponseEntity.status(errStatus)
            .body(
                ApiResponse.failure(
                    error = errCode,
                    message = errMessage
                )
            )
    }

    /* 사용자 정의 Business Exception 또는 FeignClient Exception이 아닌 것들 start */
    @ExceptionHandler(Exception::class)
    fun handleUnexpectedException(
        ex: Exception,
    ): ResponseEntity<ApiResponse<Nothing>> {

        logger.error("Unexpected Exception: {}", ex.message, ex)

        val errCode = ErrorCode.INTERNAL_SERVER_ERROR.code
        val errMessage = ErrorCode.INTERNAL_SERVER_ERROR.message
        val errStatus = ErrorCode.INTERNAL_SERVER_ERROR.status.value()

        incrementErrorCounter(errCode, errStatus)

        return ResponseEntity.status(errStatus)
            .body(
                ApiResponse.failure(
                    error = errCode,
                    message = errMessage
                )
            )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(
        ex: MethodArgumentNotValidException,
    ): ResponseEntity<ApiResponse<Nothing>> {

        val errorMessage = ex.bindingResult.fieldErrors
            .joinToString(", ") { "${it.field}: ${it.defaultMessage}" }

        logger.warn("Validation Error: {}", errorMessage)
        incrementErrorCounter("VALIDATION_ERROR", HttpStatus.BAD_REQUEST.value())

        return ResponseEntity.badRequest()
            .body(
                ApiResponse.failure(
                    error = "VALIDATION_ERROR",
                    message = errorMessage
                )
            )
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException
    ): ResponseEntity<ApiResponse<Nothing>> {

        logger.warn("Invalid request body: {}", ex.message)

        val errorCode = ErrorCode.INVALID_REQUEST
        incrementErrorCounter(errorCode.code, errorCode.status.value())

        return ResponseEntity.badRequest()
            .body(
                ApiResponse.failure(
                    error = errorCode.code,
                    message = "요청 본문을 읽을 수 없습니다. JSON 형식을 확인해주세요."
                )
            )
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleTypeMismatch(
        ex: MethodArgumentTypeMismatchException
    ): ResponseEntity<ApiResponse<Nothing>> {

        val message = "${ex.name} 파라미터의 타입이 올바르지 않습니다. " +
                "기대 타입: ${ex.requiredType?.simpleName}"

        logger.warn(message)

        return ResponseEntity.badRequest()
            .body(
                ApiResponse.failure(
                    error = "TYPE_MISMATCH",
                    message = message
                )
            )
    }
    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun handleMissingParameter(
        ex: MissingServletRequestParameterException
    ): ResponseEntity<ApiResponse<Nothing>> {

        val message = "필수 쿼리 파라미터가 누락되었습니다: ${ex.parameterName}"
        logger.warn(message)

        return ResponseEntity.badRequest()
            .body(
                ApiResponse.failure(
                    error = "MISSING_PARAMETER",
                    message = message
                )
            )
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleMethodNotSupported(
        ex: HttpRequestMethodNotSupportedException
    ): ResponseEntity<ApiResponse<Nothing>> {

        val message = "${ex.method} 메서드는 지원하지 않습니다. " +
                "지원 메서드: ${ex.supportedHttpMethods?.joinToString()}"

        logger.warn(message)

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
            .body(
                ApiResponse.failure(
                    error = "METHOD_NOT_ALLOWED",
                    message = message
                )
            )
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDenied(
        ex: AccessDeniedException
    ): ResponseEntity<ApiResponse<Nothing>> {

        logger.warn("Access denied: {}", ex.message)

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(
                ApiResponse.failure(
                    error = "ACCESS_DENIED",
                    message = "접근 권한이 없습니다"
                )
            )
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(
        ex: AccessDeniedException
    ): ResponseEntity<ApiResponse<Nothing>> {

        logger.warn("Access denied: {}", ex.message)

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(
                ApiResponse.failure(
                    error = "ACCESS_DENIED",
                    message = "접근 권한이 없습니다"
                )
            )
    }
    /* 사용자 정의 Business Exception 또는 FeignClient Exception이 아닌 것들 end */

    private fun incrementErrorCounter(errCode: String, errMessage: String, errStatus: Int) {
        val counterKey = "${errCode}_${errMessage}_${errStatus}"

        val counter = errorCounters.computeIfAbsent(counterKey) {
            Counter.builder("application.errors.total")
                .tag("error_code", errCode)
                .tag("error_message", errMessage)
                .tag("error_status", errStatus.toString())
                .description("Total number of errors by (error_code, error_message, error_status")
                .register(meterRegistry)
        }

        counter.increment()
    }
}