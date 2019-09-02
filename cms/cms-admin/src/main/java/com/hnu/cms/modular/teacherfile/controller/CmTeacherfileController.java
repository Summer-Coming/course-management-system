package com.stylefeng.guns.modular.teacherfile.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.support.BeanKit;
import com.stylefeng.guns.core.util.ToolUtil;
import com.stylefeng.guns.modular.system.model.CmStuupload;
import com.stylefeng.guns.modular.teacherfile.warpper.CmTeacherfileWarpper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import com.stylefeng.guns.modular.system.model.CmTeacherfile;
import com.stylefeng.guns.modular.teacherfile.service.ICmTeacherfileService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.stylefeng.guns.core.support.DateTimeKit.date;

/**
 * 个人资料控制器
 * @author qqzj
 * @since 2018-12-07
 */
@Controller
@RequestMapping("/cmTeacherfile")
public class CmTeacherfileController extends BaseController {

    private String PREFIX = "/teacherfile/cmTeacherfile/";
    private Integer downloadId = 0;

    @Autowired
    private ICmTeacherfileService cmTeacherfileService;

    /**
     * 跳转到个人资料首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "cmTeacherfile.html";
    }

    /**
     * 跳转到添加个人资料
     */
    @RequestMapping("/cmTeacherfile_add")
    public String cmTeacherfileAdd() {
        return PREFIX + "cmTeacherfile_add.html";
    }

    /**
     * 跳转到下载个人资料
     *
     */
    @RequestMapping("/cmTeacherfile_download/{cmTeacherfileId}")
    public String cmTeacherfileDownload(@PathVariable Integer cmTeacherfileId, Model model) {
        CmTeacherfile cmTeacherfile = cmTeacherfileService.selectById(cmTeacherfileId);
        System.out.println("id是"+cmTeacherfile.getId());
        downloadId = cmTeacherfileId;
        return PREFIX + "cmTeacherfile_download.html";
    }


    /**
     * 跳转到修改个人资料
     */
    @RequestMapping("/cmTeacherfile_update/{cmTeacherfileId}")
    public String cmTeacherfileUpdate(@PathVariable Integer cmTeacherfileId, Model model) {
        CmTeacherfile cmTeacherfile = cmTeacherfileService.selectById(cmTeacherfileId);
        model.addAttribute("item",cmTeacherfile);
        LogObjectHolder.me().set(cmTeacherfile);
        return PREFIX + "cmTeacherfile_edit.html";
    }


    /**
     * 下载推荐资料
     */
    @RequestMapping(value = "/cmTeacherfile_download/download")
    @ResponseBody
    public String download(HttpServletRequest req, HttpServletResponse resp, CmTeacherfile  cmTeacherfile) throws IOException {
        cmTeacherfile  = cmTeacherfileService.selectById(downloadId);
        System.out.println("!!!!!!!!!!!!!!!!!!cmTeacherfile到底是何面目:" + cmTeacherfile);
        System.out.println("进入download啦！！！！！！！！！！！！！！");
        String Url = cmTeacherfile.getFilePath();
        System.out.println("!!!!!!!!!url" + Url);
        String fileName = cmTeacherfile.getFileName();
        // path是指欲下载的文件的路径。
        File file = new File(Url);
        // 取得文件名。
//        String filename = file.getName();
        // 取得文件的后缀名。
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1).toUpperCase();

