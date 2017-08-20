package com.ximalaya.ops.schedule.web.controller;

import com.google.common.base.Preconditions;
import com.ximalaya.ops.schedule.web.dao.ITaskDAO;
import com.ximalaya.ops.schedule.web.dao.IUserDAO;
import com.ximalaya.ops.schedule.web.model.SessionInfo;
import com.ximalaya.ops.schedule.web.model.po.ScheduleTaskPO;
import com.ximalaya.ops.schedule.web.model.po.ScheduleUserPO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by nihao on 17/8/17.
 */
@Controller
@RequestMapping("/page")
public class PageController {
    @Resource
    private ITaskDAO taskDAO;
    @Resource
    private IUserDAO userDAO;

    @RequestMapping("/regist")
    public String regist(Model model){
        return "regist";
    }
    @RequestMapping("/login")
    public String login(Model model){
        return "login";
    }
    @RequestMapping("/index")
    public String index(Model model, HttpServletRequest request, HttpServletResponse response){
        Object obj = request.getSession().getAttribute("user");
        if(obj == null){
            return "redirect:/page/login";
        }
        model.addAttribute("user", obj);
        return "index";
    }
    @RequestMapping("/addTask")
    public String addTask(Model model){
        return "addTask";
    }
    @RequestMapping("/editTask")
    public String editTask(Model model,HttpServletRequest request){
        SessionInfo sessionInfo = (SessionInfo)request.getSession().getAttribute("user");
        Integer id = Integer.parseInt(request.getParameter("id"));
        ScheduleTaskPO taskPO = taskDAO.selectById(id);
        Preconditions.checkNotNull(taskPO);
        if(!sessionInfo.getTasks().contains(taskPO)){
            throw new RuntimeException("没有权限编辑此任务");
        }
        model.addAttribute("task", taskPO);
        return "editTask";
    }
    @RequestMapping("/authTask")
    public String authTask(Model model,HttpServletRequest request){
        SessionInfo sessionInfo = (SessionInfo)request.getSession().getAttribute("user");
        Integer id = Integer.parseInt(request.getParameter("id"));
        ScheduleTaskPO taskPO = taskDAO.selectById(id);
        Preconditions.checkNotNull(taskPO);
        if(!sessionInfo.getId().equals(taskPO.getCreateUid())){
            throw new RuntimeException("没有权限");
        }
        List<ScheduleUserPO> list = userDAO.selectUsersByTaskId(id);
        StringBuilder sb = new StringBuilder();
        String users = "";
        if(list!=null && !list.isEmpty()){
            for(ScheduleUserPO scheduleUserPO : list){
                sb.append(scheduleUserPO.getUsername()).append(",");
            }
            users = sb.toString().substring(0,sb.toString().length()-1);
        }
        model.addAttribute("task", taskPO);
        model.addAttribute("users", users);
        return "authTask";
    }
}
