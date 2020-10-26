package com.colm.blog.service;

import com.colm.blog.dao.TypeRepository;
import com.colm.blog.exception.NotFoundException;
import com.colm.blog.po.Type;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Colm on 2020/10/20
 */
@Service
public class TypeServiceImpl implements TypeService {

    @Autowired
    private TypeRepository typeRepository;

    @Transactional
    @Override
    public Type saveType(Type type) {
        return typeRepository.save(type);
    }

    @Transactional
    @Override
    public Type getType(Long id) {
        return typeRepository.getOne(id);
    }

    @Transactional
    @Override
    public Page<Type> listType(Pageable pageable) {
        return typeRepository.findAll(pageable);
    }

    @Override
    public List<Type> listType() {
        return typeRepository.findAll();
    }

    @Transactional
    @Override
    public Type updateType(Long id, Type type) {
        Type t = typeRepository.getOne(id);
        if (t == null){
            throw new NotFoundException("不存在该分类");
        }
        // 将传进来的 type 属性复制给查出来的 t
        BeanUtils.copyProperties(type, t);
        return typeRepository.save(t);
    }

    @Transactional
    @Override
    public void deleteType(Long id) {
        typeRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Type getByName(String name) {
        return typeRepository.getByName(name);
    }

    @Override
    public List<Type> listTop(int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "blogs.size");
        PageRequest request = PageRequest.of(0, size, sort);
        // PageRequest extends AbstractPageRequest , AbstractPageRequest implements Pageable
        return typeRepository.listTop(request);
    }
}
