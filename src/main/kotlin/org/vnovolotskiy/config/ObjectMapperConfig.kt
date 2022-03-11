package org.vnovolotskiy.config

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import java.io.IOException

@Configuration
class ObjectMapperConfig {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

    @Bean
    @Primary
    fun objectMapper(): ObjectMapper {
        val objectMapper = ObjectMapper()
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT)
        objectMapper.disable(JsonGenerator.Feature.IGNORE_UNKNOWN)
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
        objectMapper.registerModule(ParameterNamesModule())
            .registerModule(Jdk8Module())
            .registerModule(JavaTimeModule())
            .registerModule(JavaTimeModule())
        objectMapper.addHandler(object : DeserializationProblemHandler() {
            @Throws(IOException::class)
            override fun handleUnknownProperty(
                ctxt: DeserializationContext,
                p: JsonParser,
                deserializer: JsonDeserializer<*>?,
                beanOrClass: Any,
                propertyName: String
            ): Boolean {
                try {
                    log.warn("Deserialization problem: unknown property: '{}' = '{}'", propertyName, p.valueAsString)
                } catch (e: Exception) {
                    // NOOP
                }
                return super.handleUnknownProperty(ctxt, p, deserializer, beanOrClass, propertyName)
            }
        })
        return objectMapper
    }
}