package org.vnovolotskiy

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ActiveMQTesterApplication

fun main(args: Array<String>) {
    runApplication<ActiveMQTesterApplication>(*args)
}