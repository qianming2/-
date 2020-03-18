package com.ihrm.domain.company;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;
import java.util.Date;

/**
 * lombok使用注解配置
 *          setter  ：setter方法
 *          getter  ：getter方法
 *          NoArgsConstructor ：无参构造
 *          AllArgsConstructor ：满参构造
 *          Data     ：setter  ，getter，构造方法
 *
 * 使用jpa操作数据库
 * 配置实体类和数据库表的映射关系：jpa
 * 1.实体类和表的映射关系
 * 2.字段和属性的映射关系
 *   1.主键属性的映射
 *   11.普通属性的映射
 *
 * */

@Entity
@Table(name = "co_company")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Company implements Serializable {

    //公司id
    @Id
    private String id;
    //公司名字
    private String name;
    //企业登录账号ID
    private String managerId;
    //当前版本
    private String version;
    //持续时间
    private Date renewalDate;
    //到期时间
    private Date expirationDate;
    //公司地区
    private String companyArea;
    //公司地址
    private String companyAddress;
    //营业执照-图片ID
    private String businessLicenseId;
    //法人代表
    private String legalRepresentative;
    //公司电话
    private String companyPhone;
    //邮箱
    private String mailbox;
    //公司规模
    private String companySize;
    //所属行业
    private String industry;
    //备注
    private String remarks;
    //审核状态
    private String auditState;
    //状态
    private Integer state;
    //当前余额
    private Double balance;
    //创建时间
    private Date createTime;

}
