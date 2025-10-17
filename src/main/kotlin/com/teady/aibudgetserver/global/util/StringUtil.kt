package com.teady.aibudgetserver.global.util

fun String.toUserId() = this.take(20)

fun String.toTimestamp(): String {
    val dropped = this.drop(20)
    return dropped.take(17)
}
fun toId(userId: String, timestamp: String): String {
    return userId.take(20).padStart(20, '0') + timestamp.take(17).padStart(17, '0')
}
