package com.colm.blog.web.admin;

import com.colm.blog.po.Blog;
import com.colm.blog.po.Tag;
import com.colm.blog.po.User;
import com.colm.blog.service.BlogService;
import com.colm.blog.service.TagService;
import com.colm.blog.service.TypeService;
import com.colm.blog.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by Colm on 2020/10/20
 */
@Controller
@RequestMapping("/admin")
public class BlogController {

    private static final String LIST = "admin/blogs";
    private static final String INPUT = "admin/blogs-input";
    private static final String REDIRECT_INPUT = "redirect:/admin/blogs";

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    /**
     * 获取全量博客列表
     * @param pageable
     * @param blogQuery
     * @param model
     * @return
     */
    @GetMapping("/blogs")
    public String blogs(@PageableDefault(size = 10, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable, BlogQuery blogQuery, Model model) {
        model.addAttribute("types", typeService.listType());
//        model.addAttribute("page", blogService.getList(pageable, blogQuery));
        model.addAttribute("page", blogService.getList(pageable, blogQuery));
        return LIST;
    }

    /**
     * 根据标题、分类、是否推荐查询博客列表
     * @param pageable
     * @param blog
     * @param model
     * @return
     */
    @PostMapping("/blogs/search")
    public String search(@PageableDefault(size = 10, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable, BlogQuery blog, Model model) {
        model.addAttribute("page", blogService.getList(pageable, blog));
        return "admin/blogs :: blogList";
    }

    /**
     * 新增页面
     * @param model
     * @return
     */
    @GetMapping("/blogs/input")
    public String input(Model model) {
        model.addAttribute("blog", new Blog());
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags", tagService.listTag());
        return INPUT;
    }

    /**
     * 修改页面的回显
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/blogs/{id}/input")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags", tagService.listTag());
        Blog blog = blogService.getBlog(id);
        blog.initTagIds();
        model.addAttribute("blog", blog);
        return INPUT;
    }

    /**
     * 新增 blog 时的保存
     * @param blog
     * @param attributes
     * @param session
     * @return
     */
    @PostMapping("/blogs")
    public String save(Blog blog, RedirectAttributes attributes, HttpSession session) {
        // todo 可以在这里加后端校验，后期优化再维护
        if (blog.getId() == null) {
            blog.setUser((User) session.getAttribute("user"));
        }
        String ids = tagService.createOrNot(blog.getTagIds());
        blog.setTagIds(ids);
        blog.setTags(tagService.listTag(blog.getTagIds()));  // ex: 1,2,3
        blog.setType(typeService.getType(blog.getType().getId()));  // name="type.id"
        Blog blog1 = null;
        if (blog.getId() == null) {
            blog1 = blogService.saveBlog(blog);
        } else {
            blog1 = blogService.updateBlog(blog.getId(), blog);
        }
        if (blog1 != null) {
            attributes.addFlashAttribute("msg", "操作成功");
        } else {
            attributes.addFlashAttribute("msg", "操作失败");
        }
        return REDIRECT_INPUT;
    }

    /**
     * 删除一个博客
     * @param id
     * @param attributes
     * @return
     */
    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("msg", "删除成功");
        return REDIRECT_INPUT;
    }
}
