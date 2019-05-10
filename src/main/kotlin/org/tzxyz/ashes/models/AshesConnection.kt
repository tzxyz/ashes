package org.tzxyz.ashes.models

data class AshesConnection(val id: String, val name: String, val host: String, val port: Int = 6379, val db: Int = 0, val password: String? = null)