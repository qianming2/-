package com.ihrm.company.dao;

import com.ihrm.domain.company.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;


/**
 * 自定义的dao接口继承
 * JpaRepository<实体类，主键></>和JpaSpecificationExecutor<实体类></>
 * */
public interface DepartmentDao extends JpaRepository<Department,String>, JpaSpecificationExecutor<Department> {
    @Query(value = "SELECT * FROM co_department WHERE code = ?1 AND company_id= ?2", nativeQuery = true)
    public Department findByCodeAndCompanyId(String code,String companyId);
}
