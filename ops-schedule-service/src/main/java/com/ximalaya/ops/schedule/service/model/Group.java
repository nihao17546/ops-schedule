package com.ximalaya.ops.schedule.service.model;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by nihao on 17/8/15.
 */
public class Group {
    public static final Map<String,SchedulePO> schedule = new ConcurrentHashMap();
}
