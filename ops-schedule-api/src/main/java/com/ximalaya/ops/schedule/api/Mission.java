package com.ximalaya.ops.schedule.api;

/**
 * Created by nihao on 17/8/16.
 */
public abstract class Mission {
    private TaskTypeEnum type;
    private Long period;
    private String time;
    private String key;

    public abstract void execute();

    public TaskTypeEnum getType() {
        return type;
    }

    public void setType(TaskTypeEnum type) {
        this.type = type;
    }

    public Long getPeriod() {
        return period;
    }

    public void setPeriod(Long period) {
        this.period = period;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
