package com.tencent.wxcloudrun.service.impl;

import com.tencent.wxcloudrun.dto.MealsRequest;
import com.tencent.wxcloudrun.model.MealsModel;
import com.tencent.wxcloudrun.service.DateService;
import com.tencent.wxcloudrun.service.ExcelService;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.StringUtil;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.util.List;

/**
 * @author cq_wang
 * @description
 * @date 2024/9/18
 */
@Service
public class ExcelServiceImpl implements ExcelService {
    @Autowired
    private DateService dateService;

    @SneakyThrows
    @Override
    public String generateMeals(MealsRequest request, List<MealsModel> mealsModelList) {
        // 创建一个新的工作簿
        Workbook workbook = new XSSFWorkbook();
        // 创建一个工作表
        Sheet sheet = workbook.createSheet(request.getGradeType());

        // 第一行标题
        Row titleRow = sheet.createRow(0);
        String[] rowTitles = new String[] {"日期", "可食用早点", "可食用午餐", "可食用午点", "午餐计划"};
        for (int index = 0; index < rowTitles.length; index++) {
            titleRow.createCell(index, CellType.STRING);
            titleRow.getCell(index).setCellValue(rowTitles[index]);
        }

        // 按照行写入
        for (int i = 0; i < mealsModelList.size(); i++) {
            Row contentRow = sheet.createRow(i + 1);
            MealsModel mealsModel = mealsModelList.get(i);

            boolean goBack = mealsModel.returnHomeForLunch(request.isLittleGrade());

            contentRow.createCell(0, CellType.STRING);
            contentRow.getCell(0).setCellValue(mealsModel.getDate());

            contentRow.createCell(1, CellType.STRING);
            contentRow.getCell(1).setCellValue(mealsModel.getBreakfast().selectEatAbleMeals(request.isLittleGrade()));
            contentRow.createCell(2, CellType.STRING);
            contentRow.getCell(2).setCellValue(mealsModel.getLunch().selectEatAbleMeals(request.isLittleGrade()));
            contentRow.createCell(3, CellType.STRING);
            contentRow.getCell(3).setCellValue(mealsModel.getNoon().selectEatAbleMeals(request.isLittleGrade()));
            contentRow.createCell(4, CellType.STRING);

            contentRow.getCell(4).setCellValue(goBack ? "回家" : "在校");
            String fontName = goBack ? "华文彩云" : null;
            contentRow.getCell(4).setCellStyle(buildCellStyle(workbook, buildFont(workbook, fontName)));
        }

        sheet.setColumnWidth(0, 4200);  // 第0列列宽设置为4200
        sheet.setColumnWidth(1, 4200);
        sheet.setColumnWidth(2, 12000);
        sheet.setColumnWidth(3, 12000);
        sheet.setColumnWidth(4, 4200);

        // sheet.addMergedRegion(new CellRangeAddress(1, 3, 1, 3));  // 参数分别为起始行，结束行，起始列，结束列


        String fileName = "王知文的餐食计划" + dateService.getCommingFriday().toString() + ".xlsx";
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        workbook.write(fileOutputStream);
        fileOutputStream.close();
        workbook.close();
        return fileName;
    }

    private XSSFCellStyle buildCellStyle(Workbook workbook, Font font) {
        // 写入样式
        XSSFCellStyle cellStyle = (XSSFCellStyle) workbook.createCellStyle();
        //
        // cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);  // 垂直居中
        // cellStyle.setAlignment(HorizontalAlignment.CENTER); // 水平居中
        // cellStyle.setBorderBottom(BorderStyle.THIN); // 下边框
        // cellStyle.setBorderRight(BorderStyle.THIN);  // 右边框
        // cellStyle.setBorderLeft(BorderStyle.THIN);   // 左边框
        // cellStyle.setBorderTop(BorderStyle.THIN);    // 上边框
        // cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        // cellStyle.setWrapText(true);
        cellStyle.setFont(font);   // 赋值样式对象
        return cellStyle;
    }

    private Font buildFont(Workbook workbook, String fontName) {
        // 字体
        Font font = workbook.createFont();  // 声明字体对象
        font.setBold(true);        // 字体加粗
        if (StringUtil.isNotBlank(fontName)) {
            font.setFontName(fontName);  // 字体类型
            font.setColor(IndexedColors.RED.getIndex());
        }
        font.setFontHeightInPoints((short) 20);  // 字体大小
        return font;
    }
}
