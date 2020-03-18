package com.ihrm.employee.dao;


import com.ihrm.domain.employee.EmployeeTransferPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * 自定义的dao接口继承
 * JpaRepository<实体类，主键></>和JpaSpecificationExecutor<实体类></>
 * */
public interface TransferPositionDao extends JpaRepository<EmployeeTransferPosition,String>, JpaSpecificationExecutor<EmployeeTransferPosition> {
    @Query(value = "SELECT * FROM em_transferposition WHERE user_id = ?1", nativeQuery = true)
    EmployeeTransferPosition findByUserId(String uid);
}
