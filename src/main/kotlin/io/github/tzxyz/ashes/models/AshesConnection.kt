package io.github.tzxyz.ashes.models

import java.util.*

data class AshesConnection(val id: String = UUID.randomUUID().toString(), val name: String, val host: String, val port: Int = 6379, val db: Int = 0, val password: String? = null)