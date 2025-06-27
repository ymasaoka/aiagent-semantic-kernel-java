package jp.mappiekochi.sample.semantickernel.webapi.controller;

import jp.mappiekochi.sample.semantickernel.webapi.dto.ChatMessageRequest;
import jp.mappiekochi.sample.semantickernel.webapi.dto.ChatMessageResponse;
import jp.mappiekochi.sample.semantickernel.webapi.service.ChatService;
import reactor.core.publisher.Mono;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    /**
     * Constructor
     */
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/message")
    public Mono<ResponseEntity<ChatMessageResponse>> sendMessage(@Valid @RequestBody ChatMessageRequest request) {
        return chatService.chat(request)
                .map(ResponseEntity::ok);
    }
}
