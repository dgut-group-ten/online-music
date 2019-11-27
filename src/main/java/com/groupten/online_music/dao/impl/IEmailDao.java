package com.groupten.online_music.dao.impl;

import com.groupten.online_music.entity.EmailConfirm;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEmailDao extends JpaSpecificationExecutor<EmailConfirm>, PagingAndSortingRepository<EmailConfirm, Integer> {
    @Query("from EmailConfirm uc where uc.email = ?1")
    public EmailConfirm findByEmail(String email);
}
