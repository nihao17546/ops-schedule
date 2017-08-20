package com.ximalaya.ops.schedule.api;


/**
 * Created by nihao on 17/8/15.
 */
public class JobConfig {
    private Mission mission;
    private String group;
    private String key;

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
