package com.colm.blog.web;

import com.colm.blog.po.Tag;
import com.colm.blog.service.BlogService;
import com.colm.blog.service.TagService;
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
 * Created by Colm on 2020/10/30
 */
@Controller
public class TagIndexController {

    @Autowired
    private TagService tagService;

    @Autowired
    private BlogService blogService;

    @GetMapping("/tags/{id}")
    public String tags(@PathVariable Long id,
                       @PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                       Model model) {
        List<Tag> tags = tagService.listTop(9999);
        if(id == -1) {
            id = tags.get(0).getId();
        }
        model.addAttribute("tags", tags);
        model.addAttribute("page", blogService.getListByTagId(pageable, id));
        model.addAttribute("activeTagId", id);
        return "tags";
    }
}
