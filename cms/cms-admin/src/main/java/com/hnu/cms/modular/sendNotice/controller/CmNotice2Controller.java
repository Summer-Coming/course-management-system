package com.stylefeng.guns.modular.sendNotice.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.support.BeanKit;
import com.stylefeng.guns.modular.sendNotice.warpper.CmNotice2Warpper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.CmNotice2;
import com.stylefeng.guns.modular.sendNotice.service.ICmNotice2Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 发送通知控制器
 */
@Controller
@RequestMapping("/cmNotice2")
public class CmNotice2Controller extends BaseController {

    private String PREFIX = "/sendNotice/cmNotice2/";

    @Autowired
    private ICmNotice2Service cmNotice2Service;

    /**
     * 跳转到发送通知首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "cmNotice2.html";
    }

    /**
     * 跳转到添加发送通知
     */
    @RequestMapping("/cmNotice2_add")
    public String cmNotice2Add() {
        return PREFIX + "cmNotice2_add.html";
    }

    /**
     * 跳转到修改发送通知
     */
    @RequestMapping("/cmNotice2_update/{cmNotice2Id}")
    public String cmNotice2Update(@PathVariable Integer cmNotice2Id, Model model) {
        CmNotice2 cmNotice2 = cmNotice2Service.selectById(cmNotice2Id);
        model.addAttribute("item",cmNotice2);
        LogObjectHolder.me().set(cmNotice2);
        return PREFIX + "cmNotice2_edit.html";
    }

    /**
     * 获取发送通知列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        EntityWrapper<CmNotice2> cmNotice2EntityWrapper = new EntityWrapper<>();
        Wrapper<CmNotice2> personalWork = cmNotice2EntityWrapper.in("deptid", ShiroKit.getDeptDataScope());
        List<CmNotice2> list = cmNotice2Service.selectList(personalWork);
        List<Map<String,Object>> stringObjectMap = new ArrayList<>();
        Map<String,Object> mp = null;
        for(int i=0;i<list.size();i++){
            mp = BeanKit.beanToMap(list.get(i));
            if(mp!=null){
                stringObjectMap.add(mp);
            }
        }
        return new CmNotice2Warpper(stringObjectMap).warp();
//                return cmNotice2Service.selectList(null);
    }

    /**
     * 新增发送通知
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(CmNotice2 cmNotice2) {
        cmNotice2Service.insert(cmNotice2);
        return SUCCESS_TIP;
    }

    /**
     * 删除发送通知
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer cmNotice2Id) {
        cmNotice2Service.deleteById(cmNotice2Id);
        return SUCCESS_TIP;
    }

    /**
     * 修改发送通知
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(CmNotice2 cmNotice2) {
        cmNotice2Service.updateById(cmNotice2);
        return SUCCESS_TIP;
    }

    /**
     * 发送通知详情
     */
    @RequestMapping(value = "/detail/{cmNotice2Id}")
    @ResponseBody
    public Object detail(@PathVariable("cmNotice2Id") Integer cmNotice2Id) {
        return cmNotice2Service.selectById(cmNotice2Id);
    }
}
