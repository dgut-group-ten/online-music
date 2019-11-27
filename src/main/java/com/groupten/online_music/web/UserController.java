package com.groupten.online_music.web;

import com.groupten.online_music.common.jwt.JWTUtils;
import com.groupten.online_music.common.utils.FileUploadUtil;
import com.groupten.online_music.common.utils.UserDTO;
import com.groupten.online_music.common.utils.ResponseEntity;
import com.groupten.online_music.entity.EmailConfirm;
import com.groupten.online_music.entity.User;
import com.groupten.online_music.entity.entityEnum.ConfirmStatus;
import com.groupten.online_music.service.impl.IEmailService;
import com.groupten.online_music.service.impl.IUserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Api(tags = "用户操作相关接口")
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService userService;
    @Autowired
    private IEmailService emailService;

    @ApiOperation(value = "用户登录接口")
    @PostMapping("/token")
    public @ResponseBody
    ResponseEntity login(
            @RequestParam Map<String, String> userMap,
            HttpServletResponse response) {
        //userMap的数据封装到user里
        User user = new User(userMap);
        //响应结果
        ResponseEntity<User> responseEntity = new ResponseEntity<User>();
        boolean result = false;
        String token = null;
        //登录操作
        int uid = userService.login(user);//登录失败返回-1
        if ((result = (uid != -1))) {//验证成功生成token
            user.setUid(uid);
            token = JWTUtils.createToken(user);
        }

        return responseEntity.message(result ? "登录请求成功" : "不存在该用户或密码错误! 登录请求失败")
                .token(token);
    }

    @ApiOperation(value = "用户注册接口")
    @PostMapping
    public @ResponseBody
    ResponseEntity register(
            @RequestParam Map<String, String> userMap,
            HttpServletRequest request) {
        //1. userMap的数据封装到user里
        User user = new User(userMap);

        ResponseEntity<User> responseEntity = new ResponseEntity<User>();
        StringBuffer message = new StringBuffer("");

        //2. 不存在该用户, 注册
        if (!userService.hasUser(user)) {
            //2-1. 获取验证码信息
            EmailConfirm emailConfirm = emailService.findOne(user.getEmail());
            //2-2. 校验验证码，成功则注册用户
            if (emailService.isCorrectCode(emailConfirm, userMap.get("checkCode"), message)) {
                message.append(userService.register(user) ? "用户注册请求成功" : "用户注册请求失败");
                //注册成功后标记认证邮箱
                emailConfirm.setStatus(ConfirmStatus.CONFIRMED);
                emailService.save(emailConfirm);
            }
        } else {
            message.append("已存在重名用户!");
        }

        return responseEntity.message(message.toString());
    }

    @ApiOperation("用户信息更改接口(未改好)")
    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity update(@RequestBody UserDTO userDTO, @PathVariable int id, HttpServletRequest request) {
        //查原有用户数据
        User target = userService.findById(id);
        User rs = null;
        //响应结果
        ResponseEntity<User> responseEntity = new ResponseEntity<User>();
        boolean result = false;

        if (target != null) {
            UserDTO.copyProperties(userDTO, target);
            result = (rs = userService.save(target)) != null;//update
        }

        return responseEntity
                .message(result ? "用户信息更改请求成功" : "用户信息更改请求失败")
                .data(rs);
    }

//    @ApiOperation("更换用户头像接口")
//    @PutMapping("/{id}")
//    @ResponseBody
//    public ResponseEntity updateIcon(@RequestParam MultipartFile file, @PathVariable int id) {
//        //查原有用户数据
//        User target = new User();
//        User rs = null;
//        //响应结果
//        ResponseEntity responseEntity = new ResponseEntity();
//        boolean result = false;
//        //上传头像
//        target.setHeadIcon(FileUploadUtil.uploadFile(file));
//        target.setUid(id);
//        result = (rs = userService.save(target)) != null;//update
//
//        return responseEntity.success(result)
//                .status(result ? HttpStatus.OK : HttpStatus.BAD_REQUEST)
//                .message(result ? "用户信息更改请求成功" : "用户信息更改请求失败")
//                .data(rs);
//    }
}
