package com.tencent.wxcloudrun.service.impl;

import com.tencent.wxcloudrun.service.DateService;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

/**
 * @author cq_wang
 * @description
 * @date 2024/9/18
 */
@Service
public class DateServiceImpl implements DateService {
    @Override
    public LocalDate getCommingFriday() {
        LocalDate currentDate = LocalDate.now();
        LocalDate firstDayOfWeek = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate lastDayOfWeek = firstDayOfWeek.plusDays(4);
        return lastDayOfWeek;
    }
}
