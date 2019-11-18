package com.groupten.online_music.dao.impl;

import com.groupten.online_music.entity.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository
public interface IUserDao
        extends JpaSpecificationExecutor<User>, PagingAndSortingRepository<User, Integer> {
    @Query(value = "from User u where u.user_name = ?1")
    public User findByUserName(String target);
//    JpaSpecificationExecutor接口内提供功能如下：
//    Optional<T> findOne(@Nullable Specification<T> var1);
//    List<T> findAll(@Nullable Specification<T> var1);
//    Page<T> findAll(@Nullable Specification<T> var1, Pageable var2);
//    List<T> findAll(@Nullable Specification<T> var1, Sort var2);
//    long count(@Nullable Specification<T> var1);

//    PagingAndSortingRepository接口内提供功能如下：
//    Iterable<T> findAll(Sort var1);
//    Page<T> findAll(Pageable var1);
}