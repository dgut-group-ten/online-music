package com.groupten.online_music.service.impl;

import com.groupten.online_music.entity.User;

public interface IUserService {
    public boolean login(User user);
    public boolean register(User user);
    public boolean hasUser(User user);
    public boolean deleteUser(int id);
    User findById(int id);
    void save(User target);

    public String getToken(User user);//生成token并返回
}
