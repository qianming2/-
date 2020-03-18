package com.ihrm.domain.system.response;

import com.ihrm.domain.system.Role;
import com.ihrm.domain.system.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class UserResult implements Serializable {

    //用户id
    @Id
    private String id;
    //手机号码
    private String mobile;
    //用户名称
    private String username;
    //用户密码
    private String password;
    //启用状态 0是禁用，1是启用
    private Integer enableState;
    //创建时间
    private Date createTime;
    //部门ID
    private String departmentId;
    //入职时间
    private Date timeOfEntry;
    //聘用形式
    private Integer formOfEmployment;
    //工号
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

    private List<String> roleIds=new ArrayList<String>();

    private String staffPhone;//用户头像
    public UserResult(User user){
        BeanUtils.copyProperties(user,this);
        for (Role role:user.getRoles()){
            this.roleIds.add(role.getId());
        }
    }
}
