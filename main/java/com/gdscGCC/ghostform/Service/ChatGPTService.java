package com.gdscGCC.ghostform.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.gdscGCC.ghostform.Config.ChatGPTConfig;
import com.gdscGCC.ghostform.Dto.ChatGPT.ChatGPTRequestDto;
import com.gdscGCC.ghostform.Dto.ChatGPT.ChatGPTResponseDto;
import com.gdscGCC.ghostform.Dto.ChatGPT.ChatGPTMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

/** ChatGPT와의 통신을 담당하는 service
 *  - Cakeblade
 *  */
@Service
@RequiredArgsConstructor
public class ChatGPTService {

    /** yml 파일에서 가져오는 API Key*/
    @Value(value = "${chatgpt.api_key}")
    private String api_key;
    private final RestTemplate restTemplate = new RestTemplate();

    /** Object Mapper */
    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE );

    /** https header를 만드는 method */
    public HttpEntity<ChatGPTRequestDto> buildHttpEntity(ChatGPTRequestDto chatGptRequest){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType(ChatGPTConfig.MEDIA_TYPE));
        httpHeaders.add(ChatGPTConfig.AUTHORIZATION, ChatGPTConfig.BEARER + api_key);
        return new HttpEntity<>(chatGptRequest, httpHeaders);
    }

    /** OpenAI API 서버로 보낼 dto 생성 */
    private ChatGPTRequestDto makeRequestDto(List<ChatGPTMessage> messages) {
        return new ChatGPTRequestDto(
            ChatGPTConfig.CHAT_MODEL, // 사용 모델
            ChatGPTConfig.MAX_TOKEN, // 최대 token
            ChatGPTConfig.TEMPERATURE, // temperature
            ChatGPTConfig.STREAM, // 완성된 답변
            messages // message
            //ChatGptConfig.TOP_P
        );
    }

    /** OpenAI API 서버로 보낼 dto 생성, stream 형태로 답변을 받을 수 잇게 함 */
    private ChatGPTRequestDto makeStreamRequestDto(List<ChatGPTMessage> messages) {
        return new ChatGPTRequestDto(
                ChatGPTConfig.CHAT_MODEL,
                ChatGPTConfig.MAX_TOKEN,
                ChatGPTConfig.TEMPERATURE,
                ChatGPTConfig.STREAM_TRUE, // stream 형태로 답변을 달라고 요청
                messages
                //ChatGptConfig.TOP_P
        );
    }

    /** chatgpt 통신시 사용할 message 생성 method */
    private List<ChatGPTMessage> makeMessage(String question) {
        List<ChatGPTMessage> output = new ArrayList<>();
        output.add(ChatGPTMessage.builder()
                .role(ChatGPTConfig.ROLE) // 역할
                .content(question) // 질문
                .build());

        return output;
    }

    /** OpenAI API와 통신을 개시하는 method */
    public ChatGPTResponseDto askQuestion(String question){
        List<ChatGPTMessage> messages = this.makeMessage(question);
        return this.getChatGptResponse(
                this.buildHttpEntity(
                        this.makeRequestDto(messages)
                )
        );
    }

    /** askQuestion에서 진행한 통신을 받아 dto 형태로 return하는 method */
    public ChatGPTResponseDto getChatGptResponse(HttpEntity<ChatGPTRequestDto> chatGptRequestHttpEntity)
    {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory(); // http request 생성
        requestFactory.setConnectTimeout(60000);
        requestFactory.setReadTimeout(60 * 1000);   //  1min = 60 sec * 1,000ms, chatgpt가 충분히 답변할 수 있는 시간을 줌
        restTemplate.setRequestFactory(requestFactory); // restTemplate에다 집어넣음

        // openai api 서버로 보냄
        ResponseEntity<ChatGPTResponseDto> responseEntity = restTemplate.postForEntity(
                ChatGPTConfig.CHAT_URL,
                chatGptRequestHttpEntity,
                ChatGPTResponseDto.class);

        return responseEntity.getBody();
    }

    /** ChaptGPT의 답변을 stream 형태로 받아오는 Method */
    public Flux<String> getChatGptStreamResponse(String question) throws JsonProcessingException
    {
        // WebClient를 위한 Header 생성
        WebClient client = WebClient.builder()
                .baseUrl(ChatGPTConfig.CHAT_URL)
                .defaultHeader(
                        HttpHeaders.CONTENT_TYPE,
                        MediaType.APPLICATION_JSON_VALUE
                )
                .defaultHeader(
                        ChatGPTConfig.AUTHORIZATION,
                        ChatGPTConfig.BEARER + api_key)
                .build();

        List<ChatGPTMessage> messages = this.makeMessage(question);
        ChatGPTRequestDto chatGptRequest = this.makeStreamRequestDto(messages);

        String requestValue = objectMapper.writeValueAsString(chatGptRequest);

        return client.post()
                .bodyValue(requestValue)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(String.class);
    }

}
