package dev.vobar.vobar_backend.dto;

import dev.vobar.vobar_backend.model.Article;

import java.time.Instant;
import java.util.List;

public record ArticleResponse(
        Long id,
        String title,
        String summary,
        String content,
        List<String> tags,
        boolean published,
        Instant createdAt
) {
    public static ArticleResponse from(Article article) {
        return new ArticleResponse(
                article.getId(),
                article.getTitle(),
                article.getSummary(),
                article.getContent(),
                article.getTags(),
                article.isPublished(),
                article.getCreatedAt()
        );
    }
}
