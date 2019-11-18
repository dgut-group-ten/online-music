package com.groupten.online_music.service;

import com.groupten.online_music.dao.impl.IUserDao;
import com.groupten.online_music.entity.User;
import com.groupten.online_music.entity.UserStatus;
import com.groupten.online_music.entity.UserType;
import com.groupten.online_music.service.impl.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class UserService implements IUserService {
    @Autowired
    private IUserDao userDao;
    @Override
    public boolean login(User user) {
        User rs = userDao.findByUserName(user.getUser_name());
        return user.getUser_password().equals(rs.getUser_password());
    }

    @Transactional
    public boolean register(User user) {
        if(hasUser(user)) {
           return false;
        }

        user.setUser_status(UserStatus.ENABLE);
        user.setUser_type(UserType.NORMAL);
        user.setUser_createTime(new Date());
        return null != userDao.save(user);
    }

    @Override
    public boolean hasUser(User user) {
        return null != userDao.findByUserName(user.getUser_name());
    }
}
