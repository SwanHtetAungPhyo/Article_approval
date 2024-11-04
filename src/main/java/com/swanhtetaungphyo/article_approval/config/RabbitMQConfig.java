package com.swanhtetaungphyo.article_approval.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    
    public static final String QUEUE_ARTICLE_PROCESSING = "article.processing";
    public static final String EXCHANGE_ARTICLE = "article.exchange";
    public static final String ROUTING_KEY_PROCESSING = "article.create";

    @Bean
    Queue articleProcessingQueue() {
        return new Queue(QUEUE_ARTICLE_PROCESSING, true); // Durable queue
    }

    @Bean
    TopicExchange articleExchange() {
        return new TopicExchange(EXCHANGE_ARTICLE);
    }

    @Bean
    Binding bindingArticleProcessing(Queue articleProcessingQueue, TopicExchange articleExchange) {
        return BindingBuilder.bind(articleProcessingQueue).to(articleExchange).with(ROUTING_KEY_PROCESSING);
    }
}