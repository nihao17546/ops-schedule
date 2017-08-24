package com.ximalaya.ops.schedule.api;

import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by nihao on 17/8/23.
 */
public class ScheduleLeaderLatchListener implements LeaderLatchListener {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String path;

    public ScheduleLeaderLatchListener(String path) {
        this.path = path;
    }

    @Override
    public void isLeader() {
        logger.warn("I became the executor of this mission[" + path + "]");
        try {
            if (!TaskHandler.isRunning(path)) {
                TaskHandler.start(path);
            }
        } catch (ScheduleException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notLeader() {
        logger.warn("I am no longer the executor of this mission[" + path + "]");
        try {
            if (TaskHandler.isRunning(path)) {
                TaskHandler.stop(path);
            }
        } catch (ScheduleException e) {
            e.printStackTrace();
        }
    }
}
