package com.tencent.wxcloudrun.model;

import lombok.Data;

/**
 * @author cq_wang
 * @description
 * @date 2024/9/13
 */

@Data
public class MealItem {
    private String name;
    private boolean eatAble;

    public MealItem(String name, boolean eatAble) {
        this.name = name;
        this.eatAble = eatAble;
    }
}
