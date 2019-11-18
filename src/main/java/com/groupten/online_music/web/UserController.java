package com.groupten.online_music.web;

import com.groupten.online_music.entity.User;
import com.groupten.online_music.service.impl.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Api(tags = "用户操作相关接口", description = "提供用户相关的 Rest API")
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService userService;

    @ApiOperation("用户登录接口")
    @GetMapping("/login")
    public String login(@RequestBody User user){
        if(userService.login(user))
            return "登录成功";
        return "登录失败";
    }
}
