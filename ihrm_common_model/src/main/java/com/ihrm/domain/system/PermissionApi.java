package com.ihrm.domain.system;

import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * api资源
 * */

@Entity
@Table(name = "pe_permission_api")
@Data
public class PermissionApi implements Serializable {
    //主键
    @Id
    private String id;
    //链接
    private String apiUrl;
    //请求类型
    private String apiMethod;
    //权限等级，1为通用接口权限，2为需校验接口权限
    private String apiLevel;

}
