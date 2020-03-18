package com.ihrm.system.service;

import com.ihrm.common.service.BaseService;
import com.ihrm.common.util.IdWorker;
import com.ihrm.common.util.PermissionConstants;
import com.ihrm.domain.system.Permission;
import com.ihrm.domain.system.Role;
import com.ihrm.system.dao.PermissionDao;
import com.ihrm.system.dao.RoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class RoleService extends BaseService {
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;
    @Autowired
    private IdWorker idWorker;
    /**
     * 保存角色

     * */
    public void save(Role role){
        //基本属性的设置
        String id= idWorker.nextId()+"";
        role.setId(id);
        //保存角色
        roleDao.save(role);
    }
    /**
     * 更新角色
     * */
    public void update(Role role){
        //根据id查询角色
        Role temp = roleDao.findById(role.getId()).get();
        //设置角色属性
        temp.setName(role.getName());
        temp.setDescription(role.getDescription());
        //更新角色
        roleDao.save(temp);
    }
    /**
     * 根据id角色
     * */
    public Role findById(String id){
        return roleDao.findById(id).get();
    }
    /**
     * 删除用户
     * */
    public void deleteById(String id){
        roleDao.deleteById(id);
    }
    /**
     * 查询角色列表
     * */
    public Page<Role> findAll(int page,int pagesize,String company_id){
        Page<Role> pageUser = roleDao.findAll(getSpec(company_id),new PageRequest(page-1, pagesize));
        return pageUser;
    }
    /**
     * 查询角色列表不分页
     * */
    public List<Role> findRoleList(String company_id){
        List list = roleDao.findAll(getSpec(company_id));
        return list;
    }
    /**
     * 分配权限
     * */
    public void assignPerms(String roleId,List<String> permIds){
        //1.获取分配的角色对象
        Role role = roleDao.findById(roleId).get();
        //2.构造角色权限的集合
        Set<Permission> perms = new HashSet<Permission>();
        for (String permId:permIds){
            Permission permission = permissionDao.findById(permId).get();
            //根据父id的类型查询API的权限
            List<Permission> apiList = permissionDao.findByTypeAndPid(PermissionConstants.PERMISSION_API, permission.getId());
            perms.addAll(apiList);//自定义赋予API权限
            perms.add(permission);//当前菜单或按钮的权限
        }
        //3.设置角色和权限的关系
        role.setPermissions(perms);
        //4.更新角色
        roleDao.save(role);
    }
}
