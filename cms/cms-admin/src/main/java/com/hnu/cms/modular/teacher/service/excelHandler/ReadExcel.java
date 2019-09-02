package com.stylefeng.guns.modular.teacher.service.excelHandler;

import com.stylefeng.guns.modular.system.model.CmTeacher;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ReadExcel {
    // 总行数
    private int totalRows = 0;
    // 总条数
    private int totalCells = 0;
    // 错误信息接收器
    private String errorMsg;

    // 构造方法
    public ReadExcel() {
    }

    // 获取总行数
    public int getTotalRows() {
        return totalRows;
    }

    // 获取总列数
    public int getTotalCells() {
        return totalCells;
    }

    // 获取错误信息
    public String getErrorInfo() {
        return errorMsg;
    }

    /**
     * 读EXCEL文件，获取信息集合
     * @param mFile
     * @return
     */
    public List<CmTeacher> getExcelInfo(MultipartFile mFile) {
        String fileName = mFile.getOriginalFilename();// 获取文件名
        System.out.println(fileName);
        try {
            if (!validateExcel(fileName)) {// 验证文件名是否合格
                return null;
            }
            boolean isExcel2003 = true;// 根据文件名判断文件是2003版本还是2007版本
            if (isExcel2007(fileName)) {
                isExcel2003 = false;
            }
            List<CmTeacher> cmTeacherList = createExcel(mFile.getInputStream(), isExcel2003);
            System.out.println("！！！！！！！！判断Excel文件是不是isExcel2003"+isExcel2003);
            return cmTeacherList;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据excel里面的内容读取客户信息
     *
     * @param isExcel2003 excel是2003还是2007版本
     * @return
     * @throws IOException
     */
    public List<CmTeacher> createExcel(InputStream is, boolean isExcel2003) {
        try {
            Workbook wb = null;
            if (isExcel2003) {// 当excel是2003时,创建excel2003
                wb = new HSSFWorkbook(is);
            } else {// 当excel是2007时,创建excel2007
                wb = new XSSFWorkbook(is);
            }
            List<CmTeacher> cmTeacherList = readExcelValue(wb);// 读取Excel里面客户的信息
            return cmTeacherList;

        } catch (IOException e) {
            e.printStackTrace();
            return  null;
        }
    }

    /**
     * 读取Excel里面客户的信息
     *
     * @param wb
     * @return
     */
    private List<CmTeacher> readExcelValue(Workbook wb) {
        System.out.println("读取文件信息啦！！！！！！！！！！！！！！");
        // 得到第一个shell
        Sheet sheet = wb.getSheetAt(0);
        // 得到Excel的行数
        this.totalRows = sheet.getPhysicalNumberOfRows();
        // 得到Excel的列数(前提是有行数)
        if (totalRows > 1 && sheet.getRow(0) != null) {
            this.totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
        }
        List<CmTeacher> cmTeacherList = new ArrayList<CmTeacher>();
        // 循环Excel行数
        for (int r = 1; r < totalRows; r++) {
            Row row = sheet.getRow(r);
            if (row == null) {
                continue;
            }
//            User user = new User();
            CmTeacher cmTeacher = new CmTeacher();
            // 循环Excel的列
            for (int c = 0; c < this.totalCells; c++) {
                Cell cell = row.getCell(c);
                if (null != cell) {
                    if (c == 0) {
                        // 如果是纯数字,比如你写的是25,cell.getNumericCellValue()获得是25.0,通过截取字符串去掉.0获得25
                        if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                            String account = String.valueOf(cell.getNumericCellValue());
                            cmTeacher.setAccount(account.substring(0, account.length() - 2 > 0 ? account.length() - 2 : 1));// 账号
                        } else {
                            cmTeacher.setAccount(cell.getStringCellValue());// 名称
                        }
                    } else if (c == 1) {
                        if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                            String name = String.valueOf(cell.getNumericCellValue());
                            cmTeacher.setName(name.substring(0, name.length() - 2 > 0 ? name.length() - 2 : 1));// 名字
                        } else {
                            cmTeacher.setName(cell.getStringCellValue());// 名称
                        }
                    } else if (c == 2) {
                        if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                            String email = String.valueOf(cell.getNumericCellValue());
                            cmTeacher.setEmail(email.substring(0, email.length() - 2 > 0 ? email.length() - 2 : 1));// 电子邮件
                        } else {
                            cmTeacher.setEmail(cell.getStringCellValue());// 年龄
                        }
                    }else if (c == 3) {
                        if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                            String phone = String.valueOf(cell.getNumericCellValue());
                            cmTeacher.setPhone(phone.substring(0, phone.length() - 2 > 0 ? phone.length() - 2 : 1));// 电子邮件
                        } else {
                            cmTeacher.setPhone(cell.getStringCellValue());// 年龄
                        }
                    }else if (c == 4) {
                        if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                            String classId = String.valueOf(cell.getNumericCellValue());
                            cmTeacher.setClassId(classId.substring(0, classId.length() - 2 > 0 ? classId.length() - 2 : 1));// 电子邮件
                        } else {
                            cmTeacher.setClassId(cell.getStringCellValue());// 年龄
                        }
                    }
                }
            }
            // 添加到list
            cmTeacherList.add(cmTeacher);
        }
        System.out.println("!!!!!!!!!!!!教师信息的组合："+cmTeacherList);
        return cmTeacherList;
    }

    /**
     * 验证EXCEL文件
     * @param filePath
     * @return
     */
    public boolean validateExcel(String filePath) {
        if (filePath == null || !(isExcel2003(filePath) || isExcel2007(filePath))) {
            errorMsg = "文件名不是excel格式";
            return false;
        }
        return true;
    }

    // @描述：是否是2003的excel，返回true是2003
    public static boolean isExcel2003(String filePath) {
        return filePath.matches("^.+\\.(?i)(xls)$");
    }

    // @描述：是否是2007的excel，返回true是2007
    public static boolean isExcel2007(String filePath) {
        return filePath.matches("^.+\\.(?i)(xlsx)$");
    }
}