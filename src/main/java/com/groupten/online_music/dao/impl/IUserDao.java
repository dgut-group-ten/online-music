package com.groupten.online_music.dao.impl;

import com.groupten.online_music.entity.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserDao
        extends JpaSpecificationExecutor<User>, PagingAndSortingRepository<User, Integer> {
    @Query(value = "from User u where u.user_name = ?1")
    public User findByUserName(String target);
}
