package com.groupten.online_music.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer user_id;
    @Column(length = 33)
    private String user_name;
    @Column(length = 16)
    private String user_password;
    @Enumerated(EnumType.STRING)
    private UserStatus user_status;
    @Enumerated(EnumType.STRING)
    private UserType user_type;
    private Date user_createTime;
    @Column(nullable = true)
    private String headIcon;
    @Column(nullable = true, length = 150)
    private String description;
    @Column(length = 40)
    private String Email;

    public User(){}

    public User(String userName, String password) {
        this.user_name = userName;
        this.user_password = password;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
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

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
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

    public UserStatus getUser_status() {
        return user_status;
    }

    public void setUser_status(UserStatus user_status) {
        this.user_status = user_status;
    }

    public UserType getUser_type() {
        return user_type;
    }

    public void setUser_type(UserType user_type) {
        this.user_type = user_type;
    }

    public Date getUser_createTime() {
        return user_createTime;
    }

    public void setUser_createTime(Date user_createTime) {
        this.user_createTime = user_createTime;
    }
}
