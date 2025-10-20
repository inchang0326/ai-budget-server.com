package com.teady.gatewayserver.filter

import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order

@Configuration
class GlobalFilter {

    @Value("\${gateway.secret}")
    private lateinit var secretKey: String

    @Bean
    @Order(-1)
    fun customGlobalFilter(): GlobalFilter {
        return GlobalFilter { exchange, chain ->
            val request = exchange.request.mutate()
                .header("X-GATEWAY-SECRET", secretKey)
                .build()
            chain.filter(exchange.mutate().request(request).build())
        }
    }
}
