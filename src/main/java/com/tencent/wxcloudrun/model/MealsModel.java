package com.tencent.wxcloudrun.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cq_wang
 * @description
 * @date 2024/9/13
 */

@Data
public class MealsModel {
    private String date;
    private FanModel breakfast;
    private FanModel lunch;
    private FanModel noon;

    public MealsModel(String date) {
        this.date = date;
        this.breakfast = new FanModel();
        this.lunch = new FanModel();
        this.noon = new FanModel();
    }

    public boolean returnHomeForLunch(boolean isLittleGrade) {
        List<MealItem> mealItemList = lunch.getMealItems(isLittleGrade);
        for (MealItem mealItem : mealItemList) {
            if (!mealItem.isEatAble()) {
                return true;
            }
        }
        return false;
    }
}
