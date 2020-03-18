package com.ihrm.domain.system;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 菜单资源
 * */

@Entity
@Data
@Table(name = "pe_permission_menu")
public class PermissionMenu implements Serializable {
    //主键
    @Id
    private String id;
    //权限代码
    private String menuIcon;
    //排序号
    private String menuOrder;
}
