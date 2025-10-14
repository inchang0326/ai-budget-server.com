package com.teady.aibudgetserver.global.aop

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

@Aspect
@Component
class LoggingAspect {

    private val logger = LoggerFactory.getLogger(javaClass)

    // Controller 메서드에 적용
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    fun controllerMethods() {}

    // Service 메서드에 적용
    @Pointcut("within(@org.springframework.stereotype.Service *)")
    fun serviceMethods() {}

    @Around("controllerMethods() || serviceMethods()")
    fun logAround(joinPoint: ProceedingJoinPoint): Any? {
        val startTime = System.currentTimeMillis()
        val className = joinPoint.signature.declaringTypeName
        val methodName = joinPoint.signature.name

        // HTTP 요청 정보 가져오기
        val request = (RequestContextHolder.getRequestAttributes() as? ServletRequestAttributes)?.request

        logger.info("==> [START] $className.$methodName() | URI: ${request?.requestURI}")

        return try {
            val result = joinPoint.proceed()
            val elapsedTime = System.currentTimeMillis() - startTime
            logger.info("<== [END] $className.$methodName() | 실행시간: ${elapsedTime}ms")
            result
        } catch (e: Exception) {
            val elapsedTime = System.currentTimeMillis() - startTime
            logger.error("<== [ERROR] $className.$methodName() | 실행시간: ${elapsedTime}ms | 예외: ${e.message}", e)
            throw e
        }
    }
}
