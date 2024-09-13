package com.gdscGCC.ghostform.Service;

import com.gdscGCC.ghostform.Dto.Project.ProjectResponseDto;
import com.gdscGCC.ghostform.Entity.Project;
import com.gdscGCC.ghostform.Entity.StaredProject;
import com.gdscGCC.ghostform.Entity.User;
import com.gdscGCC.ghostform.Repository.ProjectRepository;
import com.gdscGCC.ghostform.Repository.StaredProjectRepository;
import com.gdscGCC.ghostform.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.aot.generate.AccessControl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CommunityService {
    private final ProjectRepository projectRepository;
    private final StaredProjectRepository staredProjectRepository;
    private final UserRepository userRepository;


    @Transactional
    public String setStar(Long project_id, Long user_id){
        Project project = projectRepository.findById(project_id).orElseThrow(()-> new IllegalArgumentException("해당 프로젝트가 없습니다. id=" + project_id));
        User user = userRepository.findById(user_id).orElseThrow(()-> new IllegalArgumentException("해당 프로젝트가 없습니다. id=" + project_id));

        if(staredProjectRepository.findByProjectAndUser(project, user) == null) {
            // 좋아요를 누른적 없다면 StarredProject 생성 후, 좋아요 처리
            project.setStar(project.getStar() + 1);
            StaredProject staredProject = new StaredProject(project, user); // true 처리
            staredProjectRepository.save(staredProject);
            return "Star";
        } else {
            // 좋아요를 누른적 있다면 취소 처리 후 테이블 삭제
            StaredProject staredProject = staredProjectRepository.findByProjectAndUser(project, user);
            staredProject.unStarredProject(project);
            staredProjectRepository.deleteById(staredProject.getId());
            return "Undo star";
        }

    }

    @Transactional
    public List<ProjectResponseDto> findByStared(Pageable pageable, Long user_id) {
        Page<StaredProject> staredProjects = staredProjectRepository.findAllByStaredIsAndUserIs(pageable, true, user_id);
        List<ProjectResponseDto> projectResponseDtoList = new ArrayList<>();
        staredProjects.stream().forEach(i -> projectResponseDtoList.add(new ProjectResponseDto(i.getProject())));

        return projectResponseDtoList;
    }

    @Transactional
    public List<ProjectResponseDto> findBestProjects(Pageable pageable) {
        // star 받은 project 중에서 탐색
        Page<Project> projects = projectRepository.findByStarGreaterThanEqualAndStarIsNot(pageable, 10L, 0);
        List<ProjectResponseDto> projectResponseDtoList = new ArrayList<>();
        projects.stream().forEach(i -> projectResponseDtoList.add(new ProjectResponseDto(i)));

        return projectResponseDtoList;
    }

    /** user_id 변경, star 초기화, project_id 재발급, 공개 범위도 초기화 */
    @Transactional
    public ProjectResponseDto fork(Long project_id, Long user_id) {
        Project project = projectRepository.findById(project_id).orElseThrow(()-> new IllegalArgumentException("해당 프로젝트가 없습니다. id=" + project_id));
        Project forkedProject = Project.builder().build();
        forkedProject.updateProject(forkedProject.getProject_id(),
                                    project.getTitle(),
                                    project.getDescription(),
                                    project.getLastModifiedDate(),
                                    project.getContent(),
                                    project.getVariables(),
                                    user_id // 이 함수를 호출한 user_id로 fork한 프로젝트의 사용자 변경
//                                    forkedProject.getRun_id()
                                    );
        return new ProjectResponseDto(projectRepository.save(forkedProject));
    }

    /** 공개된 프로젝트만 조회 */
    @Transactional
    public List<ProjectResponseDto> findPublic(Pageable pageable) {
        Project.Visibility visibility = Project.Visibility.PRIVATE;
        Page<Project> projects = projectRepository.findByVisibilityIs(pageable, visibility);

        List<ProjectResponseDto> projectResponseDtoList = new ArrayList<>();
        projects.stream().forEach(i -> projectResponseDtoList.add(new ProjectResponseDto(i)));

        return projectResponseDtoList;
    }
}
