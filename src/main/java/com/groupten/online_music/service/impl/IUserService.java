package com.groupten.online_music.service.impl;

import com.groupten.online_music.entity.User;
import com.groupten.online_music.entity.UserInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface IUserService {
    public int login(User user);//登录
    public boolean register(User user);//注册
    public boolean hasUser(User user);//判断用户是否存在
    public User findById(int id);//通过用户id查找
    public User findByEmail(String email);//通过邮箱查找用户
    public User save(User target);//保存用户
    public Map<String, Object> getUserInfo(int uid);//获取某一用户信息
    public String changeHeadIcon(MultipartFile file);
    public void changeUserInfo(User target, Map<String, Object> userMap);
    UserInfo getUserInfoByName(String name); //根据用户名获取用户信息
}
