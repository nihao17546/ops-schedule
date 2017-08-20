package com.ximalaya.ops.schedule.web.controller;

import com.ximalaya.ops.schedule.web.model.SessionInfo;
import com.ximalaya.ops.schedule.web.model.po.ScheduleUserPO;
import com.ximalaya.ops.schedule.web.model.result.JSONResult;
import com.ximalaya.ops.schedule.web.service.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by nihao on 17/8/17.
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;

    @RequestMapping("/exit")
    public String exit(HttpServletRequest request){
        request.getSession().removeAttribute("user");
        return "redirect:/page/index";
    }

    @RequestMapping("/regist")
    @ResponseBody
    public JSONResult regist(HttpServletRequest request){
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        return userService.addUser(username, password);
    }

    @RequestMapping("/update")
    @ResponseBody
    public JSONResult update(HttpServletRequest request){
        HttpSession session = request.getSession();
        SessionInfo sessionInfo = (SessionInfo) session.getAttribute("user");
        return userService.updateUser(sessionInfo.getId(), request.getParameter("password"));
    }

    @RequestMapping("/login")
    @ResponseBody
    public JSONResult login(HttpServletRequest request){
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        JSONResult jsonResult = userService.login(username, password);
        if(jsonResult.getCode() == 200){
            request.getSession().setAttribute("user", jsonResult.getData());
        }
        return jsonResult;
    }

}
