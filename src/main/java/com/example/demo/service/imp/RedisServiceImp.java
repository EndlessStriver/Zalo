package com.example.demo.service.imp;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.service.RedisService;

@Service
public class RedisServiceImp implements RedisService {
	
	private RedisTemplate<String, Object> redisTemplate;
	
	public RedisServiceImp(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Override
	public void save(String key, Object value) {
		redisTemplate.opsForValue().set(key, value);
	}

	@Override
	public void delete(String key) {
		redisTemplate.delete(key);
	}

	@Override
	public Object get(String key) {
		return redisTemplate.opsForValue().get(key);
	}

}
