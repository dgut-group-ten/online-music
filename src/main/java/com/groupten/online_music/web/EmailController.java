package com.groupten.online_music.web;

import com.groupten.online_music.common.utils.ResponseEntity;
import com.groupten.online_music.service.impl.IMailService;
import com.groupten.online_music.service.impl.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "邮箱验证相关接口")
@RestController
@RequestMapping("/email")
public class EmailController {
    @Autowired
    private IMailService mailService;
    @Autowired
    private IUserService userService;

    @ApiOperation(value = "发送验证码")
    @ApiImplicitParam(name = "to", value = "验证的邮箱地址", required = true, paramType = "path", dataType="String")
    @PostMapping
    public ResponseEntity sendCheckCode(@RequestParam String to, HttpServletRequest request) {
        String message = "";
        if (userService.findByEmail(to) == null) {
            String title = "来自在线音乐平台的验证邮件";
            String content = "验证码: ";
            String checkCode = "";
            for (int i = 0; i < 6; i++) {
                int num = (int) (Math.random() * 10);
                checkCode = num + checkCode;
            }
            mailService.sendSimpleMail(to, title, content + checkCode);
            request.getSession().setAttribute(to, checkCode);
            message += "已发送验证码，请打开邮箱确认";
        } else {
            message += "邮箱已注册，请更换邮箱！";
        }

        return new ResponseEntity().success(true).status(HttpStatus.OK).message(message);
    }
}
