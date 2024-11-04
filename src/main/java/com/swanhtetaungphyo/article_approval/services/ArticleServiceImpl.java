package com.swanhtetaungphyo.article_approval.services;

import com.swanhtetaungphyo.article_approval.contract.ArticleServices;
import com.swanhtetaungphyo.article_approval.dto.ArticleDto;
import com.swanhtetaungphyo.article_approval.models.ArticleStatus;
import com.swanhtetaungphyo.article_approval.repo.ArticleRepository;
import com.swanhtetaungphyo.article_approval.repo.UserRepository;
import com.swanhtetaungphyo.article_approval.utils.ArticleProcessResp;
import com.swanhtetaungphyo.article_approval.utils.InternalResponse;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ArticleServiceImpl implements ArticleServices {

    private static final Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final NLProcessor nlProcessor;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public ArticleServiceImpl(ArticleRepository articleRepository, UserRepository userRepository, ModelMapper modelMapper, NLProcessor nlProcessor, SimpMessagingTemplate simpMessagingTemplate) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.nlProcessor = nlProcessor;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    @RabbitListener(queuesToDeclare = @Queue("article-approval"), concurrency = "3-10")
    public void CreateArticle(ArticleDto articleDto) throws IOException {
        if (articleDto == null ) {
            logger.warn("Received invalid ArticleDto: {}", articleDto);
            assert articleDto != null;

            return;
        }

        logger.info("Received ArticleDto: {} with userId: {}", articleDto,1);

        ArticleProcessResp condition;
        try {
            logger.info("Processing article: {}", articleDto);
            condition = ProcessArticles(articleDto);
            logger.info("Article processed with condition: {}", condition);
        } catch (Exception e) {
            logger.error("Error processing article: {}. Exception: {}", articleDto, e.getMessage(), e);
            return;
        }

        if (condition.getCondition() && condition.getArticleStatus() == ArticleStatus.APPROVED) {
            Long Id = 1L;
//            userRepository.findById(Id).ifPresentOrElse(user -> {
//                try {
//                    Article articleToSave = modelMapper.map(articleDto, Article.class);
//                    articleToSave.setUser(user);
//                    articleRepository.save(articleToSave);
//                    logger.info("Article saved successfully for user: {}", user.getId());
//                } catch (Exception e) {
//                    logger.error("Failed to save article for user: {}. Exception: {}", user.getId(), e.getMessage(), e);
//                }
//            }, () -> {
//                logger.warn("User not found for ID: {}", 1);
//            });
        } else {
            logger.info("Article not approved. Status: {}", condition.getArticleStatus());
        }


        simpMessagingTemplate.convertAndSend("/topic/article-status", condition);
        logger.info("Sent article status to WebSocket: {}", condition);
    }



    @Override
    public ArticleProcessResp ProcessArticles(ArticleDto articleDto) throws IOException {
        ArticleProcessResp articleProcessResp = new ArticleProcessResp();

        try {
            String[] sentences = nlProcessor.SentenceProcessing(articleDto.getContent());
            String[][] tokens = nlProcessor.TokenProcessing(sentences);

            InternalResponse condition = nlProcessor.DecisionMaking(tokens);

            articleProcessResp.setMessage(condition.getRejectionMessage());
            articleProcessResp.setCondition(condition.getCondition());
            articleProcessResp.setArticleStatus(condition.getCondition() ? ArticleStatus.APPROVED : ArticleStatus.REJECTED);

            logger.info("condition : {}", condition.getCondition());
        } catch (Exception e) {

            articleProcessResp.setMessage("An error occurred while processing the article.");

        }

        return articleProcessResp;
    }

}
