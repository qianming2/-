package com.ihrm.domain.system.response;

import com.ihrm.domain.system.Permission;
import com.ihrm.domain.system.Role;
import com.ihrm.domain.system.User;
import lombok.Data;
import org.crazycake.shiro.AuthCachePrincipal;

import java.io.Serializable;
import java.util.*;

@Data
public class ProfileResult implements Serializable, AuthCachePrincipal {
    private String mobile;
    private String username;
    private String company;
    private String companyId;
    private Map<String,Object> roles =new HashMap<String,Object>();

    /**
     * 根据传入的权限的类别找到对应的权限
     * */
    public ProfileResult(User user, List<Permission> list){
        this.mobile=user.getMobile();
        this.username=user.getUsername();
        this.company=user.getCompanyName();
        this.companyId = user.getCompanyId();
        Set<String> menus = new HashSet<String>();
        Set<String> points =  new HashSet<String>();
        Set<String> apis =  new HashSet<String>();

        for (Permission perm:list){
            String code = perm.getCode();
            if (perm.getType()==1){
                menus.add(code);
            }else  if (perm.getType()== 2){
                points.add(code);
            }else{
                apis.add(code);
            }
        }
        this.roles.put("menus",menus);
        this.roles.put("points",points);
        this.roles.put("apis",apis);
    }
    /**
     * 查找用户的角色
     *
     * 根据权限的类别找到对应的权限
     * */
    public ProfileResult(User user){
        this.mobile=user.getMobile();
        this.username=user.getUsername();
        this.company=user.getCompanyName();
        this.companyId = user.getCompanyId();

        Set<Role> roles = user.getRoles();
        Set<String> menus = new HashSet<String>();
        Set<String> points =  new HashSet<String>();
        Set<String> apis =  new HashSet<String>();

        for (Role role:roles){
            Set<Permission> perms = role.getPermissions();
            for (Permission perm:perms){
                String code = perm.getCode();
                if (perm.getType()== 1){
                    menus.add(code);
                }else  if (perm.getType()== 2){
                    points.add(code);
                }else {
                    apis.add(code);
                }
            }
        }
        this.roles.put("menus",menus);
        this.roles.put("points",points);
        this.roles.put("apis",apis);
    }

    @Override
    public String getAuthCacheKey() {
        return null;
    }
}
