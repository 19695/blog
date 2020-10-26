package com.colm.blog.interceptor;

import org.omg.CORBA.*;
import org.omg.CORBA.Object;
import org.omg.PortableInterceptor.Interceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * springboot2.0、spring5中废弃了 WebMvcConfigurerAdapter
 * 使用实现 WebMvcConfiger 的方式来实现
 *
 * Created by Colm on 2020/10/20
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin")
                .excludePathPatterns("/admin/login");
    }

}
