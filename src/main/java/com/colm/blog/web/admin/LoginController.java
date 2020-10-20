package com.colm.blog.web.admin;

import com.colm.blog.po.User;
import com.colm.blog.service.UserService;
import com.colm.blog.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

/**
 * Created by Colm on 2020/10/19
 */
@Controller
@RequestMapping("/admin")
public class LoginController {

    @Autowired
    private UserService userService;

    /**
     * 来到登录页
     * @return
     */
    @GetMapping
    public String loginPage() {
        return "admin/login";
    }

    /**
     * 验证用户名密码登录
     * @param username 用户名
     * @param password 密码
     * @param session 登录成功保存用户信息
     * @param attributes 登录失败保存错误信息
     * @return
     */
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session, RedirectAttributes attributes) {
        User user = userService.checkUser(username, MD5Utils.code(password));
        if(user != null){
            user.setPassword(null); // 不要把密码传到前端session中
            session.setAttribute("user", user);
            return "admin/index";
        } else {
            /*
            不能使用 Model 和 model.addAttribute()方法，因为是redirect
             */
            attributes.addFlashAttribute("message", "用户名密码错误");
            return "redirect:/admin";
        }
    }

    /**
     * 退出登录
     * @param session 从session中移除user
     * @return
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/admin";
    }
}
