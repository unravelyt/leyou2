package com.leyou2.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class LyCorsConfiguration {

    @Bean //springMVC提供的
    public CorsFilter corsFilter() {
        //初始化配置对象
        CorsConfiguration configuration = new CorsConfiguration();
        //允许跨域的域名
        configuration.addAllowedOrigin("http://manage.leyou.com");
        configuration.setAllowCredentials(true); //允许携带cookie
        configuration.addAllowedMethod("*"); //允许的请求方法
        configuration.addAllowedHeader("*"); //允许的请求头

        //初始化配置源对象
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        //返回corsFilter实例，参数：cors配置源对象
        return new CorsFilter(source);
    }

}
