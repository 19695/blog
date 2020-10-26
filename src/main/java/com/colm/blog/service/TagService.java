package com.colm.blog.service;

import com.colm.blog.po.Blog;
import com.colm.blog.po.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.awt.print.Book;
import java.util.List;

/**
 * Created by Colm on 2020/10/21
 */
public interface TagService {

    Tag saveTag(Tag tag);

    Tag getTag(Long id);

    Page<Tag> listTag(Pageable pageable);

    List<Tag> listTag();

    List<Tag> listTag(String ids);

    Tag updateTag(Long id, Tag tag);

    void deleteTag(Long id);

    Tag getByName(String name);

    String createOrNot(String ids);

}
