package org.vnovolotskiy.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AmqpConfig(
    private val objectMapper: ObjectMapper
) {

    @Bean
    @ConfigurationProperties(prefix = "spring.rabbitmq")
    fun connectionFactory(): ConnectionFactory? {
        return CachingConnectionFactory()
    }

    @Bean
    fun rabbitTemplate(): RabbitTemplate? {
        val template = RabbitTemplate(connectionFactory()!!)
        template.messageConverter = amqpJsonMessageConverter()
        template.setUserCorrelationId(true)
        return template
    }

    @Bean
    fun amqpJsonMessageConverter(): MessageConverter {
        return Jackson2JsonMessageConverter(objectMapper)
    }
}