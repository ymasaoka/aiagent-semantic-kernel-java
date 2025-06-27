package jp.mappiekochi.sample.semantickernel.webapi.service;

import jp.mappiekochi.sample.semantickernel.webapi.dto.ChatMessageRequest;
import jp.mappiekochi.sample.semantickernel.webapi.dto.ChatMessageResponse;
import reactor.core.publisher.Mono;

import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.services.ServiceNotFoundException;
import com.microsoft.semantickernel.services.chatcompletion.AuthorRole;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;

import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final Kernel kernel;
    private final InvocationContext invocationContext;

    public ChatService(Kernel kernel, InvocationContext invocationContext) {
        this.kernel = kernel;
        this.invocationContext = invocationContext;
    }

    public Mono<ChatMessageResponse> chat(ChatMessageRequest request) {
        try {
            ChatCompletionService chatService = kernel.getService(ChatCompletionService.class);
            ChatHistory chatHistory = request.context() != null
                    ? request.context().createChatHistory()
                    : new ChatHistory();
            chatHistory.addUserMessage(request.content());

            return chatService.getChatMessageContentsAsync(chatHistory, kernel, invocationContext)
                    .map(result -> {
                        StringBuilder content = new StringBuilder();
                        for (var message : result) {
                            if (message.getAuthorRole() == AuthorRole.ASSISTANT) {
                                content.append(message.getContent());
                            }
                        }
                        chatHistory.addAssistantMessage(content.toString());
                        return ChatMessageResponse.withContext(
                            UUID.randomUUID().toString(),
                            request.conversationId(),
                            content.toString(),
                            chatHistory);
                    });

        } catch (ServiceNotFoundException e) {
            throw new RuntimeException("ChatCompletionService is not configured in the Semantic Kernel", e);
        }
    }
}
