package com.ximalaya.ops.schedule.web.model.zk;

import java.util.ArrayList;

/**
 * Created by nihao on 17/8/19.
 */
public class ScheduleList<E> extends ArrayList<E> {
    public ScheduleList<E> addR(E e){
        add(e);
        return this;
    }
}
