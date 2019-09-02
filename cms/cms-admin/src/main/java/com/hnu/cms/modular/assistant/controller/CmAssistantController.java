package com.stylefeng.guns.modular.assistant.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.common.constant.Const;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.support.BeanKit;
import com.stylefeng.guns.modular.assistant.warpper.CmAssistantWarpper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import com.stylefeng.guns.modular.system.model.CmAssistant;
import com.stylefeng.guns.modular.assistant.service.ICmAssistantService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 助教管理控制器
 */
@Controller
@RequestMapping("/cmAssistant")
public class CmAssistantController extends BaseController {

    private String PREFIX = "/assistant/cmAssistant/";
    private Integer ID = null;
    @Autowired
    private ICmAssistantService cmAssistantService;

    /**
     * 跳转到助教管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "cmAssistant.html";
    }

    /**
     * 跳转到批量添加助教管理
     */
    @RequestMapping("/cmAssistant_batchUpload")
    public String cmAssistantBatchUpload() {
        return PREFIX + "cmAssistant_batchUpload.html";
    }

    /**
     * 跳转到添加助教管理
     */
    @RequestMapping("/cmAssistant_add")
    public String cmAssistantAdd() {
        return PREFIX + "cmAssistant_add.html";
    }


    /**
     * 跳转到修改助教管理
     */
    @RequestMapping("/cmAssistant_update/{cmAssistantId}")
    public String cmAssistantUpdate(@PathVariable Integer cmAssistantId, Model model) {
        CmAssistant cmAssistant = cmAssistantService.selectById(cmAssistantId);
        model.addAttribute("item",cmAssistant);
        LogObjectHolder.me().set(cmAssistant);
        ID = cmAssistantId;
        return PREFIX + "cmAssistant_edit.html";
    }

    /**
     * 获取助教管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {

//        return cmAssistantService.selectList(null);
        EntityWrapper<CmAssistant> cmTeacherEntityWrapper = new EntityWrapper<>();
        Wrapper<CmAssistant> personalSchool = cmTeacherEntityWrapper.in("deptid",ShiroKit.getDeptDataScope());
        List<CmAssistant> list = cmAssistantService.selectList(personalSchool);
        List<Map<String,Object>> stringObjectMap = new ArrayList<>();
        Map<String,Object> mp = null;
        for(int i=0;i<list.size();i++){
            mp = BeanKit.beanToMap(list.get(i));
            if(mp!=null){
                stringObjectMap.add(mp);
            }
        }
        return  new CmAssistantWarpper(stringObjectMap).warp();
    }

    /**
     * 新增助教管理
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(CmAssistant cmAssistant) {
        cmAssistant.setRoleid("4");
        cmAssistant.setStatus(1);
        cmAssistant.setDeptid(ShiroKit.getUser().getDeptId());
        cmAssistant.setSalt(ShiroKit.getRandomSalt(5));
        cmAssistant.setPassword(ShiroKit.md5(Const.DEFAULT_PWD, cmAssistant.getSalt()));
        cmAssistantService.insert(cmAssistant);
        return SUCCESS_TIP;
    }

    /**
     * 文件批量上传具体实现方法;
     *
     * @param file
     * @return
     */
    @RequestMapping(value="/upload",method= RequestMethod.POST)
    @ResponseBody
    public String handleFileUpload(HttpServletRequest request, @RequestParam("file") MultipartFile file, CmAssistant cmAssistant) throws Exception{
        List<CmAssistant> assistants = cmAssistantService.readExcelFile(file);
        System.out.println("!!!!!助教列表"+assistants);
        for(int i=0;i<assistants.size();i++){
            cmAssistant = assistants.get(i);
            cmAssistant.setRoleid("4");
            cmAssistant.setStatus(1);
            cmAssistant.setDeptid(ShiroKit.getUser().getDeptId());
            cmAssistant.setSalt(ShiroKit.getRandomSalt(5));
            cmAssistant.setPassword(ShiroKit.md5(Const.DEFAULT_PWD, cmAssistant.getSalt()));
            cmAssistantService.insert(cmAssistant);
        }
        if (!file.isEmpty()) {
            try {
                BufferedOutputStream out = new BufferedOutputStream(
                        new FileOutputStream(new File(
                                file.getOriginalFilename())));
                System.out.println(file.getName());
                out.write(file.getBytes());
                out.flush();
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return "上传失败," + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "上传失败," + e.getMessage();
            }
            //上传文件路径
            String path = request.getServletContext().getRealPath("/batchUploadAssistant/");
            //上传文件名
            String filename = file.getOriginalFilename();
            File filepath = new File(path,filename);
            //判断路径是否存在，如果不存在就创建一个
            if (!filepath.getParentFile().exists()) {
                filepath.getParentFile().mkdirs();
            } //将上传文件保存到一个目标文件当中
            File mFile = new File(path + File.separator + filename);
            file.transferTo(mFile);
            System.out.println("文件名："+filename);
            System.out.println("上传文件路径："+path);
            return "上传成功";
        } else {
            return "上传失败，因为文件是空的.";

        }

    }

    /**
     * 删除助教管理
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer cmAssistantId) {
        cmAssistantService.deleteById(cmAssistantId);
        return SUCCESS_TIP;
    }

    /**
     * 修改助教管理
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(CmAssistant cmAssistant) {
        cmAssistant.setId(ID);
        cmAssistantService.updateById(cmAssistant);
        return SUCCESS_TIP;
    }

    /**
     * 助教管理详情
     */
    @RequestMapping(value = "/detail/{cmAssistantId}")
    @ResponseBody
    public Object detail(@PathVariable("cmAssistantId") Integer cmAssistantId) {
        return cmAssistantService.selectById(cmAssistantId);
    }
}
