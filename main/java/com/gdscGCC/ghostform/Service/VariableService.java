package com.gdscGCC.ghostform.Service;

import com.gdscGCC.ghostform.Dto.Variable.VariableRequestDto;
import com.gdscGCC.ghostform.Entity.Project;
import com.gdscGCC.ghostform.Repository.ProjectRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class VariableService {
    private final ProjectRepository projectRepository;

    /** 모든 변수 조회 */
    @Transactional
    public HashMap<String, Object> getAllVariables(Long project_id){
        
        Project project = projectRepository.findById(project_id).orElseThrow(()-> new IllegalArgumentException("해당 템플릿이 없습니다. id=" + project_id));
        System.out.println(project.getVariables());
        HashMap<String, Object> variable_list = project.getVariables();
        return variable_list;

    }

    /** 변수 하나 조회 */
    @Transactional
    public Object getOneVariable(Long project_id, String key){
        Project project = projectRepository.findById(project_id).orElseThrow(()-> new IllegalArgumentException("해당 템플릿이 없습니다. id=" + project_id));
        HashMap<String, Object> variable_list = project.getVariables();
        Object value = variable_list.get(key);
        if( value != null){
            return value;
        }
        else{
            System.out.println("해당 변수가 존재하지 않습니다.");
            return null;
        }
    }

    /** 변수 하나 생성 */
    @Transactional
    public HashMap<String, Object> create(Long project_id, HashMap<String, Object> map){
        Project project = projectRepository.findById(project_id).orElseThrow(()-> new IllegalArgumentException("해당 템플릿이 없습니다. id=" + project_id));
        HashMap<String, Object> variables = project.getVariables();

        Set<String> keys = map.keySet();
        for (String key : keys){
            variables.put(key, map.get(key));
        }

        // 추가 후 업데이트
        project.updateProject(project.getProject_id(), project.getTitle(), project.getDescription(), project.getLastModifiedDate(), project.getContent(), project.getVariables(), project.getUser_id());

        return map;
    }

    /** 변수 하나 삭제 */
    @Transactional
    public void deleteOne(Long project_id, String deleteKey){
        Project project = projectRepository.findById(project_id).orElseThrow(()-> new IllegalArgumentException("해당 템플릿이 없습니다. id=" + project_id));

        HashMap<String, Object> variables = project.getVariables();


        if(variables.containsKey(deleteKey)){
            variables.remove(deleteKey);
        }
        else{
            System.out.println("해당 key가 존재하지 않습니다.");
        }

        // 삭제 후 업데이트
        project.updateProject(project.getProject_id(), project.getTitle(), project.getDescription(), project.getLastModifiedDate(), project.getContent(), project.getVariables(), project.getUser_id());

    }

    /** 변수 하나 수정 */
    @Transactional
    public void updateOneVariable(Long project_id, VariableRequestDto variableRequestDto){
        Project project = projectRepository.findById(project_id).orElseThrow(()-> new IllegalArgumentException("해당 템플릿이 없습니다. id=" + project_id));

        String key = variableRequestDto.getKey();
        Object value = variableRequestDto.getValue();

        // 기존 project의 변수들
        HashMap<String, Object> variables = project.getVariables();


        if(variables.containsKey(key)){
            variables.put(key, value);
        }
        else{
            System.out.println("해당 key가 존재하지 않습니다.");
        }

        // 수정 후 업데이트
        project.updateProject(project.getProject_id(), project.getTitle(), project.getDescription(), project.getLastModifiedDate(), project.getContent(), project.getVariables(), project.getUser_id());
    }




}
