package com.gdscGCC.ghostform.Repository;

import com.gdscGCC.ghostform.Entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Page<Project> findAll(Pageable pageable);
    Page<Project> findByStarGreaterThanEqualAndStarIsNot(Pageable pageable, Long stars, int zero);
    Page<Project> findByVisibilityIs(Pageable pageable, Project.Visibility visibility);
}
