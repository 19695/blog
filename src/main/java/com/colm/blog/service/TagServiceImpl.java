package com.colm.blog.service;

import com.colm.blog.dao.TagRepository;
import com.colm.blog.exception.NotFoundException;
import com.colm.blog.po.Blog;
import com.colm.blog.po.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.thymeleaf.expression.Strings;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Colm on 2020/10/21
 */
@Service
public class TagServiceImpl implements TagService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TagServiceImpl.class);

    @Autowired
    private TagRepository tagRepository;

    @Transactional
    @Override
    public Tag saveTag(Tag tag) {
        return tagRepository.save(tag);
    }

    @Transactional
    @Override
    public Tag getTag(Long id) {
        return tagRepository.getById(id);
    }

    @Transactional
    @Override
    public Page<Tag> listTag(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    @Transactional
    @Override
    public List<Tag> listTag(String ids) {
        return tagRepository.findAllById(convertToList(ids));
    }

    private List<Long> convertToList(String ids) {
        List<Long> list = new ArrayList<>();
        if (StringUtils.isEmpty(ids))
            return list;
        String[] strings = ids.split(",");
        for (String str : strings) {
            if (str != "") {
                list.add(Long.parseLong(str));
            }
        }
        return list;
    }

    @Transactional
    @Override
    public List<Tag> listTag() {
        return tagRepository.findAll();
    }

    @Transactional
    @Override
    public Tag updateTag(Long id, Tag tag) {
        Tag t = tagRepository.getOne(id);
        if (t == null) {
            throw new NotFoundException("");
        }
        // tag --> t
        BeanUtils.copyProperties(tag, t);
        return tagRepository.save(t);
    }

    @Transactional
    @Override
    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Tag getByName(String name) {
        return tagRepository.findByName(name);
    }

    @Override
    public String createOrNot(String ids) {
        String[] list = ids.split(",");
        for(String id : list) {
            try {
                if ("".equals(id)){
                    continue;
                }
                Tag tag = getTag(Long.parseLong(id));
                if (tag == null) {
                    Tag tag1 = new Tag();
                    tag1.setName(id);
                    Tag save = tagRepository.save(tag1);
                    LOGGER.info("新增博客时获取标签时有新增标签，新增标签：{}", save);
                    ids = ids.replace(id, save.getId().toString());
                }
            } catch (EntityNotFoundException e) {
                LOGGER.info("新增博客时获取标签时有新增标签，获取标签发生异常：{}", e);
                Tag tag = new Tag();
                tag.setName(id);
                Tag save = tagRepository.save(tag);
                LOGGER.info("新增博客时获取标签时有新增标签，新增标签：{}", save);
                ids = ids.replace(id, save.getId().toString());
            }
        }
        return ids;
    }
}