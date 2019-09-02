package com.stylefeng.guns.modular.PM.service;

import com.stylefeng.guns.modular.system.model.CmPm;
import com.baomidou.mybatisplus.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * VIEW 服务类
 * </p>
 *
 */
public interface ICmPmService extends IService<CmPm> {
    List<CmPm> readExcelFile(MultipartFile file);
}
