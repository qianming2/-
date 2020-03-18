package com.ihrm.common.interceptor;

import com.ihrm.common.entity.ResultCode;
import com.ihrm.common.exception.CommonException;
import com.ihrm.common.util.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Handler;

/**
 * 自定义拦截器
 * 继承   HandlerInterceptorAdapter
 *  preHandle：进入到控制器方法之前的执行内容
 *        true：可以继续执行控制器的方法
 *        false：拦截
 *  postHandle：这些控制器之后执行内容
 *  afterCompletion ：响应结束之前执行的内容
 *
 *   1.简化获取token数据代码的编写
 *      统一的用户权限校验（是否登录）
 *   2.判断用户是否具有当前访问接口的权限
 * */
@Component
public class JwtInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private JwtUtils jwtUtils;
    /**
     * 简化获取token数据代码的编写
     *   1.通过request获取请求token信息
     *   2.token中解析获取claims
     *   3.将claims绑定到request中
     * */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
       //1.通过request获取请求token信息
        String authorization = request.getHeader("Authorization");
        //判断请求头信息是否为空，或者是否已有Bearer开头

        if (!StringUtils.isEmpty(authorization) && authorization.startsWith("Bearer")){
            //2.替换Bearer+空格  获取token
            String token = authorization.replace("Bearer ", "");
            //3.解析token
            Claims claims = jwtUtils.parseJwt(token);
            if (claims!=null){
                String apis = (String)claims.get("apis");//api-user-delete,api-user-update
                //通过handler
                HandlerMethod h=(HandlerMethod)handler;
                //获取接口上的requestMapping注解
                RequestMapping annotation = h.getMethodAnnotation(RequestMapping.class);
                //获取当前接口中name的属性
                String name = annotation.name();
                //判断当前用户是否具有相应的请求权限
                System.out.println(apis.contains(name));
                if (apis.contains(name)){
                    request.setAttribute("user_claims",claims);
                    return true;
                }else {
                    throw new CommonException(ResultCode.UNAUTHORISE);
                }
            }
        }
        throw new CommonException(ResultCode.UNAUTHENTICATED);
    }

}
