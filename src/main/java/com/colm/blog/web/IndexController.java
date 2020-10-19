package com.colm.blog.web;

import com.colm.blog.exception.BlogNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Created by Colm on 2020/10/18
 */
@Controller
public class IndexController {
    @GetMapping("/")
    public String index() {
//        int i = 8/0;
//        String blog = null;
//        if (blog == null) {
//           throw new BlogNotFoundException("博客不存在");
//        }
//        return "index";
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
