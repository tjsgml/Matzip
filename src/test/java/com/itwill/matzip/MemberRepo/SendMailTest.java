package com.itwill.matzip.MemberRepo;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class SendMailTest {
	
	@Autowired private JavaMailSender mailSender;
	 @Autowired private SpringTemplateEngine templateEngine;
	
	//@Test
	public void sendMailTest1() {
		//자바 메일 의존성 주입 테스트
		Assertions.assertNotNull(mailSender);		
		log.info("mailSender : {} ", mailSender);
		
		
		//일반 텍스트 메일을 보낼 수 있는 객체 생성
		SimpleMailMessage message = new SimpleMailMessage();
		
		//수신자, 메일 제목, 내용 작성
		message.setTo("dustn7020@naver.com");		
		message.setSubject("메일 테스트 제목" + LocalDateTime.now());
		message.setText("메일 테스트 내용" + LocalDateTime.now());
		
		//메일 발송
		mailSender.send(message);
	}
	
	@Test
	public void sendHtmlMail() {
		MimeMessage message = mailSender.createMimeMessage();
		String username = "username111";

		try {
			message.setSubject("제목1111");
			message.setText(setContext(username), "UTF-8", "html");
			
			message.addRecipients(MimeMessage.RecipientType.TO, "dustn7020@gmail.com");
			
			//보내기
			mailSender.send(message);
		}catch(MessagingException e) {
			e.printStackTrace();
		}
	}
	
	public String setContext(String username) {
		Context context = new Context();
		context.setVariable("username", username);
		
		return templateEngine.process("/member/mail2", context);
	}
}
