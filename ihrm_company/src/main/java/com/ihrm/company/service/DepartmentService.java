package com.ihrm.company.service;

import com.ihrm.common.service.BaseService;
import com.ihrm.common.util.IdWorker;
import com.ihrm.company.dao.CompanyDao;
import com.ihrm.company.dao.DepartmentDao;
import com.ihrm.domain.company.Company;
import com.ihrm.domain.company.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Service
public class DepartmentService extends BaseService {

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 保存部门
     * 配置idwork到工程中
     * 造service中注入idwork
     * 通过idwork生成id
     * 保存部门
     * */

    public void save(Department department){
        //基本属性的设置
        String id= idWorker.nextId()+"";
        department.setId(id);
        departmentDao.save(department);
    }

    /**
     * 更新部门
     * 根据id查询部门对象
     * 设置修改的属性
     * 调用dao完成更新
     * */

    public void update(Department department){
        System.out.println(department);
        //根据id查询部门
        Department temp = departmentDao.findById(department.getId()).get();
        //设置部门属性
        temp.setName(department.getName());
        temp.setCode(department.getCode());
        temp.setIntroduce(department.getIntroduce());
      //更新部门
        departmentDao.save(temp);
    }

    /**
     * 删除部门
     * */
    public void deleteById(String id){
        departmentDao.deleteById(id);
    }


    /**
     * 根据id查询部门
     * */

    public Department findById(String id){
        return departmentDao.findById(id).get();
    }


    /**
     * 查询部门列表
     * */
    public List<Department> findAll(String companyId){
        /**
         * 用户构造查询条件
         *    root   ：包含了所有的数据对象
         *    query  ：一般不用
         *    criteriaBuilder ：构造查询条件
         * */

//        Specification<Department> spec=new Specification<Department>() {
//            @Override
//            public Predicate toPredicate(Root<Department> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//                //根据企业id查询
//                return    criteriaBuilder.equal(root.get("companyId").as(String.class),companyId);
//            }
//        };
        return departmentDao.findAll(getSpec(companyId));
    }

    //根据部门编码和企业id查询
    public Department findByCode(String code, String companyId) {
             return   departmentDao.findByCodeAndCompanyId(code,companyId);
    }
}
