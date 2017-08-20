package com.ximalaya.ops.schedule.web.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import com.ximalaya.ops.schedule.web.service.ITaskService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by nihao on 17/8/17.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
    @Resource
    private ITaskService taskService;

    @RequestMapping("/getChildren")
    @ResponseBody
    public String getChildren(HttpServletRequest request) throws Exception {
        String path = request.getParameter("path");
        Preconditions.checkNotNull(path);
        List<String> list = taskService.getChildren(path);
        return JSON.toJSONString(list);
    }
    @RequestMapping("/getData")
    @ResponseBody
    public String getData(HttpServletRequest request) throws Exception {
        String path = request.getParameter("path");
        Preconditions.checkNotNull(path);
        return taskService.getData(path);
    }
    @RequestMapping("/delete")
    @ResponseBody
    public String delete(HttpServletRequest request) throws Exception {
        String path = request.getParameter("path");
        Preconditions.checkNotNull(path);
        taskService.delete(path);
        return "ok";
    }
}
