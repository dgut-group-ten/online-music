package com.groupten.online_music.web;

import com.groupten.online_music.common.dto.UserDTO;
import com.groupten.online_music.common.utils.ApplicationException;
import com.groupten.online_music.common.utils.ResponseEntity;
import com.groupten.online_music.entity.User;
import com.groupten.online_music.service.impl.IUserService;
import io.swagger.annotations.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Api(tags = "用户操作相关接口")
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService userService;

    @ApiOperation(value = "用户登录接口")
    @PostMapping("/login")
    public @ResponseBody
    ResponseEntity login(@RequestBody @ApiParam(name = "userDTO", value = "只需传入user_name, user_password数据", type = "body")
                                 UserDTO userDTO, HttpServletResponse response) {
        User user = new User(userDTO.getUser_name(), userDTO.getUser_password());
        boolean result = userService.login(user);
        String token = "";
        if (result) {//验证成功生成token
            token = userService.getToken(user);
            response.setHeader("Authorization", token);
        }

        return ResponseEntity.ofSuccess(result).message(result ? "登录成功" : "登录失败");
    }

    @ApiOperation("用户注册接口")
    @PostMapping("/register")
    public @ResponseBody
    ResponseEntity register(@RequestBody @ApiParam(name = "userDTO", value = "只需传入user_name, user_password，, Email数据", type = "body")
                                    UserDTO userDTO) {
        User user = new User(userDTO.getUser_name(), userDTO.getUser_password(), userDTO.getEmail());
        //1.通过邮箱发送验证码
        //2.验证注册信息-验证码校验，是否重名用户，密码加密后封装信息存入数据库
        boolean result = userService.register(user);

        return ResponseEntity.ofSuccess(result).message(result ? "注册成功" : "注册失败");
    }

    @ApiOperation("用户信息更改接口")
    @PutMapping(value = "/{id}")
    public ResponseEntity update(@PathVariable("id") int id, @RequestBody @ApiParam(name = "userDTO", value = "可修改user_name, headIcon, description参数", type = "body")
            UserDTO userDTO) {
        User source = new User();
        source.setUser_name(userDTO.getUser_name());
        source.setDescription(userDTO.getDescription());
        source.setHeadIcon(userDTO.getHeadIcon());
        try {
            User target = userService.findById(id);
            if (target != null) {
                BeanUtils.copyProperties(source, target);
                userService.save(target);//update
            } else {
                userService.save(source);//save
            }
        } catch (ApplicationException ex) {
            return ResponseEntity.ofSuccess(false).message("修改用户信息 error.");
        }
        return ResponseEntity.ofSuccess(true).message("修改用户信息 Success.");
    }

    @ApiOperation("删除用户接口")
    @DeleteMapping("/{id}")
    public @ResponseBody
    ResponseEntity delete(@PathVariable @ApiParam(name = "id", value = "传入用户id", type = "path") int id) {
        boolean result = userService.deleteUser(id);
        return ResponseEntity.ofSuccess(result).message(result ? "删除成功" : "删除失败");
    }
}
