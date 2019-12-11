package com.groupten.online_music.common.utils;

import com.groupten.online_music.entity.User;
import com.groupten.online_music.entity.entityEnum.UserStatus;
import com.groupten.online_music.entity.entityEnum.UserType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel(value = "UserDto")
public class UserDTO {
    private String name;
    private String headIcon;
    private String description;
    private Date created;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public UserDTO(User user) {
        this.name = user.getName();
        this.headIcon = user.getHeadIcon();
        this.description = user.getDescription();
        this.created = user.getCreated();
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
