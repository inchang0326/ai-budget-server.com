package com.teady.eurekaserver.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class FromGatewayFilter(
    @Value("\${gateway.secret}") private val gatewaySecret: String
) : OncePerRequestFilter() {

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val path = request.requestURI
        // Render 헬스 체크 경로(/actuator/health)나 루트(/) 등은 제외
        return path == "/actuator/health" || path == "/"
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val secretHeader = request.getHeader("X-GATEWAY-SECRET")

//        if (secretHeader == null || secretHeader != gatewaySecret) {
//            response.status = HttpServletResponse.SC_FORBIDDEN
//            response.writer.write("Access Denied: Invalid")
//            return
//        }

        filterChain.doFilter(request, response)
    }
}
