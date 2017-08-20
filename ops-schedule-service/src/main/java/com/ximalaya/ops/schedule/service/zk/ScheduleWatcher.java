package com.ximalaya.ops.schedule.service.zk;

import com.ximalaya.ops.schedule.service.dao.IScheduleDAO;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by nihao on 17/8/14.
 */
public class ScheduleWatcher implements Watcher {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private IScheduleDAO scheduleDAO;
    private ZooKeeper zooKeeper;

    public ScheduleWatcher(IScheduleDAO scheduleDAO, ZooKeeper zooKeeper) {
        this.scheduleDAO = scheduleDAO;
        this.zooKeeper = zooKeeper;
    }

    @Override
    public void process(WatchedEvent event) {
        logger.info("触发事件,路径:" + event.getPath() + "类型:" + event.getType());
        if(event.getType() == Event.EventType.None){

        }
        try {

        }catch (Exception e){

        }
    }
}
