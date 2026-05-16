package dev.vobar.vobar_backend.controller;

import dev.vobar.vobar_backend.dto.ArticleRequest;
import dev.vobar.vobar_backend.dto.ArticleResponse;
import dev.vobar.vobar_backend.service.ArticleService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public List<ArticleResponse> getAll(Authentication authentication) {
        boolean isAdmin = authentication != null && authentication.isAuthenticated();
        return articleService.findAll(isAdmin);
    }

    @GetMapping("/{id}")
    public ArticleResponse getOne(@PathVariable Long id) {
        return articleService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ArticleResponse create(@RequestBody ArticleRequest request) {
        return articleService.create(request);
    }

    @PutMapping("/{id}")
    public ArticleResponse update(@PathVariable Long id, @RequestBody ArticleRequest request) {
        return articleService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        articleService.delete(id);
    }
}
