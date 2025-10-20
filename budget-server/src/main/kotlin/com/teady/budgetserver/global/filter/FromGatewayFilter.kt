package com.teady.budgetserver.global.filter

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

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val secretHeader = request.getHeader("X-GATEWAY-SECRET")

        if (secretHeader == null || secretHeader != gatewaySecret) {
            response.status = HttpServletResponse.SC_FORBIDDEN
            response.writer.write("Access Denied: Invalid")
            return
        }

        filterChain.doFilter(request, response)
    }
}
