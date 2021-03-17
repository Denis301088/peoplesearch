package com.example.peoplesearch.service;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Component
public class InitializationDriverComponent {

    private Queue<WebDriver> webDrivers=new LinkedBlockingQueue<>();

    @Async
    public void initialization(){

        ChromeOptions options=new ChromeOptions();
//        options.setHeadless(true);
        options.addArguments("--disable-blink-features=AutomationControlled");
        ChromeDriver driver=new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(60, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(60,TimeUnit.SECONDS);

        driver.get("https://www.google.com/");
        webDrivers.add(driver);

    }

    public WebDriver getWebDriver(){
        return webDrivers.poll();
    }

    public void setWebDriver(WebDriver driver){
        webDrivers.add(driver);
    }
}
