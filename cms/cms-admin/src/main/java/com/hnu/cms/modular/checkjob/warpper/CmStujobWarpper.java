package com.stylefeng.guns.modular.checkjob.warpper;

import com.stylefeng.guns.core.base.warpper.BaseControllerWarpper;
import com.stylefeng.guns.core.common.constant.factory.ConstantFactory;
import com.stylefeng.guns.core.constant.IfAudit;
import com.stylefeng.guns.core.constant.IfLock;
import io.swagger.models.auth.In;

import java.util.List;
import java.util.Map;

public class CmStujobWarpper extends BaseControllerWarpper {

    public CmStujobWarpper(List<Map<String, Object>> list){
        super(list);
    }

    @Override
    protected void warpTheMap(Map<String, Object> map) {
//        map.put("ifLockName", ConstantFactory.me().getIfLockName((Integer) map.get("ifLock")));
//        map.put("ifAuditName", ConstantFactory.me().getIfAuditName((Integer) map.get("ifAudit")));
        map.put("ifLockName", IfLock.valueOf((Integer) map.get("ifLock")));
        map.put("ifAuditName", IfAudit.valueOf((Integer) map.get("ifAudit")));
    }
}
