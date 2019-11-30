package com.groupten.online_music.web;

import com.groupten.online_music.common.jwt.JWTUtils;
import com.groupten.online_music.common.utils.FileUploadUtil;
import com.groupten.online_music.common.utils.ResponseEntity;
import com.groupten.online_music.common.utils.exception.ApplicationException;
import com.groupten.online_music.common.utils.exception.AuthenticationException;
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
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
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
    @ResponseBody
    public ResponseEntity login(@RequestParam Map<String, String> userMap) {
        //userMap的数据封装到user里
        User user = new User(userMap);
        //响应结果
        ResponseEntity responseEntity = new ResponseEntity<User>();
        boolean result = false;
        String token = null;
//        System.out.println(userMap.toString());
//        System.out.println("userMap.get(\"name\"):" + userMap.get("name"));
        //登录操作
        int uid = userService.login(user);//登录失败返回-1
        if ((result = (uid != -1))) {//验证成功生成token
            user.setUid(uid);
            token = JWTUtils.createToken(user);
        } else {
            throw new AuthenticationException("登录失败！请检查用户名与密码");
        }

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("token", token);
        //data.put("uid", uid);
        data.put("name", userMap.get("name"));
        return responseEntity.message(result ? "登录请求成功" : "不存在该用户或密码错误! 登录请求失败").data(data);
    }

    @ApiOperation(value = "用户注册接口")
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity register(@RequestParam Map<String, String> userMap) {
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
            } else {
                throw new AuthenticationException("验证码错误! 请重新确认您的验证码");
            }
        } else {
            throw new ApplicationException("已存在重名用户!");
        }

        return responseEntity.message(message.toString());
    }

    @ApiIgnore
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity getOneUserInfo(@PathVariable int id) {
        Map<String, Object> userMap = userService.getUserInfo(id);
        return new ResponseEntity().message("用户信息获取成功").data(userMap);
    }

    @ApiIgnore
    @GetMapping
    @ResponseBody
    public ResponseEntity getPersonalInfo(HttpServletRequest request) {
        String token = request.getHeader("token");
        int target_id = JWTUtils.verifyToken(token).get("uid").asInt();
        User user = userService.findById(target_id);

        return new ResponseEntity().message("用户信息获取成功").data(user);
    }

    @ApiIgnore
    @PutMapping
    @ResponseBody
    public ResponseEntity update(@RequestParam Map<String, Object> userMap, HttpServletRequest request) {
        //token验证，得到uid
        String token = request.getHeader("token");
        int target_id = JWTUtils.verifyToken(token).get("uid").asInt();
        //查原有用户数据
        User target = userService.findById(target_id);
        //修改用户信息
        userService.changeUserInfo(target, userMap);
        //保存已修改信息
        User user = userService.save(target);

        return new ResponseEntity<User>().message("用户信息更改请求成功").data(user);
    }

//    @ApiIgnore
//    @PutMapping("/{id}")
//    @ResponseBody
//    public ResponseEntity update(@RequestParam MultipartFile file, @PathVariable int id) {
//        //查原有用户数据
//        User target = userService.findById(id);
//        //修改用户信息
//        String path = FileUploadUtil.uploadFile(file);
//        target.setHeadIcon(path);
//        //保存已修改信息
//        User user = userService.save(target);
//
//        return new ResponseEntity<User>().message("用户信息更改请求成功").data(user);
//    }
}
