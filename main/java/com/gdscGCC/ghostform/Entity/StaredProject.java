package com.gdscGCC.ghostform.Entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StaredProject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE) // 사용자 정보 삭제되면 얘도 삭제
    private User user;

    @Column(nullable = false)
    private boolean stared; // true = 좋아요, false = 좋아요 취소

    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDateTime createDateTime; // 날짜

    @PrePersist /** DB에 INSERT 되기 직전에 실행. 즉 DB에 값을 넣으면 자동으로 실행됨 */
    public void createDate() {
        this.createDateTime = LocalDateTime.now();
    }

    /** 생성자 */
    public StaredProject(Project project, User user) {
        this.project = project;
        this.user = user;
        this.stared = true;
    }

    public void unStarredProject(Project project) {
        this.stared = false;
        project.setStar(project.getStar() - 1);
    }
}
