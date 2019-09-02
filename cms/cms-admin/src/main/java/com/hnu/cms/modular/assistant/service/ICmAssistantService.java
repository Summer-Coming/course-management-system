package com.stylefeng.guns.modular.assistant.service;

import com.stylefeng.guns.modular.system.model.CmAssistant;
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
public interface ICmAssistantService extends IService<CmAssistant> {
    List<CmAssistant> readExcelFile(MultipartFile file);
}
