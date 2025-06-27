package jp.mappiekochi.sample.semantickernel.webapi.dto;

import java.time.LocalDateTime;

import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;

/**
 * DTO for chat message response.
 * @param messageId Unique identifier for the message.
 * @param conversationId Unique identifier for the conversation.
 * @param content The content of the chat message.
 * @param confidence Confidence score of the message, if applicable.
 * @param datetime The date and time when the message was sent.
 * @param context Additional context for the message, such as previous messages or metadata.
 */
public record ChatMessageResponse (
    String messageId,
    String conversationId,
    String content,
    Double confidence,
    LocalDateTime datetime,
    ChatHistory context
){
    /**
     * Factory method to create a basic ChatMessageResponse instance.
     */
    public static ChatMessageResponse of(String messageId, String conversationId, String content) {
        return new ChatMessageResponse (
            messageId,
            conversationId,
            content,
            null,
            LocalDateTime.now(),
            null
        );
    }

    /**
     * Factory method to create a ChatMessageResponse instance with confidence score.
     */
    public static ChatMessageResponse withConfidence(String messageId, String conversationId, String content, Double condifence) {
        return new ChatMessageResponse (
            messageId,
            conversationId,
            content,
            condifence,
            LocalDateTime.now(),
            null
        );
    }

    /**
     * Factory method to create a ChatMessageResponse instance with context.
     */
    public static ChatMessageResponse withContext(String messageId, String conversationId, String content, ChatHistory context) {
        return new ChatMessageResponse (
            messageId,
            conversationId,
            content,
            null,
            LocalDateTime.now(),
            context
        );
    }
}
