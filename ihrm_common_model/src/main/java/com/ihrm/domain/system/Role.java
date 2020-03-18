package com.ihrm.domain.system;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "pe_role")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Role implements Serializable {
    //主键ID
    @Id
    private String id;
    //权限名称
    private String name;

    //说明
    private String description;
    //企业id
    private String companyId;

    //角色与用户多对多
    @JsonIgnore
    @ManyToMany(mappedBy = "roles")//不维护中间表
    private Set<User> users=new HashSet<User>(0);

    /**
     * JsonIgnore
     *    ：忽略json转化
     * */
    //角色和模块多对多

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "pe_role_permission",joinColumns = {@JoinColumn(name = "role_id",referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name="permission_id",referencedColumnName = "id")}
    )
    private Set<Permission> permissions=new HashSet<Permission>(0);
}
