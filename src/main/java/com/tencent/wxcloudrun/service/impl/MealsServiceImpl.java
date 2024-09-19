package com.tencent.wxcloudrun.service.impl;

import com.tencent.wxcloudrun.dto.MealsRequest;
import com.tencent.wxcloudrun.model.MealsModel;
import com.tencent.wxcloudrun.service.DateService;
import com.tencent.wxcloudrun.service.ExcelService;
import com.tencent.wxcloudrun.service.MealsService;
import com.tencent.wxcloudrun.service.SpiderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author cq_wang
 * @description
 * @date 2024/9/18
 */

@Service
public class MealsServiceImpl implements MealsService {
    @Autowired
    private SpiderService spiderService;
    @Autowired
    private ExcelService excelService;

    @Override
    public String getMeals(MealsRequest request) {
        List<MealsModel> mealsModelList = spiderService.babyMeals(request.getAddress());
        String file = excelService.generateMeals(request, mealsModelList);
        return file;
    }
}
