package com.groupten.online_music.service;

import com.groupten.online_music.common.utils.EncryptionUtil;
import com.groupten.online_music.dao.impl.IUserDao;
import com.groupten.online_music.entity.User;
import com.groupten.online_music.entity.entityEnum.UserStatus;
import com.groupten.online_music.entity.entityEnum.UserType;
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

    /**
     * 登录操作
     * @param user 传入登录信息
     * @return 返回登录结果
     */
    @Override
    public int login(User user) {
        User rs = userDao.findByUserName(user.getName());
        if (rs != null) {//存在用户，匹配密码，正确返回uid
            if(EncryptionUtil.encryption(user.getPassword()).equals(rs.getPassword())) {
                return rs.getUid();
            }
        }

        return -1;
    }

    @Transactional
    public boolean register(User user) {
        //1.是否存在重名用户
        if (hasUser(user)) {
            return false;
        }
        //2.存入用户信息
        user.setStatus(UserStatus.ENABLE);
        user.setType(UserType.NORMAL);
        user.setCreated(new Date());
        //3.为用户密码加密
        user.setPassword(EncryptionUtil.encryption(user.getPassword()));

        return null!=userDao.save(user);
    }


    @Override
    public boolean hasUser(User user) {
        return null != userDao.findByUserName(user.getName());
    }

    @Transactional
    public boolean deleteUser(int id) {
        User target = userDao.findById(id).get();
        userDao.delete(target);
        return true;
    }

    @Override
    public User findById(int id) {
        return userDao.findById(id).get();
    }

    @Override
    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Transactional
    public User save(User target) {
        return userDao.save(target);
    }
}
