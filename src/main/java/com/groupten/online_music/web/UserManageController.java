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

//@Api(tags = "用户管理相关接口")
@RestController
@RequestMapping("/admin")
public class UserManageController {
    @Autowired
    IUserManageService userManageService;

    //@ApiOperation(value = "新增用户接口")
    @PostMapping
    public String save(@RequestBody UserDTO userDTO){
        return null;
    }

//    @ApiOperation(value = "用户分页查询接口")
//    @ApiImplicitParams(
//            @ApiImplicitParam(name = "tablePageRequest", value = "封装分页参数, 可接受Json格式或对象数据", required = true, paramType = "body", dataType="STablePageRequest")
//    )
    @GetMapping
    public @ResponseBody ResponseEntity<Page<User>> FindAll(@RequestBody STablePageRequest tablePageRequest){
        ResponseEntity<Page<User>> responseEntity = new ResponseEntity<>();
        Page<User> page = Page.empty(tablePageRequest.sTablePageable());
        try {
            page = userManageService.findAll( tablePageRequest.sTablePageable());
        } catch (ApplicationException ex) {
            return responseEntity.success(false).status(HttpStatus.BAD_REQUEST).message("分页查询失败");
        }

        return responseEntity.success(true).status(HttpStatus.OK).message("分页查询成功").data(page);
    }

//    @ApiOperation("删除用户接口")
//    @ApiImplicitParams(
//            @ApiImplicitParam(name = "id", value = "传入用户id", required = true, paramType = "path", dataType="int")
//    )
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
