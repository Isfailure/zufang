package com.qianfeng.controller;

import com.alibaba.druid.support.json.JSONUtils;
import com.qianfeng.common.JsonBean;
import com.qianfeng.pojo.User;
import com.qianfeng.service.UserService;
import com.qianfeng.utils.JsonUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Iterator;

@Controller
public class LoginController {
   @Autowired
    private UserService userService;

   @RequestMapping("/login.do")
    @ResponseBody
    // 5 在方法的参数里直接使用验证的注解
    public JsonBean login(@NotEmpty(message="{name.empty}") String uname, @NotEmpty(message=("密码不能为空")) String password, HttpSession session) {
        JsonBean bean = null;
        try {
            User user = userService.login(uname, password);
            session.setAttribute("user", user);
            bean = JsonUtils.createJsonBean(1,null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            bean = JsonUtils.createJsonBean(0, e.getMessage());
        }
        return bean;
    }

    // springmvc异常处理的方法，需要使用@ExceptionHandle修饰，一般会指定一个需要处理的异常
    // 如果报指定的异常，就会调用该方法
    // 如果验证不通过，会报ConstraintViolationException
    // 6 使用@ExceptionHandler修饰方法处理验证不通过的情况
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public JsonBean exception(ConstraintViolationException  e){
        String expInfo = null;
        // 获取验证不通过的异常信息
        Iterator<ConstraintViolation<?>> iterator = e.getConstraintViolations().iterator();
        while (iterator.hasNext()) {
            ConstraintViolation<?> next = iterator.next();
            // 获取验证不通过的信息
            System.out.println(next.getMessage());
            expInfo += next.getMessage() + ";";
        }
        System.out.println("exception method");
        return JsonUtils.createJsonBean(0, expInfo);
    }
}
