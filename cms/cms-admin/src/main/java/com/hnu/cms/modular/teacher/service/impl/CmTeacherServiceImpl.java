package com.stylefeng.guns.modular.teacher.service.impl;

import com.stylefeng.guns.modular.system.model.CmTeacher;
import com.stylefeng.guns.modular.system.dao.CmTeacherMapper;
import com.stylefeng.guns.modular.teacher.service.ICmTeacherService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.stylefeng.guns.modular.teacher.service.excelHandler.ReadExcel;
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
public class CmTeacherServiceImpl extends ServiceImpl<CmTeacherMapper, CmTeacher> implements ICmTeacherService {

    @Override
    public List<CmTeacher> readExcelFile(MultipartFile file) {
//        String result ="";
        //创建处理EXCEL的类
        ReadExcel readExcel=new ReadExcel();
        //解析excel，获取上传的事件单
        System.out.println("跳转到service实现啦！");
        List<CmTeacher> userList = readExcel.getExcelInfo(file);

        //至此已经将excel中的数据转换到list里面了,接下来就可以操作list,可以进行保存到数据库,或者其他操作,
        //和你具体业务有关,这里不做具体的示范
        if(userList != null && !userList.isEmpty()){
            return userList;
//            result = "上传成功";
        }else{
            return null;
//            result = "上传失败";
        }
//        return result;
    }
}
