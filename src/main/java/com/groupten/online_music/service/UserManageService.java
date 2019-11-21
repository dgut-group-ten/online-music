package com.groupten.online_music.service;

import com.groupten.online_music.dao.impl.IUserDao;
import com.groupten.online_music.entity.User;
import com.groupten.online_music.service.impl.IUserManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserManageService implements IUserManageService {
    @Autowired
    IUserDao userDao;

    /**
     * 新增用户
     * @param user 用户数据
     */
    @Transactional
    public void save(User user) {
        userDao.save(user);
    }

    /**
     * 根据用户id删除用户
     * @param id 用户id
     * @return
     */
    @Transactional
    public boolean deleteById(int id) {
        userDao.deleteById(id);
        return false;
    }

    /**
     * 根据id查询用户
     * @param id 查询id
     * @return 返回用户数据
     */
    @Override
    public User findById(int id) {
        return userDao.findById(id).get();
    }

    /**
     * 查找所有用户
     * @return 返回所有用户
     */
    @Override
    public List<User> findAll() {
        return (List<User>)userDao.findAll();
    }

    /**
     * 根据多个id一次查询多个用户
     * @param ids 多个用户id
     * @return 返回用户数据集合
     */
    @Override
    public List<User> findAllByIds(List<Integer> ids) {
        return (List<User>) userDao.findAllById(ids);
    }

    /**
     * 分页查找用户
     * @param pageable 分页条件
     * @return 返回一页用户数据
     */
    @Override
    public Page<User> findAll(Pageable pageable) {
        return userDao.findAll(pageable);
    }
}
