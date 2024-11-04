package com.swanhtetaungphyo.article_approval.contract;

import com.swanhtetaungphyo.article_approval.dto.ArticleDto;
import com.swanhtetaungphyo.article_approval.utils.ArticleProcessResp;

public interface ArticleServices {
    ArticleProcessResp CreateArticle(ArticleDto articleDto);
   ArticleProcessResp ProcessArticles(ArticleDto articleDto);
}
