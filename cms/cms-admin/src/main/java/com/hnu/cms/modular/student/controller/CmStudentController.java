package com.stylefeng.guns.modular.student.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.common.constant.Const;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.support.BeanKit;
import com.stylefeng.guns.modular.student.warpper.CmStudentWarpper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import com.stylefeng.guns.modular.system.model.CmStudent;
import com.stylefeng.guns.modular.student.service.ICmStudentService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 学生管理控制器
 *
 */
@Controller
@RequestMapping("/cmStudent")
public class CmStudentController extends BaseController {

    private String PREFIX = "/student/cmStudent/";
    private Integer ID = null;
    @Autowired
    private ICmStudentService cmStudentService;

    /**
     * 跳转到学生管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "cmStudent.html";
    }

    /**
     * 跳转到批量添加学生管理
     */
    @RequestMapping("/cmStudent_batchUpload")
    public String cmStudentBatchUpload() {
        return PREFIX + "cmStudent_batchUpload.html";
    }

    /**
     * 跳转到添加学生管理
     */
    @RequestMapping("/cmStudent_add")
    public String cmStudentAdd() {
        return PREFIX + "cmStudent_add.html";
    }

    /**
     * 跳转到修改学生管理
     */
    @RequestMapping("/cmStudent_update/{cmStudentId}")
    public String cmStudentUpdate(@PathVariable Integer cmStudentId, Model model) {
        CmStudent cmStudent = cmStudentService.selectById(cmStudentId);
        model.addAttribute("item",cmStudent);
        LogObjectHolder.me().set(cmStudent);
        ID = cmStudentId;
        return PREFIX + "cmStudent_edit.html";
    }

    /**
     * 获取学生管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {

//        return cmStudentService.selectList(null);
        EntityWrapper<CmStudent> cmTeacherEntityWrapper = new EntityWrapper<>();
        Wrapper<CmStudent> personalSchool = cmTeacherEntityWrapper.in("deptid",ShiroKit.getDeptDataScope());
        List<CmStudent> list = cmStudentService.selectList(personalSchool);
        List<Map<String,Object>> stringObjectMap = new ArrayList<>();
        Map<String,Object> mp = null;
        for(int i=0;i<list.size();i++){
            mp = BeanKit.beanToMap(list.get(i));
            if(mp!=null){
                stringObjectMap.add(mp);
            }
        }
        return  new CmStudentWarpper(stringObjectMap).warp();
    }

    /**
     * 新增学生管理
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(CmStudent cmStudent) {
        cmStudent.setRoleid("5");
        cmStudent.setDeptid(ShiroKit.getUser().getDeptId());
        cmStudent.setStatus(1);
        cmStudent.setSalt(ShiroKit.getRandomSalt(5));
        cmStudent.setPassword(ShiroKit.md5(Const.DEFAULT_PWD, cmStudent.getSalt()));
        cmStudentService.insert(cmStudent);
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
    public String handleFileUpload(HttpServletRequest request, @RequestParam("file") MultipartFile file, CmStudent cmStudent) throws Exception{
        List<CmStudent> students = cmStudentService.readExcelFile(file);
        System.out.println("!!!!!学生列表"+students);
        for(int i=0;i<students.size();i++){
            cmStudent = students.get(i);
            cmStudent.setRoleid("5");
            cmStudent.setDeptid(ShiroKit.getUser().getDeptId());
            cmStudent.setStatus(1);
            cmStudent.setSalt(ShiroKit.getRandomSalt(5));
            cmStudent.setPassword(ShiroKit.md5(Const.DEFAULT_PWD, cmStudent.getSalt()));
            cmStudentService.insert(cmStudent);
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
     * 删除学生管理
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer cmStudentId) {
        cmStudentService.deleteById(cmStudentId);
        return SUCCESS_TIP;
    }

    /**
     * 修改学生管理
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(CmStudent cmStudent) {
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!当前学生的id:"+cmStudent.getId());
        System.out.println("!!!!!!!!!!!!!!!!!!!!!当前学生："+cmStudent);
        cmStudent.setId(ID);
        cmStudentService.updateById(cmStudent);
        return SUCCESS_TIP;
    }

    /**
     * 学生管理详情
     */
    @RequestMapping(value = "/detail/{cmStudentId}")
    @ResponseBody
    public Object detail(@PathVariable("cmStudentId") Integer cmStudentId) {
        return cmStudentService.selectById(cmStudentId);
    }
}
