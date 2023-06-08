package com.rz.manuscript.common;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.formula.functions.T;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.builder.ExcelReaderSheetBuilder;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;

/**
 * 阿里巴巴easyexcel 简单封装工具类
 *
 * @author Terry
 * @date 2020年3月26日
 */
public class EasyExcelUtil {

    /**
     * 使用阿里巴巴easyexcel 获取excel sheet名和sheetNo
     *
     * @param filePath
     * @return
     */
    public static Map<String, Integer> getExcelSheets(String filePath) {
        List<ReadSheet> sheetList = EasyExcel.read(filePath).build().excelExecutor().sheetList();
        if (null != sheetList && sheetList.size() > 0) {
            Map<String, Integer> sheetMap = new HashMap<String, Integer>();
            int i = 0;
            for (ReadSheet readSheet : sheetList) {
                sheetMap.put(readSheet.getSheetName(), i);
                i++;
            }
            return sheetMap;
        } else {
            return null;
        }
    }

    /**
     * 使用阿里巴巴easyexcel 获取excel sheet名和sheetNo
     *
     * @param inputStream
     * @return
     */
    public static Map<String, Integer> getExcelSheets(InputStream inputStream) {
        List<ReadSheet> sheetList = EasyExcel.read(inputStream).build().excelExecutor().sheetList();
        if (null != sheetList && sheetList.size() > 0) {
            Map<String, Integer> sheetMap = new HashMap<String, Integer>();
            int i = 0;
            for (ReadSheet readSheet : sheetList) {
                sheetMap.put(readSheet.getSheetName(), i);
                i++;
            }
            return sheetMap;
        } else {
            return null;
        }
    }

    /**
     * 使用阿里巴巴easyexcel 获取excel sheet名和sheetNo
     *
     * @param file
     * @return
     */
    public static Map<String, Integer> getExcelSheets(File file) {
        List<ReadSheet> sheetList = EasyExcel.read(file).build().excelExecutor().sheetList();
        if (null != sheetList && sheetList.size() > 0) {
            Map<String, Integer> sheetMap = new HashMap<String, Integer>();
            int i = 0;
            for (ReadSheet readSheet : sheetList) {
                sheetMap.put(readSheet.getSheetName(), i);
                i++;
            }
            return sheetMap;
        } else {
            return null;
        }
    }

    /**
     * 使用阿里巴巴easyexcel 按页面读取整个excel 的数据 注意:数据量不是很大可以使用 默认从第二行读取数据
     *
     * @param filePath
     *            文件的路径
     * @param sheetNo
     * @return
     */
    public static List<Map<Integer, String>> read(String filePath, Integer sheetNo) {
        return EasyExcel.read(filePath).sheet(sheetNo).doReadSync();
    }

    /**
     *
     * 使用阿里巴巴easyexcel 按页面读取整个excel 的数据 注意:数据量不是很大可以使用
     *
     * @param filePath
     *            文件的路径
     * @param sheetNo
     * @param headerNo
     *            根据实际设定数据从那行读取，如果是-1标识从0行开始读取
     * @return
     */
    public static List<Map<Integer, String>> read(String filePath, Integer sheetNo, Integer headerNo) {
        return EasyExcel.read(filePath).sheet(sheetNo).headRowNumber(headerNo).doReadSync();
    }

    /**
     * 使用阿里巴巴easyexcel 按页面读取整个excel 的数据 注意:数据量不是很大可以使用
     *
     * @param file
     *            文件
     * @param sheetNo
     * @param headerNo
     *            根据实际设定数据从第几行开始读取默认是1，如果是-1标识从0行开始读取
     * @return
     */
    public static List<Map<Integer, String>> read(File file, Integer sheetNo, Integer headerNo) {
        return EasyExcel.read(file).sheet(sheetNo).headRowNumber(headerNo).doReadSync();
    }

    /**
     * 使用阿里巴巴easyexcel 按页面读取整个excel 的数据 注意:数据量不是很大可以使用
     *
     * @param inputStream
     *            输入流
     * @param sheetNo
     * @param headerNo
     *            根据实际设定数据从第几行开始读取默认是1，如果是-1标识从0行开始读取
     * @return
     */
    public static List<Map<Integer, String>> read(InputStream inputStream, Integer sheetNo, Integer headerNo) {
        return EasyExcel.read(inputStream).sheet(sheetNo).headRowNumber(headerNo).doReadSync();
    }

