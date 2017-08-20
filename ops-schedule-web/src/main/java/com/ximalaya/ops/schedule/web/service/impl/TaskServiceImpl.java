package com.ximalaya.ops.schedule.web.service.impl;

import com.google.common.base.Preconditions;
import com.ximalaya.ops.schedule.web.dao.ITaskDAO;
import com.ximalaya.ops.schedule.web.model.enums.TypeEnum;
import com.ximalaya.ops.schedule.web.model.po.ScheduleTaskPO;
import com.ximalaya.ops.schedule.web.model.po.ScheduleUser2TaskPO;
import com.ximalaya.ops.schedule.web.model.result.JSONResult;
import com.ximalaya.ops.schedule.web.model.zk.ScheduleList;
import com.ximalaya.ops.schedule.web.service.ITaskService;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nihao on 17/8/17.
 */
@Service
public class TaskServiceImpl implements ITaskService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private ITaskDAO taskDAO;

    @Value("#{configProperties['zk_connection']}")
    private String zk_connection;
    @Value("#{configProperties['session_timeout']}")
    private Integer session_timeout;
    @Value("#{configProperties['connection_timeout']}")
    private Integer connection_timeout;
    @Value("#{configProperties['zk_root_path']}")
    private String zk_root_path;
    @Value("#{configProperties['zk_dispatch_path']}")
    private String zk_dispatch_path;
    @Value("#{configProperties['zk_task_path']}")
    private String zk_task_path;
    @Value("#{configProperties['acl_admin_scheme']}")
    private String acl_admin_scheme;
    @Value("#{configProperties['acl_admin_id']}")
    private String acl_admin_id;

    private CuratorFramework client;
    private ACL anyoneACL = null;
    private ACL adminACL = null;

    @PostConstruct
    public void init() throws Exception{
        anyoneACL = new ACL(ZooDefs.Perms.READ, new Id("world", "anyone"));
        adminACL = new ACL(ZooDefs.Perms.ALL, new Id(acl_admin_scheme,
                DigestAuthenticationProvider.generateDigest(acl_admin_id)));
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 600);
        client = CuratorFrameworkFactory.builder()
                .connectString(zk_connection)
                .sessionTimeoutMs(session_timeout)
                .connectionTimeoutMs(connection_timeout)
                .retryPolicy(retryPolicy)
                .authorization(acl_admin_scheme, acl_admin_id.getBytes())
                .build();
        client.start();
        if(client.checkExists().forPath(zk_root_path) == null){
            client.create().withMode(CreateMode.PERSISTENT)
                    .withACL(new ScheduleList<ACL>().addR(anyoneACL).addR(adminACL))
                    .forPath(zk_root_path);
            logger.info("创建根节点[" + zk_root_path + "]");
        }
        else{
            client.setACL().withACL(new ScheduleList<ACL>().addR(anyoneACL).addR(adminACL))
                    .forPath(zk_root_path);
            logger.info("根节点[" + zk_root_path + "]已存在");
        }

        if(client.checkExists().forPath(zk_dispatch_path) == null){
            client.create().withMode(CreateMode.PERSISTENT)
                    .withACL(new ScheduleList<ACL>().addR(anyoneACL).addR(adminACL))
                    .forPath(zk_dispatch_path);
            logger.info("创建调度节点[" + zk_dispatch_path + "]");
        }
        else{
            client.setACL().withACL(new ScheduleList<ACL>().addR(anyoneACL).addR(adminACL))
                    .forPath(zk_dispatch_path);
            logger.info("调度节点[" + zk_dispatch_path + "]已存在");
        }

        if(client.checkExists().forPath(zk_task_path) == null){
            client.create().withMode(CreateMode.PERSISTENT)
                    .withACL(new ScheduleList<ACL>().addR(anyoneACL).addR(adminACL))
                    .forPath(zk_task_path);
            logger.info("创建任务节点[" + zk_task_path + "]");
        }
        else{
            client.setACL().withACL(new ScheduleList<ACL>().addR(anyoneACL).addR(adminACL))
                    .forPath(zk_task_path);
            logger.info("任务节点[" + zk_task_path + "]已存在");
        }
    }
    @PreDestroy
    public void destroy(){
        if(client != null)
            client.close();
    }

    @Transactional
    @Override
    public JSONResult add(String group, String key, String description,Integer type, Long period, String time, List<Integer> userIds, Integer userId) {
        JSONResult jsonResult = new JSONResult();
        Preconditions.checkNotNull(group);
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(description);
        Preconditions.checkNotNull(type);
        if(acl_admin_id.equals(key)){
            jsonResult.setCode(500);
            jsonResult.setMessage("不能设置该密钥");
            return jsonResult;
        }
        if(TypeEnum.周期任务.getType().equals(type)){
            Preconditions.checkNotNull(period);
        }
        else if(TypeEnum.固定时间任务.getType().equals(type)){
            Preconditions.checkNotNull(time);
        }
        ScheduleTaskPO scheduleTaskPO = taskDAO.selectByGroup(group);
        if(scheduleTaskPO != null){
            jsonResult.setCode(500);
            jsonResult.setMessage("任务组已经存在");
            return jsonResult;
        }
        else{
            scheduleTaskPO = new ScheduleTaskPO();
            scheduleTaskPO.setGroup(group);
            scheduleTaskPO.setKey(key);
            scheduleTaskPO.setDescription(description);
            scheduleTaskPO.setCreateUid(userId);
            scheduleTaskPO.setType(type);
            scheduleTaskPO.setPeriod(period);
            scheduleTaskPO.setTime(time);
            int i = taskDAO.insert(scheduleTaskPO);
            if(i == 1){
                if(userIds != null && !userIds.isEmpty()){
                    List<ScheduleUser2TaskPO> scheduleTaskPOList = new ArrayList<>();
                    for(Integer u_id : userIds){
                        if(u_id.equals(userId)){
                            continue;
                        }
                        ScheduleUser2TaskPO scheduleUser2TaskPO = new ScheduleUser2TaskPO();
                        scheduleUser2TaskPO.setUserId(u_id);
                        scheduleUser2TaskPO.setTaskId(scheduleTaskPO.getId());
                        scheduleTaskPOList.add(scheduleUser2TaskPO);
                    }
                    if(!scheduleTaskPOList.isEmpty())
                        taskDAO.batchInsertUser2Task(scheduleTaskPOList);
                }
                try {
                    String data = type.toString();
                    if(type == TypeEnum.周期任务.getType()){
                        data = data + "#" + period;
                    }
                    else if(type == TypeEnum.固定时间任务.getType()){
                        data = data + "#" + time;
                    }
                    client.create().withMode(CreateMode.PERSISTENT)
                            .withACL(new ScheduleList<ACL>()
                                    .addR(new ACL(ZooDefs.Perms.ALL,
                                            new Id("digest", DigestAuthenticationProvider.generateDigest(key))))
                                    .addR(adminACL).addR(anyoneACL))
                            .forPath(zk_dispatch_path + "/" + group, data.getBytes());
                    jsonResult.setCode(200);
                    jsonResult.setMessage("添加任务成功");
                    return jsonResult;
                } catch (Exception e) {
                    throw new RuntimeException("创建任务节点失败,错误信息:" + e.getMessage());
                }
            }
            else{
                throw new RuntimeException("添加失败");
            }
        }
    }

    @Transactional
    @Override
    public JSONResult edit(Integer id, String group, String key, String description, Integer type, Long period, String time,Integer userId) {
        JSONResult jsonResult = new JSONResult();
        Preconditions.checkNotNull(id);
        Preconditions.checkNotNull(group);
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(description);
        Preconditions.checkNotNull(userId);
        Preconditions.checkNotNull(type);
        if(acl_admin_id.equals(key)){
            jsonResult.setCode(500);
            jsonResult.setMessage("不能设置该密钥");
            return jsonResult;
        }
        String newData = type.toString();
        if(TypeEnum.周期任务.getType().equals(type)){
            Preconditions.checkNotNull(period);
            newData = newData + "#" + period;
        }
        else if(TypeEnum.固定时间任务.getType().equals(type)){
            Preconditions.checkNotNull(time);
            newData = newData + "#" + time;
        }
        ScheduleTaskPO oldPO = taskDAO.selectById(id);
        Preconditions.checkNotNull(oldPO);
        ScheduleTaskPO scheduleTaskPO = taskDAO.selectByGroup(group);
        if(scheduleTaskPO != null && !oldPO.getId().equals(scheduleTaskPO.getId())){
            jsonResult.setCode(500);
            jsonResult.setMessage("任务组已经存在");
            return jsonResult;
        }
        if(!key.equals(oldPO.getKey()) && group.equals(oldPO.getGroup()) && count(group) != 0){
            jsonResult.setCode(500);
            jsonResult.setMessage("group["+group+"]正在执行任务,不能更新密钥");
            return jsonResult;
        }
        ScheduleTaskPO newPO = new ScheduleTaskPO();
        newPO.setId(id);
        newPO.setGroup(group);
        newPO.setKey(key);
        newPO.setDescription(description);
        newPO.setUpdateUid(userId);
        newPO.setType(type);
        newPO.setPeriod(period);
        newPO.setTime(time);
        String oldData = oldPO.getType().toString();
        if(oldPO.getType() == TypeEnum.周期任务.getType()){
            oldData = oldData + "#" + oldPO.getPeriod();
        }
        else if(oldPO.getType() == TypeEnum.固定时间任务.getType()){
            oldData = oldData + "#" + oldPO.getTime();
        }
        int i = taskDAO.update(newPO);
        if(i == 1){
            if(!newPO.getGroup().equals(oldPO.getGroup())){
                try{
                    client.delete().deletingChildrenIfNeeded().forPath(zk_dispatch_path + "/" + oldPO.getGroup());
                    client.create().withMode(CreateMode.PERSISTENT)
                            .withACL(new ScheduleList<ACL>()
                                    .addR(new ACL(ZooDefs.Perms.ALL,
                                            new Id("digest", DigestAuthenticationProvider.generateDigest(newPO.getKey()))))
                                    .addR(adminACL).addR(anyoneACL))
                            .forPath(zk_dispatch_path + "/" + group, newData.getBytes());
                }catch (Exception e){
                    throw new RuntimeException("更新任务节点失败,错误信息:" + e.getMessage());
                }
            }
            else{
                if(!newData.equals(oldData)){
                    try{
                        client.setData().forPath(zk_dispatch_path + "/" + group, newData.getBytes());
                    }catch (Exception e){
                        throw new RuntimeException("更新任务节点执行类型失败,错误信息:" + e.getMessage());
                    }
                }
                if(!key.equals(oldPO.getKey())){
                    try{
                        client.setACL().withACL(new ScheduleList<ACL>()
                                .addR(new ACL(ZooDefs.Perms.ALL,
                                        new Id("digest", DigestAuthenticationProvider.generateDigest(newPO.getKey()))))
                                .addR(adminACL).addR(anyoneACL))
                                .forPath(zk_dispatch_path + "/" + group);
                    }catch (Exception e){
                        throw new RuntimeException("更新任务节点ACL失败,错误信息:" + e.getMessage());
                    }
                }
            }
            jsonResult.setCode(200);
            jsonResult.setMessage("编辑成功");
        }
        else{
            jsonResult.setCode(500);
            jsonResult.setMessage("编辑失败");
        }
        return jsonResult;
    }

    @Transactional
    @Override
    public JSONResult auth(Integer userId, List<Integer> userIds, Integer id) {
        JSONResult jsonResult = new JSONResult();
        Preconditions.checkNotNull(userId);
        Preconditions.checkNotNull(id);
        Preconditions.checkNotNull(userIds);
        ScheduleTaskPO scheduleTaskPO = taskDAO.selectById(id);
        Preconditions.checkNotNull(scheduleTaskPO);
        if(!scheduleTaskPO.getCreateUid().equals(userId)){
            jsonResult.setCode(500);
            jsonResult.setMessage("没有权限");
            return jsonResult;
        }
        taskDAO.deleteUser2Task(id);
        if(!userIds.isEmpty()){
            List<ScheduleUser2TaskPO> scheduleTaskPOList = new ArrayList<>();
            for(Integer u_id : userIds){
                if(u_id.equals(userId)){
                    continue;
                }
                ScheduleUser2TaskPO scheduleUser2TaskPO = new ScheduleUser2TaskPO();
                scheduleUser2TaskPO.setTaskId(scheduleTaskPO.getId());
                scheduleUser2TaskPO.setUserId(u_id);
                scheduleTaskPOList.add(scheduleUser2TaskPO);
            }
            if(!scheduleTaskPOList.isEmpty())
                taskDAO.batchInsertUser2Task(scheduleTaskPOList);
        }
        jsonResult.setCode(200);
        jsonResult.setMessage("授权成功");
        return jsonResult;
    }

    @Override
    public JSONResult delete(Integer userId, Integer id) {
        JSONResult jsonResult = new JSONResult();
        Preconditions.checkNotNull(userId);
        Preconditions.checkNotNull(id);
        ScheduleTaskPO scheduleTaskPO = taskDAO.selectById(id);
        Preconditions.checkNotNull(scheduleTaskPO);
        if(!scheduleTaskPO.getCreateUid().equals(userId)){
            jsonResult.setCode(500);
            jsonResult.setMessage("没有权限");
            return jsonResult;
        }
        taskDAO.deleteUser2Task(id);
        int i = taskDAO.delete(id, userId);
        if(i == 1){
            try{
                client.delete().deletingChildrenIfNeeded().forPath(zk_dispatch_path + "/" + scheduleTaskPO.getGroup());
                jsonResult.setCode(200);
                jsonResult.setMessage("删除成功");
            }catch (Exception e){
                throw new RuntimeException("删除任务节点失败,错误信息:" + e.getMessage());
            }
        }
        else{
            throw new RuntimeException("删除失败");
        }
        return jsonResult;
    }

    @Override
    public List<String> getChildren(String path) throws Exception {
        return client.getChildren().forPath(path);
    }

    @Override
    public String getData(String path) throws Exception {
        byte[] b = client.getData().forPath(path);
        if(b == null){
            return "[该节点没有数据]";
        }
        return new String(b, "UTF-8");
    }

    @Override
    public void delete(String path) throws Exception {
        client.delete().deletingChildrenIfNeeded().forPath(zk_dispatch_path + "/" + path);
    }

    @Override
    public Integer count(String group) {
        try {
            List<String> list = client.getChildren().forPath(zk_dispatch_path + "/" + group);
            return list.size();
        } catch (Exception e) {
            logger.error("client.getChildren() error ",e);
        }
        return -1;
    }
}
