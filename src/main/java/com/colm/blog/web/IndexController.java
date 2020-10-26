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

    @GetMapping("/")
    public String index(@PageableDefault(size = 10, sort = {"views"}, direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        model.addAttribute("page", blogService.getList(pageable));
        model.addAttribute("topTypes", typeService.listTop(6));
        model.addAttribute("topTags", typeService.listTop(10));
        model.addAttribute("recommendBlogs", blogService.listRecommendTop(8));
        return "index";
    }

    @GetMapping("/blogs")
    public String blogs(){
        return "admin/blogs";
    }
    @GetMapping("/input")
    public String input(){
        return "admin/index";
    }
}
