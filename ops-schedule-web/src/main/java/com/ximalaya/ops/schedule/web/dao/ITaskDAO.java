package com.ximalaya.ops.schedule.web.dao;

import com.ximalaya.ops.schedule.web.model.po.ScheduleTaskPO;
import com.ximalaya.ops.schedule.web.model.po.ScheduleUser2TaskPO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by nihao on 17/8/17.
 */
public interface ITaskDAO {
    int insert(ScheduleTaskPO scheduleTaskPO);
    ScheduleTaskPO selectByGroup(@Param("group") String group);
    List<ScheduleTaskPO> selectTaskByUserId(@Param("userId") Integer userId);
    List<ScheduleTaskPO> selectByGroups(List<String> list);
    int batchInsertUser2Task(List<ScheduleUser2TaskPO> list);
    ScheduleTaskPO selectById(@Param("id") Integer id);
    int update(ScheduleTaskPO scheduleTaskPO);
    int delete(@Param("id") Integer id,@Param("createUid") Integer createUid);
    int deleteUser2Task(@Param("taskId") Integer taskId);
}
