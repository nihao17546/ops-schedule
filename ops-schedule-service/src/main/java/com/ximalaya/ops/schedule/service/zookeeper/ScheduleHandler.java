package com.ximalaya.ops.schedule.service.zookeeper;

import com.ximalaya.ops.schedule.service.dao.IScheduleDAO;
import com.ximalaya.ops.schedule.service.model.Group;
import com.ximalaya.ops.schedule.service.model.SchedulePO;
import com.ximalaya.ops.schedule.service.quartz.QuartzJob;
import com.ximalaya.ops.schedule.service.quartz.QuartzManager;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
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
//    private final Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    @Resource
//    private IScheduleDAO scheduleDAO;
//
//    @Value("#{configProperties['zk.connection']}")
//    private String zk_connection;
//    @Value("#{configProperties['zk.timeout']}")
//    private Integer zk_timeout;
//    @Value("#{configProperties['zk.root.path']}")
//    private String zk_root_path;
//
//    private String zk_dispatch_path;
//    private String zk_task_path;
//
//    private CuratorFramework client;
//
//    private boolean isLeader;
//
//    @PostConstruct
//    public void init() throws Exception {
//        zk_dispatch_path = zk_root_path + "/dispatch";
//        zk_task_path = zk_root_path + "/task";
//
//        client = CuratorFrameworkFactory.newClient(zk_connection, new RetryNTimes(10, 5000));
//        client.start();
//        if(client.checkExists().forPath(zk_root_path) == null){
//            try{
//                client.create().withMode(CreateMode.PERSISTENT).forPath(zk_root_path);
//                logger.info("创建根节点[" + zk_root_path + "]");
//            }catch (Exception e){
//                logger.error("创建根节点错误,错误信息:" + e.getMessage());
//            }
//        }
//        else{
//            logger.info("根节点[" + zk_root_path + "]已存在");
//        }
//
//        if(client.checkExists().forPath(zk_dispatch_path) == null){
//            try{
//                client.create().withMode(CreateMode.PERSISTENT).forPath(zk_dispatch_path);
//                logger.info("创建调度节点[" + zk_dispatch_path + "]");
//            }catch (Exception e){
//                logger.error("创建调度节点错误,错误信息:" + e.getMessage());
//            }
//        }
//        else{
//            logger.info("调度节点[" + zk_dispatch_path + "]已存在");
//        }
//
//        if(client.checkExists().forPath(zk_task_path) == null){
//            try{
//                client.create().withMode(CreateMode.PERSISTENT).forPath(zk_task_path);
//                logger.info("创建任务节点[" + zk_task_path + "]");
//            }catch (Exception e){
//                logger.error("创建任务节点错误,错误信息:" + e.getMessage());
//            }
//        }
//        else{
//            logger.info("任务节点[" + zk_task_path + "]已存在");
//        }
//
//        LeaderLatch leaderLatch = new LeaderLatch(client, zk_dispatch_path);
//        leaderLatch.addListener(new LeaderLatchListener() {
//            @Override
//            public void isLeader() {
//                isLeader = true;
//                logger.info("I am leader.");
//            }
//            @Override
//            public void notLeader() {
//                isLeader = false;
//                logger.info("I am not leader.");
//            }
//        });
//        leaderLatch.start();
//
//        //监听
//        PathChildrenCache watcher = new PathChildrenCache(client, zk_task_path, false);
//        watcher.getListenable().addListener((client1, event) -> {
//            System.out.println("------");
////            if(isLeader){
////                ChildData data = event.getData();
////                if(data != null){
////                    if(event.getType() == PathChildrenCacheEvent.Type.CHILD_ADDED){
////                        byte[] bb = client.getData().forPath(data.getPath());
////                        if(bb != null){
////                            String value = new String(bb, "UTF-8");
////                            String[] vals = value.split("#");
////                            if(vals.length == 2){
////
////                            }
////                        }
////
////                        String value = "null";
////                        if(bb != null){
////                            value = new String(bb, "UTF-8");
////                        }
////                        logger.info("创建子节点["+data.getPath()+"],数据["+value+"]");
////                    }
////                    else if(event.getType() == PathChildrenCacheEvent.Type.CHILD_REMOVED){
////                        logger.info("删除子节点["+data.getPath()+"]");
////                    }
////                    else if(event.getType() == PathChildrenCacheEvent.Type.CHILD_UPDATED){
////                        byte[] bb = client.getData().forPath(data.getPath());
////                        String value = "null";
////                        if(bb != null){
////                            value = new String(bb, "UTF-8");
////                        }
////                        logger.info("更新子节点["+data.getPath()+"],更新后结果["+value+"]");
////                    }
////                }
////            }
//        });
//        watcher.start();
//
//        List<SchedulePO> schedulePOList = scheduleDAO.selectAll();
//        for(SchedulePO schedulePO : schedulePOList){
//            Group.schedule.put(schedulePO.getGroup(), schedulePO);
//            QuartzManager.addJob(schedulePO.getGroup(), QuartzJob.class, schedulePO.getCron());
//        }
//    }

}
