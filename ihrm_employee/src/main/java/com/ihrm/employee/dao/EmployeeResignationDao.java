package com.ihrm.employee.dao;

import com.ihrm.domain.employee.EmployeeResignation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * 自定义的dao接口继承
 * JpaRepository<实体类，主键></>和JpaSpecificationExecutor<实体类></>
 * */
public interface EmployeeResignationDao extends JpaRepository<EmployeeResignation,String>, JpaSpecificationExecutor<EmployeeResignation> {
    @Query(value = "SELECT * FROM em_resignation WHERE user_id = ?1", nativeQuery = true)
    EmployeeResignation findByUserId(String userId);
}
