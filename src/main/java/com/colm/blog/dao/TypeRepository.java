package com.colm.blog.dao;

import com.colm.blog.po.Type;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Colm on 2020/10/20
 */
public interface TypeRepository extends JpaRepository<Type, Long> {
    Type getByName(String name);

    @Query("select t from Type t")
    List<Type> listTop(Pageable pageable);
}
