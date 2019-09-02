package com.stylefeng.guns.modular.teacher.service;

import com.stylefeng.guns.modular.system.model.CmTeacher;
import com.baomidou.mybatisplus.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * VIEW 服务类
 * </p>
 *
 * @author qqzj
 * @since 2018-12-18
 */
public interface ICmTeacherService extends IService<CmTeacher> {
    List<CmTeacher> readExcelFile(MultipartFile file);
}
