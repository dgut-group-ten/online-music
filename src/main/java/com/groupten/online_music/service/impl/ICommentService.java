package com.groupten.online_music.service.impl;

import com.groupten.online_music.common.utils.STablePageRequest;
import com.groupten.online_music.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface ICommentService {
    Comment createComment(Comment comment, int uid);//新建评论
    Comment createReply(Comment comment, int uid, int beRepliedId);//新建回复
    Comment findById(int id);//通过id查找评论
    void deleteComment(Comment target);//删除评论
    List<Comment> findAll();
    Page<Comment> findByPage(int type, Long rid, int pid, Pageable pageable);//分页查询评论
    List<Comment> findSonCommentByPid(Integer pid, Map<String, String> commentMap);//分页查询子评论
    List<Comment> findSonCommentByPid(Integer pid, Integer offset, Integer size);//分页查询子评论
    Integer countByPid(Integer pid);//统计父评论下子评论数目
    boolean isCorrectComment(Map<String, String> commentMap, StringBuffer message);
}
