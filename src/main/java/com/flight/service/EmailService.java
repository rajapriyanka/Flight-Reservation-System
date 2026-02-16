package com.flight.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;

	public void sendOtp(String toEmail, String firstName, String otp, String subject) {
		String body = buildOtpEmailTemplate(firstName, otp);
		sendEmail(toEmail, subject, body);
	}

	private void sendEmail(String toEmail, String subject, String body) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(toEmail);
		message.setSubject(subject);
		message.setText(body);
		mailSender.send(message);
	}

	private String buildOtpEmailTemplate(String firstName, String otp) {
		return "Dear " + firstName + ",\n\n" + "We received a request to reset your password.\n\n"
				+ "Your OTP for password reset is: " + otp + "\n\n" + "This OTP is valid for 5 minutes.\n\n"
				+ "If you did not request this, please ignore this email.\n\n" + "Regards,\nFlight Reservation System";
	}
}
