package com.tencent.wxcloudrun.dto;

import lombok.Data;

import java.beans.Transient;

/**
 * @author cq_wang
 * @description
 * @date 2024/9/18
 */

@Data
public class MealsRequest {
    /**
     * 鹤栖路幼儿园 https://mp.weixin.qq.com/s/bMcUFMUirv6UD0-tW8maRw
     */
    private String address;
    /**
     * 托小班 中大班
     */
    private String gradeType;

    @Transient
    public boolean isLittleGrade(){
        return "托小班".equalsIgnoreCase(gradeType);
    }
}
