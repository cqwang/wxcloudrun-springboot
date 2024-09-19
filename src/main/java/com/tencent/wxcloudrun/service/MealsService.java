package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.dto.MealsRequest;

/**
 * @author cq_wang
 * @description
 * @date 2024/9/18
 */

public interface MealsService {
    public String getMeals(MealsRequest request);
}
