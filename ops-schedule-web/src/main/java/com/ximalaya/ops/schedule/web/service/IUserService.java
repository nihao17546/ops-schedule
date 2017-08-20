package com.ximalaya.ops.schedule.web.service;

import com.ximalaya.ops.schedule.web.model.result.JSONResult;


/**
 * Created by nihao on 17/8/17.
 */
public interface IUserService {
    JSONResult addUser(String username, String password);
    JSONResult updateUser(Integer id, String password);
    JSONResult login(String username, String password);
}
