package com.colm.blog.web;

import com.colm.blog.po.Blog;
import com.colm.blog.po.Type;
import com.colm.blog.service.BlogService;
import com.colm.blog.service.TypeService;
import com.colm.blog.vo.BlogQuery;
import javassist.runtime.Desc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 不能叫 TypeController，因为 ./admin/下有同名controller，springboot容器中有两个同名的无法装配
 * Created by Colm on 2020/10/29
 */
@Controller
public class TypeIndexController {

    @Autowired
    private TypeService typeService;

    @Autowired
    private BlogService blogService;

    /**
     * 跳转到分类页面，查出来所有分类信息以及选中分类下的博客列表。
     * 未选择的情况下默认从获取的type列表中取第一个分类
     * @param pageable
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/types/{id}")
    public String types(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        @PathVariable() Long id, Model model) {
        List<Type> types = typeService.listTop(9999);
        if (id == -1) {
            id = types.get(0).getId();
        }
        BlogQuery blogQuery = new BlogQuery();
        blogQuery.setTypeId(id);
        model.addAttribute("types", types);
        model.addAttribute("page", blogService.getList(pageable, blogQuery));
        model.addAttribute("activeTypeId", id);
        return "types";
    }

}
