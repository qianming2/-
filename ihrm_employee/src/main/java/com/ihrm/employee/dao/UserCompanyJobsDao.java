package com.ihrm.employee.dao;

import com.ihrm.domain.employee.UserCompanyJobs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * 自定义的dao接口继承
 * JpaRepository<实体类，主键></>和JpaSpecificationExecutor<实体类></>
 * */
public interface UserCompanyJobsDao extends JpaRepository<UserCompanyJobs,String>, JpaSpecificationExecutor<UserCompanyJobs> {
    @Query(value = "SELECT * FROM em_user_company_jobs WHERE user_id = ?1", nativeQuery = true)
    UserCompanyJobs findByUserId(String userId);
}
