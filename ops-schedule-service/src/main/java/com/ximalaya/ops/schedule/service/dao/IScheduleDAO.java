package com.ximalaya.ops.schedule.service.dao;

import com.ximalaya.ops.schedule.service.model.SchedulePO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by nihao on 17/8/14.
 */
public interface IScheduleDAO {
    SchedulePO selectById(@Param("id") Integer id);
    SchedulePO selectByGroup(@Param("group") String group);
    List<SchedulePO> selectAll();
}
