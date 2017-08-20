package com.ximalaya.ops.schedule.web.dao;

import com.ximalaya.ops.schedule.web.model.po.ScheduleUserPO;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by nihao on 17/8/17.
 */
public interface IUserDAO {
    int insert(ScheduleUserPO scheduleUserPO);
    ScheduleUserPO selectByUsernameAndPassword(@Param("username") String username,
                                               @Param("password") String password);
    ScheduleUserPO selectById(@Param("id") Integer id);
    ScheduleUserPO selectByUsername(@Param("username") String username);
    int update(ScheduleUserPO scheduleUserPO);
    @MapKey("id")
    Map<Integer,ScheduleUserPO> selectByIds(List<Integer> list);
    List<ScheduleUserPO> selectUsersByTaskId(@Param("taskId") Integer taskId);
}
