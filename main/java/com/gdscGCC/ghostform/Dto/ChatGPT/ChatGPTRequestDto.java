package com.gdscGCC.ghostform.Dto.ChatGPT;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

/** ChatGPT로 요청을 보내는 DTO */
@Getter
@ToString
@NoArgsConstructor
public class ChatGPTRequestDto {

    /** 사용 모델 */
    private String model;

    /** 최대 Token */
    @JsonProperty("max_tokens")
    private int maxTokens;

    /** temperature */
    private double temperature;

    /** stream */
    private boolean stream;

    /** chatgpt에게 질문할 message */
    private List<ChatGPTMessage> messages;

    @Builder
    public ChatGPTRequestDto(String model, int maxTokens, double temperature, boolean stream, List<ChatGPTMessage> messages) {
        this.model = model;
        this.maxTokens = maxTokens;
        this.temperature = temperature;
        this.stream = stream;
        this.messages = messages;
    }

}
