package com.birth.fit.config

import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.core.RedisKeyValueAdapter
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories

@Configuration
@EnableRedisRepositories(enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP)
class RedisConfig