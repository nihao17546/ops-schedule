package com.test;

import com.ximalaya.ops.schedule.service.model.SimpleDateUtil;
import com.ximalaya.ops.schedule.service.quartz.QuartzJob;
import com.ximalaya.ops.schedule.service.quartz.QuartzManager;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import org.junit.Test;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by nihao on 17/8/15.
 */
public class SimpleTest {
//    @Test
//    public void test01() throws Exception {
//        ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183",3000,null);
//        zooKeeper.create("/ops-schedule/java","哈哈".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
//        zooKeeper.create("/ops-schedule/java","哈哈".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
//        Thread.sleep(5000);
//    }
//    @Test
//    public void test02() throws Exception {
//        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient("127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183", new RetryNTimes(10, 5000));
//        curatorFramework.start();
//        curatorFramework.create().forPath("/test");
//    }
//    @Test
//    public void test03(){
//        try {
//            String job_name = "动态任务调度";
//            System.out.println("【系统启动】开始(每1秒输出一次)...");
//            QuartzManager.addJob(job_name, QuartzJob.class, "0/1 * * * * ?");
//
//            Thread.sleep(5000);
//            System.out.println("【修改时间】开始(每2秒输出一次)...");
//            QuartzManager.modifyJobTime(job_name, "10/2 * * * * ?");
//            Thread.sleep(6000);
//            System.out.println("【移除定时】开始...");
//            QuartzManager.removeJob(job_name);
//            System.out.println("【移除定时】成功");
//
//            System.out.println("【再次添加定时任务】开始(每10秒输出一次)...");
//            QuartzManager.addJob(job_name, QuartzJob.class, "*/10 * * * * ?");
//            Thread.sleep(60000);
//            System.out.println("【移除定时】开始...");
//            QuartzManager.removeJob(job_name);
//            System.out.println("【移除定时】成功");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    @Test
//    public void cdvavd(){
//        String s = "15:12:00";
//        Date d = SimpleDateUtil.parsePPP(s);
//        System.out.println("----");
//    }
//
//    @Test
//    public void ttttt() throws InterruptedException {
////        Timer timer = new Timer();
////        TimerTask task1 = new TimerTask() {
////            @Override
////            public void run() {
////                System.out.println(new Date() + " ----> 1111");
////            }
////        };
////        TimerTask task2 = new TimerTask() {
////            @Override
////            public void run() {
////                System.out.println(new Date() + " ----> 2222");
////                if(new Date().getTime()%3==0){
////                    System.out.println("任务2取消");
////                    System.out.println("取消结果1"+this.cancel());
////                    System.out.println("取消结果2"+this.cancel());
////                }
////            }
////        };
//////        timer.scheduleAtFixedRate(task1,0,2000);
//////        timer.scheduleAtFixedRate(task2,0,4000);
////        Date d = SimpleDateUtil.parse("2017-08-16 15:10:00");
////        timer.scheduleAtFixedRate(task1,d,10000);
////        System.out.println("------------");
////        System.out.println("task1.scheduledExecutionTime():"+task1.scheduledExecutionTime());
////        Thread.sleep(1000*100);
////    }
//
////    @Test
////    public void yyyy() throws Exception {
////        CuratorFramework  client = CuratorFrameworkFactory.builder()
////                .connectString("127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183")
////                .sessionTimeoutMs(5000)
////                .connectionTimeoutMs(5000)
////                .retryPolicy(new ExponentialBackoffRetry(1000, 600))
////                .build();
////        client.start();
////        PathChildrenCache watcher = new PathChildrenCache(client, "/ops-schedule", false);
////        watcher.getListenable().addListener((client1, event) -> {
////            System.out.println("type:"+event.getType().name());
////            if(event.getData() != null){
////                System.out.println("path:"+event.getData().getPath());
////                byte[] data = client.getData().forPath(event.getData().getPath());
////                if(data!=null){
////                    System.out.println("data:"+new String(data));
////                }
////            }
////            System.out.println("--------------------------");
////        });
////        watcher.start();
////        Thread.sleep(10000000000000000l);
////    }
//    @Test
//    public void dafewfewf() throws Exception{
//        CuratorFramework  client = CuratorFrameworkFactory.builder()
//                .connectString("127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183")
//                .sessionTimeoutMs(5000)
//                .connectionTimeoutMs(5000)
//                .retryPolicy(new ExponentialBackoffRetry(1000, 600))
//                .build();
//        client.start();
//        List<ACL> list = new ArrayList<>();
//        ACL acl = new ACL(ZooDefs.Perms.READ| ZooDefs.Perms.WRITE, new Id("digest", DigestAuthenticationProvider.generateDigest("jike:123456")));
//        list.add(acl);
//        client.create().withMode(CreateMode.EPHEMERAL).withACL(list).forPath("/testacl","aaa".getBytes());
//
//        client.setACL().withACL(list);
//        String s = new String(
//                client.getData().forPath("/testacl")
//        );
//        System.out.println(s);
//        Thread.sleep(10000000000000000l);
//    }
//    @Test
//    public void sadasdas() throws Exception{
////        ACLProvider aclProvider = new ACLProvider() {
////            private List<ACL> acl ;
////            @Override
////            public List<ACL> getDefaultAcl() {
////                if(acl ==null){
////                    ArrayList<ACL> acl = ZooDefs.Ids.CREATOR_ALL_ACL;
////                    acl.clear();
////                    acl.add(new ACL(ZooDefs.Perms.ALL, new Id("digest", "admin:admin") ));
////                    this.acl = acl;
////                }
////                return acl;
////            }
////            @Override
////            public List<ACL> getAclForPath(String path) {
////                return acl;
////            }
////        };
//        String schema = "digest";
//        String auth = "admin:admin";
//        String auth1 = "admin1:admin1";
//        CuratorFramework  client = CuratorFrameworkFactory.builder()
//                .authorization(schema, auth.getBytes())
//                .connectString("127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183")
//                .sessionTimeoutMs(5000)
//                .connectionTimeoutMs(5000)
//                .retryPolicy(new ExponentialBackoffRetry(1000, 600))
//                .build();
//        client.start();
////        List<ACL> list = new ArrayList<>();
////        ACL acl = new ACL(ZooDefs.Perms.ALL, new Id("digest", DigestAuthenticationProvider.generateDigest("admin:admin")));
////        list.add(acl);
////        client.create().withMode(CreateMode.PERSISTENT).withACL(list).forPath("/acl", "hhhh".getBytes());
////        client.getZookeeperClient().getZooKeeper().addAuthInfo(schema, auth.getBytes());
//        client.getZookeeperClient().getZooKeeper().addAuthInfo(schema, auth1.getBytes());
//        String s = new String(
//                client.getData().forPath("/acl")
//        );
//        System.out.println(s);
//        String s1 = new String(
//                client.getData().forPath("/acl1")
//        );
//        System.out.println(s1);
//    }
//    @Test
//    public void acl() throws Exception{
//        List<ACL> acls = new ArrayList<>();
//        //添加第一个id，采用用户名密码形式
//        Id id1 = new Id("digest",DigestAuthenticationProvider.generateDigest("admin:admin"));
//        ACL acl1 = new ACL(ZooDefs.Perms.ALL, id1);
//        acls.add(acl1);
//
////　　　　 添加第二个id，所有用户可读权限
//        Id id2 = new Id("world", "anyone");
//        ACL acl2 = new ACL(ZooDefs.Perms.READ, id2);
////        acls.add(acl2);
//
//        CuratorFramework  client = CuratorFrameworkFactory.builder()
//                .connectString("127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183")
//                .sessionTimeoutMs(5000)
//                .connectionTimeoutMs(5000)
//                .retryPolicy(new ExponentialBackoffRetry(1000, 600))
////                .authorization("digest","admin:admin".getBytes())
//                .build();
//        client.start();
////        client.create().withMode(CreateMode.PERSISTENT)
////                .forPath("/test/aa","aa".getBytes());
////        client.setACL().withACL(acls).forPath("/test");
//        PathChildrenCache watcher = new PathChildrenCache(
//                client,
//                "/test",
//                false    // if cache data
//        );
//        watcher.getListenable().addListener((client1, event) -> {
//            ChildData data = event.getData();
//            if (data == null) {
//                System.out.println("No data in event[" + event + "]");
//            } else {
//                System.out.println("Receive event: "
//                        + "type=[" + event.getType() + "]"
//                        + ", path=[" + data.getPath() + "]"
//                        + ", data=[" + new String(data.getData()) + "]"
//                        + ", stat=[" + data.getStat() + "]");
//            }
//        });
//        watcher.start();
//        Thread.sleep(100000000000000000l);
//    }
//    @Test
//    public void 正则(){
//        String pattern = "^[a-zA-Z0-9]{1,10}:{1}[a-zA-Z0-9]{1,10}$";
//        String s = "cqwcw:cvewew";
//        String s1 = "1:cwecewdwdwewe";
//        String s2 = "0jicio";
//        System.out.println(Pattern.matches(pattern, s));
//        System.out.println(Pattern.matches(pattern, s1));
//        System.out.println(Pattern.matches(pattern, s2));
//    }
//    @Test
//    public void dasdasdas() throws Exception {
//        CuratorFramework  client = CuratorFrameworkFactory.builder()
//                .connectString("127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183")
//                .sessionTimeoutMs(5000)
//                .connectionTimeoutMs(5000)
//                .retryPolicy(new ExponentialBackoffRetry(1000, 600))
////                .authorization("digest","admin:admin".getBytes())
//                .build();
//        client.start();
//        PathChildrenCache watcher = new PathChildrenCache(
//                client,
//                "/qqq",
//                false    // if cache data
//        );
//        watcher.getListenable().addListener((client1, event) -> {
//            ChildData data = event.getData();
//            if (data == null) {
//                System.out.println("No data in event[" + event + "]");
//            } else {
//                System.out.println("Receive event: "
//                        + "type=[" + event.getType() + "]"
//                        + ", path=[" + data.getPath() + "]"
//                        + ", stat=[" + data.getStat() + "]");
//            }
//        });
//        watcher.start();
//        Thread.sleep(100000000000000000l);
//    }
}
