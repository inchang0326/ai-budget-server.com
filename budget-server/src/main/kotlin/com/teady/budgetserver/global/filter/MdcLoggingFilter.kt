package com.teady.budgetserver.global.filter

import jakarta.servlet.*
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.MDC
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class MdcLoggingFilter : OncePerRequestFilter() {

    companion object {
        const val USER_ID = "userId"
        const val REQUEST_DATE = "requestDate"
        const val REQUEST_ID = "requestId"
        const val REQUEST_URI = "requestUri"
        const val REQUEST_METHOD = "requestMethod"
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: jakarta.servlet.http.HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val userId = extractUserId(request)
            val requestDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
            val requestId = UUID.randomUUID().toString().substring(0, 8)
            val requestUri = request.requestURI
            val requestMethod = request.method

            // MDC 설정
            MDC.put(USER_ID, userId)
            MDC.put(REQUEST_DATE, requestDate)
            MDC.put(REQUEST_ID, requestId)
            MDC.put(REQUEST_URI, requestUri)
            MDC.put(REQUEST_METHOD, requestMethod)

            filterChain.doFilter(request, response)

        } finally {
            MDC.clear()
        }
    }

    private fun extractUserId(request: HttpServletRequest): String {
        request.getHeader("X-USER-ID")?.let { return it }
        request.getParameter("userId")?.let { return it }
        return "anonymous"
    }

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val path = request.requestURI
        return path.startsWith("/actuator") ||
                path.startsWith("/swagger-ui") ||
                path.startsWith("/v3/api-docs") ||
                path.startsWith("/static") ||
                path.endsWith(".css") ||
                path.endsWith(".js") ||
                path.endsWith(".ico")
    }
}
