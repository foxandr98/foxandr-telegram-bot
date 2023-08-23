package com.foxandr.telegrambot.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.DestroyMode;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.time.Duration;

@Configuration
@Slf4j
public class WebDriverFactory extends BasePooledObjectFactory<WebDriver> {

    @Bean
    @Scope("prototype")
    @Override
    public WebDriver create() throws Exception {

        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--headless");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-popup-blocking");
        return new ChromeDriver(options);
    }

    @Override
    public PooledObject<WebDriver> wrap(WebDriver webDriver) {
        return new DefaultPooledObject<WebDriver>(webDriver);
    }

    @Override
    public void destroyObject(PooledObject<WebDriver> p, DestroyMode destroyMode) throws Exception {
        log.info("Web driver closed!");
        p.getObject().quit();
    }

    @Bean
    public ObjectPool<WebDriver> initPool(WebDriverFactory factory) throws Exception {
        GenericObjectPoolConfig<WebDriver> config = new GenericObjectPoolConfig<WebDriver>();
        config.setMaxTotal(3);
        config.setBlockWhenExhausted(true);
        config.setMaxWait(Duration.ofSeconds(20));

        return new GenericObjectPool<>(factory, config);
    }
}
