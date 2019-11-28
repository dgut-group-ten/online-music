package com.groupten.online_music.web;

import com.groupten.online_music.common.utils.ApplicationException;
import com.groupten.online_music.common.utils.ResponseEntity;
import com.groupten.online_music.common.utils.STablePageRequest;
import com.groupten.online_music.entity.EmailConfirm;
import com.groupten.online_music.entity.User;
import com.groupten.online_music.service.impl.IEmailService;
import com.groupten.online_music.service.impl.IUserManageService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(tags = "用户管理相关接口")
@RestController
@RequestMapping("/admin")
public class UserManageController {
    @Autowired
    private IUserManageService userManageService;
    @Autowired
    private IEmailService emailService;

    @ApiOperation(value = "新增用户接口")
    @PostMapping
    public ResponseEntity add(@RequestParam Map<String, String> userMap){
        User user = new User(userMap);
        ResponseEntity<User> responseEntity = new ResponseEntity<User>();
        if(!userManageService.hasUser(user) && userManageService.findByEmail(user.getEmail())==null){
            user = userManageService.save(user);
        } else {
            return responseEntity.message("有重名用户或邮箱已注册");
        }

        return responseEntity.message("用户添加成功").data(user);
    }

    @ApiOperation(value = "用户分页查询接口")
    @GetMapping
    public @ResponseBody
    ResponseEntity FindAll(@RequestParam Map<String, String> pagingMap){
        ResponseEntity<Page<User>> responseEntity = new ResponseEntity<>();
        STablePageRequest tablePageRequest = new STablePageRequest(pagingMap);
        Page<User> page;
        try {
            page = Page.empty(tablePageRequest.sTablePageable());
            page = userManageService.findAll( tablePageRequest.sTablePageable());
        } catch (ApplicationException ex) {
            return responseEntity.message("分页查询失败");
        }

        return responseEntity.message("分页查询成功").data(page);
    }

    @ApiOperation("删除用户接口")
    @DeleteMapping("/{id}")
    public @ResponseBody
    ResponseEntity delete(@PathVariable int id) {
        //响应结果
        ResponseEntity<User> responseEntity = new ResponseEntity<User>();
        User target = userManageService.findById(id);
        EmailConfirm emailConfirm = emailService.findOne(target.getEmail());
        try {
            //存在用户，删除相关信息
            userManageService.delete(target);
            if (emailConfirm != null)
                emailService.delete(emailConfirm);
        } catch (ApplicationException e) {
            e.printStackTrace();
            return responseEntity.message("删除请求失败！\n" + e.getCause());
        }

        return responseEntity.message("删除请求成功！");
    }
}
