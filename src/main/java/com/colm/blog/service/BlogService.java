package com.colm.blog.service;

import com.colm.blog.po.Blog;
import com.colm.blog.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by Colm on 2020/10/22
 */
public interface BlogService {

    Blog getBlog(Long id);

    // 使用 PO
    Page<Blog> getList(Pageable pageable, Blog blog);

    // 使用 VO
    Page<Blog> getList(Pageable pageable, BlogQuery blogQuery);

    Blog updateBlog(Long id, Blog blog);

    Blog saveBlog(Blog blog);

    void deleteBlog(Long id);
}
