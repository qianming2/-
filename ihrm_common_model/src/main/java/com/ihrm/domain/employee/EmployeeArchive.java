package com.ihrm.domain.employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
/**
 * 员工归档
 * */
@Entity
@Table(name = "em_archive")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeArchive implements Serializable {
    //id
     @Id
    private String id;
    //操作用户
    private String opUser;
    //月份
    private String month;
    //企业ID
    private String companyId;
    //总人数
    private Integer totals;
    //在职人数
    private Integer payrolls;
    //离职人数
    private Integer departures;
    //数据
    private String data;
    //创建时间
    private Date createTime;





}
