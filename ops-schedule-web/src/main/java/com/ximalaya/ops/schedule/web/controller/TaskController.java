package com.ximalaya.ops.schedule.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.base.Preconditions;
import com.ximalaya.ops.schedule.web.dao.ITaskDAO;
import com.ximalaya.ops.schedule.web.dao.IUserDAO;
import com.ximalaya.ops.schedule.web.model.SessionInfo;
import com.ximalaya.ops.schedule.web.model.enums.TypeEnum;
import com.ximalaya.ops.schedule.web.model.po.ScheduleTaskPO;
import com.ximalaya.ops.schedule.web.model.po.ScheduleUserPO;
import com.ximalaya.ops.schedule.web.model.result.JSONResult;
import com.ximalaya.ops.schedule.web.model.vo.TaskListVO;
import com.ximalaya.ops.schedule.web.service.ITaskService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by nihao on 17/8/17.
 */
@Controller
@RequestMapping("/task")
public class TaskController {
    @Resource
    private ITaskDAO taskDAO;
    @Resource
    private IUserDAO userDAO;
    @Resource
    private ITaskService taskService;

    private final String patternKey = "^[a-zA-Z0-9]{1,10}:{1}[a-zA-Z0-9]{1,10}$";

    @RequestMapping("/list")
    @ResponseBody
    public String list(HttpServletRequest request){
        SessionInfo sessionInfo = (SessionInfo)request.getSession().getAttribute("user");
        List<ScheduleTaskPO> tasks = taskDAO.selectTaskByUserId(sessionInfo.getId());
        sessionInfo.setTasks(tasks);
        List<TaskListVO> list = new ArrayList<>();
        List<Integer> userIds = new ArrayList<>();
        for(ScheduleTaskPO scheduleTaskPO : tasks){
            TaskListVO taskListVO = new TaskListVO();
            taskListVO.setId(scheduleTaskPO.getId());
            taskListVO.setGroup(scheduleTaskPO.getGroup());
            taskListVO.setKey(scheduleTaskPO.getKey());
            taskListVO.setDescription(scheduleTaskPO.getDescription());
            taskListVO.setCreateAt(scheduleTaskPO.getCreateAt());
            taskListVO.setUpdateAt(scheduleTaskPO.getUpdateAt());
            if(scheduleTaskPO.getCreateUid()!=null){
                taskListVO.setCreateUser(scheduleTaskPO.getCreateUid().toString());
                if(!userIds.contains(scheduleTaskPO.getCreateUid())){
                    userIds.add(scheduleTaskPO.getCreateUid());
                }
            }
            if(scheduleTaskPO.getUpdateUid()!=null){
                taskListVO.setUpdateUser(scheduleTaskPO.getUpdateUid().toString());
                if(!userIds.contains(scheduleTaskPO.getUpdateUid())){
                    userIds.add(scheduleTaskPO.getUpdateUid());
                }
            }
            if(scheduleTaskPO.getCreateUid().equals(sessionInfo.getId())){
                taskListVO.setOwn(true);
            }
            else{
                taskListVO.setOwn(false);
            }
            taskListVO.setCount(taskService.count(scheduleTaskPO.getGroup()));
            taskListVO.setType(TypeEnum.getType(scheduleTaskPO.getType()));
            if(taskListVO.getType().equals(TypeEnum.周期任务.name())){
                taskListVO.setTypeValue(scheduleTaskPO.getPeriod().toString());
            }
            else if(taskListVO.getType().equals(TypeEnum.固定时间任务.name())){
                taskListVO.setTypeValue(scheduleTaskPO.getTime());
            }
            list.add(taskListVO);
        }
        if(!userIds.isEmpty()){
            Map<Integer,ScheduleUserPO> userPOMap = userDAO.selectByIds(userIds);
            for(TaskListVO taskListVO : list){
                if(taskListVO.getCreateUser() != null){
                    taskListVO.setCreateUser(userPOMap.get(Integer.parseInt(taskListVO.getCreateUser())).getUsername());
                }
                if(taskListVO.getUpdateUser() != null){
                    taskListVO.setUpdateUser(userPOMap.get(Integer.parseInt(taskListVO.getUpdateUser())).getUsername());
                }
            }
        }

        JSONResult jsonResult = new JSONResult();
        jsonResult.setCode(200);
        jsonResult.setData(list);
        return JSON.toJSONString(jsonResult, SerializerFeature.WriteDateUseDateFormat,
                SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteMapNullValue);
    }

