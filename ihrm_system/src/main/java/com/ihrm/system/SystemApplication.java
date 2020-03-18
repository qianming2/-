package com.ihrm.system;


import com.ihrm.common.util.IdWorker;
import com.ihrm.common.util.JwtUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;


@SpringBootApplication(scanBasePackages = "com.ihrm")
@EntityScan(value = "com.ihrm.domain.system")
@EnableEurekaClient
@EnableDiscoveryClient
@EnableFeignClients
public class SystemApplication {
    /**
     * 启动方法
     * */

    public static void main(String[] args) {
        SpringApplication.run(SystemApplication.class,args);
    }

    /**
     * 雪花算法加入到bean中去
     * */
    @Bean
    public IdWorker idWorker(){
        return new IdWorker();
    }

    /**
     * jwtUtils加入到bean中去
     * */
    @Bean
    public JwtUtils jwtUtils(){
        return new JwtUtils();
    }


    //解决no session
    @Bean
    public OpenEntityManagerInViewFilter openEntityManagerInViewFilter(){
        return new OpenEntityManagerInViewFilter();
    }

}
