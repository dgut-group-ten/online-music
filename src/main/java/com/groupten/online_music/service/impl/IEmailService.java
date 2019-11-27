package com.groupten.online_music.service.impl;

import com.groupten.online_music.entity.EmailConfirm;

import java.util.Date;

public interface IEmailService {
    //发送邮件
    public boolean sendSimpleMail(String to, String title, String content);
    //根据邮箱查找邮箱验证信息
    public EmailConfirm findOne(String email);
    //生成6位验证码
    public String generateCheckCode();
    //保存邮箱验证信息
    public void save(EmailConfirm emailConfirm);
    //判断验证码发送的时间差是否符合要求
    public boolean isLimitedTime(Date confirmTime);
    //判断验证码是否过期
    public boolean isNotExpiredTime(Date confirmTime);
    //校验验证码
    public boolean isCorrectCode(EmailConfirm emailConfirm, String checkCode, StringBuffer message);
}
