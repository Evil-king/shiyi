package com.baibei.shiyi.admin.modules.utils;


import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baibei.shiyi.common.tool.constants.ExcelConstant;
import com.baibei.shiyi.common.tool.exception.ServiceException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Component
public class ExcelUtils {

    /**
     * excel 默认的导出工具类
     * 通过数据量分成多个sheet,增加导出excel的内存使用量
     * 当List查询出来的数据为空时,设置内容为空
     *
     * @param entityList
     * @param response
     * @param <T>
     */
    public <T extends BaseRowModel> void defaultExcelExport(List<T> entityList, HttpServletResponse response, Class<T> entity, String fileName) {
        if (entityList != null) {
            try {
                ExcelWriter excelWriter = new ExcelWriter(response.getOutputStream(), ExcelTypeEnum.XLSX, true);
                response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
                response.setHeader("Content-disposition", "filename=" + URLEncoder.encode(fileName, "UTF-8") + LocalDate.now() + ".xlsx");
                if (entityList.isEmpty()) {
                    Sheet sheet = new Sheet(1, 0, entity);
                    excelWriter.write(entityList, sheet);
                    excelWriter.finish();
                } else {
                    Integer sheetCount = ExcelConstant.countSheetCount(entityList.size());
                    Stream.iterate(0, n -> n + 1).limit(sheetCount).forEach(
                            i -> {
                                List<T> groupByList = entityList.stream().skip(i * ExcelConstant.MAX_ROW_NUMBER).limit(ExcelConstant.MAX_ROW_NUMBER).collect(Collectors.toList());
                                Sheet sheet = new Sheet(i, 0, entity);
                                sheet.setSheetName(fileName + i);
                                excelWriter.write(groupByList, sheet);
                                excelWriter.finish();
                            }
                    );
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 读取某个 sheet 的110 Excel
     *
     * @param excel    文件
     * @param rowModel 实体类映射，继承 BaseRowModel 类
     * @param sheetNo  sheet 的序号 从1开始
     * @return Excel 数据 list
     */
    public List<Object> readExcel(MultipartFile excel, BaseRowModel rowModel) throws IOException {
        return readExcel(excel, rowModel, 1, 1);
    }

    /**
     * 读取多行sheet的excel导入的方法
     *
     * @param multipartFile
     * @param entity
     * @param <T>
     * @return
     */
    public <T extends BaseRowModel> List<T> defaultReadExcelFile(MultipartFile multipartFile, Class<T> entity) {
        MyExcelListener<T> excelListener = new MyExcelListener();
        BufferedInputStream bufferedInputStream = null;
        try {
            bufferedInputStream = new BufferedInputStream(multipartFile.getInputStream());
            ExcelReader excelReader = new ExcelReader(bufferedInputStream, null, excelListener, true);
            for (Sheet sheet : excelReader.getSheets()) {
                sheet.setClazz(entity);
                excelReader.read(sheet);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedInputStream == null) {
                try {
                    bufferedInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if(excelListener.getDataList().size() > 10000){
            throw new ServiceException("导入数据过大");
        }
        return excelListener.getDataList();
    }


    /**
     * 读取某个 sheet 的 Excel
     *
     * @param excel       文件
     * @param rowModel    实体类映射，继承 BaseRowModel 类
     * @param sheetNo     sheet 的序号 从1开始
     * @param headLineNum 表头行数，默认为1
     * @return Excel 数据 list
     */
    public List<Object> readExcel(MultipartFile excel, BaseRowModel rowModel, int sheetNo, int headLineNum) throws IOException {
        ExcelListener excelListener = new ExcelListener();
        ExcelReader reader = getReader(excel, excelListener);
        if (reader == null) {
            return null;
        }
        reader.read(new Sheet(sheetNo, headLineNum, rowModel.getClass()));
        return excelListener.getDatas();
    }

    /**
     * 读取指定sheetName的Excel(多个 sheet)
     *
     * @param excel    文件
     * @param rowModel 实体类映射，继承 BaseRowModel 类
     * @return Excel 数据 list
     * @throws IOException
     */
    public static List<Object> readExcel(MultipartFile excel, BaseRowModel rowModel, String sheetName) throws IOException {
        ExcelListener excelListener = new ExcelListener();
        ExcelReader reader = getReader(excel, excelListener);
        if (reader == null) {
            return null;
        }
        for (Sheet sheet : reader.getSheets()) {
            if (rowModel != null) {
                sheet.setClazz(rowModel.getClass());
            }
            //读取指定名称的sheet
            if (sheet.getSheetName().contains(sheetName)) {
                reader.read(sheet);
                break;
            }
        }
        return excelListener.getDatas();
    }

    /**
     * 返回 ExcelReader
     *
     * @param excel         需要解析的 Excel 文件
     * @param excelListener new ExcelListener()
     * @throws IOException
     */
    private static ExcelReader getReader(MultipartFile excel, ExcelListener excelListener) throws IOException {
        String filename = excel.getOriginalFilename();
        if (filename != null && (filename.toLowerCase().endsWith(".xls") || filename.toLowerCase().endsWith(".xlsx"))) {
            InputStream is = new BufferedInputStream(excel.getInputStream());
            return new ExcelReader(is, null, excelListener, false);
        } else {
            return null;
        }
    }

}
