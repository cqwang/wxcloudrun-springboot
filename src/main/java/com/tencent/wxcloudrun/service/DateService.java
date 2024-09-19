package com.tencent.wxcloudrun.service;

import java.time.LocalDate;

/**
 * @author cq_wang
 * @description
 * @date 2024/9/18
 */

public interface DateService {
    /**
     * 获取即将到来的第一个周五
     * @return
     */
    public LocalDate getCommingFriday();
}
