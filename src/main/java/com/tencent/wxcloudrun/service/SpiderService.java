package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.model.MealsModel;

import java.util.List;

/**
 * @author cq_wang
 * @description
 * @date 2024/9/13
 */

public interface SpiderService {
    List<MealsModel> babyMeals(String address);
}
