package com.ximalaya.ops.schedule.web.model;

import com.ximalaya.ops.schedule.web.model.po.ScheduleTaskPO;

import java.util.List;

/**
 * Created by nihao on 17/8/17.
 */
public class SessionInfo {
    private Integer id;
    private String username;
    private List<ScheduleTaskPO> tasks;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<ScheduleTaskPO> getTasks() {
        return tasks;
    }

    public void setTasks(List<ScheduleTaskPO> tasks) {
        this.tasks = tasks;
    }
}
