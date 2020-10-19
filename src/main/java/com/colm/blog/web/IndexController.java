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
    @GetMapping("/{param1}/{param2}")
    public String index(@PathVariable String param1, @PathVariable String param2) {
//        int i = 8/0;
//        String blog = null;
//        if (blog == null) {
//           throw new BlogNotFoundException("博客不存在");
//        }
        System.out.println("------index------");
        System.out.println("params: " + param1 + "," + param2);
        return "index";
    }
}
