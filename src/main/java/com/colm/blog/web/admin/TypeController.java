package com.colm.blog.web.admin;

import com.colm.blog.po.Type;
import com.colm.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Colm on 2020/10/20
 */
@Controller
@RequestMapping("/admin")
public class TypeController {

    @Autowired
    private TypeService typeService;

    /**
     * 获取列表
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping("/types")
    public String types(@PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        model.addAttribute("page", typeService.listType(pageable));
        System.out.println("这一段不需要日志：》》》：" + typeService.listType(pageable).getContent());
        return "admin/types";
    }

    /**
     * 跳到新增页
     * @return
     */
    @GetMapping("/types/input")
    public String input() {
        return "admin/types-input";
    }

    /**
     * 新增操作
     * @return
     */
    @PostMapping("/types")
    public String save(Type type) {
        System.out.println("这一段不需要日志：》》》：" + type);
        Type repoType = typeService.getByName(type.getName());
        System.out.println("这一段不需要日志：》》》：" + repoType);
        if (repoType == null) {
            Type saveType = typeService.saveType(type);
            if (saveType != null) {
                //添加成功
            }
            // 添加失败
        } else {
            // 已存在，添加失败
        }
        return "redirect:/admin/types";
    }
}
