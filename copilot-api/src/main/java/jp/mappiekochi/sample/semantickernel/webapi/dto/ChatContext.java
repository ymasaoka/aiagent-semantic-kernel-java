package jp.mappiekochi.sample.semantickernel.webapi.dto;

import java.util.List;

import com.microsoft.semantickernel.services.chatcompletion.AuthorRole;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;

public class ChatContext {
    public List<ChatMessage> messages;
    public ChatMessage lastMessage;

    public ChatContext(List<ChatMessage> messages, ChatMessage lastMessage) {
        this.messages = messages;
        this.lastMessage = lastMessage;
    }

    public ChatHistory createChatHistory() {
        ChatHistory chatHistory = new ChatHistory();
        for (ChatMessage message : messages) {
            AuthorRole role;
            switch (message.getRole()) {
                case AuthorRole.USER:
                    role = AuthorRole.USER;
                    break;
                case AuthorRole.SYSTEM:
                    role = AuthorRole.SYSTEM;
                    break;
                case AuthorRole.ASSISTANT:
                    role = AuthorRole.ASSISTANT;
                    break;
                case AuthorRole.TOOL:
                    role = AuthorRole.TOOL;
                    break;
                default:
                    throw new IllegalArgumentException("Unknown role: " + message.getRole());
            }
            chatHistory.addMessage(role, message.getContent());
        }
        return chatHistory;
    }
}
