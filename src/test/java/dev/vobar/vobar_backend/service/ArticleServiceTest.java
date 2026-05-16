package dev.vobar.vobar_backend.service;

import dev.vobar.vobar_backend.dto.ArticleRequest;
import dev.vobar.vobar_backend.dto.ArticleResponse;
import dev.vobar.vobar_backend.model.Article;
import dev.vobar.vobar_backend.repository.ArticleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @Mock
    private ArticleRepository repository;

    @InjectMocks
    private ArticleService articleService;

    private Article article(Long id, boolean published) {
        Article a = new Article();
        a.setId(id);
        a.setTitle("Title");
        a.setSummary("Summary");
        a.setContent("Content");
        a.setTags(List.of("tag"));
        a.setPublished(published);
        a.setCreatedAt(Instant.now());
        return a;
    }

    @Test
    void findAll_whenIncludeUnpublished_returnsAllArticles() {
        // given
        when(repository.findAll()).thenReturn(List.of(article(1L, true), article(2L, false)));

        // when
        List<ArticleResponse> result = articleService.findAll(true);

        // then
        assertThat(result).hasSize(2);
        verify(repository).findAll();
        verify(repository, never()).findByPublishedTrue();
    }

    @Test
    void findAll_whenExcludeUnpublished_returnsPublishedOnly() {
        // given
        when(repository.findByPublishedTrue()).thenReturn(List.of(article(1L, true)));

        // when
        List<ArticleResponse> result = articleService.findAll(false);

        // then
        assertThat(result).hasSize(1);
        verify(repository).findByPublishedTrue();
        verify(repository, never()).findAll();
    }

    @Test
    void findById_whenArticleExists_returnsArticle() {
        // given
        when(repository.findById(1L)).thenReturn(Optional.of(article(1L, true)));

        // when
        ArticleResponse result = articleService.findById(1L);

        // then
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.title()).isEqualTo("Title");
    }

    @Test
    void findById_whenArticleNotFound_throwsNotFoundException() {
        // given
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> articleService.findById(99L))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void create_whenValidRequest_savesAndReturnsArticle() {
        // given
        ArticleRequest request = new ArticleRequest("Title", "Summary", "Content", List.of("tag"), true);
        when(repository.save(any())).thenReturn(article(1L, true));

        // when
        ArticleResponse result = articleService.create(request);

        // then
        assertThat(result.id()).isEqualTo(1L);
        verify(repository).save(any(Article.class));
    }

    @Test
    void update_whenArticleExists_updatesAndReturnsArticle() {
        // given
        Article existing = article(1L, true);
        ArticleRequest request = new ArticleRequest("Updated", "Summary", "Content", List.of(), false);
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        // when
        ArticleResponse result = articleService.update(1L, request);

        // then
        assertThat(result).isNotNull();
        verify(repository).save(existing);
    }

    @Test
    void update_whenArticleNotFound_throwsNotFoundException() {
        // given
        ArticleRequest request = new ArticleRequest("Title", "Summary", "Content", List.of(), true);
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> articleService.update(99L, request))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void delete_whenArticleExists_deletesArticle() {
        // given
        when(repository.existsById(1L)).thenReturn(true);

        // when
        articleService.delete(1L);

        // then
        verify(repository).deleteById(1L);
    }

    @Test
    void delete_whenArticleNotFound_throwsNotFoundException() {
        // given
        when(repository.existsById(99L)).thenReturn(false);

        // when / then
        assertThatThrownBy(() -> articleService.delete(99L))
                .isInstanceOf(ResponseStatusException.class);
        verify(repository, never()).deleteById(any());
    }
}
