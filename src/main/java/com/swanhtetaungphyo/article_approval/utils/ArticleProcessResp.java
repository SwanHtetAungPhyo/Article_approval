package com.swanhtetaungphyo.article_approval.utils;

import com.swanhtetaungphyo.article_approval.models.ArticleStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ArticleProcessResp {
    private  String message;
    private ArticleStatus articleStatus;
    private Boolean condition;
    private String[][] tokens;
}
