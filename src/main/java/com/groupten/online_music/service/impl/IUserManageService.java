package com.groupten.online_music.service.impl;

import com.groupten.online_music.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IUserManageService {
    public void save(User user);
    public boolean deleteById(int id);
    public User findById(int id);
    public List<User> findAll();
    public List<User> findAllByIds(List<Integer> ids);
    public Page<User> findAll(Pageable pageable);
}
