package com.tencent.wxcloudrun.service.impl;

import com.tencent.wxcloudrun.model.FanModel;
import com.tencent.wxcloudrun.model.MealsModel;
import com.tencent.wxcloudrun.service.HttpService;
import com.tencent.wxcloudrun.service.SpiderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author cq_wang
 * @description
 * @date 2024/9/13
 */
@Service
public class SpiderServiceImpl implements SpiderService {
    @Autowired
    private HttpService httpService;
    /**
     * 中文字符串
     */
    private static Pattern pattern = Pattern.compile("[^\\x00-\\xff]+（\\d*.\\d*）|[^\\x00-\\xff]+[\\s|&nbsp;]*[^\\x00-\\xff]+");

    /**
     * @param address 鹤栖路幼儿园 https://mp.weixin.qq.com/s/bMcUFMUirv6UD0-tW8maRw
     */

    @Override
    public List<MealsModel> babyMeals(String address) {
        String content = httpService.httpGet(address, "第","上周美食回顾");
        if (content == null) {
            return null;
        }

        List<MealsModel> mealsModelList = new ArrayList<>();
        MealsModel current = null;
        FanModel currentFan = null;
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            String text = matcher.group();
            if (text.startsWith("星期")) {
                current = new MealsModel(text);
                mealsModelList.add(current);
                continue;
            }

            if (current == null) {
                continue;
            }

            if (text.startsWith("早点")) {
                currentFan = current.getBreakfast();
                continue;
            } else if (text.startsWith("午餐")) {
                currentFan = current.getLunch();
                continue;
            } else if (text.startsWith("午点")) {
                currentFan = current.getNoon();
                continue;
            }

            if(currentFan == null){
                continue;
            }

            if (text.startsWith("托小班")) {
                currentFan.setLittleGradeMeals(text);
            } else if (text.startsWith("中大班")) {
                currentFan.setMiddleHighGradeMeals(text);
            }else if(text.contains("、")) {
                currentFan.setLittleGradeMeals(text);
                currentFan.setMiddleHighGradeMeals(text);
                current = null;
            }
        }
        return mealsModelList;
    }

}
