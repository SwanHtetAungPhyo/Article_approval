package com.swanhtetaungphyo.article_approval.dto;

import org.springframework.validation.annotation.Validated;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for {@link com.swanhtetaungphyo.article_approval.models.Article}
 */
@Getter
@Setter
@Validated
public class ArticleDto  {


    String title;
    String tag;


    String content;



}