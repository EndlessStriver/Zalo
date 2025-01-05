package com.example.demo.config;

public class EndPoint {
	
	public static String[] ALLOWED_ORIGINS = { "*" };
	public static String[] ALLOWED_METHODS = { "GET", "POST", "PUT", "DELETE", "PATCH" };
	public static String[] PUBLIC_METHODS_GET = {};
	public static String[] PUBLIC_METHODS_POST = {
			"/api/v1/auth/login",
			"/api/v1/auth/register",
			"/api/v1/auth/send-otp",
			"/api/v1/auth/verify-otp",
	};
	
}
