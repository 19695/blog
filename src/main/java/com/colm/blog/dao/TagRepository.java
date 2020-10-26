package com.colm.blog.dao;

import com.colm.blog.po.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Colm on 2020/10/21
 */
public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag findByName(String tag);

    @Query("select t from Tag t where t.id = ?1 ")
    Tag getById(Long id);

    @Query("select t from Tag t")
    List<Tag> listTop(Pageable pageable);
}
