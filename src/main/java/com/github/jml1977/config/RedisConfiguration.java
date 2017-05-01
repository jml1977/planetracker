package com.github.jml1977.config;

import java.net.URI;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

public class RedisConfiguration {
	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		JedisConnectionFactory redis = new JedisConnectionFactory();
		String redisUrl = System.getenv("REDIS_URL");

		try {
			URI redisUri = new URI(redisUrl);
			redis.setHostName(redisUri.getHost());
			redis.setPort(redisUri.getPort());
			redis.setPassword(redisUri.getUserInfo().split(":", 2)[1]);
			return redis;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	@Bean
	@Primary
	public RedisTemplate stringRedisTemplate() {
		StringRedisTemplate srt = new StringRedisTemplate();
		srt.setConnectionFactory(redisConnectionFactory());
		srt.setKeySerializer(new StringRedisSerializer());
		return srt;
	}
}
