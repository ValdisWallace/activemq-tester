package org.vnovolotskiy.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.amqp.core.Message
import org.springframework.amqp.core.MessageProperties
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import ru.tinkoff.acm.billing.api.model.messages.request.EcommPayRequest
import java.nio.charset.StandardCharsets

@Service
class Service(
    private val rabbitTemplate: RabbitTemplate,
    private val objectMapper: ObjectMapper
) {
    fun send(request: EcommPayRequest) {
        val messageProperties = MessageProperties()
        messageProperties.contentType = MediaType.APPLICATION_JSON.toString()
        messageProperties.contentEncoding = StandardCharsets.UTF_8.name()

        val message = Message(objectMapper.writeValueAsString(request).toByteArray(), messageProperties)

        rabbitTemplate.send("vnovolotskiy_ex", "vnovolotskiy_req", message)
    }
}