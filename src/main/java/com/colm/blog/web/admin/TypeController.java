package com.colm.blog.web.admin;

import com.colm.blog.po.Type;
import com.colm.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

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
//        System.out.println("这一段不需要日志：》》》：" + typeService.listType(pageable).getContent());
        return "admin/types";
    }

    /**
     * 跳到新增页
     * @return
     */
    @GetMapping("/types/input")
    public String input(Model model) {
        model.addAttribute("type", new Type());
        return "admin/types-input";
    }

    /**
     * 跳到修改页，页面复用添加页面
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/types/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        model.addAttribute("type", typeService.getType(id));
        return "admin/types-input";
    }

    /**
     * 更新分类信息
     * @param type
     * @param result
     * @param id
     * @param attributes
     * @return
     */
    @PostMapping("/types/{id}")
    public String editPost(@Valid Type type, BindingResult result, @PathVariable Long id, RedirectAttributes attributes){
        if (result.hasErrors()) {
            return "admin/types-input";
        }
        Type repoType = typeService.getByName(type.getName());
        if (repoType != null) {
            result.rejectValue("name", "nameError", type.getName() + " 分类已存在");
            return "admin/types-input";
        }
        Type saveType = typeService.updateType(id, type);
        if (saveType != null) {
            //添加成功
            attributes.addFlashAttribute("message", "更新成功");
            return "redirect:/admin/types";
        }
        // 添加失败
        result.rejectValue("name", "nameError", "更新失败请重试");
        return "admin/types-input";
    }

    /**
     * 新增操作
     * @return
     */
    @PostMapping("/types")
    public String save(@Valid Type type, BindingResult result, RedirectAttributes attributes) {
        // type 中 name 有非空校验，校验失败的话这里会捕获错误
        if (result.hasErrors()) {
            return "admin/types-input";
        }
        Type repoType = typeService.getByName(type.getName());
        if (repoType != null) {
            // arg1必须和加了校验注解的属性保持一致，这里是 name
            result.rejectValue("name", "nameError", type.getName() + " 分类已存在");
            return "admin/types-input";
        }
        Type saveType = typeService.saveType(type);
        if (saveType != null) {
            //添加成功
            attributes.addFlashAttribute("message", "添加成功");
            return "redirect:/admin/types";
        }
        // 添加失败
        result.rejectValue("name", "nameError", "添加失败请重试");
        return "admin/types-input";
    }

    /**
     * 删除分类信息
     * @param id
     * @param attributes
     * @return
     */
    @GetMapping("/types/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        typeService.deleteType(id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/types";
    }
}
