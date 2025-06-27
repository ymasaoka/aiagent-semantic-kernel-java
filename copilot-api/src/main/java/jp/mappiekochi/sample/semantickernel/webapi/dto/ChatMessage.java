package jp.mappiekochi.sample.semantickernel.webapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.microsoft.semantickernel.services.chatcompletion.AuthorRole;

public record ChatMessage (
    @JsonProperty("authorRole")
    AuthorRole role,
    @JsonProperty("content")
    String content
){
    public AuthorRole getRole() {
        return role;
    }

    public String getContent() {
        return content;
    }
}
