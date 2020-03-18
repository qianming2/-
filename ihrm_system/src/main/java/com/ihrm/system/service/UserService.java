package com.ihrm.system.service;

import com.ihrm.common.util.IdWorker;
import com.ihrm.common.util.QiniuUploadUtil;
import com.ihrm.domain.company.Department;
import com.ihrm.domain.system.Role;
import com.ihrm.domain.system.User;
import com.ihrm.system.client.DepartmentFeignClient;
import com.ihrm.system.dao.RoleDao;
import com.ihrm.system.dao.UserDao;
import com.ihrm.system.utils.BaiduAiUtil;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.IOException;
import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private DepartmentFeignClient departmentFeignClient;
    /**
     * 根据mobile 查询用户
     * */
    public User findByMobile(String mobile){
       return userDao.findByMobile(mobile);
    }
    /**
     * 保存用户
     * */


    public void save(User user){
        //基本属性的设置
        String id= idWorker.nextId()+"";
        user.setId(id);
        //指定初始密码
        String password = new Md5Hash("123456", user.getMobile(), 3).toString();
        user.setLevel("user");
        user.setPassword(password);
        //设置启用状态
        user.setEnableState(1);
        //保存用户
        userDao.save(user);
    }
    /**
     * 批量用户保存
     * */
    @Transactional
    public void saveAll(List<User> list,String company_id,String company_name){
        for (User user:list){
            //默认密码
            String password = new Md5Hash("123456", user.getMobile(), 3).toString();
            user.setPassword(password);
            //id
            String id= idWorker.nextId()+"";
            user.setId(id);
            //设置默认属性
            user.setLevel("user");
            user.setEnableState(1);
            user.setCompanyId(company_id);
            user.setCompanyName(company_name);
            user.setEnableState(1);
            //填充部门属性
            Department department = departmentFeignClient.findByCode(user.getDepartmentId(), company_id);
            if (department!=null){

                user.setDepartmentId(department.getId());
                user.setDepartmentName(department.getName());
            }
            //保存用户
            userDao.save(user);
        }

    }


    /**
     * 更新用户
     * */
    public void update(User user){
        //根据id用户部门
        User temp = userDao.findById(user.getId()).get();
        //设置用户属性
        temp.setUsername(user.getUsername());
        temp.setPassword(user.getPassword());
        temp.setDepartmentId(user.getDepartmentId());
        temp.setDepartmentName(user.getDepartmentName());
        //更新用户
        userDao.save(temp);
    }
    /**
     * 根据id查询部门
     * */
    public User findById(String id){
        User user = userDao.findById(id).get();
        System.out.println(user);
        return user;
    }

    /**
     * 删除用户
     * */
    public void deleteById(String id){
        userDao.deleteById(id);
    }
    /**
     *   查询用户列表
     *     参数  ：map集合形式
     *     has_dept
     *     department_id
     *     company_id
     *
     * */
    public Page findAll(Map<String,Object> map,int page,int size){
       //需要查询条件
        Specification<User> spec=new Specification<User>() {
            /**
             * 动态拼接条件
             * */
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Object> list = new ArrayList<>();
                /**
                 * 根据请求的company_id是否为空构造查询条件
                 * */
                if (!StringUtils.isEmpty(map.get("companyId"))){
                    list.add(criteriaBuilder.equal(root.get("companyId").as(String.class),(String)map.get("companyId")));
                }
                /**
                 * 根据请求的department_id是否为空构造查询条件
                 * */
                if (!StringUtils.isEmpty(map.get("departmentId"))){
                    list.add(criteriaBuilder.equal(root.get("departmentId").as(String.class),(String)map.get("departmentId")));
                }
                /**
                 * 根据请求的has_dept判断 是否分配部门 0未分配(department_id=null)： 1已分配(department_id！=null)
                 * */
                if (StringUtils.isEmpty(map.get("hasDept"))){
                    if ("0".equals((String)map.get("hasDept"))){
                        //  list.add(criteriaBuilder.isNull(root.get("department_id")));
                    }else {
                        // list.add(criteriaBuilder.isNotNull(root.get("department_id")));
                    }
                }
                return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
            }
        };
        //分页
        Page<User> pageUser = userDao.findAll(spec, new PageRequest(page-1, size));
        return pageUser;
    }
    /**
     * 分配角色
     * */
    public void assignRoles(String userId,List<String> roleIds){
        //1.根据id查询用
        User user = userDao.findById(userId).get();
        //2设置用户的对象集合
        Set<Role> roles = new HashSet<Role>();
        for (String roleId:roleIds){
            Role role = roleDao.findById(roleId).get();
            roles.add(role);
        }
        //设置用户和角色的集合
        user.setRoles(roles);
        //3.更新用户
        userDao.save(user);
    }

    /**
     * 完成图片处理
     * 以base64的方法存储到数据库
     * */
//    public String uploadImage(String id, MultipartFile file) throws IOException {
//        //1.根据id查询用户
//        User user = userDao.findById(id).get();
//        //2.使用DataUrl的形式存储图片（对图片byte数组进行base64处理编码）
//        String encode = "data:image/png;base64,"+Base64.encode(file.getBytes());
//        user.setStaffPhoto(encode);
//        userDao.save(user);
//        return encode;
//    }

    /**
     * 完成图片处理
     * 将图片上传到七牛云存储
     * 还要注册到百度云人脸库中
     *      1.调用百度云接口，判断当前用户是否已经注册
     *      2.已注册更新
     *      3.未注册注册
     * */
    @Autowired
    private BaiduAiUtil baiduAiUtil;

    public String uploadImage(String id, MultipartFile file) throws IOException {
        //1.根据id查询用户
        User user = userDao.findById(id).get();
        //将图片上传到七牛云存储，获取请求路径
        String imgUrl = new QiniuUploadUtil().upload(user.getId(),file.getBytes());
        user.setStaffPhoto(imgUrl);
        userDao.save(user);
        //判断是否已经注册
        Boolean aBoolean = baiduAiUtil.faceCheck(id);
        String imgBase64=Base64.encode(file.getBytes());
        if (aBoolean){
            //更新
            baiduAiUtil.faceUpdate(id,imgBase64);
        }else {
            //注册
            baiduAiUtil.faceRegister(id,imgBase64);
        }
        return imgUrl;
    }
}
