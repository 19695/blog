package com.colm.blog.dao;

import com.colm.blog.po.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Colm on 2020/10/28
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Comment getById(Long id);

    // 根据blod id 查询并且parentComment为null
    List<Comment> findByBlogIdAndParentCommentNull(Long blogId, Sort sort);
}
