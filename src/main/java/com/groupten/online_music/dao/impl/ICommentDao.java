package com.groupten.online_music.dao.impl;

import com.groupten.online_music.entity.Comment;
import com.groupten.online_music.entity.entityEnum.CommentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ICommentDao extends JpaSpecificationExecutor<Comment>, PagingAndSortingRepository<Comment, Integer> {
    //根据pid和type和rid进行分页查询
    Page<Comment> findCommentsByResourceIdAndTypeAndPid(Long resourceId, CommentType type, Integer pid, Pageable pageable);
    //根据pid分页查询子评论
    @Query(value = "select * from t_comment c where c.pid = :pid order by c.created desc limit :offset, :ps", nativeQuery = true)
    List<Comment> findSonCommentByPid(@Param("pid") Integer pid, @Param("offset") Integer offset, @Param("ps") Integer size);
    //获取子评论数目
    Integer countCommentByPid(Integer pid);
    @Modifying
    @Transactional
    @Query("delete from Comment c where c.pid = ?1")
    void deleteByPid(Integer cid);
}
