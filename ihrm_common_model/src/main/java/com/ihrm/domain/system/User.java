package com.ihrm.domain.system;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ihrm.domain.poi.ExcelAttribute;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "bs_user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User implements Serializable {
    //用户id
    @Id
    private String id;
    //手机号码
    @ExcelAttribute(sort = 2)
    private String mobile;
    //用户名称
    @ExcelAttribute(sort = 1)
    private String username;
    //用户密码
    private String password;
    //启用状态 0是禁用，1是启用
    private Integer enableState;
    //创建时间
    private Date createTime;
    //部门ID
    @ExcelAttribute(sort = 6)
    private String departmentId;
    //入职时间
    @ExcelAttribute(sort = 5)
    private Date timeOfEntry;
    //聘用形式
    @ExcelAttribute(sort = 4)
    private Integer formOfEmployment;
    //工号
    @ExcelAttribute(sort = 3)
    private String workNumber;
    //管理形式
    private String formOfManagement;
    //工作城市
    private String workingCity;
    //转正时间
    private Date correctionTime;
    //在职状态 1.在职  2.离职
    private Integer inServiceStatus;
    //部门名字
    private String departmentName;
    //企业ID
    private String companyId;
    //企业名字
    private String companyName;

    /**
     * level
     *  String
     *     saasAdmin saas管理员具备所有权限
     *     coAdmin 企业管理（创建租户企业管理的时候添加）
     *     user  普通用户（需要分配角色）
     * */
    private String Level;

    private String staffPhoto;//用户数据

    public User(Object[] values){
        //  用户名  手机号  工号   聘用形式  入职时间  部门编码
        this.username=values[1].toString();
        this.mobile=values[2].toString();
        this.workNumber=new DecimalFormat("#").format(values[3]).toString();
        this.formOfEmployment=((Double)values[4]).intValue();
        this.timeOfEntry=(Date)values[5];
        this.departmentId=values[6].toString();//部门编码不等于！=部门id
    }

    /**
     * JsonIgnore
     *    ：忽略json转化
     * */
    //用户和角色多对多
    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "pe_user_role",joinColumns = {@JoinColumn(name = "user_id",referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name="role_id",referencedColumnName = "id")}
    )
    private Set<Role> roles=new HashSet<Role>();


}
