package com.ximalaya.ops.schedule.web.service;

import com.ximalaya.ops.schedule.web.model.result.JSONResult;

import java.util.List;

/**
 * Created by nihao on 17/8/17.
 */
public interface ITaskService {
    JSONResult add(String group, String key, String description, Integer type, Long period, String time, List<Integer> userIds, Integer userId);
    JSONResult edit(Integer id, String group, String key, String description, Integer type, Long period, String time,Integer userId);
    JSONResult auth(Integer userId, List<Integer> userIds, Integer id);
    JSONResult delete(Integer userId, Integer id);
    List<String> getChildren(String path) throws Exception;
    String getData(String path) throws Exception;
    void delete(String path) throws Exception;
    Integer count(String group);
}
