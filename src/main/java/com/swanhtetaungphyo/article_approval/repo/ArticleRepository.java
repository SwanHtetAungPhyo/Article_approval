package com.swanhtetaungphyo.article_approval.repo;

import com.swanhtetaungphyo.article_approval.models.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}