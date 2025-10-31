package com.teady.budgetserver.global.configuration

import org.apache.hc.client5.http.config.ConnectionConfig
import org.apache.hc.client5.http.config.RequestConfig
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient
import org.apache.hc.client5.http.impl.classic.HttpClients
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory
import org.apache.hc.core5.http.io.SocketConfig
import org.apache.hc.core5.util.TimeValue
import org.apache.hc.core5.util.Timeout
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * FeignClient 구현체 Apache HttpClient 5 설정
 * - 요청/응답 timeout 설정
 * - Connection Pool 관리
 * - HTTP와 TCP 계층의 Keep-Alive 설정
 */
@Configuration
class HttpClientConfiguration {

    @Bean
    fun apacheHttpClient(): CloseableHttpClient {
        // connection timeout, read timeout 설정
        val connectionConfig = ConnectionConfig.custom()
            // connection(TCP 3-way-hand-shaking)을 맺는 유효 시간
            .setConnectTimeout(Timeout.ofMilliseconds(3000))
            // connection 이후 데이터를 주고 받는 유효 시간
            .setSocketTimeout(Timeout.ofMilliseconds(10000))
            .build()

        // TCP 계층의 Keep-Alive 위한 Socket 설정
        val socketConfig = SocketConfig.custom()
            // TCP 계층의 Keep-Alive 활성화: 해당 connection의 생사여부 탐지를 위한 Probe 패킷 전송
            .setSoKeepAlive(true)
            // Nagle 알고리즘 비활성화: 작은 패킷도 즉시 전송하여 지연 감소
            .setTcpNoDelay(true)
            .build()

        // Connection Pool 관리 설정
        val connectionManager = PoolingHttpClientConnectionManagerBuilder.create()
            // SSL 설정 (HTTPS 용)
            .setSSLSocketFactory(SSLConnectionSocketFactory.getSocketFactory())
            .setMaxConnTotal(100)
            .setMaxConnPerRoute(20)
            .setDefaultConnectionConfig(connectionConfig)
            .setDefaultSocketConfig(socketConfig)
            .build()

        val requestConfig = RequestConfig.custom()
            // Connection Pool로 가용한 connection을 요청할 때의 타임아웃 설정
            .setConnectionRequestTimeout(Timeout.ofSeconds(5))
            .build()

        return HttpClients.custom()
            .setConnectionManager(connectionManager)
            .setDefaultRequestConfig(requestConfig)
            // HTTP 계층의 Keep-Alive 전략 설정: 응답 서버의 Keep-Alive 헤더를 우선 사용하고, 없으면 30초 동안 연결 유지
            .setKeepAliveStrategy { response, context ->
                val keepAliveIterator = response.headerIterator("Keep-Alive")
                while (keepAliveIterator.hasNext()) {
                    val element = keepAliveIterator.next()
                    val param = element.name
                    val value = element.value

                    if (value != null && param.equals("timeout", ignoreCase = true)) {
                        return@setKeepAliveStrategy TimeValue.ofSeconds(value.toLong())
                    }
                }
                // 기본값: 30초
                TimeValue.ofSeconds(30)
            }

            // Connection Pool 자체적으로 idle 상태의 connection을 clear 함
            .evictIdleConnections(TimeValue.ofSeconds(30))
            .evictExpiredConnections()
            .build()
    }
}

/*  ☑️ 참고 지식
    connection timeout: connection(TCP 3-way-hand-shaking)을 맺을 수 있는 유효 시간
    read timeout: connection을 맺은 후 data를 주고받을 수 있는 유효 시간

    HTTP 계층의 Keep-Alive: connected 되었던 connection을 idle 상태로 지속하는 시간 (응답 서버와의 약속)
    - idle 상태: TCP 3-way-hand-shaking 연결 설정은 되어 있으나 현재 유휴 상태로 재사용 될 수 있음

    connection pooling: connected 되었던 idle 상태의 connection을 재사용하기 위해 모아두는 pool
    - TCP 3-way-hand-shaking 비용 감소 효과
    - connection pool 자체적으로도 idle 상태의 connection을 clear할 수 있음: evictIdleConnections(Timeout)
      - dead connection을 미리 탐지할 순 없지만 dead connection 사용 자체를 예방하는 수단

    TCP 계층의 Keep-Alive: HTTP 계층의 Keep-alive와 다른, connection의 생사여부를 판단하기 위해 전송하는 Probe 용도임
    - 응답 서버와 HTTP 계층의 Keep-Alive를 통해 시간 약속을 했지만,
      패킷이 거치는 중간 노드 및 네트워크 물지 장비 등에서 connection을 끊었는지 않 끊었는지 알 수 없음. 이를 미리 알기 위한 수단임
 */