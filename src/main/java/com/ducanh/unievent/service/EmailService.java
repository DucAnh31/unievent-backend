package com.ducanh.unievent.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Value("${app.frontend-base-url}")
    private String frontendBaseUrl;

    public void sendOtp(String to, String otp) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(to);
        message.setSubject("UniEvent - Ma xac minh");
        message.setText("Ma xac minh cua ban la: " + otp + "\nMa co hieu luc trong 5 phut");

        javaMailSender.send(message);
    }

    public void sendVerificationEmail(String email, String token) {
        String verificationLink = frontendBaseUrl + "/verify-email?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(email);
        message.setSubject("UniEvent - Xác minh địa chỉ email");
        message.setText(
                """
			Chào bạn,

			Link kich hoat tai khoan UniEvent:

			%s

			Link có hiệu lực trong 24 giờ. Nếu bạn không đăng ký tài khoản,
			hãy bỏ qua email này.
			"""
                        .formatted(verificationLink));

        javaMailSender.send(message);
    }
}
