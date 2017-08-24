package com.ximalaya.ops.schedule.api;

import com.google.common.base.Preconditions;
import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by nihao on 17/8/16.
 */
public class TaskHandler {
    private static final Logger logger = LoggerFactory.getLogger(TaskHandler.class);

    private static final Timer timer = new Timer();
    private static final Map<String, Mission> taskMap = new ConcurrentHashMap<>();
    private static final Map<String, TimerTask> running = new ConcurrentHashMap<>();

    protected static void addAuth(CuratorFramework client) throws Exception{
        for(String group : taskMap.keySet()){
            client.getZookeeperClient().getZooKeeper().addAuthInfo("digest",taskMap.get(group).getKey().getBytes());
        }
    }

    protected static void addJob(String group, Mission mission) throws ScheduleException{
        if(taskMap.putIfAbsent(group, mission) != null){
            throw new ScheduleException("mission[" + group + "] is exists");
        }
        logger.info("add job group[" + group + "]");
    }

    protected static void updateJob(String group, TaskTypeEnum type, Long period, String time) throws ScheduleException{
        Mission mission = taskMap.get(group);
        if(group == null){
            throw new ScheduleException("mission[" + group + "] is not exists");
        }
        Preconditions.checkNotNull(type);
        mission.setType(type);
        if(type == TaskTypeEnum.fixed){
            Preconditions.checkNotNull(time);
            mission.setTime(time);
            mission.setPeriod(null);
        }
        else if(type == TaskTypeEnum.unfixed){
            Preconditions.checkNotNull(period);
            mission.setPeriod(period);
            mission.setTime(null);
        }
    }

    protected static boolean hasGroup(String group){
        return taskMap.containsKey(group);
    }

    protected static boolean isRunning(String group){
        return running.containsKey(group);
    }

    protected static void start(String group) throws ScheduleException{
        Mission mission = taskMap.get(group);
        if(mission == null){
            throw new ScheduleException("mission[" + group + "] is not exists");
        }
        synchronized (running){
            if(running.containsKey(group)){
                throw new ScheduleException("mission[" + group + "] is running");
            }
            String logStr = "";
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    try {
                        mission.execute();
                    }catch (Exception e){
                        logger.error("group[" + group + "]任务执行异常", e);
                    }
                }
            };
            if(mission.getType() == TaskTypeEnum.unfixed){
                timer.schedule(timerTask, mission.getPeriod(), mission.getPeriod());
                logStr = "this task runs every "
                        + mission.getPeriod()
                        + " milliseconds and will run for the first time in "
                        + mission.getPeriod() + " milliseconds.";
            }
            else if(mission.getType() == TaskTypeEnum.fixed){
                int hour,minute,second;
                try{
                    String[] ss = mission.getTime().split(":");
                    hour = Integer.parseInt(ss[0]);
                    minute = Integer.parseInt(ss[1]);
                    second = Integer.parseInt(ss[2]);
                    if(hour < 0 || hour >23 || minute < 0 || minute > 59 || second < 0 || second > 59 ){
                        throw new RuntimeException();
                    }
                }catch (Exception e){
                    throw new ScheduleException("property time configuration error");
                }
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, second);
                Date date = calendar.getTime();
                if(date.before(new Date())){
                    date = addDay(date, 1);
                }
                timer.scheduleAtFixedRate(timerTask, date, 24*60*60*1000);
                logStr = "this task will run at " + mission.getTime() + " every day.";
            }
            else{
                throw new ScheduleException("mission[" + group + "] type set error");
            }
            running.put(group, timerTask);
            logger.warn("mission [" + group + "] start success," + logStr);
        }
    }

    protected static void stop(String group) throws ScheduleException {
        Mission mission = taskMap.get(group);
        if(mission == null){
            throw new ScheduleException("mission[" + group + "] is not exists");
        }
        synchronized (running){
            if(running.containsKey(group)){
                running.get(group).cancel();
                running.remove(group);
                logger.warn("mission [" + group + "] stop success");
            }
            else{
                throw new ScheduleException("mission[" + group + "] is stopped");
            }
        }
    }

    public static Date addDay(Date date, int num) {
        Calendar startDT = Calendar.getInstance();
        startDT.setTime(date);
        startDT.add(Calendar.DAY_OF_MONTH, num);
        return startDT.getTime();
    }
}
