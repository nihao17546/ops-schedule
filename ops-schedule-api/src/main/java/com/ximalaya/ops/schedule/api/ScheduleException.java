package com.ximalaya.ops.schedule.api;

/**
 * Created by nihao on 17/8/16.
 */
public class ScheduleException extends Exception {
    public ScheduleException() {
        super();
    }

    public ScheduleException(String msg) {
        super(msg);
    }

    public ScheduleException(Throwable e) {
        super(e);
    }
}
