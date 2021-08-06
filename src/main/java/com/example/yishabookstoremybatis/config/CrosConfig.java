package com.example.yishabookstoremybatis.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 18028
 * //用于定义配置类，指出该类是 Bean 配置的信息源，相当于传统的xml配置文件，
 * //        一般加在主类上。如果有些第三方库需要用到xml文件，
 * //        建议仍然通过@Configuration类作为项目的配置主类——可以使用@ImportResource注解加载xml配置文件
 */
@Configuration
public class CrosConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry){
        //设置允许跨域的路径
        registry.addMapping("/**")
                //设置允许跨域请求的域名
                .allowedOriginPatterns("*")
                //设置允许方法
                .allowedMethods("GET","HEAD","POST","PUT","DELETE","OPTIONS")
                //是否允许证书
                .allowCredentials(true)
                //跨域允许时间
                .maxAge(3600)
                //设置允许的header属性
                .allowedHeaders("*");
    }
}
