package com.ihrm.system.service;

import com.ihrm.common.entity.ResultCode;
import com.ihrm.common.exception.CommonException;
import com.ihrm.common.util.BeanMapUtils;
import com.ihrm.common.util.IdWorker;
import com.ihrm.common.util.PermissionConstants;
import com.ihrm.domain.system.*;
import com.ihrm.system.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PermissionService {
    @Autowired
    private PermissionDao permissionDao;
    @Autowired
    private PermissionMenuDao permissionMenuDao;
    @Autowired
    private PermissionPointDao permissionPointDao;
    @Autowired
    private PermissionApiDao permissionApiDao;
    @Autowired
    private IdWorker idWorker;
    /**
     * 保存权限
     * */
    public void save(Map<String,Object> map) throws Exception {
        //1.设置主键的值
        String id= idWorker.nextId()+"";
        Permission permission = BeanMapUtils.mapToBean(map, Permission.class);
        permission.setId(id);
        //2.根据类型构造同资源对象（菜单，按钮，api）
        int type=permission.getType();
        switch (type){
            case PermissionConstants.PERMISSION_MENU:
                PermissionMenu permissionMenu = BeanMapUtils.mapToBean(map, PermissionMenu.class);
                permissionMenu.setId(id);
                permissionMenuDao.save(permissionMenu);
                break;
            case PermissionConstants.PERMISSION_POINT:
                PermissionPoint permissionPoint = BeanMapUtils.mapToBean(map, PermissionPoint.class);
                permissionPoint.setId(id);
                permissionPointDao.save(permissionPoint);
                break;
            case PermissionConstants.PERMISSION_API:
                PermissionApi permissionApi = BeanMapUtils.mapToBean(map, PermissionApi.class);
                permissionApi.setId(id);
                permissionApiDao.save(permissionApi);
                break;
            default: throw new CommonException(ResultCode.FAIL);
        }
        //保存角色
        permissionDao.save(permission);
    }
    /**
     * 更新权限
     * */
    public void update(Map<String,Object> map) throws Exception {
        Permission permission = BeanMapUtils.mapToBean(map, Permission.class);
        //1.通过传递的权限id查询权限
        Permission temp = permissionDao.findById(permission.getId()).get();
        temp.setName(permission.getName());
        temp.setCode(permission.getCode());
        temp.setDescription(permission.getDescription());
        temp.setEnVisible(permission.getEnVisible());
        //2.根据类型构造同资源对象（菜单，按钮，api）
        int type=permission.getType();
        switch (type){
            case PermissionConstants.PERMISSION_MENU:
                PermissionMenu permissionMenu = BeanMapUtils.mapToBean(map, PermissionMenu.class);
                permissionMenu.setId(permission.getId());
                permissionMenuDao.save(permissionMenu);
                break;
            case PermissionConstants.PERMISSION_POINT:
                PermissionPoint permissionPoint = BeanMapUtils.mapToBean(map, PermissionPoint.class);
                permissionPoint.setId(permission.getId());
                permissionPointDao.save(permissionPoint);
                break;
            case PermissionConstants.PERMISSION_API:
                PermissionApi permissionApi = BeanMapUtils.mapToBean(map, PermissionApi.class);
                permissionApi.setId(permission.getId());
                permissionApiDao.save(permissionApi);
                break;
            default: throw new CommonException(ResultCode.FAIL);
        }
        //更新权限
        permissionDao.save(temp);
    }
    /**
     * 查询权限列表
     *    type   :查询权限列表type 0  菜单+按钮（权限点） 1  菜单  2  按钮（权限点）  3 API接口
     *    enVisible :0  查询所有saas平台的最高权限  1 查询企业权限
     *    pid  :父id
     */
    public List<Permission> findAll(Map<String, Object> map){
        Specification<Permission> spec=new Specification<Permission>() {
            /**
             * 动态拼接条件
             * */
            @Override
            public Predicate toPredicate(Root<Permission> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Object> list = new ArrayList<>();
                /**
                 * 根据pid查询
                 * */
                if (!StringUtils.isEmpty(map.get("pid"))){
                    list.add(criteriaBuilder.equal(root.get("pid").as(String.class),(String)map.get("pid")));
                }
                /**
                 * 根据请求的en_visible查询
                 * */
                if (!StringUtils.isEmpty(map.get("en_visible"))){
                    list.add(criteriaBuilder.equal(root.get("en_visible").as(String.class),(String)map.get("en_visible")));
                }
                /**
                 * 根据type查询
                 * */
                if (!StringUtils.isEmpty(map.get("type"))){
                    String ty = (String)map.get("type");
                    CriteriaBuilder.In<Object> in = criteriaBuilder.in(root.get("type"));
                    if ("0".equals(ty)){
                        in.value(1).value(2);
                    }else {
                        in.value(Integer.parseInt(ty));
                    }
                }
                return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
            }
        };
        return permissionDao.findAll(spec);
    }
    /**
     * 根据id权限
     *  1.查询权限
     *  2.根据权限类型查询资源
     *  3.构造map集合
     * */
    public Map<String, Object> findById(String id) throws CommonException{
        // 1.查询权限
        Permission permission = permissionDao.findById(id).get();
        //2.获取权限类型
        int type = permission.getType();
        Object object=null;
        if (type==PermissionConstants.PERMISSION_MENU){
         object=permissionMenuDao.findById(id).get();
        }else if (type==PermissionConstants.PERMISSION_POINT){
            object=permissionPointDao.findById(id).get();
        }else if (type==PermissionConstants.PERMISSION_API){
            object=permissionApiDao.findById(id).get();
        }else {
            throw new CommonException(ResultCode.FAIL);
        }
        Map<String, Object> map = BeanMapUtils.beanToMap(object);
        map.put("name",permission.getName());
        map.put("type",permission.getType());
        map.put("code",permission.getCode());
        map.put("description",permission.getDescription());
        map.put("en_visible",permission.getEnVisible());
        return map;
    }
    /**
     * 删除权限
     * 1.删除权限
     * 2.删除权限对应的资源
     * */
    public void deleteById(String id) throws CommonException {
        //1.通过id查询权限
        Permission permission = permissionDao.findById(id).get();
        permissionDao.delete(permission);
        //2.根据类型构造同资源对象（菜单，按钮，api）
        int type=permission.getType();
        switch (type){
            case PermissionConstants.PERMISSION_MENU:
                permissionMenuDao.deleteById(id);
                break;
            case PermissionConstants.PERMISSION_POINT:
                permissionPointDao.deleteById(id);
                break;
            case PermissionConstants.PERMISSION_API:
                permissionApiDao.deleteById(id);
                break;
            default: throw new CommonException(ResultCode.FAIL);
        }
    }
}
