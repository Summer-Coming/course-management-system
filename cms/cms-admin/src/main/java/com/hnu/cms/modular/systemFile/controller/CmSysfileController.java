package com.stylefeng.guns.modular.systemFile.controller;

import com.stylefeng.guns.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import com.stylefeng.guns.modular.system.model.CmSysfile;
import com.stylefeng.guns.modular.systemFile.service.ICmSysfileService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;

/**
 * 推荐资料控制器
 * @author qqzj
 * @since 2018-12-07
 */
@Controller
@RequestMapping("/cmSysfile")
public class CmSysfileController extends BaseController {

    private String PREFIX = "/systemFile/cmSysfile/";
    private Integer downloadId = 0;
    @Autowired
    private ICmSysfileService cmSysfileService;

    /**
     * 跳转到推荐资料首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "cmSysfile.html";
    }

    /**
     * 跳转到添加推荐资料
     */
    @RequestMapping("/cmSysfile_upload")
    public String cmSysfileAdd() {
        return PREFIX + "cmSysfile_upload.html";
    }


    /**
     * 跳转到下载查看作业
     */
    @RequestMapping("/cmSysfile_download/{cmSysfileId}")
    public String cmSysfileDownload(@PathVariable Integer cmSysfileId, Model model) {
        CmSysfile cmSysfile = cmSysfileService.selectById(cmSysfileId);
        System.out.println("id是"+cmSysfile.getId());
        downloadId = cmSysfileId;
        return PREFIX + "cmSysfile_download.html";
    }

    /**
     * 跳转到修改推荐资料
     */
    @RequestMapping("/cmSysfile_update/{cmSysfileId}")
    public String cmSysfileUpdate(@PathVariable Integer cmSysfileId, Model model) {
        CmSysfile cmSysfile = cmSysfileService.selectById(cmSysfileId);
        model.addAttribute("item",cmSysfile);
        LogObjectHolder.me().set(cmSysfile);
        return PREFIX + "cmSysfile_edit.html";
    }

    /**
     * 获取推荐资料列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return cmSysfileService.selectList(null);
    }

    /**
     * 上传推荐资料
     */
    @RequestMapping(value = "/upload",method= RequestMethod.POST)
    @ResponseBody
//        public Object add(CmSysfile cmSysfile) {
//        cmSysfileService.insert(cmSysfile);
//        return SUCCESS_TIP;
//    }
    public String handleFileUpload(HttpServletRequest request, @RequestParam("file") MultipartFile file, CmSysfile cmSysfile) throws Exception {
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
            String path = request.getServletContext().getRealPath("/systemFiles/");
            //上传文件名
            String filename = file.getOriginalFilename();
            File filepath = new File(path, filename);
            //判断路径是否存在，如果不存在就创建一个
            if (!filepath.getParentFile().exists()) {
                filepath.getParentFile().mkdirs();
            } //将上传文件保存到一个目标文件当中
            file.transferTo(new File(path + File.separator + filename));
            System.out.println("文件名：" + filename);
            System.out.println("上传文件路径：" + path);
//            cmTeacherfile.setClassId(220);
            cmSysfile.setFileName(filename);
            cmSysfile.setFilePath(path+filename);
            cmSysfileService.insert(cmSysfile);
            return "上传成功";

        } else {
            return "上传失败，因为文件是空的.";
        }
    }

        /**
         * 下载推荐资料
         */
    @RequestMapping(value = "/cmSysfile_download/download")
    @ResponseBody
//    public Object download(CmSysfile cmSysfile) {
//        cmSysfileService.insert(cmSysfile);
//        return SUCCESS_TIP;
//    }
    public String download(HttpServletRequest req, HttpServletResponse resp,CmSysfile cmSysfile) throws IOException {
        cmSysfile = cmSysfileService.selectById(downloadId);
        System.out.println("!!!!!!!!!!!!!!!!!!cmSysfile到底是何面目:" + cmSysfile);
        System.out.println("进入download啦！！！！！！！！！！！！！！");
        String Url = cmSysfile.getFilePath();
        System.out.println("!!!!!!!!!url" + Url);
        String fileName = cmSysfile.getFileName();
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
     * 删除推荐资料
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer cmSysfileId) {
        cmSysfileService.deleteById(cmSysfileId);
        return SUCCESS_TIP;
    }

    /**
     * 修改推荐资料
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(CmSysfile cmSysfile) {
        cmSysfileService.updateById(cmSysfile);
        return SUCCESS_TIP;
    }

    /**
     * 推荐资料详情
     */
    @RequestMapping(value = "/detail/{cmSysfileId}")
    @ResponseBody
    public Object detail(@PathVariable("cmSysfileId") Integer cmSysfileId) {
        return cmSysfileService.selectById(cmSysfileId);
    }

}
