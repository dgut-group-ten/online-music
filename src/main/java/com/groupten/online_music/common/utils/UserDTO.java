package com.groupten.online_music.common.utils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "UserDto")
public class UserDTO {
    @ApiModelProperty(value = "用户名", example = "username")
    private String user_name;
    @ApiModelProperty(value = "用户密码", example = "password")
    private String user_password;
    @ApiModelProperty(value = "头像链接", example = "http://music-01.niracler.com/headIcon.jpg")
    private String headIcon;
    @ApiModelProperty(value = "个人描述", example = "who am I?")
    private String description;
    @ApiModelProperty(value = "邮箱地址", example = "xxxx@qq.com")
    private String email;
    @ApiModelProperty(value = "验证码", example = "123456")
    private String checkCode;

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
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
}
