package com.example.demo.config;

public class EndPoint {

	public static String[] ALLOWED_ORIGINS = { "http://localhost:5173" };
	public static String[] ALLOWED_METHODS = { "*" };
	public static String[] ALLOWED_HEADERS = { "*" };
	public static String[] ALLOWED_PATHS = { "/ws/**" };
	public static String[] PUBLIC_METHODS_GET = { "/api/v1/auth/refresh-token" };
	public static String[] PUBLIC_METHODS_POST = 
		{ 
			"/api/v1/auth/login", 
			"/api/v1/auth/send-otp",
			"/api/v1/auth/register", 
			"/api/v1/auth/verify-otp", 
		};
	public static String[] PUBLIC_METHODS_PATCH = { "/api/v1/auth/forgot-password" };

}
