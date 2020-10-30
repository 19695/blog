package com.colm.blog.service;

import com.colm.blog.dao.BlogRepository;
import com.colm.blog.exception.NotFoundException;
import com.colm.blog.po.Blog;
import com.colm.blog.po.Type;
import com.colm.blog.util.MarkdownUtils;
import com.colm.blog.util.MyBeanUtils;
import com.colm.blog.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

/**
 * Created by Colm on 2020/10/22
 */
@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    /**
     * 根据id获取博客
     * @param id
     * @return
     */
    @Transactional
    @Override
    public Blog getBlog(Long id) {
        return blogRepository.getOne(id);
    }

    /*
    * 查询博客，查询条件为 title、type、recommend
    * blog.getType().getId 会有空指针异常，
    * 方式一：可以通过如下方式，此时使用的还是PO
    */
    @Transactional
    @Override
    @Deprecated
    public Page<Blog> getList(Pageable pageable, Blog blog) {
        return blogRepository.findAll(pageable);
        /*return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                // 标题
                if (!"".equals(blog.getTitle()) || blog.getTitle() != null) {
                    predicates.add(criteriaBuilder.like(root.<String>get("title"), "%" + blog.getTitle() + "%"));
                }
                // 分类
                // 短路，先判断是否有type，没有直接跳过，否则报空指针
                if (blog.getType() != null && blog.getType().getId() != null) {
                    predicates.add(criteriaBuilder.equal(root.<Type>get("type").get("id"), blog.getType().getId()));
                }
                // 推荐
                if (blog.isRecommend()) {
                    predicates.add(criteriaBuilder.equal(root.<Boolean>get("recommend"), blog.isRecommend()));
                }
                criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        }, pageable);*/
    }

    /*
    * 方式二：使用 VO
    * 查询博客，查询条件为 title、type、recommend
    */
    @Override
    public Page<Blog> getList(Pageable pageable, BlogQuery blogQuery) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (!"".equals(blogQuery.getTitle()) && blogQuery.getTitle() != null) {
                    predicates.add(criteriaBuilder.like(root.<String>get("title"), "%" + blogQuery.getTitle() + "%"));
                }
                if (blogQuery.getTypeId() != null) {
                    predicates.add(criteriaBuilder.equal(root.<Type>get("type").get("id"), blogQuery.getTypeId()));
                }
                if (blogQuery.isRecommend()) {
                    predicates.add(criteriaBuilder.equal(root.<Boolean>get("recommend"), blogQuery.isRecommend()));
                }
//                criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])); // 这样没查出来
                // 参考 ：https://zhuanlan.zhihu.com/p/122539515
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        }, pageable);
    }

    /**
     * saveBlog 更新时会丢失字段，改为使用此方法更新博客信息
     * @param id
     * @param blog
     * @return
     */
    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog repositoryOne = blogRepository.getById(id);
        if (repositoryOne == null) {
            throw new NotFoundException("该博客不存在");
        }
        // blog --> repositoryOne
//        BeanUtils.copyProperties(blog, repositoryOne); // 会把 null 也copy
        BeanUtils.copyProperties(blog, repositoryOne, MyBeanUtils.getNullPropertyNames(blog));
        repositoryOne.setUpdateTime(new Date());
        return blogRepository.save(repositoryOne);
    }

    /**
     * 保存或更新博客
     * @param blog
     * @return
     */
    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        if (blog.getId() == null) {
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date()); // 不然新建的时候，列表页面没有时间显示
            blog.setViews(0);
        } else {
            blog.setUpdateTime(new Date());
        }
        return blogRepository.save(blog);
    }

    @Transactional
    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }

    /**
     * 查出来的是要显示在首页的，这个时候有未发布的我不想显示
     * @param pageable
     * @return
     */
    @Transactional
    @Override
    public Page<Blog> getList(Pageable pageable) {
        // 返回所有已发布的博客
        blogRepository.getByPublished(pageable, true);
        // 返回所有博客
        return blogRepository.findAll(pageable);
    }

    @Transactional
    @Override
    public List<Blog> listRecommendTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "updateTime");
        PageRequest request = PageRequest.of(0, size, sort);
        return blogRepository.listRecommendTop(request);
    }

    /**
     * 根据指定条件查询title及description
     * @param pageable
     * @param query
     * @return
     */
    @Transactional
    @Override
    public Page<Blog> getList(Pageable pageable, String query) {
        return blogRepository.findByQuery(pageable, query);
    }

    /**
     * 获取blog的content，并转换为Markdown返回给页面
     * @param id
     * @return
     */
    @Transactional
    @Override
    public Blog getAndConvert(Long id) {
        Blog blog = blogRepository.getById(id); // 此方法获取不到的时候返回 null。findOne返回Optional，getOne获取不到抛异常
        if (blog == null) {
            throw new NotFoundException("该博客不存在");
        }
        Blog copy = new Blog(); // 新建一个对象，避免将转换结果写回数据库
        BeanUtils.copyProperties(blog, copy);
        String content = copy.getContent();
        String result = MarkdownUtils.markdownToHtmlExtensions(content);// 此版本的第三方工具包，不会改变源文本，会返回一个修改后的结果
        copy.setContent(result);
        // 每次被阅读都需要增加阅读数views
        blogRepository.updateViews(id); // 它的返回值是影响了几条记录，不是更新后有多少浏览量
        copy.setViews(copy.getViews()+1);
        return copy;
    }

    /**
     * 此方法获取不到的时候返回 null。findOne返回Optional，getOne获取不到抛异常
     * @param id
     * @return
     */
    @Transactional
    @Override
    public Blog getById(Long id) {
        return blogRepository.getById(id);
    }

    /**
     * 根据tagId 获取博客列表
     * 连表查询
     * @param pageable
     * @param tagId
     * @return
     */
    @Override
    public Page<Blog> getListByTagId(Pageable pageable, Long tagId) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Join tags = root.join("tags");
                return criteriaBuilder.equal(tags.get("id"), tagId);
            }
        }, pageable);
    }

    /**
     * 归档
     * @return
     */
    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years = blogRepository.findGroupYear();
        Map<String, List<Blog>> map = new LinkedHashMap<>();
        for (String year : years) {
            map.put(year, blogRepository.findByYear(year));
        }
        return map;
    }

    /**
     * 统计博客数量
     * @return
     */
    @Override
    public Long countBlog() {
        return blogRepository.count();
    }
}
