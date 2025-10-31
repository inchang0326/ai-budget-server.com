package com.teady.budgetserver.adapter.secondary.openapi.openbanking

import com.teady.budgetserver.global.exception.FeignServiceUnavailableException

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

/**
 * ExternalApiClient의 Fallback 구현
 * Circuit Breaker가 OPEN 상태이거나 예외 발생 시 이 메서드들이 호출됩니다.
 *
 * 주의: @Component 어노테이션 필수!
 */
@Component
class ExternalApiClientFallback : ExternalApiClient {

    private val logger = LoggerFactory.getLogger(ExternalApiClientFallback::class.java)

    /**
     * getUserById의 Fallback
     * 외부 API 호출 실패 시 기본값 반환
     */
    override fun getUserById(userId: Long): UserResponse {
        logger.error("Circuit Breaker 작동: getUserById($userId) 호출 실패, Fallback 실행")

        // 1. 캐시된 데이터 반환 (Redis, 로컬 캐시 등)
        // 2. 기본값 반환
        // 3. 예외 던지기 (특정 상황에서)

        return UserResponse(
            id = userId,
            name = "Unknown User",
            email = "unknown@example.com",
            createdAt = "N/A"
        )
    }

    /**
     * createUser의 Fallback
     * 생성 작업은 Fallback으로 처리하기 어려우므로 예외를 던집니다.
     */
    override fun createUser(request: CreateUserRequest): UserResponse {
        logger.error("Circuit Breaker 작동: createUser 호출 실패, Fallback 실행")

        // 생성/수정/삭제 같은 작업은 Fallback에서 처리하기 어렵습니다.
        // 대신 명확한 예외를 던져 클라이언트에게 알립니다.
        throw FeignServiceUnavailableException("사용자 생성 서비스를 일시적으로 사용할 수 없습니다. 잠시 후 다시 시도해주세요.")
    }

    /**
     * searchUsers의 Fallback
     * 빈 목록 반환
     */
    override fun searchUsers(name: String, page: Int, size: Int): UserListResponse {
        logger.error("Circuit Breaker 작동: searchUsers 호출 실패, Fallback 실행")

        // 빈 결과 반환
        return UserListResponse(
            users = emptyList(),
            totalCount = 0,
            page = page,
            size = size
        )
    }
}
