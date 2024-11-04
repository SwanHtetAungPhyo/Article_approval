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

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleServices {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public ArticleServiceImpl(ArticleRepository articleRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @RabbitListener(queues = "article-approval")
    @Override
    public ArticleProcessResp CreateArticle(ArticleDto articleDto) {
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
    public ArticleProcessResp ProcessArticles(ArticleDto articleDto) {
        ArticleProcessResp articleProcessResp = new ArticleProcessResp();
        List<String> swearWords = UtilsClass.WordLists();

        boolean containsSwearWords = swearWords.stream().anyMatch(articleDto.getContent()::contains);

        if (containsSwearWords) {
            articleProcessResp.setMessage("Reason to reject is because of swear words");
            articleProcessResp.setArticleStatus(ArticleStatus.REJECTED);
            articleProcessResp.setCondition(false);
        } else {
            articleProcessResp.setMessage("");
            articleProcessResp.setArticleStatus(ArticleStatus.APPROVED);
            articleProcessResp.setCondition(true);
        }

        return articleProcessResp;
    }
}
