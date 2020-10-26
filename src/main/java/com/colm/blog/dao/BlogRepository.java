package com.colm.blog.dao;

import com.colm.blog.po.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Colm on 2020/10/22
 */
public interface BlogRepository extends JpaRepository<Blog, Long>, JpaSpecificationExecutor<Blog> {
    @Query("select b from Blog b where b.id = ?1 ")
    Blog getById(Long id);

    @Query("select b from Blog b where b.recommend = true")
    List<Blog> listRecommendTop(Pageable pageable);

    Page<Blog> getByPublished(Pageable pageable, Boolean published);
}
