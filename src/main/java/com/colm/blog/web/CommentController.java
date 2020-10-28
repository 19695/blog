package com.colm.blog.web;

import com.colm.blog.po.Comment;
import com.colm.blog.po.User;
import com.colm.blog.service.BlogService;
import com.colm.blog.service.CommentService;
import com.colm.blog.util.RandomAvatarUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Colm on 2020/10/28
 */
@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private BlogService blogService;

    @Value("${comment.avatar}")
    private String avatarDir;

    /**
     * 获取博客下的所有评论
     * @param blogId
     * @param model
     * @return
     */
    @GetMapping("/comments/{blogId}")
    public String comments(@PathVariable Long blogId, Model model) {
        model.addAttribute("comments", commentService.listCommentByBlogId(blogId));
        return "blog :: commentList";
    }

    /**
     * 新增一条评论
     * @param comment
     * @return
     */
    @PostMapping("/comments")
    public String save(Comment comment, HttpSession session) {
        Long blogId = comment.getBlog().getId();
        comment.setBlog(blogService.getById(blogId));
        // 从session中获取用户，如果存在用户则是admin评论
        User user = (User) session.getAttribute("user");
        if (user != null) {
            comment.setAvatar(user.getAvatar());
            comment.setAdminComment(true);
        } else {
            comment.setAvatar(avatarDir + RandomAvatarUtils.avatar());
        }
        commentService.saveComment(comment);
        return "redirect:/comments/" + blogId;
    }

}
