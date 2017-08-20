package com.ximalaya.ops.schedule.service.zk;

import com.ximalaya.ops.schedule.service.dao.IScheduleDAO;
import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * Created by nihao on 17/8/14.
 */
//@Component
public class ScheduleHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("#{configProperties['zk.connection']}")
    private String zk_connection;
    @Value("#{configProperties['zk.timeout']}")
    private Integer zk_timeout;
    @Value("#{configProperties['zk.root.path']}")
    private String zk_root_path;

    private ZooKeeper zooKeeper;

    @PostConstruct
    public void init() throws Exception {
        zooKeeper = new ZooKeeper(zk_connection, zk_timeout, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                try {
                    zooKeeper.getChildren(zk_root_path, childrenWatcher);
                } catch (Exception e) {
                    logger.error("监听事件错误", e);
                }
            }
        });
        if(zooKeeper.exists(zk_root_path, false) == null){
            zooKeeper.create(zk_root_path, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            logger.info("创建根节点:" + zk_root_path);
        }
        else{
            logger.info("根节点:" + zk_root_path + " 已存在");
        }
    }

    Watcher childrenWatcher = new Watcher() {
        @Override
        public void process(WatchedEvent event) {
            logger.info("触发事件,路径:" + event.getPath() + "类型:" + event.getType());
            try {
                List<String> list = zooKeeper.getChildren(event.getPath(), true);
                for (String s : list){
                    byte[] bytes = zooKeeper.getData(event.getPath() + "/" + s, dataWatcher, null);
                    logger.info("数据变更为 :" + new String(bytes));
                }
            } catch (Exception e) {
                logger.error("监听事件错误", e);
            }
        }
    };
    Watcher dataWatcher = new Watcher() {
        @Override
        public void process(WatchedEvent event) {
            logger.info("触发事件,路径:" + event.getPath() + "类型:" + event.getType());
            try {
                byte[] bytes = zooKeeper.getData(event.getPath(), dataWatcher, null);
                logger.info("数据变更为 :" + new String(bytes));
            } catch (Exception e) {
                logger.error("监听事件错误", e);
            }
        }
    };
}