        // 以流的形式下载文件。
        InputStream fis = new BufferedInputStream(new FileInputStream(Url));
        byte[] buffer = new byte[fis.available()];
        fis.read(buffer);
        fis.close();
        // 清空response
        resp.reset();
        // 设置response的Header
        resp.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes()));
        resp.addHeader("Content-Length", "" + file.length());
        OutputStream toClient = new BufferedOutputStream(resp.getOutputStream());
        resp.setContentType("application/octet-stream");
        toClient.write(buffer);
        toClient.flush();
        toClient.close();
        return "下载成功";
    }


    /**
     * 获取个人资料列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {

//        return cmTeacherfileService.selectList(null);
        List<CmTeacherfile> list = new ArrayList<>();
        EntityWrapper<CmTeacherfile> cmTeacherfileEntityWrapper = new EntityWrapper<>();
        String role = ShiroKit.getUser().getRoleid();
        String account = ShiroKit.getUser().getAccount();
        if(ToolUtil.equals(role,"3"))  //教师：只能查看自己的资料
        {
            Wrapper<CmTeacherfile> personalWork = cmTeacherfileEntityWrapper.eq("teacher_account",ShiroKit.getUser().getAccount());
            list =  cmTeacherfileService.selectList(personalWork);
        }
        else if(ToolUtil.equals(role,"4")||ToolUtil.equals(role,"5")||ToolUtil.equals(role,"6")) //助教、学生和PM只能查看本校教师允许被分享的资料
        {
            Wrapper<CmTeacherfile> personalWork = cmTeacherfileEntityWrapper.in("deptid",ShiroKit.getDeptDataScope()).eq("is_share",1);
            list =  cmTeacherfileService.selectList(personalWork);
        }
        else {  //系统管理员可以看到所有资料
            list = cmTeacherfileService.selectList(null);
        }
        List<Map<String,Object>> stringObjectMap = new ArrayList<>();
        Map<String,Object> mp = null;
        for(int i=0;i<list.size();i++){
            mp = BeanKit.beanToMap(list.get(i));
            if(mp!=null){
                stringObjectMap.add(mp);
            }
        }
        return  new CmTeacherfileWarpper(stringObjectMap).warp();
    }

    /**
     * 新增个人资料
     */
//    @RequestMapping(value = "/add")
//    @ResponseBody
//    public Object add(CmTeacherfile cmTeacherfile) {
//        cmTeacherfileService.insert(cmTeacherfile);
//        return SUCCESS_TIP;
//    }
    /**
     * 文件上传具体实现方法;
     *
     * @param file
     * @return
     */
    @RequestMapping(value="/upload",method= RequestMethod.POST)
    @ResponseBody
    public String handleFileUpload(HttpServletRequest request, @RequestParam("file") MultipartFile file, CmTeacherfile cmTeacherfile) throws Exception{
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
            String path = request.getServletContext().getRealPath("/teacherFiles/");
            //上传文件名
            String filename = file.getOriginalFilename();
            File filepath = new File(path,filename);
            //判断路径是否存在，如果不存在就创建一个
            if (!filepath.getParentFile().exists()) {
                filepath.getParentFile().mkdirs();
            } //将上传文件保存到一个目标文件当中
            file.transferTo(new File(path + File.separator + filename));
            System.out.println("文件名："+filename);
            System.out.println("上传文件路径："+path);
            cmTeacherfile.setClassId(220);
            cmTeacherfile.setFileName(filename);
            cmTeacherfile.setFilePath(path+filename);
            cmTeacherfile.setTeacherAccount(ShiroKit.getUser().getAccount());
            cmTeacherfile.setDeptid(ShiroKit.getUser().getDeptId());
            cmTeacherfile.setIsShare(1);
            cmTeacherfileService.insert(cmTeacherfile);
            return "上传成功";

        } else {
            return "上传失败，因为文件是空的.";
        }

    }
    /**
     * 删除个人资料
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer cmTeacherfileId) {
        cmTeacherfileService.deleteById(cmTeacherfileId);
        return SUCCESS_TIP;
    }

    /**
     * 修改个人资料
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(CmTeacherfile cmTeacherfile) {
        cmTeacherfileService.updateById(cmTeacherfile);
        return SUCCESS_TIP;
    }

    /**
     * 个人资料详情
     */
    @RequestMapping(value = "/detail/{cmTeacherfileId}")
    @ResponseBody
    public Object detail(@PathVariable("cmTeacherfileId") Integer cmTeacherfileId) {
        return cmTeacherfileService.selectById(cmTeacherfileId);
    }
}
