package com.xuecheng.framework.domain.ucenter.ext;

import com.xuecheng.framework.domain.ucenter.XcMenu;
import com.xuecheng.framework.domain.ucenter.XcUser;

import java.util.List;

/**
 * Created by admin on 2018/3/20.
 */

public class XcUserExt extends XcUser {

    //权限信息
    private List<XcMenu> permissions;

    //企业信息
    private String companyId;

    public List<XcMenu> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<XcMenu> permissions) {
        this.permissions = permissions;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
}
