package com.ihrm.system.dao;

import com.ihrm.domain.system.PermissionMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 自定义的dao接口继承
 * JpaRepository<实体类，主键></>和JpaSpecificationExecutor<实体类></>
 * */
public interface PermissionMenuDao extends JpaRepository<PermissionMenu,String>, JpaSpecificationExecutor<PermissionMenu> {
}
