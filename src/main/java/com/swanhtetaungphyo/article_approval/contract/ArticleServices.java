package com.swanhtetaungphyo.article_approval.contract;

import com.swanhtetaungphyo.article_approval.dto.ArticleDto;
import com.swanhtetaungphyo.article_approval.utils.ArticleProcessResp;

import java.io.IOException;

public interface ArticleServices {
   void CreateArticle(ArticleDto articleDto) throws IOException;
   ArticleProcessResp ProcessArticles(ArticleDto articleDto) throws IOException;
}
