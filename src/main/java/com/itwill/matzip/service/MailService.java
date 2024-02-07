package com.itwill.matzip.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.itwill.matzip.domain.Member;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MailService {
	private final JavaMailSender mailSender;
	private final SpringTemplateEngine templateEngine;
	private final PasswordEncoder passwordEncoder;

	// HTML 메일 보내기
	public void sendHtmlEmail(Member member) {
		MimeMessage message = mailSender.createMimeMessage();
		String username = member.getUsername();

		try {
			message.setSubject("[MAT.ZIP] 문의하신 계정 정보입니다.");
			message.setText(setContext(username), "UTF-8", "html");

			message.addRecipients(MimeMessage.RecipientType.TO, member.getEmail());

			// 보내기
			mailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	// mail2.html에 attribute 저장하기
	public String setContext(String username) {
		Context context = new Context();
		context.setVariable("username", username);
		context.setVariable("key", passwordEncoder.encode(username));;
		return templateEngine.process("/member/mail2", context);
	}

}
