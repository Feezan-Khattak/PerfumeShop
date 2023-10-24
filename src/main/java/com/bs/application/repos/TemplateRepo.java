package com.bs.application.repos;

import com.bs.application.entities.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TemplateRepo extends JpaRepository<Template, Long> {
    Optional<Template> findByTemplateKey(String key);
}
