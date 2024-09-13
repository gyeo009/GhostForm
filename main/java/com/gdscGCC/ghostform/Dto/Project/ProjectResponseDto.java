package com.gdscGCC.ghostform.Dto.Project;

import com.gdscGCC.ghostform.Entity.Project;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.HashMap;

@Getter

public class ProjectResponseDto {
    private Long project_id;
    private String title;
    private String description;
    private String content;
    private HashMap<String, Object> variables = new HashMap<>();
    private Long user_id;
    private LocalDateTime lastModifiedDate;
    private Long star;
    private Project.Visibility visibility;
//    private Run run_id;

    public ProjectResponseDto(Project project) {
        this.project_id = project.getProject_id();
        this.title = project.getTitle();
        this.description = project.getDescription();
        this.lastModifiedDate = project.getLastModifiedDate();
        this.content = project.getContent();
        this.variables = project.getVariables();
        this.user_id = project.getUser_id();
        this.star = project.getStar();
        this.visibility = project.getVisibility();
//        this.run_id = project.getRun_id();
    }
}
