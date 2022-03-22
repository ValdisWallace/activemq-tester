package org.vnovolotskiy.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.vnovolotskiy.service.AmqpProducer
import ru.tinkoff.acm.billing.api.model.messages.request.EcommPayRequest

@RestController
class Controller(
    private val amqpProducer: AmqpProducer,
    private val objectMapper: ObjectMapper
) {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

    @PostMapping("/start")
    fun start(@RequestBody request: EcommPayRequest): String {
        log.info("Process start with row reqeust: {}", request)

        amqpProducer.send(request)

        return "OK"
    }

    @PostMapping("/")
}