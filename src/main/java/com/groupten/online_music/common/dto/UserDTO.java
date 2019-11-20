package com.groupten.online_music.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;

@ApiModel(value = "UserDto")
public class UserDTO {
    @ApiModelProperty(value = "用户名")
    private String user_name;
    @ApiModelProperty(value = "用户密码")
    private String user_password;
    @ApiModelProperty(value = "头像链接")
    private String headIcon;
    @ApiModelProperty(value = "个人描述")
    private String description;
    @ApiModelProperty(value = "邮箱地址")
    private String Email;

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
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
