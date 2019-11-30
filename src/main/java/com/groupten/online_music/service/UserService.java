package com.groupten.online_music.service;

import com.groupten.online_music.common.utils.EncryptionUtil;
import com.groupten.online_music.common.utils.FileUploadUtil;
import com.groupten.online_music.dao.impl.IUserDao;
import com.groupten.online_music.entity.User;
import com.groupten.online_music.entity.entityEnum.UserStatus;
import com.groupten.online_music.entity.entityEnum.UserType;
import com.groupten.online_music.service.impl.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class UserService implements IUserService {
    @Autowired
    private IUserDao userDao;

    /**
     * 登录操作
     *
     * @param user 传入登录信息
     * @return 返回登录结果
     */
    @Override
    public int login(User user) {
        User rs = userDao.findByUserName(user.getName());
        if (rs != null) {//存在用户，匹配密码，正确返回uid
            if (rs.getPassword().equals(EncryptionUtil.encryption(user.getPassword()))) {
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

        return null != userDao.save(user);
    }


    @Override
    public boolean hasUser(User user) {
        return null != userDao.findByUserName(user.getName());
    }

    @Override
    public User findById(int id) {
        Optional<User> user = userDao.findById(id);
        return user.orElse(null);
    }

    @Override
    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Transactional
    public User save(User target) {
        return userDao.save(target);
    }

    @Override
    public Map<String, Object> getUserInfo(int uid) {
        User user = findById(uid);
        Map<String, Object> userInfo = new HashMap<String, Object>();
        userInfo.put("name", user.getName());
        //userInfo.put("email", user.getEmail());
        userInfo.put("created", user.getCreated());
        userInfo.put("status", user.getStatus());
        userInfo.put("type", user.getType());
        userInfo.put("description", user.getDescription());
        userInfo.put("headIcon", user.getHeadIcon());

        return userInfo;
    }

    /**
     * 更换用户头像操作
     *
     * @param file 新头像
     */
    @Override
    public String changeHeadIcon(MultipartFile file) {
        String path = FileUploadUtil.uploadFile(file);

        if (path == null)
            return null;

        return path;
    }

    @Override
    public void changeUserInfo(User target, Map<String, Object> userMap) {
        //更换头像
        String path = FileUploadUtil.uploadFile((MultipartFile) userMap.get("headIcon"));
        if (path != null) target.setHeadIcon(path);
        //更改其他信息
        target.setName((String) userMap.get("name"));
        target.setName((String) userMap.get("description"));
    }
}
