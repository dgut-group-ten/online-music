package com.groupten.online_music.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.groupten.online_music.entity.entityEnum.CommentType;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "t_comment")
public class Comment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cid;    //评论id
    @Column(nullable = false)
    private Integer pid = 0;    //父评论id,评论分两级,二级评论的pid为被回复的一级评论cid, 默认为一级评论-0
    @Column(nullable = false)
    private Long resourceId;//对应资源id
    @NotEmpty(message = "评论内容不能为空")
    @Size(max = 150, message = "评论内容不能多余150个字符")
    @Column(length = 150, nullable = false)
    private String content; //评论内容
    @Column(nullable = false)
    private Integer likedCount = 0;//点赞
    @Column(nullable = false)
    private Date created;//评论创建时间
    @Column(nullable = false)
    @Enumerated(value = EnumType.ORDINAL)
    private CommentType type;//评论类型——歌单, 歌曲

    @JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid")

    private User user;//评论用户

    @JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repliedId")
    private User repliedUser;//被回复用户

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "pid", referencedColumnName = "cid")
    @OrderBy("created DESC")
    private List<Comment> commentList = new ArrayList<>();//二级评论列表

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getLikedCount() {
        return likedCount;
    }

    public void setLikedCount(Integer likedCount) {
        this.likedCount = likedCount;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public CommentType getType() {
        return type;
    }

    public void setType(CommentType type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getRepliedUser() {
        return repliedUser;
    }

    public void setRepliedUser(User repliedUser) {
        this.repliedUser = repliedUser;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public Comment() {
    }

    public Comment(Map<String, String> commentMap) {
        this.content = commentMap.get("content");
        this.type = CommentType.values()[Integer.parseInt(commentMap.get("type"))];
        this.resourceId = Long.parseLong(commentMap.get("resourceId"));
    }
}
