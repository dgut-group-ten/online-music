package com.groupten.online_music.common.utils;

import com.groupten.online_music.entity.User;
import com.groupten.online_music.entity.entityEnum.UserStatus;
import com.groupten.online_music.entity.entityEnum.UserType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel(value = "UserDto")
public class UserDTO {
    @ApiModelProperty(value = "用户名", example = "username")
    private String name;
    @ApiModelProperty(value = "用户密码", example = "password")
    private String password;
    @ApiModelProperty(value = "头像链接", example = "http://music-01.niracler.com/headIcon.jpg")
    private String headIcon;
    @ApiModelProperty(value = "个人描述", example = "who am I?")
    private String description;
    @ApiModelProperty(value = "邮箱地址", example = "xxxx@qq.com")
    private String email;
    @ApiModelProperty(value = "验证码", example = "123456")
    private String checkCode;
    @ApiModelProperty(value = "用户状态", example = "ENABLE")
    private UserStatus status;
    @ApiModelProperty(value = "用户类型", example = "NORMAL")
    private UserType type;
    @ApiModelProperty(value = "创建时间", example = "xxxx-xx-xx")
    private Date created;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHeadIcon() {
        return headIcon;
    }

    public void setHeadIcon(String headIcon) {
        this.headIcon = headIcon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    /**
     * 将修改过的信息覆盖源信息
     * @param userDTO 修改过的信息
     * @param user 未修改过的信息
     */
    public static void copyProperties(UserDTO userDTO, User user){
        user.setName(userDTO.getName());
        user.setDescription(userDTO.getDescription());
    }
}
