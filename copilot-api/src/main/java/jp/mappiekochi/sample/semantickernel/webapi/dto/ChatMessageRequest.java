package jp.mappiekochi.sample.semantickernel.webapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for chat message request.
 */
public record ChatMessageRequest (
    @NotBlank(message = "Message content is required.")
    @Size(max = 1000, message = "Message content must not exceed 1000 characters")
    String content,
    String conversationId,
    String sessionId,
    ChatContext context
) {

    /**
     * Factory method to create a ChatMessageRequest instance.
     */
    public static ChatMessageRequest of(String content) {
        return new ChatMessageRequest(
            content,
            null,
            null,
            null
        );
    }

    /**
     * Factory method to create a ChatMessageRequest instance with context.
     */
    public static ChatMessageRequest withContext(String content, ChatContext context) {
        return new ChatMessageRequest(
            content,
            null,
            null,
            context
        );
    }

    /**
     * Factory method to create a ChatMessageRequest instance with session and conversation IDs.
     */
    public static ChatMessageRequest withSession(String content, String sessionId, String conversationId) {
        return new ChatMessageRequest(
            content,
            conversationId,
            sessionId,
            null
        );
    }
}
