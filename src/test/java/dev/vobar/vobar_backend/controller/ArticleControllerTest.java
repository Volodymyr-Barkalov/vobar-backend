package dev.vobar.vobar_backend.controller;

import dev.vobar.vobar_backend.dto.ArticleResponse;
import dev.vobar.vobar_backend.service.ArticleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ArticleControllerTest {

    @Mock
    private ArticleService articleService;

    @InjectMocks
    private ArticleController articleController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(articleController).build();
    }

    private ArticleResponse response(Long id) {
        return new ArticleResponse(id, "Title", "Summary", "Content", List.of("tag"), true, Instant.now());
    }

    @Test
    void getAll_whenAnonymous_returnsPublishedArticles() throws Exception {
        // given
        when(articleService.findAll(false)).thenReturn(List.of(response(1L), response(2L)));

        // when / then
        mockMvc.perform(get("/api/articles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getOne_whenArticleExists_returnsArticle() throws Exception {
        // given
        when(articleService.findById(1L)).thenReturn(response(1L));

        // when / then
        mockMvc.perform(get("/api/articles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Title"));
    }

    @Test
    void getOne_whenArticleNotFound_returnsNotFound() throws Exception {
        // given
        when(articleService.findById(99L)).thenThrow(new ResponseStatusException(NOT_FOUND));

        // when / then
        mockMvc.perform(get("/api/articles/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_whenValidRequest_returnsCreated() throws Exception {
        // given
        when(articleService.create(any())).thenReturn(response(1L));

        // when / then
        mockMvc.perform(post("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title":"Title","summary":"Summary","content":"Content","tags":["tag"],"published":true}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void update_whenArticleExists_returnsUpdated() throws Exception {
        // given
        when(articleService.update(eq(1L), any())).thenReturn(response(1L));

        // when / then
        mockMvc.perform(put("/api/articles/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title":"Updated","summary":"Summary","content":"Content","tags":[],"published":false}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void delete_whenArticleExists_returnsNoContent() throws Exception {
        // given
        doNothing().when(articleService).delete(1L);

        // when / then
        mockMvc.perform(delete("/api/articles/1"))
                .andExpect(status().isNoContent());

        verify(articleService).delete(1L);
    }

    @Test
    void delete_whenArticleNotFound_returnsNotFound() throws Exception {
        // given
        doThrow(new ResponseStatusException(NOT_FOUND)).when(articleService).delete(99L);

        // when / then
        mockMvc.perform(delete("/api/articles/99"))
                .andExpect(status().isNotFound());
    }
}
