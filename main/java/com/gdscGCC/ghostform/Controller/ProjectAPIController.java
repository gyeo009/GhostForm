package com.gdscGCC.ghostform.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gdscGCC.ghostform.Dto.Ask.AskRequestDto;
import com.gdscGCC.ghostform.Dto.ChatGPT.ChatGPTResponseDto;
import com.gdscGCC.ghostform.Dto.Project.ProjectRequestDto;
import com.gdscGCC.ghostform.Dto.Project.ProjectResponseDto;
import com.gdscGCC.ghostform.Dto.Variable.VariableRequestDto;
import com.gdscGCC.ghostform.Entity.Ask;
import com.gdscGCC.ghostform.Entity.Project;
import com.gdscGCC.ghostform.Service.ChatGPTService;
import com.gdscGCC.ghostform.Service.ProjectService;
import com.gdscGCC.ghostform.Service.VariableService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/projects")
@Tag(name = "Project", description = "Project API")
public class ProjectAPIController {
    private final ProjectService projectService;
    private final ChatGPTService chatGPTService;
    private final VariableService variableService;



    /** 한 개의 프로젝트 조회 */
    @GetMapping("/{project_id}")
    public ResponseEntity<ProjectResponseDto> projectGet(@PathVariable Long project_id){
        return ResponseEntity.status(HttpStatus.OK).body(projectService.findById(project_id));
    }

    /** 모든 프로젝트 조회 */
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ProjectResponseDto>> projectListGet(@PageableDefault(size = 5) Pageable pageable){

        return ResponseEntity.status(HttpStatus.OK).body(projectService.findAll(pageable));
    }

    /** 한 개의 프로젝트 생성 */
    @PostMapping("")
    public Long projectSave(@RequestBody ProjectRequestDto requestDto){
        return projectService.save(requestDto);
    }

    /** 한 개의 프로젝트 삭제 */
    @DeleteMapping("")
    public void projectDelete(@PathVariable Long project_id){
        projectService.delete(project_id);
    }

    /** 한 개의 프로젝트 수정 */
    @PutMapping("/{project_id}")
    public ResponseEntity<ProjectResponseDto> projectUpdate(@PathVariable Long project_id, @RequestBody ProjectRequestDto requestDto){
        return ResponseEntity.status(HttpStatus.OK).body(projectService.update(project_id, requestDto));
    }

    /** 프로젝트 공개범위 변경
     * RequestParameter로 변경할 visibility를 받아옴 */
    @PutMapping("/{project_id}/visibility")
    public ResponseEntity<String> updateVisibility(@PathVariable Long project_id, @RequestParam String visibility){
        return ResponseEntity.status(HttpStatus.OK).body(projectService.updateVisibility(project_id, visibility));
    }

    /** ChatGPT로부터 Stream 형태의 답변 받아오기 */
    @PostMapping(value = "/{project_id}/ask", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> makeQuestionStream (@PathVariable Long project_id, @RequestBody AskRequestDto askRequestDto) {
        ProjectResponseDto project = projectService.findById(project_id);
        String question = project.getContent();
        for (Ask a : askRequestDto.getAskList()) {
            question = question.replace("{{" + a.getKey() + "}}", a.getValue());
        }
        try {
            return chatGPTService.getChatGptStreamResponse(question);
        } catch (JsonProcessingException e) {
            log.warn(e.toString());
            return Flux.empty();
        }
    }

    /** ChatGPT로부터 JSON 형태의 답변 받아오기 */
    @PostMapping(value = "/{project_id}/ask/json")
    public ResponseEntity<ChatGPTResponseDto> makeQuestion (@PathVariable Long project_id, @RequestBody AskRequestDto askRequestDto) {
        ProjectResponseDto project = projectService.findById(project_id);
        String question = project.getContent();
        for (Ask a : askRequestDto.getAskList()) {
            question = question.replace("{{" + a.getKey() + "}}", a.getValue());
        }
        ChatGPTResponseDto chatGPTResponseDto = null;
        try {
            chatGPTResponseDto = chatGPTService.askQuestion(question);
        } catch (Exception e) {
            log.warn(e.toString());
        }
        return ResponseEntity.status(HttpStatus.OK).body(chatGPTResponseDto);
    }

    /** Project의 모든 변수 조회
     * RequestParameter로 project_id를 받아옴*/
    @GetMapping("/{project_id}/variables")
    public ResponseEntity<HashMap<String, Object>> getAllVariables(@PathVariable Long project_id){
        return ResponseEntity.status(HttpStatus.OK).body(variableService.getAllVariables(project_id));
    }

    /** Project의 특정 변수 하나 조회
     * PathVariable로 project_id를 받아옴
     * RequestParameter로 project_id 및 variable_key를 받아옴*/
    @GetMapping("/{project_id}/variable")
    public ResponseEntity<Object> getOneVariables(@PathVariable Long project_id, @RequestParam String key){
        return ResponseEntity.status(HttpStatus.OK).body(variableService.getOneVariable(project_id, key));
    }

    /** Project에 한 개의 변수 생성
     * PathVariable로 project_id를 받아옴
     * RequestBody로 생성할 JSON을 받아옴 */
    @PostMapping("/{project_id}/variables")
    public HashMap<String, Object> createOneVariable(@PathVariable Long project_id, @RequestBody HashMap<String, Object> map){
        return variableService.create(project_id, map);
    }

    /** Project에 한 개의 변수 삭제
     * PathVariable로 project_id를 받아옴
     * RequestParameter로 삭제할 변수 key를 받아옴 */
    @DeleteMapping("/{project_id}/variables")
    public void deleteOneVariable(@PathVariable Long project_id, @RequestParam String deleteKey){
        variableService.deleteOne(project_id, deleteKey);
    }

    /** 한 개의 변수 수정
     * PathVariable로 project_id를 받아옴
     * RequestBody로 수정할 변수 key와 value를 받아옴*/
    @PutMapping("/{project_id}/variables")
    public void updateOneVariable(@PathVariable Long project_id, @RequestBody VariableRequestDto variableRequestDto){
        variableService.updateOneVariable(project_id, variableRequestDto);
    }
}
