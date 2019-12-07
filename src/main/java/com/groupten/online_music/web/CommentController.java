package com.groupten.online_music.web;

import com.groupten.online_music.common.jwt.JWTUtils;
import com.groupten.online_music.common.utils.ResponseEntity;
import com.groupten.online_music.common.utils.STablePageRequest;
import com.groupten.online_music.common.utils.exception.ApplicationException;
import com.groupten.online_music.common.utils.exception.AuthenticationException;
import com.groupten.online_music.entity.Comment;
import com.groupten.online_music.service.impl.ICommentService;
import com.groupten.online_music.service.impl.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private ICommentService commentService;
    @Autowired
    private IUserService userService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity createComment(@RequestParam Map<String, String> commentMap, HttpServletRequest request) {
        //用户登录验证
        String token = request.getHeader("token");
        int uid = JWTUtils.verifyToken(token).get("uid").asInt();
        //1.封装数据到Comment实体中
        Comment comment = new Comment(commentMap);
        //2.根据被评论对象与评论操作类型, 新建评论
        String repliedCommentId = commentMap.get("repliedCommentId");
        Comment result;
        if (repliedCommentId != null && !repliedCommentId.equals("")) {
            //2.1被回复评论id不为空, 执行回复操作
            result = commentService.createReply(comment, uid, Integer.parseInt(repliedCommentId));
            if (result == null) throw new AuthenticationException("操作不合法! 发送回复的版块不同或回复的评论不存在");
        } else {
            //2.2被回复评论id为空, 执行评论操作
            result = commentService.createComment(comment, uid);
            if (result == null) throw new AuthenticationException("评论用户已停用, 请先更换用户登录! ");
        }
        //3.返回结果
        return new ResponseEntity().message("添加评论成功! ").data(result);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @ResponseBody
    public ResponseEntity deleteComment(@PathVariable int id, HttpServletRequest request) {
        //用户登录验证
        String token = request.getHeader("token");
        int uid = JWTUtils.verifyToken(token).get("uid").asInt();
        //1.查找评论与所属用户
        Comment target = commentService.findById(id);
        if (target.getUser().getUid() != uid) throw new AuthenticationException("权限不足, 只能删除自己所属评论! ");
        //2.删除操作
        commentService.deleteComment(target);
        //2.返回结果
        return new ResponseEntity().message("删除评论成功! ");
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity searchCommentByCommentType(@RequestParam Map<String, String> commentMap) {
        //处理请求数据
        if(commentMap.get("rid")==null || commentMap.get("type")==null) throw new ApplicationException("请求的查询参数格式有误! ");
        long rid = Long.parseLong(commentMap.get("rid"));
        int type = Integer.parseInt(commentMap.get("type"));
        //处理排序参数
        String ordering = "-created";//排序默认值
        if (commentMap.get(ordering)==null) commentMap.put("ordering", ordering);
        STablePageRequest sTablePageRequest = new STablePageRequest(commentMap);

        //1.根据type, rid, pid查找相应资源, type= 0-song, 1-song list
        Page<Comment> comments = commentService.findByPage(type, rid, 0, sTablePageRequest.sTablePageable());
        //2.返回结果
        return new ResponseEntity<Page<Comment>>().message("查询成功").data(comments);
    }

}
