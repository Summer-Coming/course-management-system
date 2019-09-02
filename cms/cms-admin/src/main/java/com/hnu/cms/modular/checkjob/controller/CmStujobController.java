package com.stylefeng.guns.modular.checkjob.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.support.BeanKit;
import com.stylefeng.guns.core.util.ToolUtil;
import com.stylefeng.guns.modular.checkjob.warpper.CmStujobWarpper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.CmStujob;
import com.stylefeng.guns.modular.checkjob.service.ICmStujobService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 查看作业控制器
 *
 */
@Controller
@RequestMapping("/cmStujob")
public class CmStujobController extends BaseController {

    private String PREFIX = "/checkjob/cmStujob/";
    private Integer downloadId = 0;
    private Integer ID = null;
    @Autowired
    private ICmStujobService cmStujobService;

    /**
     * 跳转到查看作业首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "cmStujob.html";
    }

    /**
     * 跳转到添加查看作业
     */
    @RequestMapping("/cmStujob_download/{cmStujobId}")
    public String cmStujobDownload(@PathVariable Integer cmStujobId, Model model) {
        CmStujob cmStujob = cmStujobService.selectById(cmStujobId);
        System.out.println("id是"+cmStujob.getId());
        downloadId = cmStujobId;
        return PREFIX + "cmStujob_download.html";
    }

    /**
     * 跳转到修改查看作业
     */
    @RequestMapping("/cmStujob_update/{cmStujobId}")
    public String cmStujobUpdate(@PathVariable Integer cmStujobId, Model model) {
        CmStujob cmStujob = cmStujobService.selectById(cmStujobId);
        model.addAttribute("item",cmStujob);
        LogObjectHolder.me().set(cmStujob);
        ID = cmStujobId;
        return PREFIX + "cmStujob_edit.html";
    }

    /**
     * 获取查看作业列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
//        return cmStujobService.selectList(null);
        List<CmStujob> list = new ArrayList<>();
        String role = ShiroKit.getUser().getRoleid();
        EntityWrapper<CmStujob> cmStuuploadEntityWrapper = new EntityWrapper<>();
        if(ToolUtil.equals(role,"5")||ToolUtil.equals(role,"6"))//如果是学生和PM的话，它们只能查看自己交上去的作业
        {
            Wrapper<CmStujob> studentWork = cmStuuploadEntityWrapper.eq("stu_id",ShiroKit.getUser().getAccount());
            list =  cmStujobService.selectList(studentWork);
        }
        else if(ToolUtil.equals(role,"4"))//如果是助教的话，他只能查看自己要批改的学生的作业
        {
            Wrapper<CmStujob> assistantWork = cmStuuploadEntityWrapper.eq("ass_id",ShiroKit.getUser().getAccount());
            list =  cmStujobService.selectList(assistantWork);
        }
        else if(ToolUtil.equals(role,"3")) //如果是教师的话，只能查看本校学生的作业
        {
            Wrapper<CmStujob> teacherWork = cmStuuploadEntityWrapper.in("deptid",ShiroKit.getDeptDataScope());
            list = cmStujobService.selectList(teacherWork);
        }
        else{  //如果是系统管理员的话，能查看所有的作业
            list = cmStujobService.selectList(null);
        }
        List<Map<String,Object>> stringObjectMap = new ArrayList<>();
        Map<String,Object> mp = null;
        for(int i=0;i<list.size();i++){
            mp = BeanKit.beanToMap(list.get(i));
            if(mp!=null){
                stringObjectMap.add(mp);
            }
        }
        return  new CmStujobWarpper(stringObjectMap).warp();
    }

    /**
     * 新增下载查看作业
     */
    @RequestMapping(value = "/cmStujob_download/download")
    @ResponseBody
    public String download(HttpServletRequest req, HttpServletResponse resp,CmStujob cmStujob) throws IOException {
        cmStujob = cmStujobService.selectById(downloadId);
        System.out.println("!!!!!!!!!!!!!!!!!!cmstujob到底是何面目:"+cmStujob);
        System.out.println("进入download啦！！！！！！！！！！！！！！");
        String Url =cmStujob.getFilePath();
        System.out.println("!!!!!!!!!url"+Url);
//        C:\Users\青青子衿\AppData\Local\Temp\tomcat-docbase.6200013557007437920.8080\学生上传的作业\
        String fileName = cmStujob.getFileName();
//        System.out.println("fileName---->"+fileName);
//        String filePath = "d:";
////        File file = saveUrlAs(photoUrl, filePath + fileName,"GET");
////        System.out.println("Run ok!/n<BR>Get URL file " + file);
//        //System.out.println("fileName---->"+filePath);
//
//        //创建不同的文件夹目录
//        File file=new File(filePath);
//        //判断文件夹是否存在
//        if (!file.exists())
//        {
//            //如果文件夹不存在，则创建新的的文件夹
//            file.mkdirs();
//        }
//        FileOutputStream fileOut = null;
//        HttpURLConnection conn = null;
//        InputStream inputStream = null;

        // Url是指欲下载的文件的路径。
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
//        try
//        {
//            // 建立链接
//            URL httpUrl=new URL(Url);
//            conn=(HttpURLConnection) httpUrl.openConnection();
//            //以Post方式提交表单，默认get方式
//            conn.setRequestMethod("get");
//            conn.setDoInput(true);
//            conn.setDoOutput(true);
//            // post方式不能使用缓存
//            conn.setUseCaches(false);
//            //连接指定的资源
//            conn.connect();
//            //获取网络输入流
//            inputStream=conn.getInputStream();
//            BufferedInputStream bis = new BufferedInputStream(inputStream);
//            //判断文件的保存路径后面是否以/结尾
//            if (!filePath.endsWith("/")) {
//
//                filePath += "/";
//
//            }
//            //写入到文件（注意文件保存路径的后面一定要加上文件的名称）
//            fileOut = new FileOutputStream(filePath+fileName);
//            BufferedOutputStream bos = new BufferedOutputStream(fileOut);
//
//            byte[] buf = new byte[4096];
//            int length = bis.read(buf);
//            //保存文件
//            while(length != -1)
//            {
//                bos.write(buf, 0, length);
//                length = bis.read(buf);
//            }
//            bos.close();
//            bis.close();
//            conn.disconnect();
//        } catch (Exception e)
//        {
//            e.printStackTrace();
//            System.out.println("抛出异常！！");
//        }
//    }

    /**
     * 删除查看作业
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer cmStujobId) {
        cmStujobService.deleteById(cmStujobId);
        return SUCCESS_TIP;
    }

    /**
     * 修改查看作业
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(CmStujob cmStujob) {
//        System.out.println("!!!!!!!!!!!!!!!!!!第一个第一个cmstujob到底是何面目:"+cmStujob);
//        System.out.println("!!!!!!!!!!!!"+cmStujob.getFilePath());
        cmStujob.setId(ID);
        cmStujobService.updateById(cmStujob);
//        System.out.println("!!!!!!!!!!!!!!!!!!cmstujob到底是何面目:"+cmStujob);
        return SUCCESS_TIP;
    }

    /**
     * 查看作业详情
     */
    @RequestMapping(value = "/detail/{cmStujobId}")
    @ResponseBody
    public Object detail(@PathVariable("cmStujobId") Integer cmStujobId) {
        return cmStujobService.selectById(cmStujobId);
    }
}
