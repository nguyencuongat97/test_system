package com.foxconn.fii.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.AppCacheManifestTransformer;
import org.springframework.web.servlet.resource.VersionResourceResolver;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${path.data}")
    private String dataPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/assets/**", "/ws-data/image/**", "/ws-data/file/**", "/favicon.ico", "/sitemap*.xml")
                .addResourceLocations("classpath:/static/assets/", ("file:" + dataPath + "image/"), ("file:" + dataPath + "file/"))
//                .setCacheControl(CacheControl.maxAge(30L, TimeUnit.DAYS).cachePublic())
//                .resourceChain(true)
//                .addResolver(new VersionResourceResolver().addContentVersionStrategy("/**"))
//                .addTransformer(new AppCacheManifestTransformer())
        ;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*");
    }
}
