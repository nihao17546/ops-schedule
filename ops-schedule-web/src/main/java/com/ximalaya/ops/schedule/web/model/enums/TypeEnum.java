package com.ximalaya.ops.schedule.web.model.enums;

/**
 * Created by nihao on 17/8/18.
 */
public enum TypeEnum {
    周期任务(1),
    固定时间任务(2);
    private Integer type;

    TypeEnum(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public static String getType(Integer t){
        for(TypeEnum typeEnum : TypeEnum.values()){
            if(typeEnum.getType() == t){
                return typeEnum.name();
            }
        }
        throw new RuntimeException("不存在该类型:" + t);
    }
}
