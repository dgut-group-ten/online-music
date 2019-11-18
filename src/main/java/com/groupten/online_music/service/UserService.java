package com.groupten.online_music.service;

import com.groupten.online_music.dao.impl.IUserDao;
import com.groupten.online_music.entity.User;
import com.groupten.online_music.service.impl.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService implements IUserService {
    @Autowired
    private IUserDao userDao;
    @Override
    public boolean login(User user) {
        User rs = userDao.findByUserName(user.getUser_name());
        if(user.getUser_password().equals(rs.getUser_password()))
            return true;
        return false;
    }
}
