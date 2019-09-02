package com.stylefeng.guns.modular.student.service;

import com.stylefeng.guns.modular.system.model.CmStudent;
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
public interface ICmStudentService extends IService<CmStudent> {
    List<CmStudent> readExcelFile(MultipartFile file);
}
