package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.dto.MealsRequest;
import com.tencent.wxcloudrun.model.MealsModel;

import java.util.List;

/**
 * @author cq_wang
 * @description
 * @date 2024/9/18
 */

public interface ExcelService {
    String generateMeals(MealsRequest request, List<MealsModel> mealsModelList);
}
