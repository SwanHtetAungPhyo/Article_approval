package com.swanhtetaungphyo.article_approval.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class RabbitMqKeys {

    @Value("app.rabbitmq.processing")
    private  String processing;

    @Value("app.rabbitmq.exchange")
    private String exchange;

    @Value("app.rabbitmq.create")
    private String create;
}
