package com.colm.blog.web.admin;

import com.colm.blog.dao.TagRepository;
import com.colm.blog.po.Tag;
import com.colm.blog.service.TagService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Colm on 2020/10/21
 */
@Controller
@RequestMapping("/admin")
public class TagController {

    @Autowired
    private TagService tagService;

    /**
     * 获取所有 tag
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping("/tags")
    public String tags(@PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        model.addAttribute("page", tagService.listTag(pageable));
        model.addAttribute("tag", new Tag());
        return "admin/tags";
    }

    @GetMapping("/tags/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        tagService.deleteTag(id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/tags";
    }

    /**
     * 新增一个 tag ，此时列表和新增共用的是一个页面，新增使用模态框
     *
     * @param tag
     * @param result
     * @param attributes
     * @return
     */
//    @PostMapping("/tags")
    @Deprecated
    public String input(@Valid Tag tag, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            String msg = result.getAllErrors().get(0).getDefaultMessage();
            attributes.addFlashAttribute("message", msg);
            attributes.addFlashAttribute("color", "negative");
            return "redirect:/admin/tags";
        }
        Tag repoTag = tagService.getByName(tag.getName());
        if (repoTag != null) {
            attributes.addFlashAttribute("message", tag.getName() + "标签已存在");
            attributes.addFlashAttribute("color", "error");
            return "redirect:/admin/tags";
        }
        Tag saveTag = tagService.saveTag(tag);
        if (saveTag == null) {
            attributes.addFlashAttribute("message", "添加失败请重试");
            attributes.addFlashAttribute("color", "error");
            return "redirect:/admin/tags";
        }
        attributes.addFlashAttribute("message", "添加成功");
        attributes.addFlashAttribute("color", "success");
        return "redirect:/admin/tags";
    }

    @RestController
    @RequestMapping("/admin") // 从服务根路径开始，不加外部类的路径
    class RestTagController {
        /**
         * 获取要更新的 tag
         * @param id
         * @return
         */
        @ResponseBody
        @GetMapping("/tags/{id}/input")
        public Map edit(@PathVariable Long id, Model model) {
            Map<String, Object> map = new HashMap<>();
            Tag result = new Tag();
            BeanUtils.copyProperties(tagService.getTag(id), result);
            map.put("tag", result);
            // hibernate 返回来的是一个代理对象不能序列化所以不能直接写出
//            map.put("tag", tagService.getTag(id));
            return map;
        }

        /**
         * 添加、修改共用方法
         * @param tag
         * @param result
         * @param attributes
         * @return
         */
        @PostMapping("/tags")
        public Map input(@Valid Tag tag, BindingResult result, RedirectAttributes attributes) {
            Map<String, Object> map = new HashMap<>();
            if (result.hasErrors()) {
                map.put("error", result.getAllErrors().get(0).getDefaultMessage());
                return map;
            }
            Tag repoTag = tagService.getByName(tag.getName());
            if (repoTag != null) {
                map.put("error", tag.getName() + "标签已存在");
                return map;
            }
            Tag saveTag = null;
            String option = "";
            if (tag.getId() == null) {
                saveTag = tagService.saveTag(tag);
                option = "添加";
            } else {
                saveTag = tagService.updateTag(tag.getId(), tag);
                option = "修改";
            }
            if (saveTag == null) {
                map.put("error", tag.getName() + option + "失败请重试");
                return map;
            }
            map.put("message", tag.getName() + option + "成功");
            return map;
        }
    }
}
