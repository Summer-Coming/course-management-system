package com.stylefeng.guns.modular.assistant.service.impl;

import com.stylefeng.guns.modular.assistant.service.excelHandler.ReadExcel;
import com.stylefeng.guns.modular.system.model.CmAssistant;
import com.stylefeng.guns.modular.system.dao.CmAssistantMapper;
import com.stylefeng.guns.modular.assistant.service.ICmAssistantService;
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
public class CmAssistantServiceImpl extends ServiceImpl<CmAssistantMapper, CmAssistant> implements ICmAssistantService {

    @Override
    public List<CmAssistant> readExcelFile(MultipartFile file) {
        ReadExcel readExcel=new ReadExcel();
        List<CmAssistant> userList = readExcel.getExcelInfo(file);
        if(userList != null && !userList.isEmpty()){
            return userList;
//            result = "上传成功";
        }else{
            return null;
//            result = "上传失败";
        }

    }
}
