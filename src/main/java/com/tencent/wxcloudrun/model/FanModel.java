package com.tencent.wxcloudrun.model;

import com.tencent.wxcloudrun.config.Contants;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cq_wang
 * @description
 * @date 2024/9/13
 */

@Data
public class FanModel {
    private List<MealItem> littleGrade;
    private List<MealItem> middleHighGrade;

    public void setLittleGradeMeals(String text) {
        this.littleGrade = build(text);
    }

    public void setMiddleHighGradeMeals(String text) {
        this.middleHighGrade = build(text);
    }

    private List<MealItem> build(String text) {
        String[] meals = text.split("[：、]");

        int index = text.contains("：") ? 1 : 0;
        List<MealItem> mealItemList = new ArrayList<>();
        for (int i = index; i < meals.length; i++) {
            String item = meals[i].trim().replace(" ","").replace("&nbsp;", "");
            if (item.trim().length() == 0) {
                continue;
            }
            boolean disableEat = Contants.pattern.matcher(item).find();
            MealItem mealItem = new MealItem(item, !disableEat);
            mealItemList.add(mealItem);
        }
        return mealItemList;
    }

    public List<MealItem> getMealItems(boolean isLittleGrade) {
        return isLittleGrade ? littleGrade : middleHighGrade;
    }

    public String selectEatAbleMeals(boolean isLittleGrade) {
        List<MealItem> mealItemList = getMealItems(isLittleGrade);
        if (CollectionUtils.isEmpty(mealItemList)) {
            return "无";
        }

        String meals = mealItemList.stream()
                .filter(t -> t.isEatAble())
                .map(t -> t.getName())
                .collect(Collectors.joining(","));
        return StringUtil.isBlank(meals) ? "无" : meals;
    }
}
