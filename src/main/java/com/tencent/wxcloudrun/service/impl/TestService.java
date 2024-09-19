package com.tencent.wxcloudrun.service.impl;

import com.tencent.wxcloudrun.model.FanModel;
import com.tencent.wxcloudrun.model.MealsModel;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author cq_wang
 * @description
 * @date 2024/9/13
 */

public class TestService {
    private static Pattern pattern = Pattern.compile("[^\\x00-\\xff]+（\\d*.\\d*）|[^\\x00-\\xff]+[\\s|&nbsp;]*[^\\x00-\\xff]+");
    public static void main(String[] args) throws IOException {



        write();

        String address = "https://mp.weixin.qq.com/s/bMcUFMUirv6UD0-tW8maRw";

        String content = httpGet(address,"第","上周美食回顾");
        if(content == null){
            return;
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





        System.out.println();
    }

    public static void write() throws IOException {
        // 创建一个新的工作簿
        Workbook workbook = new XSSFWorkbook();

        LocalDate currentDate = LocalDate.now();
        LocalDate firstDayOfWeek = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate lastDayOfWeek = firstDayOfWeek.plusDays(4);


        // 创建一个工作表
        Sheet sheet = workbook.createSheet(lastDayOfWeek.toString());

        // 按照行写入
        Row titleRow = sheet.createRow(0);
        titleRow.createCell(0);
        titleRow.getCell(0).setCellValue("序号");

        // 写入样式
        XSSFCellStyle cellStyle = (XSSFCellStyle) workbook.createCellStyle();
        cellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(89, 214, 255), new DefaultIndexedColorMap()));
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);  // 垂直居中
        cellStyle.setAlignment(HorizontalAlignment.CENTER); // 水平居中
        cellStyle.setBorderBottom(BorderStyle.THIN); // 下边框
        cellStyle.setBorderRight(BorderStyle.THIN);  // 右边框
        cellStyle.setBorderLeft(BorderStyle.THIN);   // 左边框
        cellStyle.setBorderTop(BorderStyle.THIN);    // 上边框
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setWrapText(true);
        sheet.setColumnWidth(0, 4200);  // 第0列列宽设置为4200
        // 字体
        Font titleFont = workbook.createFont();  // 声明字体对象
        titleFont.setBold(true);        // 字体加粗
        titleFont.setFontName("华文彩云");  // 字体类型
        titleFont.setFontHeightInPoints((short) 20);  // 字体大小
        cellStyle.setFont(titleFont);   // 赋值样式对象

        // sheet.addMergedRegion(new CellRangeAddress(1, 3, 1, 3));  // 参数分别为起始行，结束行，起始列，结束列

        FileOutputStream fileOutputStream = new FileOutputStream("王知文的餐食计划" + lastDayOfWeek.toString() + ".xlsx");
        workbook.write(fileOutputStream);
        fileOutputStream.close();
        workbook.close();
    }

    public static String httpGet(String address,String start, String end){
        String content = httpGet(address);
        if(content == null || content.length() == 0){
            return null;
        }

        int startIndex = content.indexOf(start);
        int endIndex = content.indexOf(end);
        return content.substring(startIndex, endIndex);
    }

    public static String httpGet(String address) {
        try {
            URL url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
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
}
