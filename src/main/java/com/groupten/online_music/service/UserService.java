package com.groupten.online_music.service;

import com.groupten.online_music.common.utils.EncryptionUtil;
import com.groupten.online_music.common.utils.FileUploadUtil;
import com.groupten.online_music.dao.impl.IUserDao;
import com.groupten.online_music.entity.User;
import com.groupten.online_music.entity.UserInfo;
import com.groupten.online_music.entity.entityEnum.UserStatus;
import com.groupten.online_music.entity.entityEnum.UserType;
import com.groupten.online_music.service.impl.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class UserService implements IUserService {
    @Autowired
    private IUserDao userDao;
    private final static String defaultIcon = "images/default.jpg";
    /**
     * 登录操作
     *
     * @param user 传入登录信息
     * @return 返回登录结果
     */
    @Override
    public int login(User user) {
        User rs = userDao.findUserByName(user.getName());
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
        user.setHeadIcon(defaultIcon);
        user.setUserInfo(new UserInfo(user.getName(), user.getHeadIcon()));
        //3.为用户密码加密
        user.setPassword(EncryptionUtil.encryption(user.getPassword()));

        return null != userDao.save(user);
    }


    @Override
    public boolean hasUser(User user) {
        return null != userDao.findUserByName(user.getName());
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
        userInfo.put("created", user.getCreated());
        userInfo.put("status", user.getStatus());
        userInfo.put("type", user.getType());
        userInfo.put("description", user.getDescription());
        userInfo.put("headIcon", user.getHeadIcon());

        return userInfo;
    }

    @Override
    public User findByName(String name) {
        return userDao.findByUserName(name);
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
        MultipartFile file = (MultipartFile) userMap.get("headIcon");
        String path = FileUploadUtil.uploadFile(file);
        if (path != null) {
            //上传头像成功后删除旧头像
            target.setHeadIcon(path);
            target.getUserInfo().setHeadIcon(path);
        }
        //更改其他信息
        target.setName((String) userMap.get("name"));
        target.setDescription((String) userMap.get("description"));

        target.getUserInfo().setName((String) userMap.get("name"));
        target.getUserInfo().setDescription((String) userMap.get("description"));
    }

    /**
     * 根据用户名查询用户信息
     *
     * @param name 用户名
     * @return 用户信息
     */
    @Override
    public User getUserInfoByName(String name) {
        User user = userDao.findByUserName(name);
        return user == null ? null : user;
    }

    /**
     * 组装头像访问链接
     *
     * @param request 访问信息
     * @return 头像链接
     */
    @Override
    public String resetHeadIconUrl(HttpServletRequest request, String headIcon) {
        StringBuffer url = request.getRequestURL();
        System.out.println(url);
        System.out.println(url.substring(0, url.indexOf(request.getRequestURI())) + "/" + headIcon);
        return url.substring(0, url.indexOf(request.getRequestURI())) + "/" + headIcon;
    }

    /**
     * 更改密码
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @param uid 用户id
     * @return 结果
     */
    @Override
    public boolean changePassword(String oldPassword, String newPassword, int uid) {
        //查密码进行匹配
        User user = userDao.findById(uid).orElse(null);
        if (user == null || !user.getPassword().equals(EncryptionUtil.encryption(oldPassword))) return false;
        //匹配成功修改密码
        user.setPassword(EncryptionUtil.encryption(newPassword));
        return userDao.save(user) != null;
    }
}
