package com.gdscGCC.ghostform.Service;

import com.gdscGCC.ghostform.Dto.Project.ProjectRequestDto;
import com.gdscGCC.ghostform.Dto.Project.ProjectResponseDto;
import com.gdscGCC.ghostform.Entity.Project;
import com.gdscGCC.ghostform.Repository.ProjectRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProjectService {
    private final ProjectRepository projectRepository;


    /** DB에 save */
    @Transactional
    public Long save(ProjectRequestDto requestDto){
        // dto를 entity화 해서 repository의 save 메소드를 통해 db에 저장.
        // 저장 후 생성한 id를 반환해 줌.
        return projectRepository.save(requestDto.toEntity()).getProject_id();
    }

    /** DB에서 모든 row 조회 */
    @Transactional
    public List<ProjectResponseDto> findAll(Pageable pageable){
        Page<Project> projects = projectRepository.findAll(pageable);
        if (projects.isEmpty()) {
            throw new IllegalArgumentException("해당 프로젝트 리스트가 없습니다.");
        }
        List<ProjectResponseDto> projectResponseDtoList = new ArrayList<>();
        projects.stream().forEach(i -> projectResponseDtoList.add(new ProjectResponseDto(i)));

        return projectResponseDtoList;
    }

    /** DB에서 하나의 row 조회 */
    @Transactional
    public ProjectResponseDto findById(Long project_id){
        Project project = projectRepository.findById(project_id).orElseThrow(()-> new IllegalArgumentException("해당 프로젝트가 없습니다. id=" + project_id));
        return new ProjectResponseDto(project);
    }

    /** DB에서 하나의 row 수정 */
    @Transactional
    public ProjectResponseDto update(Long project_id, ProjectRequestDto requestDto){
        Project project = projectRepository.findById(project_id).orElseThrow(()-> new IllegalArgumentException("해당 프로젝트가 없습니다. id=" + project_id));
        project.updateProject(requestDto.getProject_id(), requestDto.getTitle(), requestDto.getDescription(), requestDto.getLastModifiedDate(), requestDto.getContent(), requestDto.getVariables(), requestDto.getUser_id());
        return new ProjectResponseDto(project);
    }
    /** DB에서 하나의 row 삭제 */
    @Transactional
    public void delete(Long project_id){
        Project project = projectRepository.findById(project_id).orElseThrow(()-> new IllegalArgumentException("해당 프로젝트가 없습니다. id=" + project_id));
        projectRepository.deleteById(project_id);
        System.out.println("project id : " + project.getProject_id() + "project was deleted.");
    }

    /** 프로젝트 공개범위 변경 */
    @Transactional
    public String updateVisibility(Long project_id, String visibility) {
        Project project = projectRepository.findById(project_id).orElseThrow(()-> new IllegalArgumentException("해당 프로젝트가 없습니다. id=" + project_id));
        return project.updateVisibility(visibility);

    }
}
