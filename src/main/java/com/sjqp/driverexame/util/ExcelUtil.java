package com.sjqp.driverexame.util;

import org.apache.poi.hssf.usermodel.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ExcelUtil {
    private ExcelUtil() {
    }

    /**
     * 获得excel模型
     *
     * @param excelParam
     * @param response
     * @return
     */
    public static HSSFWorkbook getExcelModel(ExcelParam excelParam, HttpServletResponse response) {
        {
            if (excelParam.widths == null) {
                excelParam.widths = new int[excelParam.headers.length];

                for (int i = 0; i < excelParam.headers.length; i++) {
                    excelParam.widths[i] = excelParam.width;
                }
            }
            if (excelParam.ds_format == null) {
                excelParam.ds_format = new int[excelParam.headers.length];
                for (int i = 0; i < excelParam.headers.length; i++) {
                    excelParam.ds_format[i] = 1;
                }
            }
            //创建一个工作薄
            HSSFWorkbook wb = new HSSFWorkbook();
            //创建一个sheet
            HSSFSheet sheet = wb.createSheet("excel");
            int rowCount = 0;
            if (excelParam.headers != null) {
                HSSFRow row = sheet.createRow(rowCount);
                //表头样式
                HSSFCellStyle style = wb.createCellStyle();
                HSSFFont font = wb.createFont();
                font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                font.setFontHeightInPoints((short) 11);
                style.setFont(font);
                style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

                for (int i = 0; i < excelParam.headers.length; i++) {
                    sheet.setColumnWidth(i, excelParam.widths[i]);
                    HSSFCell cell = row.createCell(i);
                    cell.setCellValue(excelParam.headers[i]);
                    cell.setCellStyle(style);
                }
                rowCount++;
            }
            HSSFCellStyle style = wb.createCellStyle();
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            //表格主体  解析list
            for (int i = 0; i < excelParam.data.size(); i++) {  //行数
                HSSFRow row = sheet.createRow(rowCount);
                for (int j = 0; j < excelParam.headers.length; j++) {  //列数
                    HSSFCell cell = row.createCell(j);
                    cell.setCellValue(excelParam.data.get(i).get(j).toString());
                    cell.setCellStyle(style);
                }
                rowCount++;
            }
            //设置文件名
            String fileName = excelParam.name + ".xls";
            if (response != null) {

                response.setContentType("application/vnd.ms-excel");
                try {
                    response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                response.setHeader("Pragma", "No-cache");
            }

            return wb;
        }
    }


    /**
     * 导出excel
     *
     * @param excelParam
     * @param response
     */
    public static void export(ExcelParam excelParam, HttpServletResponse response) {
        HSSFWorkbook wb = getExcelModel(excelParam, response);
        OutputStream outputStream = null;
        //设置文件名
        try {
            String fileName = excelParam.name + ".xls";
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setHeader("Pragma", "No-cache");
            outputStream = response.getOutputStream();
            wb.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
