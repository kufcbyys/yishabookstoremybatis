package com.example.yishabookstoremybatis.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
