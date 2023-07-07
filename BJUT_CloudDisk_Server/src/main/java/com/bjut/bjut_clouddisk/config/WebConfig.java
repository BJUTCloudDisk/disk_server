package com.bjut.bjut_clouddisk.config;

import com.bjut.bjut_clouddisk.BjutCloudDiskApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class WebConfig extends WebMvcConfigurationSupport {
    static final String ICON_PATH = BjutCloudDiskApplication.class.getResource("/disk/").getFile();
    // static final String FILE_PATH = System.getProperty("user.dir") + "/SystemData/file/";

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler( "/disk/**")
                .addResourceLocations("file:" + ICON_PATH);
//        registry.addResourceHandler("/SystemData/file/**")
//                .addResourceLocations("file:" + FILE_PATH);

        super.addResourceHandlers(registry);
    }
}
