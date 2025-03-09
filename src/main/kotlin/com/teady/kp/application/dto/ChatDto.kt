package com.teady.kp.application.dto

import javax.validation.constraints.NotNull

data class ChatDto(@NotNull val conversationId: String, @NotNull val message: String) {
}