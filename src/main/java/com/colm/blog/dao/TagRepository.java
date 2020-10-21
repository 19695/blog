package com.colm.blog.dao;

import com.colm.blog.po.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Colm on 2020/10/21
 */
public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag findByName(String tag);
}
