package com.groupten.online_music.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.groupten.online_music.dao.impl.IUserDao;
import com.groupten.online_music.entity.User;
import com.groupten.online_music.entity.UserStatus;
import com.groupten.online_music.entity.UserType;
import com.groupten.online_music.service.impl.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
@Transactional
public class UserService implements IUserService {
    @Autowired
    private IUserDao userDao;
//    @Autowired
//    private MailService mailService;
    @Override
    public boolean login(User user) {
        boolean result = false;
        User rs = userDao.findByUserName(user.getUser_name());
        if (rs != null) {//存在用户，匹配密码
            result = user.getUser_password().equals(rs.getUser_password());
            user.setUser_id(rs.getUser_id());
        }
        return result;
    }

    @Transactional
    public boolean register(User user) {
        //1.是否存在重名用户
        if (hasUser(user)) {
            return false;
        }
        //2.发送激活邮件
//        mailService.sendSimpleMail(
//                user.getEmail(),
//                "来自在线音乐平台的验证邮件",
//
//        );
        //3.存入用户信息，将用户状态设为DISABLE，等待用户激活
        //user.setUser_status(UserStatus.DISABLE);
        user.setUser_type(UserType.NORMAL);
        user.setUser_createTime(new Date());

        return null!=userDao.save(user);
    }


    @Override
    public boolean hasUser(User user) {
        return null != userDao.findByUserName(user.getUser_name());
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

    @Transactional
    public void save(User target) {
        userDao.save(target);
    }

    /**
     * @param user 包装生成token的数据
     * @return 返回生成的token
     */
    @Override
    public String getToken(User user) {
        String token = "";
        token = JWT.create().withAudience(user.getUser_id() + "")
                .sign(Algorithm.HMAC256(user.getUser_password()));
        return token;
    }
}
