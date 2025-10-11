import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.time.LocalDateTime
import java.util.UUID

data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val message: String? = null,
    val error: String? = null,
    val timestamp: String = LocalDateTime.now().toString(),
    val requestId: String = UUID.randomUUID().toString()
) {
    companion object {
        fun <T> success(data: T, message: String? = null): ApiResponse<T> {
            return ApiResponse(
                success = true,
                data = data,
                message = message
            )
        }

        fun <T> failure(error: String, message: String? = null): ApiResponse<T> {
            return ApiResponse(
                success = false,
                error = error,
                message = message
            )
        }
    }
}

data class PaginatedResponse<T>(
    val items: List<T>,
    val count: Int,
    val totalCount: Long,
    val page: Int,
    val limit: Int,
    val totalPages: Int,
    val hasNext: Boolean,
    val hasPrev: Boolean
) {
    companion object {
        fun <T> of(
            items: List<T>,
            page: Int,
            limit: Int,
            totalCount: Long
        ): PaginatedResponse<T> {
            val totalPages = (totalCount / limit + if (totalCount % limit > 0) 1 else 0).toInt()
            return PaginatedResponse(
                items = items,
                count = items.size,
                totalCount = totalCount,
                page = page,
                limit = limit,
                totalPages = totalPages,
                hasNext = page < totalPages,
                hasPrev = page > 1
            )
        }
    }
}

fun <T> T.toSuccessResponse(message: String? = null): ResponseEntity<ApiResponse<T>> {
    return ResponseEntity.ok(
        ApiResponse.success(
            data = this,
            message = message
        )
    )
}
fun <T> PaginatedResponse<T>.toSuccessResponse(message: String? = null): ResponseEntity<ApiResponse<PaginatedResponse<T>>> {
    return ResponseEntity.ok(
        ApiResponse.success(
            data = this,
            message = message
        )
    )
}
fun <T> String.toErrorResponse(
    status: HttpStatus = HttpStatus.BAD_REQUEST,
    message: String? = null
): ResponseEntity<ApiResponse<T>> {
    return ResponseEntity.status(status)
        .body(
            ApiResponse.failure(
                error = this,
                message = message
            )
        )
}