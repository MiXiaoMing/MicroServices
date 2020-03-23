package com.microservices.support;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.Properties;

public class AbstractContants {

    protected static String getProperty(String key, String defaultValue) {
        Properties p = new Properties();
        try {
            ResourcePatternResolver resourceLoader = new PathMatchingResourcePatternResolver();
            Resource[] source = resourceLoader.getResources("config/statics/*.properties");
            for (Resource resource : source) {
                Properties tmp = PropertiesLoaderUtils.loadProperties(resource);
                p.putAll(tmp);
            }
        } catch (IOException e) {
            System.out.println("load statics properties error." + e.fillInStackTrace());
        }
        return p.getProperty(key, defaultValue);
    }
}
