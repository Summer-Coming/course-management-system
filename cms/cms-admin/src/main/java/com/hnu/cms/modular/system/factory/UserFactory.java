package com.stylefeng.guns.modular.system.factory;

import com.stylefeng.guns.core.util.ToolUtil;
import com.stylefeng.guns.modular.system.model.User;
import com.stylefeng.guns.modular.system.transfer.UserDto;
import org.springframework.beans.BeanUtils;

/**
 * 用户创建工厂
 *
 *
 * @author qqzj
 * @since 2018-10-19
 */
public class UserFactory {

    public static User createUser(UserDto userDto) {
        if (userDto == null) {
            return null;
        } else {
            User user = new User();
            BeanUtils.copyProperties(userDto, user);
            return user;
        }
    }

    public static User editUser(UserDto newUser, User oldUser) {
        if (newUser == null || oldUser == null) {
            return oldUser;
        } else {
            if (ToolUtil.isNotEmpty(newUser.getAvatar())) {
                oldUser.setAvatar(newUser.getAvatar());
            }
            if (ToolUtil.isNotEmpty(newUser.getName())) {
                oldUser.setName(newUser.getName());
            }
            if (ToolUtil.isNotEmpty(newUser.getBirthday())) {
                oldUser.setBirthday(newUser.getBirthday());
            }
            if (ToolUtil.isNotEmpty(newUser.getDeptid())) {
                oldUser.setDeptid(newUser.getDeptid());
            }
            if (ToolUtil.isNotEmpty(newUser.getSex())) {
                oldUser.setSex(newUser.getSex());
            }
            if (ToolUtil.isNotEmpty(newUser.getEmail())) {
                oldUser.setEmail(newUser.getEmail());
            }
            if (ToolUtil.isNotEmpty(newUser.getPhone())) {
                oldUser.setPhone(newUser.getPhone());
            }
            if (ToolUtil.isNotEmpty(newUser.getAssistantId())) {
                oldUser.setAssistantId(newUser.getAssistantId());
            }
            if (ToolUtil.isNotEmpty(newUser.getGroupId())) {
                oldUser.setGroupId(newUser.getGroupId());
            }
            if (ToolUtil.isNotEmpty(newUser.getTeamId())) {
                oldUser.setTeamId(newUser.getTeamId());
            }
            if (ToolUtil.isNotEmpty(newUser.getClassId())) {
                oldUser.setClassId(newUser.getClassId());
            }
            return oldUser;
        }
    }
}
