package dev.vobar.vobar_backend.repository;

import dev.vobar.vobar_backend.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByPublishedTrue();
}
