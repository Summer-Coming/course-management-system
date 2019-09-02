package com.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * VIEW
 * </p>
 *
 * @author qqzj
 * @since 2018-12-26
 */
@TableName("cm_pairteam")
public class CmPairteam extends Model<CmPairteam> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Integer id;
    /**
     * 账号
     */
    private String account;
    /**
     * 名字
     */
    private String name;
    /**
     * 角色id
     */
    private String roleid;
    private String teamId;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoleid() {
        return roleid;
    }

    public void setRoleid(String roleid) {
        this.roleid = roleid;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "CmPairteam{" +
        "id=" + id +
        ", account=" + account +
        ", name=" + name +
        ", roleid=" + roleid +
        ", teamId=" + teamId +
        "}";
    }
}
