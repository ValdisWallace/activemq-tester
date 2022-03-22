package org.vnovolotskiy.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.Message
import org.springframework.amqp.core.MessageProperties
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import ru.tinkoff.acm.billing.api.model.messages.ref.ServiceEnum
import ru.tinkoff.acm.billing.api.model.messages.request.EcommPayRequest
import java.nio.charset.StandardCharsets

@Service
class AmqpProducer(
    private val rabbitTemplate: RabbitTemplate,
    private val objectMapper: ObjectMapper
) {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

    fun send(request: EcommPayRequest) {
        val messageProperties = MessageProperties()
        messageProperties.contentType = MediaType.APPLICATION_JSON.toString()
        messageProperties.contentEncoding = StandardCharsets.UTF_8.name()

        messageProperties.headers[ServiceEnum::class.java.name] = ServiceEnum.ACM_QR

        val message = Message(objectMapper.writeValueAsString(request).toByteArray(), messageProperties)

        log.info("Send message to acmp: {}", message)

        rabbitTemplate.send("ex_acm_billing", "acmbilapp_ecomm_req", message)
    }
}