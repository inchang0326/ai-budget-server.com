package com.teady.budgetserver.adapter.secondary.openapi.openbanking

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.*

@FeignClient(
    name = "openbanking-api-client",
    url = "\${openbanking.api.base-url}",
    configuration = [OpenBankingFeignConfig::class],
    fallback = ExternalApiClientFallback::class
)
interface ExternalApiClient {

    /**
     * GET 요청 예제: 사용자 정보 조회
     * @param userId 사용자 ID
     * @return 사용자 정보
     */
    @GetMapping("/todo")
    fun getFinCardNumber(): String {
        return "todo";
    }

    /**
     * POST 요청 예제: 사용자 생성
     * @param request 생성할 사용자 정보
     * @return 생성된 사용자 정보
     */
    @PostMapping("/todo")
    fun createUser()
}