package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.dto.MealsRequest;
import com.tencent.wxcloudrun.service.impl.MealsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cq_wang
 * @description
 * @date 2024/9/18
 */

@RestController
public class MealsController {

    final MealsServiceImpl mealsService;
    final Logger logger;

    public MealsController(MealsServiceImpl mealsService) {
        this.mealsService = mealsService;
        this.logger = LoggerFactory.getLogger(MealsController.class);
    }

    @PostMapping(value = "/api/meals")
    ApiResponse create(@RequestBody MealsRequest request) {
        logger.info("/api/meals post request, action: {}", request.getGradeType());
        String file = mealsService.getMeals(request);
        return ApiResponse.ok(file);
    }
}
