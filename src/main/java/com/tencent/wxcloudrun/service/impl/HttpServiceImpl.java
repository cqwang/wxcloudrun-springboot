package com.tencent.wxcloudrun.service.impl;

import com.tencent.wxcloudrun.service.HttpService;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * @author cq_wang
 * @description
 * @date 2024/9/13
 */
@Service
public class HttpServiceImpl implements HttpService {

    @Override
    public String httpGet(String address) {
        try {
            URL url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(5000);
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null) {
                    response.append(line);
                }
                bufferedReader.close();
                return response.toString();
            }
        } catch (MalformedURLException e) {
            return e.getStackTrace().toString();
        } catch (ProtocolException e) {
            return e.getStackTrace().toString();
        } catch (IOException e) {
            return e.getStackTrace().toString();
        }
        return null;
    }

    @Override
    public String httpGet(String address, String start, String end) {
        String content = httpGet(address);
        if(content == null || content.length() == 0){
            return null;
        }

        int startIndex = content.indexOf(start);
        int endIndex = content.indexOf(end);
        return content.substring(startIndex, endIndex);
    }
}
