package com.colm.blog.dao;

import com.colm.blog.po.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Colm on 2020/10/22
 */
public interface BlogRepository extends JpaRepository<Blog, Long>, JpaSpecificationExecutor<Blog> {
    // findOne、getOne都不满足实际需求，所以自定义获取一条记录的方法
    @Query("select b from Blog b where b.id = ?1 ")
    Blog getById(Long id);

    // 从所有推荐的博客中获取前几条
    @Query("select b from Blog b where b.recommend = true")
    List<Blog> listRecommendTop(Pageable pageable);

    // 获取所有已发布的博客
    Page<Blog> getByPublished(Pageable pageable, Boolean published);

    @Query("select b from Blog b where b.title like ?1 or b.description like ?1")
    Page<Blog> findByQuery(Pageable pageable, String query);

    // 更新views
    @Modifying
    @Query("update Blog b set b.views = b.views+1 where b.id = ?1")
    int updateViews(Long id);

    // 查询年份
    @Query("select function('date_format', b.createTime, '%Y') as year from Blog b group by function('date_format', b.createTime, '%Y') order by year desc ")
    List<String> findGroupYear();

    // 根据年份查询博客
    @Query("select b from Blog b where function('date_format', b.createTime, '%Y') = ?1")
    List<Blog> findByYear(String year);

}
