package com.example.demo.service.imp;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.example.demo.service.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

public class EmailServiceImp implements EmailService {
	
	private JavaMailSender javaMailSender;

	public EmailServiceImp(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	@Override
	public void sendEmailVerifyAccount(String from, String to, String verificationCode) throws MessagingException {
		MimeMessage mail = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mail, true);
		helper.setFrom(from);
		helper.setTo(to);
		helper.setSubject("ZALO - XÁC NHẬN TÀI KHOẢN");
		String content = "<h1>Zalo - Xác nhận tài khoản</h1>" + "<p>Mã xác nhận của bạn là: <b>"
				+ verificationCode + "</b></p>";
		helper.setText(content, true);
		javaMailSender.send(mail);
	}

}
