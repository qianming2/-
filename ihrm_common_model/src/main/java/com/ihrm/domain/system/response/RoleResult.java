package com.ihrm.domain.system.response;


import com.ihrm.domain.system.Permission;
import com.ihrm.domain.system.Role;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import java.io.Serializable;
import java.util.ArrayList;

import java.util.List;


@Getter
@Setter
public class RoleResult implements Serializable {

    //主键ID
    private String id;
    //权限名称
    private String name;

    //说明
    private String description;
    //企业id
    private String companyId;

    private List<String> permIds=new ArrayList<String>();

    public RoleResult(Role role) {
        BeanUtils.copyProperties(role,this);
        for (Permission permission:role.getPermissions()){
            this.permIds.add(permission.getId());
        }

    }
}
