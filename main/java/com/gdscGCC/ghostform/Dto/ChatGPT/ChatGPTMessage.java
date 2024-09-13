package com.gdscGCC.ghostform.Dto.ChatGPT;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** ChatGPTRequestDTO에서 사용하는 messages의 저장을 위한 class */
@Data
@NoArgsConstructor
public class ChatGPTMessage {
    private String role;
    private String content;

    @Builder
    public ChatGPTMessage(String role, String content) {
        this.role = role;
        this.content = content;
    }
}
