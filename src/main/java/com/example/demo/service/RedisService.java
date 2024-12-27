package com.example.demo.service;

public interface RedisService {
	
	public void save(String key, Object value);
	
	public void delete(String key);
	
	public Object get(String key);
	
}
