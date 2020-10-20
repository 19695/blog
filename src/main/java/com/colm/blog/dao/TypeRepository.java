package com.colm.blog.dao;

import com.colm.blog.po.Type;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Colm on 2020/10/20
 */
public interface TypeRepository extends JpaRepository<Type, Long> {
}
