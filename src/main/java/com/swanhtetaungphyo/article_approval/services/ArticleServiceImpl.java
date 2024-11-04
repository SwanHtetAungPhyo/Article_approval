package com.swanhtetaungphyo.article_approval.services;

import com.swanhtetaungphyo.article_approval.contract.ArticleServices;
import com.swanhtetaungphyo.article_approval.dto.ArticleDto;
import com.swanhtetaungphyo.article_approval.models.Article;
import com.swanhtetaungphyo.article_approval.models.ArticleStatus;
import com.swanhtetaungphyo.article_approval.repo.ArticleRepository;
import com.swanhtetaungphyo.article_approval.repo.UserRepository;
import com.swanhtetaungphyo.article_approval.utils.ArticleProcessResp;
import com.swanhtetaungphyo.article_approval.utils.UtilsClass;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleServices {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final NLProcessor nlProcessor;

    public ArticleServiceImpl(ArticleRepository articleRepository, UserRepository userRepository, ModelMapper modelMapper, NLProcessor nlProcessor) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.nlProcessor = nlProcessor;
    }

    @RabbitListener(queues = "article-approval")
    @Override
    public ArticleProcessResp CreateArticle(ArticleDto articleDto) throws IOException {
        ArticleProcessResp condition = ProcessArticles(articleDto);


        if (condition.getCondition() && condition.getArticleStatus() == ArticleStatus.APPROVED) {
            userRepository.findById(articleDto.getUserId()).ifPresent(user -> {
                Article articleToSave = modelMapper.map(articleDto, Article.class);
                articleToSave.setUser(user);
                articleRepository.save(articleToSave);
            });
        }

        return condition;
    }

    @Override
    public ArticleProcessResp ProcessArticles(ArticleDto articleDto) throws IOException {
        ArticleProcessResp articleProcessResp = new ArticleProcessResp();
        String[] sentences = nlProcessor.SentenceProcessing(articleDto.getContent());
        String[][] tokens = nlProcessor.TokenProcessing(sentences);
        boolean condition = nlProcessor.DecisionMaking(tokens);
        articleProcessResp.setCondition(condition);
        articleProcessResp.setTokens(tokens);
        return articleProcessResp;
    }

}