    @RequestMapping("/add")
    @ResponseBody
    public JSONResult add(HttpServletRequest request){
        JSONResult jsonResult = new JSONResult();
        SessionInfo sessionInfo = (SessionInfo)request.getSession().getAttribute("user");
        String group = request.getParameter("group");
        String key = request.getParameter("key");
        String description = request.getParameter("description");
        String users = request.getParameter("users");
        Integer type = Integer.parseInt(request.getParameter("type"));
        if(!Pattern.matches(patternKey, key)){
            jsonResult.setCode(500);
            jsonResult.setMessage("密钥格式错误");
            return jsonResult;
        }
        Long period = null;
        String time = null;
        if(TypeEnum.周期任务.getType().equals(type)){
            period = Long.parseLong(request.getParameter("typeValue"));
        }
        else if(TypeEnum.固定时间任务.getType().equals(type)){
            time = request.getParameter("typeValue");
        }
        else{
            jsonResult.setCode(500);
            jsonResult.setMessage("任务类型错误");
            return jsonResult;
        }
        List<Integer> userIds = new ArrayList<>();
        if(users != null && !users.trim().equals("")){
            String[] ss = users.split(",");
            try{
                for(String username : ss){
                    ScheduleUserPO scheduleUserPO = userDAO.selectByUsername(username);
                    if(scheduleUserPO == null){
                        throw new RuntimeException("用户["+username+"]不存在");
                    }
                    if(!userIds.contains(scheduleUserPO.getId())){
                        userIds.add(scheduleUserPO.getId());
                    }
                }
            }catch (Exception e){
                jsonResult.setCode(500);
                jsonResult.setMessage("管理人员设置错误,错误信息:" + e.getMessage());
                return jsonResult;
            }
        }
        try{
            return taskService.add(group,key,description,type,period,time,userIds,sessionInfo.getId());
        }catch (RuntimeException e){
            jsonResult.setCode(500);
            jsonResult.setMessage(e.getMessage());
            return jsonResult;
        }
    }

    @RequestMapping("/edit")
    @ResponseBody
    public JSONResult edit(HttpServletRequest request){
        JSONResult jsonResult = new JSONResult();
        SessionInfo sessionInfo = (SessionInfo)request.getSession().getAttribute("user");
        Integer id = Integer.parseInt(request.getParameter("id"));
        String group = request.getParameter("group");
        String key = request.getParameter("key");
        String description = request.getParameter("description");
        Integer type = Integer.parseInt(request.getParameter("type"));
        if(!Pattern.matches(patternKey, key)){
            jsonResult.setCode(500);
            jsonResult.setMessage("密钥格式错误");
            return jsonResult;
        }
        Long period = null;
        String time = null;
        if(TypeEnum.周期任务.getType().equals(type)){
            period = Long.parseLong(request.getParameter("typeValue"));
        }
        else if(TypeEnum.固定时间任务.getType().equals(type)){
            time = request.getParameter("typeValue");
        }
        else{
            jsonResult.setMessage("任务类型错误");
            jsonResult.setCode(500);
            return jsonResult;
        }
        ScheduleTaskPO taskPO = taskDAO.selectById(id);
        Preconditions.checkNotNull(taskPO);
        if(!sessionInfo.getTasks().contains(taskPO)){
            jsonResult.setCode(500);
            jsonResult.setMessage("没有权限编辑此任务");
            return jsonResult;
        }
        return taskService.edit(id, group, key, description, type, period, time, sessionInfo.getId());
    }

    @RequestMapping("/auth")
    @ResponseBody
    public JSONResult auth(HttpServletRequest request){
        SessionInfo sessionInfo = (SessionInfo)request.getSession().getAttribute("user");
        String users = request.getParameter("users");
        Integer id = Integer.parseInt(request.getParameter("id"));
        List<Integer> userIds = new ArrayList<>();
        if(users != null && !users.trim().equals("")){
            String[] ss = users.split(",");
            try{
                for(String username : ss){
                    ScheduleUserPO scheduleUserPO = userDAO.selectByUsername(username);
                    if(scheduleUserPO == null){
                        throw new RuntimeException("用户["+username+"]不存在");
                    }
                    if(!userIds.contains(scheduleUserPO.getId())){
                        userIds.add(scheduleUserPO.getId());
                    }
                }
            }catch (Exception e){
                JSONResult jsonResult = new JSONResult();
                jsonResult.setMessage("管理人员设置错误,错误信息:" + e.getMessage());
                jsonResult.setCode(500);
                return jsonResult;
            }
        }
        return taskService.auth(sessionInfo.getId(), userIds, id);
    }

    @RequestMapping("/delete")
    @ResponseBody
    public JSONResult delete(HttpServletRequest request){
        SessionInfo sessionInfo = (SessionInfo)request.getSession().getAttribute("user");
        Integer id = Integer.parseInt(request.getParameter("id"));
        return taskService.delete(sessionInfo.getId(), id);
    }

}