    /**
     * 以模板的方式输出到指定路径
     *
     * @param outPath
     *            输出路径
     * @param templateFileName
     *            模板路径
     * @param data
     */
    public static void fill(String outPath, String templateFileName, Object data) {
        EasyExcel.write(outPath).withTemplate(templateFileName).sheet().doFill(data);
    }

    public static void write(String outPath, String templateFileName, Class dataClass, List dataList) {
        EasyExcel.write(outPath, dataClass).withTemplate(templateFileName).sheet().doWrite(dataList);
    }

    /**
     * 以模板的方式输出到指定路径
     *
     * @param outPath
     *            输出路径
     * @param templateFileName
     *            模板路径
     * @param data
     */
    public static void fill(File outPath, String templateFileName, Object data) {
        EasyExcel.write(outPath).withTemplate(templateFileName).sheet().doFill(data);
    }

    /**
     * 以模板的方式输出到指定路径
     *
     * @param outputStream
     *            输出路径
     * @param templateFileName
     *            模板路径
     * @param data
     */
    public static void fill(OutputStream outputStream, String templateFileName, Object data) {
        EasyExcel.write(outputStream).withTemplate(templateFileName).sheet().doFill(data);
    }

    /**
     * 以模板的方式输出到指定路径
     *
     * @param outputStream
     *            输出路径
     * @param templateFileName
     *            模板路径
     * @param data
     */
    public static void fill(OutputStream outputStream, File templateFileName, Object data) {
        EasyExcel.write(outputStream).withTemplate(templateFileName).sheet().doFill(data);
    }

    /**
     * 以模板的方式输出到指定路径
     *
     * @param outputStream
     *            输出路径
     * @param templateFileName
     *            模板路径
     * @param data
     */
    public static void fill(OutputStream outputStream, InputStream templateFileName, Object data) {
        EasyExcel.write(outputStream).withTemplate(templateFileName).sheet().doFill(data);
    }

    public static <T> List<T> readFromExcel(InputStream inputStream, Class<T> clazz) {
        return EasyExcelFactory.read(inputStream, clazz, null).doReadAllSync();
    }

    public static <T> List<T> readFromExcel(InputStream inputStream, Class<T> clazz, ReadListener listener) {
        return EasyExcelFactory.read(inputStream, clazz, listener).doReadAllSync();
    }

    public static <T> List<T> readFromExcel(InputStream inputStream, Class<T> clazz, ReadListener listener,
                                            Integer sheetNo, Integer headRowNumber) {
        return ((ExcelReaderSheetBuilder)EasyExcelFactory.read(inputStream, clazz, listener).sheet(sheetNo)
                .headRowNumber(headRowNumber)).doReadSync();
    }

    public static <T> List<T> readFromExcel(String filePath, Class<T> clazz) {
        return EasyExcelFactory.read(filePath, clazz, null).sheet().doReadSync();
    }

    public static <T> List<T> readFromExcel(String filePath, Class<T> clazz, AnalysisEventListener<T> listener) {
        return EasyExcelFactory.read(filePath, clazz, listener).sheet().doReadSync();
    }

    public static <T> void writeToExcel(String filePath, List<T> data, Class<T> clazz) {
        EasyExcelFactory.write(filePath, clazz).sheet().doWrite(data);
    }

    public static <T> void writeMoreSheetToExcel(ExcelWriter excelWriter, List<T> data, Class clazz, Integer sheetNo,
                                                 String sheetName) {
        ExcelWriterSheetBuilder excelWriterSheetBuilder = new ExcelWriterSheetBuilder(excelWriter);
        excelWriterSheetBuilder.sheetNo(sheetNo);
        excelWriterSheetBuilder.sheetName(sheetName);
        WriteSheet writeSheet = excelWriterSheetBuilder.build();
        writeSheet.setClazz(clazz);
        excelWriter.write(data, writeSheet);
    }

    public static void finishWriter(OutputStream out, ExcelWriter excelWriter) throws IOException {
        out.flush();
        excelWriter.finish();
        out.close();
    }

    public static <T> void writeToExcel(HttpServletResponse response, List<T> data, Class<T> clazz, String filename)
            throws IOException {
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Expose-Headers", "Authorization,Content-Type,Content-disposition");
        EasyExcelFactory.write(response.getOutputStream(), clazz).sheet().doWrite(data);
    }

    public static void fillWithTemplate(HttpServletResponse response, Object data, String templatePath, String fileName)
            throws IOException {
        OutputStream outputStream = response.getOutputStream();
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Expose-Headers", "Authorization,Content-Type,Content-disposition");
        EasyExcel.write(outputStream).withTemplate(templatePath).sheet().doFill(data);
    }
}
