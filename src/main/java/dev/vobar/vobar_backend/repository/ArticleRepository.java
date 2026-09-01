package dev.vobar.vobar_backend.repository;

import dev.vobar.vobar_backend.model.Article;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ArticleRepository extends MongoRepository<Article, String> {
    List<Article> findByPublishedTrue();
}
