package com.tencent.wxcloudrun.service;

/**
 * @author cq_wang
 * @description
 * @date 2024/9/13
 */

public interface HttpService {
    String httpGet(String address);

    String httpGet(String address, String start, String end);
}
