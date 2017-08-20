package com.ximalaya.ops.schedule.service.zk;

import com.ximalaya.ops.schedule.service.dao.IScheduleDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by nihao on 17/8/15.
 */
//@Component
public class ActionHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private IScheduleDAO scheduleDAO;



}
