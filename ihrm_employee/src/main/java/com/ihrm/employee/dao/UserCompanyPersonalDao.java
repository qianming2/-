package com.ihrm.employee.dao;

import com.ihrm.domain.employee.UserCompanyPersonal;
import com.ihrm.domain.employee.response.EmployeeReportResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 自定义的dao接口继承
 * JpaRepository<实体类，主键></>和JpaSpecificationExecutor<实体类></>
 * */
public interface UserCompanyPersonalDao extends JpaRepository<UserCompanyPersonal, String>, JpaSpecificationExecutor<UserCompanyPersonal> {
    @Query(value = "SELECT * FROM em_user_company_personal WHERE user_id = ?1", nativeQuery = true)
   UserCompanyPersonal findByUserId(String userId);

    @Query(value = "SELECT new com.ihrm.domain.employee.response.EmployeeReportResult(a,b) from UserCompanyPersonal a LEFT JOIN EmployeeResignation b on a.userId=b.userId where a.companyId=?1 and a.timeOfEntry like ?2 or(b.resignationTime like ?2)")
    List<EmployeeReportResult> findByReport(String companyId, String month);
}
