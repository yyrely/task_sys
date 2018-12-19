package com.chuncongcong.task.config;

import com.chuncongcong.task.config.interceptor.LoginInterceptor;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.MultipartConfigElement;

/**
 * @author Hu
 * @date 2018/8/17 11:16
 */

@SpringBootConfiguration
public class MyConfig implements WebMvcConfigurer {


    private final LoginInterceptor loginInterceptor;

    public MyConfig(LoginInterceptor loginInterceptor) {
        this.loginInterceptor = loginInterceptor;
    }

    /**
     * 拦截器配置
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor).addPathPatterns("/**")
                .excludePathPatterns("/users/login")
                .excludePathPatterns("/users/register")
                .excludePathPatterns("/users/check")
                .excludePathPatterns("/users/portraits")
                .excludePathPatterns("/upload/**")
                .excludePathPatterns("/error")
                .excludePathPatterns("/tasks/download/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**").addResourceLocations("file:/upload/");
    }

    /**
     * 上传图片
     * @return
     */
    @Bean
    public MultipartConfigElement multipartConfigElement(){
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize("50MB");
        return factory.createMultipartConfig();
    }


}
