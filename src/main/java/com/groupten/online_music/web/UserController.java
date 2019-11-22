package com.groupten.online_music.web;

import com.groupten.online_music.common.jwt.JWTUtils;
import com.groupten.online_music.common.utils.UserDTO;
import com.groupten.online_music.common.utils.ResponseEntity;
import com.groupten.online_music.entity.User;
import com.groupten.online_music.service.impl.IUserService;
import io.swagger.annotations.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(tags = "用户操作相关接口")
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService userService;

    @ApiOperation(value = "用户登录接口")
    @PostMapping("/token")
    public @ResponseBody
    ResponseEntity login(
            @RequestBody @ApiParam(name = "userDTO", value = "登录只需传入user_name, user_password", type = "body") UserDTO userDTO,
            HttpServletRequest request,
            HttpServletResponse response) {
        //userDTO的数据封装到user里
        User user = new User(userDTO);
        //响应结果
        ResponseEntity<User> responseEntity = new ResponseEntity<User>();
        boolean result = userService.login(user);
        if (result) {//验证成功生成token
            response.setHeader("Authorization", JWTUtils.createToken(user));
        }

        return responseEntity.success(result)
                .status(result ? HttpStatus.OK : HttpStatus.BAD_REQUEST)
                .message(result ? "登录请求成功" : "不存在该用户! 登录请求失败");
    }

    @ApiOperation(value = "用户注册接口")
    @PostMapping
    public @ResponseBody
    ResponseEntity register(
            @RequestBody @ApiParam(name = "userDTO", value = "需传入user_name, user_password,email, checkCode参数", type = "body") UserDTO userDTO,
            HttpServletRequest request,
            HttpServletResponse response) {
        //userDTO的数据封装到user里
        User user = new User(userDTO);
        //响应结果
        ResponseEntity<User> responseEntity = new ResponseEntity<User>();
        boolean result = false;
        String message = "";
        //不存在该用户, 注册
        if (!userService.hasUser(user)) {
            //先检验验证码
            String checkCode = (String) request.getSession().getAttribute(user.getEmail());
            if (checkCode == null){
                message += "验证邮箱错误!";
            }else if((checkCode.equals(userDTO.getCheckCode()))) {
                result = userService.register(user);
            } else {
                message += "验证码错误!";
            }
        }else{
            message += "已存在重名用户!";
        }
        message += (result ? "请求成功" : "请求失败");
        return responseEntity.success(result)
                .status(result ? HttpStatus.OK : HttpStatus.BAD_REQUEST)
                .message(message);
    }

    @ApiOperation("用户信息更改接口(未测试)")
    @PutMapping(value = "/{id}")
    public ResponseEntity update(@PathVariable("id") int id,
                                 @RequestBody @ApiParam(name = "userDTO", value = "可修改user_name, headIcon, description参数", type = "body")
                                         UserDTO userDTO) {
        //userDTO的数据封装到user里
        User source = new User(userDTO);
        User target = userService.findById(id);
        //响应结果
        ResponseEntity<User> responseEntity = new ResponseEntity<User>();
        boolean result = false;

        if (target != null) {
            BeanUtils.copyProperties(source, target);
            result = (null != userService.save(target));//update
        }

        return responseEntity.success(result)
                .status(result ? HttpStatus.OK : HttpStatus.BAD_REQUEST)
                .message(result ? "用户信息更改请求成功" : "用户信息更改请求失败");
    }
}
