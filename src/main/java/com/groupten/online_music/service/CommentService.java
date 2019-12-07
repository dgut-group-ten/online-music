package com.groupten.online_music.service;

import com.groupten.online_music.common.utils.exception.AuthenticationException;
import com.groupten.online_music.dao.impl.ICommentDao;
import com.groupten.online_music.dao.impl.IUserDao;
import com.groupten.online_music.entity.Comment;
import com.groupten.online_music.entity.User;
import com.groupten.online_music.entity.entityEnum.CommentType;
import com.groupten.online_music.service.impl.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class CommentService implements ICommentService {
    @Autowired
    private ICommentDao commentDao;
    @Autowired
    private IUserDao userDao;

    /**
     * 新建评论
     *
     * @param comment 新的评论
     * @param uid     评论用户id
     * @return 返回结果
     */
    @Override
    @Transactional
    public Comment createComment(Comment comment, int uid) {
        //1.获取评论用户
        User user = userDao.findById(uid).orElse(null);
        if (user == null) return null;
        //2.建立关联关系
        comment.setCreated(new Date());
        comment.setUser(user);
        //3.保存评论
        return commentDao.save(comment);
    }

    /**
     * 新建回复
     *
     * @param comment            新的回复
     * @param uid                评论用户id
     * @param beRepliedCommentId 被回复的评论id
     * @return 返回结果
     */
    @Override
    @Transactional
    public Comment createReply(Comment comment, int uid, int beRepliedCommentId) {
        //1.获取评论用户与被回复评论
        User user = userDao.findById(uid).orElse(null);
        Comment beReplied = commentDao.findById(beRepliedCommentId).orElse(null);
        //2.判断回复的数据是否合法
        if (user == null || beReplied == null) return null;
        if (!isCorrectReply(comment, beReplied)) return null;
        //3.设置父评论id
        if (beReplied.getPid() == 0) {
            //3.1该回复为评论的回复
            comment.setPid(beReplied.getCid());
        } else {
            //3.2该回复为回复的回复
            comment.setPid(beReplied.getPid());
        }
        comment.setCreated(new Date());//保存时间
        //3.建立关联关系
        comment.setUser(user);
        comment.setRepliedUser(beReplied.getUser());
        //4.保存
        return commentDao.save(comment);
    }

    /**
     * 判断回复的数据是否合法
     *
     * @param comment   回复
     * @param beReplied 被回复的评论
     * @return 返回结果
     */
    private boolean isCorrectReply(Comment comment, Comment beReplied) {
        //判断是否在同一模块下
        if (comment.getType() != beReplied.getType()) return false;

        return true;
    }

    @Override
    public Comment findById(int id) {
        return commentDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void deleteComment(Comment target) {
        //1.删除评论
        if (target.getPid() != 0) {
            //1.1若评论类型为二级评论,直接删除
            commentDao.delete(target);
        } else {
            //1.2若评论类型为一级评论,找到其楼下二级评论后依次删除
            commentDao.deleteByPid(target.getCid());
            commentDao.delete(target);
        }
    }

    /**
     * 查找某类资源下的所有评论
     *
     * @param type 资源类型
     * @return 返回评论集合
     */
    @Override
    public List<Comment> findByType(int type) {
        return commentDao.findByType(CommentType.values()[type]);
    }

    @Override
    public List<Comment> findAll() {
        return (List<Comment>) commentDao.findAll();
    }

    /**
     * 查找某资源下的所有评论
     *
     * @param type 资源类型
     * @return 返回评论集合
     */
    @Override
    public Page<Comment> findByPage(int type, Long rid, int pid, Pageable pageable) {
        return commentDao.findCommentsByResourceIdAndTypeAndPid(rid, CommentType.values()[type], pid, pageable);
    }
}
