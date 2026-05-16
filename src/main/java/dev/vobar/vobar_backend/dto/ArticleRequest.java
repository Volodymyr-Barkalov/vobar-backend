package dev.vobar.vobar_backend.dto;

import java.util.List;

public record ArticleRequest(
        String title,
        String summary,
        String content,
        List<String> tags,
        boolean published
) {}
