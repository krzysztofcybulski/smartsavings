package me.kcybulski.smartsavings.redis

data class RedisConfiguration(
    val address: String,
    val password: String? = null,
    val connectionMinimumIdleSize: Int = 8,
    val connectionPoolSize: Int = 20
)
