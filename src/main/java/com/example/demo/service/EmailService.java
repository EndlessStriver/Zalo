package com.example.demo.service;

import jakarta.mail.MessagingException;

public interface EmailService {
	
	public void sendEmailVerifyAccount(String from, String to, String verificationCode) throws MessagingException;
	
}
