package com.gdscGCC.ghostform.Dto.Project;

import com.gdscGCC.ghostform.Entity.Project;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.HashMap;

@Setter
@NoArgsConstructor
@Getter
public class ProjectRequestDto {
    private Long project_id;
    private String title;
    private String description;
    private String content;
    private HashMap<String, Object> variables = new HashMap<>();
    private Long user_id;
    private LocalDateTime lastModifiedDate;
//    private Run run_id;



    public LocalDateTime setLastModifiedDate() {
        return this.lastModifiedDate = LocalDateTime.now();
    }
    @Builder
    public ProjectRequestDto(Long project_id, String title, String description, LocalDateTime lastModifiedDate, String content, HashMap<String, Object> variables, Long user_id) {
        this.project_id = project_id;
        this.title = title;
        this.description = description;
        this.lastModifiedDate = setLastModifiedDate();
        this.content = content;
        this.variables = variables;
        this.user_id = user_id;
//        this.run = run;
    }

    public Project toEntity(){
        return Project.builder()
                .project_id(this.project_id)
                .title(this.title)
                .description(this.description)
                .lastModifiedDate(this.lastModifiedDate)
                .content(this.content)
                .variables(this.variables)
                .user_id(this.user_id)
//                .run_id(this.run_id)
                .build();
    }
}
