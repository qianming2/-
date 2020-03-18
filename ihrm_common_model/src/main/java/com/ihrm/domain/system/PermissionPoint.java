package com.ihrm.domain.system;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
/**
 * 按钮资源
 * */

@Entity
@Table(name = "pe_permission_point")
@Data
public class PermissionPoint implements Serializable {
    //主键
    @Id
    private String id;
    //按钮类型
    private String pointClass;

    //按钮icon
    private String pointIcon;

    //状态
    private String pointStatus;
}
