package com.ximalaya.ops.schedule.api;

import com.google.common.base.Preconditions;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.net.InetAddress;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by nihao on 17/8/14.
 */
public class ScheduleHandler implements InitializingBean, DisposableBean{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String zk_connection;
    private List<JobConfig> configs;
    private Integer session_timeout;
    private Integer connection_timeout;

    public String getZk_connection() {
        return zk_connection;
    }

    public void setZk_connection(String zk_connection) {
        this.zk_connection = zk_connection;
    }

    public List<JobConfig> getConfigs() {
        return configs;
    }

    public void setConfigs(List<JobConfig> configs) {
        this.configs = configs;
    }

    public Integer getSession_timeout() {
        return session_timeout;
    }

    public void setSession_timeout(Integer session_timeout) {
        this.session_timeout = session_timeout;
    }

    public Integer getConnection_timeout() {
        return connection_timeout;
    }

    public void setConnection_timeout(Integer connection_timeout) {
        this.connection_timeout = connection_timeout;
    }

    private final String zk_dispatch_path = "/ops-schedule/dispatch";
    private final String zk_task_path = "/ops-schedule/task";
    private String IP;

    private CuratorFramework client;

    @Override
    public void afterPropertiesSet() throws Exception {
        IP = InetAddress.getLocalHost().getHostAddress().toString();
        Preconditions.checkNotNull(zk_connection);
        Preconditions.checkNotNull(configs);
        Preconditions.checkNotNull(session_timeout);
        // 重试策略：初试时间为1s 重试50次
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 50);
        client = CuratorFrameworkFactory.builder()
                .connectString(zk_connection)
                .sessionTimeoutMs(session_timeout)
                .connectionTimeoutMs(connection_timeout)
                .retryPolicy(retryPolicy)
                .build();
        client.start();
        logger.info("zookeeper client started");

        String patternKey = "^[a-zA-Z0-9]{1,10}:{1}[a-zA-Z0-9]{1,10}$";
        for(JobConfig jobConfig : configs){
            Preconditions.checkNotNull(jobConfig.getKey());
            Preconditions.checkNotNull(jobConfig.getMission());
            Preconditions.checkNotNull(jobConfig.getGroup());
            if(!Pattern.matches(patternKey, jobConfig.getKey())){
                throw new ScheduleException("group["+jobConfig.getGroup()+"] key 格式错误");
            }
            client.getZookeeperClient().getZooKeeper().addAuthInfo("digest", jobConfig.getKey().getBytes());
            String groupPath = zk_dispatch_path + "/" + jobConfig.getGroup();

            TaskHandler.addJob(groupPath, jobConfig.getMission());
        }

        //监听
        PathChildrenCache watcher = new PathChildrenCache(client, zk_dispatch_path, false);
        watcher.getListenable().addListener((client1, event) -> {
            ChildData data = event.getData();
            if (data != null) {
                if (TaskHandler.hasGroup(data.getPath())) {
                    if (event.getType() == PathChildrenCacheEvent.Type.CHILD_REMOVED) {
                        if (TaskHandler.isRunning(data.getPath())) {
                            logger.warn("节点[" + data.getPath() + "]删除,终止任务");
                            TaskHandler.stop(data.getPath());
                        }
                    } else if (event.getType() == PathChildrenCacheEvent.Type.CHILD_ADDED) {
                        setJob(data);
                        LeaderLatch leaderLatch = new LeaderLatch(client, data.getPath(), IP);
                        leaderLatch.addListener(new LeaderLatchListener() {
                            @Override
                            public void isLeader() {
                                logger.warn("I became the executor of this mission[" + data.getPath() + "]");
                                try {
                                    if (!TaskHandler.isRunning(data.getPath())) {
                                        TaskHandler.start(data.getPath());
                                    }
                                } catch (ScheduleException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void notLeader() {
                                logger.warn("I am no longer the executor of this mission[" + data.getPath() + "]");
                                try {
                                    if (TaskHandler.isRunning(data.getPath())) {
                                        TaskHandler.stop(data.getPath());
                                    }
                                } catch (ScheduleException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        leaderLatch.start();
                    } else if (event.getType() == PathChildrenCacheEvent.Type.CHILD_UPDATED) {
                        logger.warn("更新任务[" + data.getPath() + "]");
                        setJob(data);
                        if (TaskHandler.isRunning(data.getPath())) {
                            TaskHandler.stop(data.getPath());
                            TaskHandler.start(data.getPath());
                        }
                    }
                }
            }
        });
        watcher.start();
        logger.info("Distributed task started");
    }

    @Override
    public void destroy() throws Exception {
        client.close();
    }

    private void setJob(ChildData data) throws ScheduleException{
        try{
            byte[] bytes = client.getData().forPath(data.getPath());
            String newData = new String(bytes, "UTF-8");
            String[] datas = newData.split("#");
            Integer type = Integer.parseInt(datas[0]);
            if(type == 1){//周期
                TaskHandler.updateJob(data.getPath(),TaskTypeEnum.unfixed,Long.parseLong(datas[1]),null);
            }
            else if(type == 2){//时间
                TaskHandler.updateJob(data.getPath(),TaskTypeEnum.fixed,null,datas[1]);
            }
            else{
                throw new ScheduleException("type set error");
            }
        }catch (Exception e){
            throw new ScheduleException("group["+data.getPath()+"] 任务设置失败,错误信息:" + e.getClass().getName() + " " + e.getMessage());
        }
    }
}
