package dev.vobar.vobar_backend.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Document(collection = "articles")
public class Article {

    @Id
    private String id;

    private String title;
    private String summary;
    private String content;

    private List<String> tags;

    private boolean published;
    private Instant createdAt;
}
