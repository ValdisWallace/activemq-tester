package org.vnovolotskiy.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.Message
import org.springframework.amqp.core.MessageListener
import org.springframework.stereotype.Component
import ru.tinkoff.acm.billing.api.model.messages.response.EcommPayResponse

@Component
class AmqpListener(
    private val objectMapper: ObjectMapper
) : MessageListener {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

    override fun onMessage(message: Message) {
        val queue = message.messageProperties.consumerQueue

        log.info("Get message from queue '{}'.", queue)

        val payload = objectMapper.readValue(message.body, EcommPayResponse::class.java)

        log.info("Message: {}", payload)
    }

}