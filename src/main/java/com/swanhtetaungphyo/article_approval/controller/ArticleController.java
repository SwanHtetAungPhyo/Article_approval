package com.swanhtetaungphyo.article_approval.controller;

import com.swanhtetaungphyo.article_approval.dto.ArticleDto;
import com.swanhtetaungphyo.article_approval.models.ArticleStatus;
import com.swanhtetaungphyo.article_approval.services.ArticleServiceImpl;
import com.swanhtetaungphyo.article_approval.utils.ArticleProcessResp;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController
@RequestMapping("/api/v1/articles")
public class ArticleController {

    private  final ArticleServiceImpl articleService;

    private final RabbitTemplate rabbitTemplate;

    public ArticleController(ArticleServiceImpl articleService, RabbitTemplate rabbitTemplate) {
        this.articleService = articleService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("")
    public ResponseEntity<?> HandleArticleCreate(
            @RequestBody ArticleDto articleDto
            ) throws IOException {
        ArticleProcessResp decision = articleService.CreateArticle(articleDto);

        if(decision.getCondition() && decision.getArticleStatus() == ArticleStatus.APPROVED){
            return ResponseEntity.ok().body(decision);
        }
        return ResponseEntity.status(300).body(decision);
    }
}
