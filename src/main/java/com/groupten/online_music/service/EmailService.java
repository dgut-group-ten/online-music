package com.groupten.online_music.service;

import com.groupten.online_music.dao.impl.IEmailDao;
import com.groupten.online_music.entity.EmailConfirm;
import com.groupten.online_music.service.impl.IEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@Service
@Transactional
public class EmailService implements IEmailService {
    @Value("${spring.mail.username}")
    private String from;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private IEmailDao emailDao;

    /**
     * 发送邮件
     *
     * @param to      目标邮箱
     * @param title   邮箱标题
     * @param content 邮箱内容
     * @return 是否发送成功
     */
    public boolean sendSimpleMail(String to, String title, String content) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(from);
        mailMessage.setTo(to);
        mailMessage.setSubject(title);
        mailMessage.setText(content);

        try {
            mailSender.send(mailMessage);
        } catch (MailException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * 根据Email查表
     *
     * @param email 目标邮件
     * @return 返回查到的验证信息
     */
    @Override
    public EmailConfirm findOne(String email) {
        return emailDao.findByEmail(email);
    }

    /**
     * 生成6位验证码
     *
     * @return 验证码
     */
    @Override
    public String generateCheckCode() {
        String checkCode = "";
        for (int i = 0; i < 6; i++) {
            int num = (int) (Math.random() * 10);
            checkCode = num + checkCode;
        }
        return checkCode;
    }

    /**
     * 保存邮箱认证信息
     *
     * @param emailConfirm 认证信息
     */
    @Transactional
    public void save(EmailConfirm emailConfirm) {
        emailDao.save(emailConfirm);
    }

    /**
     * 判断验证码发送时间间隔
     *
     * @param confirmTime 验证开始时间
     * @return true or false
     */
    @Override
    public boolean isLimitedTime(Date confirmTime) {
        Date now = new Date();
        Long timeDif = now.getTime() - confirmTime.getTime();
        System.out.println("timeDif / 1000:" + timeDif / 1000);
        return timeDif / 1000 > 60;
    }

    /**
     * 判断验证码是否过期
     *
     * @param confirmTime 生成验证码的时间
     * @return true or false
     */
    @Override
    public boolean isNotExpiredTime(Date confirmTime) {
        Date now = new Date();
        Long timeDif = now.getTime() - confirmTime.getTime();

        return timeDif / 60000 <= 15;
    }

    /**
     * 校验验证码
     * @param emailConfirm 邮箱验证信息
     * @param checkCode 用户输入的验证码
     * @param message 提示信息
     * @return true or false
     */
    @Override
    public boolean isCorrectCode(EmailConfirm emailConfirm, String checkCode, StringBuffer message) {
        //判断有无该邮箱验证信息
        if(emailConfirm == null){
            message.append("验证邮箱错误!");
            System.out.println(message);
            return false;
        }
        //判断用户输入是否未空
        if(checkCode == null){
            message.append("验证码为空!");
            System.out.println(message);
            return false;
        }
        //判断验证码是否过期
        if(!isNotExpiredTime(emailConfirm.getConfirmTime())){
            message.append("验证码已过期!请重新获取验证码");
            System.out.println(message);
            return false;
        }
        //匹配验证码
        if (checkCode.equals(emailConfirm.getCheckCode())){
            message.append("验证码正确！");
            System.out.println(message);
            return true;
        }

        message.append("验证码错误！请检查您的验证码！");
        return false;
    }
}
