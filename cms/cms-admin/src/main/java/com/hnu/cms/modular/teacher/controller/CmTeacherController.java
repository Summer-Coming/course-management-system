package com.stylefeng.guns.modular.teacher.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.common.constant.Const;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.support.BeanKit;
import com.stylefeng.guns.modular.teacher.warpper.CmTeacherWarpper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import com.stylefeng.guns.modular.system.model.CmTeacher;
import com.stylefeng.guns.modular.teacher.service.ICmTeacherService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.stylefeng.guns.core.support.DateTimeKit.date;

/**
 * 教师管理控制器
 *
 * @author qqzj
 * @since 2018-12-07
 */
@Controller
@RequestMapping("/cmTeacher")
public class CmTeacherController extends BaseController {

    private String PREFIX = "/teacher/cmTeacher/";
    private Integer ID = null;
    @Autowired
    private ICmTeacherService cmTeacherService;

    /**
     * 跳转到教师管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "cmTeacher.html";
    }

    /**
     * 跳转到添加教师管理
     */
    @RequestMapping("/cmTeacher_add")
    public String cmTeacherAdd() {
        return PREFIX + "cmTeacher_add.html";
    }

    /**
     * 跳转到批量导入教师管理
     */
    @RequestMapping("/cmTeacher_batchUpload")
    public String cmTeacherBatchUpload() {
        return PREFIX + "cmTeacher_batchUpload.html";
    }

    /**
     * 文件批量上传具体实现方法;
     *
     * @param file
     * @return
     */
    @RequestMapping(value="/upload",method= RequestMethod.POST)
    @ResponseBody
    public String handleFileUpload(HttpServletRequest request, @RequestParam("file") MultipartFile file, CmTeacher cmTeacher) throws Exception{
        List<CmTeacher> teachers = cmTeacherService.readExcelFile(file);
        for(int i=0;i<teachers.size();i++){
            cmTeacher = teachers.get(i);
            cmTeacher.setRoleid("3");
            cmTeacher.setStatus(1);
            cmTeacher.setCreatetime(new Date());
            cmTeacher.setDeptid(ShiroKit.getUser().getDeptId());
            cmTeacher.setSalt(ShiroKit.getRandomSalt(5));
            cmTeacher.setPassword(ShiroKit.md5(Const.DEFAULT_PWD, cmTeacher.getSalt()));
            cmTeacherService.insert(cmTeacher);
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
     * 跳转到修改教师管理
     */
    @RequestMapping("/cmTeacher_update/{cmTeacherId}")
    public String cmTeacherUpdate(@PathVariable Integer cmTeacherId, Model model) {
        CmTeacher cmTeacher = cmTeacherService.selectById(cmTeacherId);
        model.addAttribute("item",cmTeacher);
        LogObjectHolder.me().set(cmTeacher);
        ID = cmTeacherId;
        return PREFIX + "cmTeacher_edit.html";
    }

    /**
     * 获取教师管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
//        return cmTeacherService.selectList(null);
        EntityWrapper<CmTeacher> cmTeacherEntityWrapper = new EntityWrapper<>();
        Wrapper<CmTeacher> personalWork = cmTeacherEntityWrapper.in("deptid",ShiroKit.getDeptDataScope());
        List<CmTeacher> list = cmTeacherService.selectList(personalWork);
        List<Map<String,Object>> stringObjectMap = new ArrayList<>();
        Map<String,Object> mp = null;
        for(int i=0;i<list.size();i++){
            mp = BeanKit.beanToMap(list.get(i));
            if(mp!=null){
                stringObjectMap.add(mp);
            }
        }
        return  new CmTeacherWarpper(stringObjectMap).warp();
    }

    /**
     * 新增教师管理
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(CmTeacher cmTeacher) {
        cmTeacher.setRoleid("3");
        cmTeacher.setStatus(1);
        cmTeacher.setCreatetime(new Date());
        cmTeacher.setDeptid(ShiroKit.getUser().getDeptId());
        cmTeacher.setSalt(ShiroKit.getRandomSalt(5));
        cmTeacher.setPassword(ShiroKit.md5(Const.DEFAULT_PWD, cmTeacher.getSalt()));
        cmTeacherService.insert(cmTeacher);
        return SUCCESS_TIP;
    }

    /**
     * 删除教师管理
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer cmTeacherId) {
        cmTeacherService.deleteById(cmTeacherId);
        return SUCCESS_TIP;
    }

    /**
     * 修改教师管理
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(CmTeacher cmTeacher) {
        cmTeacher.setId(ID);
        cmTeacherService.updateById(cmTeacher);
        return SUCCESS_TIP;
    }

    /**
     * 教师管理详情
     */
    @RequestMapping(value = "/detail/{cmTeacherId}")
    @ResponseBody
    public Object detail(@PathVariable("cmTeacherId") Integer cmTeacherId) {
        return cmTeacherService.selectById(cmTeacherId);
    }
}
