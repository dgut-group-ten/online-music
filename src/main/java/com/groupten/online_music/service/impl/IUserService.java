package com.groupten.online_music.service.impl;

import com.groupten.online_music.entity.User;

public interface IUserService {
    public boolean login(User user);
    public boolean register(User user);
    public boolean hasUser(User user);
    public User findById(int id);
    public User findByEmail(String email);
    public User save(User target);
}
