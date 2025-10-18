package com.teady.aibudgetserver.global.exception

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime

data class ErrorResponse(
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val status: Int,
    val error: String,
    val message: String?,
    val errorCode: String,
    val path: String? = null
)

@RestControllerAdvice
class GlobalExceptionHandler(
    private val meterRegistry: MeterRegistry
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    // 커스텀 비즈니스 예외 처리
    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(ex: BusinessException): ResponseEntity<ErrorResponse> {
        logger.error("Business Exception: ${ex.message}", ex)

        // Prometheus 메트릭 기록
        incrementErrorCounter(ex.errorCode)

        val response = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            error = HttpStatus.BAD_REQUEST.reasonPhrase,
            message = ex.message,
            errorCode = ex.errorCode
        )

        return ResponseEntity.badRequest().body(response)
    }

    // 리소스를 찾지 못한 경우
    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleResourceNotFoundException(ex: ResourceNotFoundException): ResponseEntity<ErrorResponse> {
        logger.error("Resource Not Found: ${ex.message}", ex)

        incrementErrorCounter(ex.errorCode)

        val response = ErrorResponse(
            status = HttpStatus.NOT_FOUND.value(),
            error = HttpStatus.NOT_FOUND.reasonPhrase,
            message = ex.message,
            errorCode = ex.errorCode
        )

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response)
    }

    // Validation 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(ex: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val errorMessage = ex.bindingResult.fieldErrors
            .joinToString(", ") { "${it.field}: ${it.defaultMessage}" }

        logger.error("Validation Error: $errorMessage", ex)

        incrementErrorCounter("VALIDATION_ERROR")

        val response = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            error = HttpStatus.BAD_REQUEST.reasonPhrase,
            message = errorMessage,
            errorCode = "VALIDATION_ERROR"
        )

        return ResponseEntity.badRequest().body(response)
    }

    // 모든 예외 처리 (최종 핸들러)
    @ExceptionHandler(Exception::class)
    fun handleAllException(ex: Exception): ResponseEntity<ErrorResponse> {
        logger.error("Unexpected Error: ${ex.message}", ex)

        incrementErrorCounter("INTERNAL_SERVER_ERROR")

        val response = ErrorResponse(
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            error = HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase,
            message = "서버 내부 오류가 발생했습니다",
            errorCode = "INTERNAL_SERVER_ERROR"
        )

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response)
    }

    private fun incrementErrorCounter(errorCode: String) {
        Counter.builder("application.errors")
            .tag("error_code", errorCode)
            .register(meterRegistry)
            .increment()
    }
}
