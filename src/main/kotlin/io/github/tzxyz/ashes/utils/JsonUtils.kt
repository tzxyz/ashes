package io.github.tzxyz.ashes.utils

import com.fasterxml.jackson.core.util.DefaultIndenter
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

class JsonUtils {

    companion object {

        val mapper = ObjectMapper().registerKotlinModule()

        private val default = DefaultIndenter("        ", DefaultIndenter.SYS_LF)

        private val printer = DefaultPrettyPrinter()

        fun toJsonString(any: Any): String {
            return mapper.writeValueAsString(any)
        }

        fun toPrettyJsonString(any: Any): String {
            printer.indentObjectsWith(default)
            printer.indentArraysWith(default)
            return mapper.writer(printer).writeValueAsString(any)
        }

        inline fun <reified T> fromJsonString(string: String): T {
            return mapper.readValue(string)
        }

    }
}