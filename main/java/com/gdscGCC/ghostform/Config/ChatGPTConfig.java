package com.gdscGCC.ghostform.Config;

import lombok.Getter;
import org.springframework.context.annotation.Configuration;

/** ChatGPT 통신과 관련된 Config */
@Configuration
@Getter
public class ChatGPTConfig {
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";
    public static final String CHAT_MODEL = "gpt-3.5-turbo"; // 사용 모델
    public static final Integer MAX_TOKEN = 300;
    public static final Boolean STREAM = false; // 완성된 답변 반환
    public static final Boolean STREAM_TRUE = true; // stream 형태로 답변 반환
    public static final String ROLE = "user"; // 역할
    public static final Double TEMPERATURE = 0.6; // chatgpt temperature
    public static final String MEDIA_TYPE = "application/json; charset=UTF-8"; // json, utf-8
    public static final String CHAT_URL = "https://api.openai.com/v1/chat/completions"; // 통신할 서버
}
