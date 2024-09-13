package com.gdscGCC.ghostform.Entity;

import java.time.LocalDateTime;
import java.util.HashMap;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;


@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
/** 프로젝트 개체 */
public class Project {
    @Id
    @GeneratedValue
    /** 프로젝트 번호 */
    private Long project_id;

    /** 프로젝트 제목 */
    private String title;

    /** 프로젝트 설명 */
    private String description;

    /** 템플릿 전체 내용 */
    private String content;

    /** 템플릿 변수들
     * key로 Long id, String name, String type이 존재 */
    @Type(JsonType.class)
    @Column(name = "variables", columnDefinition = "text")
    private HashMap<String, Object> variables = new HashMap<>();

    /** 사용자 id */
    private Long user_id;

    /** 최종 수정일 */
    private LocalDateTime lastModifiedDate;

    /** 프로젝트 STAR */
    private Long star;

    /** 프로젝트 공개 범위 */
    @Enumerated(EnumType.STRING)
    private Visibility visibility;

//    /** 실행 테이블 */
//    @OneToMany
//    @JoinColumn(name = "run_id")
//    private Run run_id;

    /** 프로젝트 star 및 공개 범위 초기화 */
    @PrePersist
    public void prePersist() {
        this.star = (this.star == null ? 0 : this.star);
        this.visibility = (this.visibility == null ? Visibility.PRIVATE : this.visibility);
    }


    public LocalDateTime setLastModifiedDate() {
        return this.lastModifiedDate = LocalDateTime.now();
    }

    public void updateProject(Long project_id, String title, String description, LocalDateTime lastModifiedDate, String content, HashMap<String, Object> variables, Long user_id) {
        this.project_id = project_id;
        this.title = title;
        this.description = description;
        this.lastModifiedDate = setLastModifiedDate();
        this.content = content;
        this.variables = variables;
        this.user_id = user_id;
//        this.run_id = run_id;
    }
    public String updateVisibility(String visibility) {
        switch (visibility.toUpperCase()) {
            case "PRIVATE":
                this.visibility = Visibility.PRIVATE;
                return "PRIVATE";
            case "PUBLIC":
                this.visibility = Visibility.PUBLIC;
                return "PUBLIC";
            default:
                throw new IllegalArgumentException("Invalid visibility string: " + visibility);
        }
    }



    @Builder
    public Project(Long project_id, String title, String description, LocalDateTime lastModifiedDate, String content, HashMap<String, Object> variables, Long user_id) {
        this.project_id = project_id;
        this.title = title;
        this.description = description;
        this.lastModifiedDate = setLastModifiedDate();
        this.content = content;
        this.variables = variables;
        this.user_id = user_id;
//        this.run_id = run_id;
    }

    public enum Visibility {
        PRIVATE, PUBLIC
    }

}


