package com.groupten.online_music.web;

import com.groupten.online_music.common.utils.ResponseEntity;
import com.groupten.online_music.entity.User;
import com.groupten.online_music.service.impl.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Api(tags = "用户操作相关接口")
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService userService;

    @ApiOperation("用户登录接口")
    @GetMapping("/login")
    public ResponseEntity login(@RequestBody User user) {
        boolean result = userService.login(user);
        return ResponseEntity.ofSuccess(result).message(result?"登录成功":"登录失败");
    }

    @ApiOperation("用户注册接口")
    @GetMapping("/register")
    public ResponseEntity register(@RequestBody User user) {
        boolean result = userService.register(user);
        return ResponseEntity.ofSuccess(result).message(result?"注册成功":"注册失败");
    }
}
