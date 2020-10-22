package com.colm.blog.service;

import com.colm.blog.po.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by Colm on 2020/10/20
 */
public interface TypeService {

    Type saveType(Type type);

    Type getType(Long id);

    Page<Type> listType(Pageable pageable);

    List<Type> listType();

    Type updateType(Long id, Type type);

    void deleteType(Long id);

    Type getByName(String name);
}
