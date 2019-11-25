package com.groupten.online_music.common.utils;

import com.groupten.online_music.entity.User;
import com.groupten.online_music.entity.UserStatus;
import com.groupten.online_music.entity.UserType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

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
    @ApiModelProperty(value = "用户状态", example = "ENABLE")
    private UserStatus userStatus;
    @ApiModelProperty(value = "用户类型", example = "NORMAL")
    private UserType userType;
    @ApiModelProperty(value = "创建时间", example = "xxxx-xx-xx")
    private Date createTime;

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

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

    /**
     * 将修改过的信息覆盖源信息
     * @param userDTO 修改过的信息
     * @param user 未修改过的信息
     */
    public static void copyProperties(UserDTO userDTO, User user){
        user.setUser_name(user.getUser_name());
        user.setDescription(userDTO.getDescription());
    }
}
