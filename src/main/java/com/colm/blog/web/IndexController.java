package com.colm.blog.web;

import com.colm.blog.service.BlogService;
import com.colm.blog.service.TagService;
import com.colm.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Colm on 2020/10/18
 */
@Controller
public class IndexController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    /**
     * 跳转到首页
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping({"/", "/blogs"})
    public String index(@PageableDefault(size = 8, sort = {"views"}, direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        model.addAttribute("page", blogService.getList(pageable));
        model.addAttribute("topTypes", typeService.listTop(6));
        model.addAttribute("topTags", typeService.listTop(10));
        model.addAttribute("recommendBlogs", blogService.listRecommendTop(8));
        return "index";
    }

    /**
     * 根据条件搜索博客，跳转到搜索结果页面
     * @param pageable
     * @param query
     * @param model
     * @return
     */
    @PostMapping("/search")
    public String search(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                         @RequestParam String query, Model model) {
        model.addAttribute("page", blogService.getList(pageable, "%" + query + "%"));
        model.addAttribute("query", query);
        return "search";
    }

    /**
     * 根据id获取博客，跳转到博客详情页
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id, Model model) {
        model.addAttribute("blog", blogService.getAndConvert(id));
        return "blog";
    }
}
