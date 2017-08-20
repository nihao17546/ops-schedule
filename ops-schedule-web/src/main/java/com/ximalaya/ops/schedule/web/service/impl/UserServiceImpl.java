package com.ximalaya.ops.schedule.web.service.impl;

import com.google.common.base.Preconditions;
import com.ximalaya.ops.schedule.web.dao.ITaskDAO;
import com.ximalaya.ops.schedule.web.dao.IUserDAO;
import com.ximalaya.ops.schedule.web.model.SessionInfo;
import com.ximalaya.ops.schedule.web.model.po.ScheduleTaskPO;
import com.ximalaya.ops.schedule.web.model.po.ScheduleUserPO;
import com.ximalaya.ops.schedule.web.model.result.JSONResult;
import com.ximalaya.ops.schedule.web.service.IUserService;
import com.ximalaya.ops.schedule.web.util.StaticUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by nihao on 17/8/17.
 */
@Service
public class UserServiceImpl implements IUserService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private IUserDAO userDAO;
    @Resource
    private ITaskDAO taskDAO;


    @Override
    public JSONResult addUser(String username, String password) {
        Preconditions.checkNotNull(username);
        Preconditions.checkNotNull(password);
        JSONResult jsonResult = new JSONResult();
        ScheduleUserPO userPO = userDAO.selectByUsername(username);
        if(userPO != null){
            jsonResult.setCode(500);
            jsonResult.setMessage("账号已存在");
            return jsonResult;
        }
        ScheduleUserPO scheduleUserPO = new ScheduleUserPO();
        scheduleUserPO.setUsername(username);
        scheduleUserPO.setPassword(StaticUtil.md5(password));
        userDAO.insert(scheduleUserPO);
        jsonResult.setCode(200);
        jsonResult.setMessage("注册成功");
        return jsonResult;
    }

    @Override
    public JSONResult updateUser(Integer id, String password) {
        Preconditions.checkNotNull(id);
        Preconditions.checkNotNull(password);
        JSONResult jsonResult = new JSONResult();
        ScheduleUserPO scheduleUserPO = new ScheduleUserPO();
        scheduleUserPO.setId(id);
        scheduleUserPO.setPassword(StaticUtil.md5(password));
        userDAO.update(scheduleUserPO);
        jsonResult.setCode(200);
        jsonResult.setMessage("更新成功");
        return jsonResult;
    }

    @Override
    public JSONResult login(String username, String password) {
        Preconditions.checkNotNull(username);
        Preconditions.checkNotNull(password);
        JSONResult jsonResult = new JSONResult();
        ScheduleUserPO userPO = userDAO.selectByUsernameAndPassword(username, StaticUtil.md5(password));
        if(userPO == null){
            jsonResult.setCode(500);
            jsonResult.setMessage("账号或密码错误");
        }
        else{
            jsonResult.setCode(200);
            jsonResult.setMessage("登录成功");
            SessionInfo sessionInfo = new SessionInfo();
            sessionInfo.setId(userPO.getId());
            sessionInfo.setUsername(userPO.getUsername());
            List<ScheduleTaskPO> tasks = taskDAO.selectTaskByUserId(userPO.getId());
            sessionInfo.setTasks(tasks);
            jsonResult.setData(sessionInfo);
        }
        return jsonResult;
    }
}
