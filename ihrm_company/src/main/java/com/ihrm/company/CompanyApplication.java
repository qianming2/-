package com.ihrm.company;

import com.ihrm.common.util.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

//1.配置springboot的包扫描
@SpringBootApplication(scanBasePackages = "com.ihrm.company")
//2.配置jpa注解的扫描
@EntityScan(value = "com.ihrm.domain.company")
//3.注册到eureka
@EnableEurekaClient
public class CompanyApplication {

    /**
     * 启动方法
     * */

    public static void main(String[] args) {
        SpringApplication.run(CompanyApplication.class,args);
    }

    /**
     * 雪花算法加入到bean中去
     * */
    @Bean
    public IdWorker idWorker(){
        return new IdWorker();
    }
}
