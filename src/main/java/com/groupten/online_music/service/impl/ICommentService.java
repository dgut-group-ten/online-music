package com.groupten.online_music.service.impl;

import com.groupten.online_music.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICommentService {
    Comment createComment(Comment comment, int uid);//新建评论
    Comment createReply(Comment comment, int uid, int beRepliedId);//新建回复
    Comment findById(int id);//通过id查找评论
    void deleteComment(Comment target);//删除评论

    List<Comment> findByType(int type);//获取某类资源下的评论集合

    List<Comment> findAll();
}
