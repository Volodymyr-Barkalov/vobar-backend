package dev.vobar.vobar_backend.service;

import dev.vobar.vobar_backend.dto.ArticleRequest;
import dev.vobar.vobar_backend.dto.ArticleResponse;
import dev.vobar.vobar_backend.model.Article;
import dev.vobar.vobar_backend.repository.ArticleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ArticleService {

    private final ArticleRepository repository;

    public ArticleService(ArticleRepository repository) {
        this.repository = repository;
    }

    public List<ArticleResponse> findAll(boolean includeUnpublished) {
        List<Article> articles = includeUnpublished
                ? repository.findAll()
                : repository.findByPublishedTrue();
        return articles.stream().map(ArticleResponse::from).toList();
    }

    public ArticleResponse findById(Long id) {
        return repository.findById(id)
                .map(ArticleResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Transactional
    public ArticleResponse create(ArticleRequest request) {
        Article article = new Article();
        article.setTitle(request.title());
        article.setSummary(request.summary());
        article.setContent(request.content());
        article.setTags(request.tags());
        article.setPublished(request.published());
        article.setCreatedAt(Instant.now());
        return ArticleResponse.from(repository.save(article));
    }

    @Transactional
    public ArticleResponse update(Long id, ArticleRequest request) {
        Article article = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        article.setTitle(request.title());
        article.setSummary(request.summary());
        article.setContent(request.content());
        article.setTags(request.tags());
        article.setPublished(request.published());
        return ArticleResponse.from(repository.save(article));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        repository.deleteById(id);
    }
}
