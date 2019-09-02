package com.stylefeng.guns.modular.GroupTeam.warpper;

import com.stylefeng.guns.core.base.warpper.BaseControllerWarpper;
import com.stylefeng.guns.core.common.constant.factory.ConstantFactory;

import java.util.List;
import java.util.Map;

public class CmGroupteamWarpper extends BaseControllerWarpper {
    public CmGroupteamWarpper(List<Map<String, Object>> list){
        super(list);
    }
    @Override
    protected void warpTheMap(Map<String, Object> map) {
        map.put("roleName", ConstantFactory.me().getDeptName((Integer) map.get("roleid")));
    }
}
