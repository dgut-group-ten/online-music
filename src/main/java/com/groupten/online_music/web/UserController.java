package com.groupten.online_music.web;

import com.groupten.online_music.common.jwt.JWTUtils;
import com.groupten.online_music.common.utils.FileUploadUtil;
import com.groupten.online_music.common.utils.UserDTO;
import com.groupten.online_music.common.utils.ResponseEntity;
import com.groupten.online_music.entity.User;
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

    @ApiOperation(value = "用户登录接口")
    @PostMapping("/token")
    public @ResponseBody
    ResponseEntity login(
            @RequestBody @ApiParam(name = "userDTO", value = "登录只需传入user_name, user_password", type = "body") UserDTO userDTO,
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
            @RequestParam Map<String, String> userMap,
            HttpServletRequest request) {
        //userDTO的数据封装到user里
        User user = new User(userMap);
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
            }else if((checkCode.equals(userMap.get("checkCode")))) {
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

    @ApiOperation("用户信息更改接口")
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

        return responseEntity.success(result)
                .status(result ? HttpStatus.OK : HttpStatus.BAD_REQUEST)
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
