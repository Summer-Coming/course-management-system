package com.stylefeng.guns.modular.stuupload.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.util.ToolUtil;
import com.stylefeng.guns.modular.system.model.CmSysfile;
import org.apache.tomcat.util.http.fileupload.FileUploadBase;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.apache.tomcat.util.http.fileupload.ProgressListener;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import com.stylefeng.guns.modular.system.model.CmStuupload;
import com.stylefeng.guns.modular.stuupload.service.ICmStuuploadService;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.AbstractMultipartHttpServletRequest;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import static com.stylefeng.guns.core.support.DateTimeKit.date;

/**
 * 上传作业控制器
 *
 * @author qqzj
 * @since 2018-12-31
 */
@Controller
@RequestMapping("/cmStuupload")
public class CmStuuploadController extends BaseController {

    private String PREFIX = "/stuupload/cmStuupload/";

    @Autowired
    private ICmStuuploadService cmStuuploadService;

    /**
     * 跳转到上传作业首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "cmStuupload.html";
    }

    /**
     * 跳转到添加上传作业
     */
    @RequestMapping("/cmStuupload_add")
    public String cmStuuploadAdd() {
        return PREFIX + "cmStuupload_add.html";
    }


    /**
     * 获取上传作业列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        System.out.println("ShiroKit.getUser().getName()"+ShiroKit.getUser().getName());
        String role = ShiroKit.getUser().getRoleid();
        System.out.println(ShiroKit.getUser());
        if(ToolUtil.equals(role,"5")||ToolUtil.equals(role,"6")){
            EntityWrapper<CmStuupload> cmStuuploadEntityWrapper = new EntityWrapper<>();
            Wrapper<CmStuupload> personalWork = cmStuuploadEntityWrapper.eq("stu_id",ShiroKit.getUser().getAccount());
            return cmStuuploadService.selectList(personalWork);
        }
        else {
            return cmStuuploadService.selectList(null);
        }
    }

    /**
     * 文件上传具体实现方法;
     *
     * @param file
     * @return
     */
    @RequestMapping(value="/upload",method= RequestMethod.POST)
    @ResponseBody
    public String handleFileUpload(HttpServletRequest request,@RequestParam("file") MultipartFile file, CmStuupload cmStuupload) throws Exception{
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
            String path = request.getServletContext().getRealPath("/uploadFiles/");
            //上传文件名
            String filename = file.getOriginalFilename();
            File filepath = new File(path,filename);
            //判断路径是否存在，如果不存在就创建一个
            if (!filepath.getParentFile().exists()) {
                filepath.getParentFile().mkdirs();
            }
            //将上传文件保存到一个目标文件当中
            file.transferTo(new File(path + File.separator + filename));
            System.out.println("文件名："+filename);
            System.out.println("上传文件路径："+path);
            cmStuupload.setFileName(filename);
            cmStuupload.setFilePath(path+filename);
            cmStuupload.setSubmitTime(date());
            cmStuupload.setStuId(ShiroKit.getUser().getAccount());
            cmStuupload.setAssId(ShiroKit.getUser().getAssistantId());
            cmStuupload.setDeptid(ShiroKit.getUser().getDeptId());
//            String str = ShiroKit.getUser().getAssistantId();
//            System.out.println("ShiroKit.getUser().getAssistantId()"+ShiroKit.getUser().getAssistantId());
//            System.out.println("ShiroKit.getUser().assistantId"+ShiroKit.getUser().assistantId);
//            System.out.println("ShiroKit.getUser().account"+ShiroKit.getUser().account);
//            System.out.println("ShiroKit.getUser().getRoleid()"+ShiroKit.getUser().getRoleid());
//            System.out.println("ShiroKit.getUser().getAssistantId()"+ShiroKit.getUser().getAssistantId());
            cmStuuploadService.insert(cmStuupload);
            return "上传成功";

        } else {
            return "上传失败，因为文件是空的.";
        }

    }

    /**
     * 删除上传作业
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer cmStuuploadId) {
        cmStuuploadService.deleteById(cmStuuploadId);
        return SUCCESS_TIP;
    }


    /**
     * 上传作业详情
     */
    @RequestMapping(value = "/detail/{cmStuuploadId}")
    @ResponseBody
    public Object detail(@PathVariable("cmStuuploadId") Integer cmStuuploadId) {
        return cmStuuploadService.selectById(cmStuuploadId);
    }
}
