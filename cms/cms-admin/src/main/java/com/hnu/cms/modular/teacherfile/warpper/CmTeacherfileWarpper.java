package com.stylefeng.guns.modular.teacherfile.warpper;

import com.stylefeng.guns.core.base.warpper.BaseControllerWarpper;
import com.stylefeng.guns.core.constant.IsShare;

import java.util.List;
import java.util.Map;

public class CmTeacherfileWarpper extends BaseControllerWarpper {
    public CmTeacherfileWarpper(List<Map<String, Object>> list){
        super(list);
    }

    @Override
    protected void warpTheMap(Map<String, Object> map) {
//        map.put("ifLockName", ConstantFactory.me().getIfLockName((Integer) map.get("ifLock")));
//        map.put("ifAuditName", ConstantFactory.me().getIfAuditName((Integer) map.get("ifAudit")));
        map.put("isShareName", IsShare.valueOf((Integer) map.get("isShare")));
    }
}
