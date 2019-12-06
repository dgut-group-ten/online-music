package com.groupten.online_music.dao.impl;

import com.groupten.online_music.entity.Comment;
import com.groupten.online_music.entity.entityEnum.CommentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ICommentDao extends JpaSpecificationExecutor<Comment>, PagingAndSortingRepository<Comment, Integer> {
    @Query("from Comment c where c.type = ?1 and c.pid = 0")
    List<Comment> findByType(CommentType type);
    Page<Comment> findCommentsByResourceIdAndTypeOrderByCreatedAsc(Long resourceId, CommentType type, Pageable pageable);
    @Modifying
    @Transactional
    @Query("delete from Comment c where c.pid = ?1")
    void deleteByPid(Integer cid);
}
