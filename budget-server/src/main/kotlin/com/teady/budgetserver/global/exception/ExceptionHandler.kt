package com.teady.budgetserver.global.exception

import ApiResponse
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import org.slf4j.LoggerFactory
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
    ) = handleException(ex, ex.errorCode)

    @ExceptionHandler(FeignClientException::class)
    fun handleFeignClientException(
        ex: FeignClientException,
    ) = handleException(ex, ex.errorCode)

    /* 사용자 정의 Business Exception 또는 FeignClient Exception이 아닌 것들 start */
    @ExceptionHandler(Exception::class)
    fun handleUnexpectedException(
        ex: Exception,
    ) = handleException(ex, ErrorCode.INTERNAL_SERVER_ERROR)

    // request body의 validation 예외
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(
        ex: MethodArgumentNotValidException,
    ) = handleException(ex, ErrorCode.VALIDATION_ERROR)

    // 대표적으로 JSON 파싱 처리 예외 (ex. 형식 불일치 등)
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException
    ) = handleException(ex, ErrorCode.SYSTEM_INVALID_JSON)

    // query parameter 또는 path variable의 type mismatch 예외
    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleTypeMismatch(
        ex: MethodArgumentTypeMismatchException
    ) = handleException(ex, ErrorCode.SYSTEM_ARGUMENT_TYPE_MISMATCH)

    // 필수 query parameter 누락 예외
    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun handleMissingParameter(
        ex: MissingServletRequestParameterException
    ) = handleException(ex, ErrorCode.SYSTEM_MISSING_PARAMETER)

    // 해당 서버가 지원하지 않는 HTTP Method 사용 예외
    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleMethodNotSupported(
        ex: HttpRequestMethodNotSupportedException
    ) = handleException(ex, ErrorCode.SYSTEM_UNSUPPORTED_METHOD)

    // Spring Security 인가 예외
    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDeniedException(
        ex: AccessDeniedException
    ) = handleException(ex, ErrorCode.SYSTEM_ACCESS_DENIED)

    // query parameter 또는 path variable의 validation 예외
    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(
        ex: ConstraintViolationException
    ) = handleException(ex, ErrorCode.SYSTEM_CONSTRAINT_VIOLATION)
    /* 사용자 정의 Business Exception 또는 FeignClient Exception이 아닌 것들 end */

    private fun handleException(ex: Exception, errorCode: ErrorCode): ResponseEntity<ApiResponse<Nothing>> {

        val errCode = errorCode.code
        val errMessage = errorCode.message
        val errStatus = errorCode.status

        if (errStatus.is4xxClientError) {
            logger.warn("[{}] {} {}", errCode, errMessage, ex.message)
        } else {
            logger.error("[{}] {} {}", errCode, errMessage, ex.message)
        }

        incrementErrorCounter(errCode, errMessage, errStatus.value())

        return ResponseEntity.status(errStatus)
            .body(
                ApiResponse.failure(
                    error = errCode,
                    message = errMessage
                )
            )
    }

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