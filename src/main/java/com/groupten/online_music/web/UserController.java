package com.groupten.online_music.web;

import com.auth0.jwt.interfaces.Claim;
import com.groupten.online_music.common.jwt.JWTUtils;
import com.groupten.online_music.common.utils.EncryptionUtil;
import com.groupten.online_music.common.utils.FileUploadUtil;
import com.groupten.online_music.common.utils.ResponseEntity;
import com.groupten.online_music.common.utils.exception.ApplicationException;
import com.groupten.online_music.common.utils.exception.AuthenticationException;
import com.groupten.online_music.entity.EmailConfirm;
import com.groupten.online_music.entity.User;
import com.groupten.online_music.entity.entityEnum.ConfirmStatus;
import com.groupten.online_music.entity.entityEnum.UserStatus;
import com.groupten.online_music.service.impl.IEmailService;
import com.groupten.online_music.service.impl.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService userService;
    @Autowired
    private IEmailService emailService;

    /**
     * 登录验证
     *
     * @param userMap 登录信息
     * @return 返回前端需要的数据
     */
    @PostMapping("/token")
    @ResponseBody
    public ResponseEntity login(@RequestParam Map<String, String> userMap) {
        //userMap的数据封装到user里
        User user = new User(userMap);
        //响应结果
        ResponseEntity responseEntity = new ResponseEntity<User>();
        boolean result = false;
        String token = null;
        //登录操作
        int uid = userService.login(user);//登录失败返回-1
        if ((result = (uid != -1))) {//验证成功生成token
            user.setUid(uid);
            token = JWTUtils.createToken(user);
        } else {
            throw new AuthenticationException("登录失败！请检查用户名与密码");
        }

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("Token", token);
        //data.put("uid", uid);
        data.put("name", userMap.get("name"));
        return responseEntity.message(result ? "登录请求成功" : "不存在该用户或密码错误! 登录请求失败").data(data);
    }

    /**
     * 刷新token
     *
     * @return 返回前端需要的数据
     */
    @PostMapping("/token/refresh")
    @ResponseBody
    public ResponseEntity loginRefresh(HttpServletRequest request) {
        String token = request.getHeader("Token");
        Map<String, Claim> claims = JWTUtils.verifyToken(token);
        String tokenRefresh = JWTUtils.refreshToken(
                claims.get("uid").asInt(),
                claims.get("name").asString(),
                claims.get("isAdmin").asBoolean(),
                claims.get("web").asString()
        );

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("Token", tokenRefresh);
        return new ResponseEntity().message("token刷新成功！").data(data);
    }

    /**
     * 注册
     *
     * @param userMap 注册信息
     * @return 返回提示信息
     */
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

    /**
     * 查看某一用户的信息
     *
     * @param name 目标用户名
     * @return 返回用户信息
     */
    @GetMapping("/{name}")
    @ResponseBody
    public ResponseEntity getOneUserInfo(@PathVariable String name, HttpServletRequest request) {
        User user = userService.findByName(name);
        if (user == null) {
            throw new ApplicationException("用户不存在");
        }
        user.getUserInfo().setHeadIcon(userService.resetHeadIconUrl(request, user.getUserInfo().getHeadIcon()));
        return new ResponseEntity().message("用户信息获取成功").data(user.getUserInfo());
    }

    /**
     * 获取个人信息
     *
     * @param request 请求实体
     * @return 返回个人信息
     */
    @GetMapping
    @ResponseBody
    public ResponseEntity getPersonalInfo(HttpServletRequest request) {
        String token = request.getHeader("Token");
        int target_id = JWTUtils.verifyToken(token).get("uid").asInt();
        User user = userService.findById(target_id);
        user.setHeadIcon(userService.resetHeadIconUrl(request, user.getHeadIcon()));
        user.setUserInfo(null);
        return new ResponseEntity().message("用户信息获取成功").data(user);
    }

    /**
     * 修改用户信息，包括头像
     *
     * @param userMap 修改后的用户信息
     * @param request 请求实体
     * @return 返回修改后的内容
     */
    @PutMapping
    @ResponseBody
    public ResponseEntity update(@RequestParam Map<String, Object> userMap, @RequestParam MultipartFile headIcon, HttpServletRequest request) {
        //token验证，得到uid
        String token = request.getHeader("Token");
        int target_id = JWTUtils.verifyToken(token).get("uid").asInt();
        //查原有用户数据
        User target = userService.findById(target_id);
        //修改用户信息
        userMap.put("headIcon", headIcon);
        userService.changeUserInfo(target, userMap);
        //保存已修改信息
        User user = userService.save(target);
        //处理返回的信息
        user.getUserInfo().setHeadIcon(userService.resetHeadIconUrl(request, user.getUserInfo().getHeadIcon()));
        return new ResponseEntity<User>().message("用户信息更改请求成功").data(user.getUserInfo());
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity update(@RequestParam MultipartFile file, @PathVariable int id) {
        //查原有用户数据
        User target = userService.findById(id);
        //修改用户信息
        String path = FileUploadUtil.uploadFile(file);
        target.setHeadIcon(path);
        //保存已修改信息
        User user = userService.save(target);

        return new ResponseEntity<User>().message("用户信息更改请求成功").data(user);
    }

    /**
     * 修改用户密码
     *
     * @param userMap 密码修改信息
     * @param request 请求信息
     * @return 提示信息
     */
    @PutMapping("/changePassword")
    @ResponseBody
    public ResponseEntity changePassword(@RequestParam Map<String, String> userMap, HttpServletRequest request) {
        //token验证, 得到uid
        String token = request.getHeader("Token");
        int target_id = JWTUtils.verifyToken(token).get("uid").asInt();
        //1.获取旧密码与新密码,需要用户登录
        String oldPassword = userMap.get("oldPassword");
        String newPassword = userMap.get("newPassword");
        //2.查用户进行匹配, 成功则修改，, 不成功提示错误
        if (!userService.changePassword(oldPassword, newPassword, target_id)) {
            throw new ApplicationException("密码错误! 请检查密码输入");
        }

        return new ResponseEntity().message("密码修改成功! ");
    }

    /**
     * 修改认证邮箱
     *
     * @param userMap 新邮箱信息
     * @param request 请求信息
     * @return 提示信息
     */
    @PutMapping("/rebind")
    @ResponseBody
    public ResponseEntity changeConfirmEmail(@RequestParam Map<String, String> userMap, HttpServletRequest request) {
        //token验证, 得到uid
        String token = request.getHeader("Token");
        int target_id = JWTUtils.verifyToken(token).get("uid").asInt();
        //1.查相关邮箱认证信息
        StringBuffer message = new StringBuffer("");//提示信息
        String newConfirmEmail = userMap.get("newEmail");//新邮箱
        String checkCode = userMap.get("checkCode");//验证码
        User user = userService.findById(target_id);//用户信息
        EmailConfirm emailConfirm = emailService.findOne(newConfirmEmail);//新邮箱认证信息
        EmailConfirm oldEmailConfirm = emailService.findOne(user.getEmail());//旧邮箱认证信息
        //2.匹配验证码
        if (user.getStatus() == UserStatus.ENABLE && emailService.isCorrectCode(emailConfirm, checkCode, message)) {
            //2-1.匹配成功, 更换认证邮箱
            if (oldEmailConfirm != null) {//解绑邮箱
                oldEmailConfirm.setStatus(ConfirmStatus.UNCONFIRMED);
                emailService.save(oldEmailConfirm);
            }
            user.setEmail(newConfirmEmail);
            emailConfirm.setStatus(ConfirmStatus.CONFIRMED);
            emailService.save(emailConfirm);
            userService.save(user);
            message.append("认证邮箱修改成功! ");
        }
        //2.修改结果
        return new ResponseEntity().message(message.toString());
    }

    /**
     * 忘记密码，通过邮箱验证修改密码
     *
     * @param userMap 修改信息
     * @return 提示信息
     */
    @PutMapping("/forgotPassword")
    @ResponseBody
    public ResponseEntity forgotPassword(@RequestParam Map<String, String> userMap) {
        //不需要登录
        //通过邮箱验证码修改密码
        //1.获取新密码和邮箱验证信息
        String name = userMap.get("name");
        String email = userMap.get("email");
        String checkCode = userMap.get("checkCode");
        String newPassword = userMap.get("newPassword");
        StringBuffer message = new StringBuffer("");//提示信息
        //2.匹配邮箱验证信息
        User user = userService.findByName(name);
        if (user == null) throw new ApplicationException("无该用户! ");
        if (!user.getEmail().equals(email)) throw new ApplicationException("用户邮箱错误! ");
        EmailConfirm emailConfirm = emailService.findOne(email);
        if (emailService.isForgotPasswordCorrectCode(emailConfirm, checkCode, message)) {
            //匹配成功, 修改密码
            user.setPassword(EncryptionUtil.encryption(newPassword));
            if (userService.save(user) != null) message.append("密码已修改! ");
        }

        return new ResponseEntity().message(message.toString());
    }
}
