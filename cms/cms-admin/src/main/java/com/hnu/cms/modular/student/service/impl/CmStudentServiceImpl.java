package com.stylefeng.guns.modular.student.service.impl;

import com.stylefeng.guns.modular.student.service.excelHandler.ReadExcel;
import com.stylefeng.guns.modular.system.model.CmStudent;
import com.stylefeng.guns.modular.system.dao.CmStudentMapper;
import com.stylefeng.guns.modular.student.service.ICmStudentService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * VIEW 服务实现类
 * </p>
 *
 * @author qqzj
 * @since 2018-12-18
 */
@Service
public class CmStudentServiceImpl extends ServiceImpl<CmStudentMapper, CmStudent> implements ICmStudentService {

    @Override
    public List<CmStudent> readExcelFile(MultipartFile file) {
        ReadExcel readExcel=new ReadExcel();
        List<CmStudent> userList = readExcel.getExcelInfo(file);
        if(userList != null && !userList.isEmpty()){
            return userList;
//            result = "上传成功";
        }else{
            return null;
//            result = "上传失败";
        }
    }
}
