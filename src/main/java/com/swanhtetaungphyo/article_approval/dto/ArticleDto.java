package com.swanhtetaungphyo.article_approval.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.swanhtetaungphyo.article_approval.models.Article}
 */
@Value
public class ArticleDto  {
    @NotNull
    @Size(max = 150)
    String title;
    String tag;
    @NotNull
    @Size(max = 255)
    String content;

    @NotNull
    Long UserId;
}