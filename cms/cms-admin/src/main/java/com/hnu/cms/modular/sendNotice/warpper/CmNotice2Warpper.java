package com.stylefeng.guns.modular.sendNotice.warpper;

import com.stylefeng.guns.core.base.warpper.BaseControllerWarpper;
import com.stylefeng.guns.core.common.constant.factory.ConstantFactory;

import java.util.Map;

public class CmNotice2Warpper extends BaseControllerWarpper {
    public CmNotice2Warpper(Object list) {
        super(list);
    }

    @Override
    public void warpTheMap(Map<String, Object> map) {
        Integer creater = (Integer) map.get("creater");
        map.put("createrName", ConstantFactory.me().getUserNameById(creater));
        map.put("deptName", ConstantFactory.me().getDeptName((Integer) map.get("deptid")));
    }
}
