package org.vnovolotskiy.controller

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import org.vnovolotskiy.service.Service
import ru.tinkoff.acm.billing.api.model.messages.request.EcommPayRequest

@RestController
class Controller(
    private val service: Service
) {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

    @PostMapping("/start")
    fun start(request: EcommPayRequest): String {
        log.info("Process start reqeust: {}", request)

        service.send(request)

        return "OK"
    }
}