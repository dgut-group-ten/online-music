package com.groupten.online_music.entity;

import com.groupten.online_music.common.utils.UserDTO;
import com.groupten.online_music.entity.entityEnum.UserStatus;
import com.groupten.online_music.entity.entityEnum.UserType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

@Entity
@Table(name = "t_user")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer uid;
    @Column(length = 33)
    private String name;
    @Column(length = 100)
    private String password;
    @Enumerated(EnumType.STRING)
    private UserStatus status;
    @Enumerated(EnumType.STRING)
    private UserType type;
    private Date created;
    @Column(nullable = true)
    private String headIcon;
    @Column(nullable = true, length = 150)
    private String description;
    @Column(length = 40)
    private String email;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private UserInfo userInfo;

    public User() {
    }

    public User(Map<String, String> userMap) {
        this.name = userMap.get("name");
        this.password = userMap.get("password");
        this.headIcon = userMap.get("headIcon");
        this.description = userMap.get("description");
        this.email = userMap.get("email");
    }

    public User(UserDTO userDTO) {
        this.name = userDTO.getName();
        this.headIcon = userDTO.getHeadIcon();
        this.description = userDTO.getDescription();
        this.created = userDTO.getCreated();
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

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

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
