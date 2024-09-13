package com.gdscGCC.ghostform.Dto.ChatGPT;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/** ChatGPT의 Response에 대한 DTO */
@Getter
@NoArgsConstructor
public class ChatGPTResponseDto {
    private String id;
    private String object;
    private long created;
    private String model;
    private Usage usage;
    private List<Choice> choices;

    @Getter
    @Setter
    public static class Usage {
        @JsonProperty("prompt_tokens")
        private int promptTokens;
        @JsonProperty("completion_tokens")
        private int completionTokens;
        @JsonProperty("total_tokens")
        private int totalTokens;
    }

    @Getter
    @Setter
    public static class Choice {
        private ChatGPTMessage message;
        @JsonProperty("finish_reason")
        private String finishReason;
        private int index;
    }
}
