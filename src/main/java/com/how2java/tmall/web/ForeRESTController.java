package com.how2java.tmall.web;

import com.how2java.tmall.dao.UserDao;
import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.pojo.User;
import com.how2java.tmall.service.CategoryService;
import com.how2java.tmall.service.ProductService;
import com.how2java.tmall.service.UserService;
import com.how2java.tmall.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class ForeRESTController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductService productService;
    @Autowired
    UserService userService;

    @GetMapping("/forehome")
    public Object home() {
        List<Category> cs = categoryService.list();
        productService.fill(cs);
        productService.fillByRow(cs);
        categoryService.removeCategoryFromProduct(cs);
        return cs;
    }
    @PostMapping("/foreregister")
    public Object register(@RequestBody User user){
        String name = user.getName();
        String password = user.getPassword();
        name = HtmlUtils.htmlEscape(name);
        user.setName(name);
        boolean isExist = userService.isExist(user.getName());
        if (isExist){
            String message = "用户名已经存在";
            return Result.fail(message);
        }
        userService.add(user);
        return Result.success();
    }
    @PostMapping("/forelogin")
    public Object login(@RequestBody User user, HttpSession session){
        String name = HtmlUtils.htmlEscape(user.getName());
        User u = userService.getByNameAndPassword(name, user.getPassword());
        if (null == u){
            return Result.fail("账号或密码错误，或不存在此账号");
        }
        session.setAttribute("user",user);
        return Result.success();
    }
}
