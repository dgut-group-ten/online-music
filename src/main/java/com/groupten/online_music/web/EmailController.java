package com.groupten.online_music.web;

import com.groupten.online_music.common.utils.ResponseEntity;
import com.groupten.online_music.common.utils.exception.ApplicationException;
import com.groupten.online_music.entity.EmailConfirm;
import com.groupten.online_music.entity.entityEnum.ConfirmStatus;
import com.groupten.online_music.service.impl.IEmailService;
import com.groupten.online_music.service.impl.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/email")
public class EmailController {
    static final String title = "来自在线音乐平台的验证邮件";
    @Autowired
    private IEmailService emailService;
    @Autowired
    private IUserService userService;

    @PostMapping
    public ResponseEntity sendCheckCode(@RequestParam String to) {
        //1.查表
        EmailConfirm emailConfirm = emailService.findOne(to);
        //2.发送邮件
        String message = "";
        String checkCode = emailService.generateCheckCode();//生成验证码
        if (emailConfirm == null) {
            //2-1.邮箱不存在, 发送认证邮件后保存
            emailService.sendSimpleMail(to, title, "验证码: " + checkCode);
            emailService.save(new EmailConfirm(to, checkCode, new Date(), ConfirmStatus.UNCONFIRMED));
            message += "验证码已发送至您的邮箱！";
        } else if (emailConfirm.getStatus() == ConfirmStatus.CONFIRMED) {
            //2-2.邮箱存在, 已认证则不发送并抛出异常提示信息给前端
            throw new ApplicationException("邮箱已注册，请更换邮箱！");
        } else if (emailService.isLimitedTime(emailConfirm.getConfirmTime())) {
            //2-3.邮箱存在, 未认证则判断发送间隔, 更新验证信息后再发送
            emailConfirm.setConfirmTime(new Date());
            emailConfirm.setCheckCode(checkCode);
            emailService.save(emailConfirm);
            emailService.sendSimpleMail(to, title, "验证码: " + checkCode);
            message += "已发送验证码，请打开邮箱确认";
        } else {
            message += "验证码已发送，60秒后才可重发验证码";
        }

        return new ResponseEntity().message(message);
    }
}
