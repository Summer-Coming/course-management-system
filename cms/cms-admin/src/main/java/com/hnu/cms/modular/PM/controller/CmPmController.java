package com.stylefeng.guns.modular.PM.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.common.constant.Const;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.support.BeanKit;
import com.stylefeng.guns.modular.PM.warpper.CmPmWarpper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import com.stylefeng.guns.modular.system.model.CmPm;
import com.stylefeng.guns.modular.PM.service.ICmPmService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * PM管理控制器
 *
 */
@Controller
@RequestMapping("/cmPm")
public class CmPmController extends BaseController {

    private String PREFIX = "/PM/cmPm/";
    private Integer ID = null;
    @Autowired
    private ICmPmService cmPmService;

    /**
     * 跳转到PM管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "cmPm.html";
    }

    /**
     * 跳转到批量添加PM管理
     */
    @RequestMapping("/cmPm_batchUpload")
    public String cmPmBatchUpload() {
        return PREFIX + "cmPm_batchUpload.html";
    }

    /**
     * 跳转到添加PM管理
     */
    @RequestMapping("/cmPm_add")
    public String cmPmAdd() {
        return PREFIX + "cmPm_add.html";
    }

    /**
     * 跳转到修改PM管理
     */
    @RequestMapping("/cmPm_update/{cmPmId}")
    public String cmPmUpdate(@PathVariable Integer cmPmId, Model model) {
        CmPm cmPm = cmPmService.selectById(cmPmId);
        model.addAttribute("item",cmPm);
        LogObjectHolder.me().set(cmPm);
        ID = cmPmId;
        return PREFIX + "cmPm_edit.html";
    }

    /**
     * 获取PM管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {

//        return cmPmService.selectList(null);
        EntityWrapper<CmPm> cmTeacherEntityWrapper = new EntityWrapper<>();
        Wrapper<CmPm> personalSchool = cmTeacherEntityWrapper.in("deptid",ShiroKit.getDeptDataScope());
        List<CmPm> list = cmPmService.selectList(personalSchool);
        List<Map<String,Object>> stringObjectMap = new ArrayList<>();
        Map<String,Object> mp = null;
        for(int i=0;i<list.size();i++){
            mp = BeanKit.beanToMap(list.get(i));
            if(mp!=null){
                stringObjectMap.add(mp);
            }
        }
        return  new CmPmWarpper(stringObjectMap).warp();
    }

    /**
     * 新增PM管理
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(CmPm cmPm) {
        cmPm.setRoleid("6");
        cmPm.setDeptid(ShiroKit.getUser().getDeptId());
        cmPm.setStatus(1);
        cmPm.setSalt(ShiroKit.getRandomSalt(5));
        cmPm.setPassword(ShiroKit.md5(Const.DEFAULT_PWD, cmPm.getSalt()));
        cmPmService.insert(cmPm);
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
    public String handleFileUpload(HttpServletRequest request, @RequestParam("file") MultipartFile file, CmPm cmPm) throws Exception{
        List<CmPm> pms = cmPmService.readExcelFile(file);
        System.out.println("!!!!!PM列表"+pms);
        for(int i=0;i<pms.size();i++){
            cmPm = pms.get(i);
            cmPm.setRoleid("6");
            cmPm.setDeptid(ShiroKit.getUser().getDeptId());
            cmPm.setStatus(1);
            cmPm.setSalt(ShiroKit.getRandomSalt(5));
            cmPm.setPassword(ShiroKit.md5(Const.DEFAULT_PWD, cmPm.getSalt()));
            cmPmService.insert(cmPm);
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
            String path = request.getServletContext().getRealPath("/batchUploadTeacher/");
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
     * 删除PM管理
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer cmPmId) {
        cmPmService.deleteById(cmPmId);
        return SUCCESS_TIP;
    }

    /**
     * 修改PM管理
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(CmPm cmPm) {
        cmPm.setId(ID);
        cmPmService.updateById(cmPm);
        return SUCCESS_TIP;
    }

    /**
     * PM管理详情
     */
    @RequestMapping(value = "/detail/{cmPmId}")
    @ResponseBody
    public Object detail(@PathVariable("cmPmId") Integer cmPmId) {
        return cmPmService.selectById(cmPmId);
    }
}
