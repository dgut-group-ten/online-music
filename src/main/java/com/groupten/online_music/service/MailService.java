package com.groupten.online_music.service;

import com.groupten.online_music.service.impl.IMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

//@Service
public class MailService implements IMailService {
    @Value("${spring.mail.name}")
    private String from;
    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleMail(String to, String title, String content){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(from);
        mailMessage.setTo(to);
        mailMessage.setSubject(title);
        mailMessage.setText(content);
        mailSender.send(mailMessage);
        System.out.println("邮件发送成功");
    }
}
