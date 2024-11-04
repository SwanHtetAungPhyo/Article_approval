package com.swanhtetaungphyo.article_approval.controller;

import com.swanhtetaungphyo.article_approval.dto.ArticleDto;
import com.swanhtetaungphyo.article_approval.services.ArticleServiceImpl;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController
@RequestMapping("/api/v1/articles")
public class ArticleController {

    private final RabbitTemplate rabbitTemplate;

    public ArticleController(ArticleServiceImpl articleService, RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("")
    public ResponseEntity<?> HandleArticleCreate(
            @Validated @RequestBody ArticleDto articleDto
            ) throws IOException {

        rabbitTemplate.convertAndSend("article-approval",articleDto);
        return ResponseEntity.status(300).body("Article is under the examination");
    }
}
