package com.colm.blog.service;

import com.colm.blog.po.Comment;

import java.util.List;

/**
 * Created by Colm on 2020/10/28
 */
public interface CommentService {

    List<Comment> listCommentByBlogId(Long blogId);

    Comment saveComment(Comment comment);
}
