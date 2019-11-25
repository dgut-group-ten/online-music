package com.groupten.online_music.web;

import com.groupten.online_music.common.utils.UserDTO;
import com.groupten.online_music.common.utils.ApplicationException;
import com.groupten.online_music.common.utils.ResponseEntity;
import com.groupten.online_music.common.utils.STablePageRequest;
import com.groupten.online_music.entity.User;
import com.groupten.online_music.service.impl.IUserManageService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "用户管理相关接口")
@RestController
@RequestMapping("/admin")
public class UserManageController {
    @Autowired
    private IUserManageService userManageService;

    @ApiOperation(value = "新增用户接口")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "userDTO", value = "传入userDTO内部所有参数", required = true, paramType = "body", dataType="UserDTO")
    )
    @PostMapping
    public ResponseEntity add(@RequestBody UserDTO userDTO){
        User user = new User(userDTO);
        ResponseEntity responseEntity = new ResponseEntity();
        if(!userManageService.hasUser(user) && userManageService.findByEmail(user.getEmail())==null){
            user = userManageService.save(user);
        } else {
            return responseEntity.status(HttpStatus.BAD_REQUEST).message("有重名用户或邮箱已注册");
        }

        return responseEntity.status(HttpStatus.OK).message("用户添加成功").data(user);
    }

    @ApiOperation(value = "用户分页查询接口")
    @GetMapping
    public @ResponseBody ResponseEntity<Page<User>> FindAll(@RequestParam int pageNo, @RequestParam int pageSize, @RequestParam String sortField, @RequestParam String sortOrder){
        ResponseEntity<Page<User>> responseEntity = new ResponseEntity<>();
        STablePageRequest tablePageRequest = new STablePageRequest(pageNo, pageSize, sortField, sortOrder);
        Page<User> page;
        try {
            page = Page.empty(tablePageRequest.sTablePageable());
            page = userManageService.findAll( tablePageRequest.sTablePageable());
        } catch (ApplicationException ex) {
            return responseEntity.success(false).status(HttpStatus.BAD_REQUEST).message("分页查询失败");
        }

        return responseEntity.success(true).status(HttpStatus.OK).message("分页查询成功").data(page);
    }

    @ApiOperation("删除用户接口")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "id", value = "传入用户id", required = true, paramType = "path", dataType="int")
    )
    @DeleteMapping("/{id}")
    public @ResponseBody
    ResponseEntity delete(@PathVariable int id) {
        //响应结果
        ResponseEntity<User> responseEntity = new ResponseEntity<User>();
        boolean result = userManageService.deleteById(id);
        return responseEntity.success(result)
                .status(result ? HttpStatus.OK : HttpStatus.BAD_REQUEST)
                .message(result ? "删除请求成功" : "删除请求失败");
    }
}
