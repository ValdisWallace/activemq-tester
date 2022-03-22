package org.vnovolotskiy.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.amqp.core.MessageListener
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerEndpoint
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpoint
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.vnovolotskiy.service.AmqpListener

@Configuration
class AmqpConfig(
    private val objectMapper: ObjectMapper,
    private val amqpListener: AmqpListener
) {

    @Bean
    @ConfigurationProperties(prefix = "spring.rabbitmq")
    fun connectionFactory(): ConnectionFactory? {
        return CachingConnectionFactory()
    }

    // Producer
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

    // Consumer
    @Bean
    @Throws(Exception::class)
    fun rabbitListenerContainerFactory(): SimpleRabbitListenerContainerFactory? {
        val factory = SimpleRabbitListenerContainerFactory()
        factory.setConnectionFactory(connectionFactory())
        factory.setMessageConverter(amqpJsonMessageConverter())

        createBillingListener(factory)

        return factory
    }

    @Throws(java.lang.Exception::class)
    private fun createBillingListener(factory: SimpleRabbitListenerContainerFactory) {
        val queue = "acmbilapp_acmqr_ecomm_res"
        val concurrency = "10"

        val container = factory.createListenerContainer(endpoint(queue, amqpListener, concurrency))
        container.start()
    }

    private fun endpoint(queue: String, listener: MessageListener, concurrency: String): RabbitListenerEndpoint {
        val endpoint = SimpleRabbitListenerEndpoint()
        endpoint.concurrency = concurrency
        endpoint.setQueueNames(queue)
        endpoint.messageListener = listener
        return endpoint
    }
}