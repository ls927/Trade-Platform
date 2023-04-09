package com.platform.wetrade.config;

import com.platform.wetrade.interceptor.CookieInterceptor;
import com.platform.wetrade.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private CookieInterceptor cookieInterceptor;
    @Autowired
    private LoginInterceptor loginInterceptor;

    /**
     * 配置跨域
     * @note:
     *
     * maxAge: 当浏览器收到一个跨域请求时，会先发送一个 Preflight 请求（OPTIONS 请求），以确定服务器是否允许该请求。
     *       如果服务器允许该请求，则浏览器再发送实际的请求（GET、POST 等）。maxAge 则是指浏览器缓存 Preflight
     *       请求的响应 的时间。在此时间内，同源策略允许发送相同的跨域请求，则浏览器会直接使用缓存中的响应，而不必再发
     *       送 Preflight 请求。
     * allowCredentials: 表示允许携带 Cookie
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8081")
                .allowCredentials(true)
                .allowedMethods("GET","POST")
                .maxAge(3600);
    }

    /**
     * 配置拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(cookieInterceptor).addPathPatterns("/**");

        registry.addInterceptor(loginInterceptor).addPathPatterns("/trade/**");
    }

}
