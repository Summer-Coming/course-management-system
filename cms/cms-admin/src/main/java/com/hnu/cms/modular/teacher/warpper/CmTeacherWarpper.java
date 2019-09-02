package com.stylefeng.guns.modular.teacher.warpper;

import com.stylefeng.guns.core.base.warpper.BaseControllerWarpper;
import com.stylefeng.guns.core.common.constant.factory.ConstantFactory;
import com.stylefeng.guns.modular.system.model.CmTeacher;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.reflection.wrapper.BaseWrapper;

import java.util.List;
import java.util.Map;

public class CmTeacherWarpper extends BaseControllerWarpper {

    public CmTeacherWarpper(List<Map<String, Object>> list){
        super(list);
    }
    @Override
    protected void warpTheMap(Map<String, Object> map) {
        map.put("deptName", ConstantFactory.me().getDeptName((Integer) map.get("deptid")));
        map.put("statusName", ConstantFactory.me().getStatusName((Integer) map.get("status")));
    }
}
