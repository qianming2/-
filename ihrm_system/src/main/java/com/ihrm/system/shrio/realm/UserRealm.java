package com.ihrm.system.shrio.realm;


import com.ihrm.common.shiro.realm.IhrmRealm;
import com.ihrm.domain.system.Permission;
import com.ihrm.domain.system.User;
import com.ihrm.domain.system.response.ProfileResult;
import com.ihrm.system.service.PermissionService;
import com.ihrm.system.service.UserService;
import org.apache.shiro.authc.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

public class UserRealm extends IhrmRealm {
    @Autowired
    private UserService userService;
    @Autowired
    private PermissionService permissionService;
    //认证方法
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
       //1.获取用户的手机号码和密码
        UsernamePasswordToken upToken=(UsernamePasswordToken)token;
        String mobile = upToken.getUsername();
        String password = new String(upToken.getPassword());
        //2.根据手机号码查找对象
        User user = userService.findByMobile(mobile);
        //3.判断用户是否存在,用户密码是否和输入密码一致
        if (user!=null && user.getPassword().equals(password)){
            //4.构造安全数据并返回（安全数据，用户基本信息，权限信息 profileResult）
            ProfileResult result=null;
            if ("user".equals(user.getLevel())){
                result=new ProfileResult(user);
            }else {
                HashMap map = new HashMap();
                if ("coAdmin".equals(user.getLevel())){
                    map.put("en_visible","1");
                }
                List<Permission> list = permissionService.findAll(map);
                result = new ProfileResult(user, list);

            }
            //构造方法返回安全数据 密码  realm域名
            SimpleAuthenticationInfo  info = new SimpleAuthenticationInfo(result, user.getPassword(), this.getName());
            return info;

        }
        //返回null  会抛出异常用户名和密码不匹配
        return null;
    }




}
