package com.example.peoplesearch.service;

import com.example.peoplesearch.botapi.handlers.fillingprofile.UserProfileData;
import com.example.peoplesearch.cache.UserDataCache;
import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class DataRetrievalService {

    private List<String>proxyList=new CopyOnWriteArrayList<>();
    private BlockingQueue<WebDriver>webDrivers=new LinkedBlockingQueue<>();
    private UserDataCache userDataCache;
    private Semaphore semaphore;

    @PostConstruct
    void initializationWebDriver(){

        //45.154.59.14:8000 // 45.139.111.146:8000
//        proxyList.add("45.154.59.14:8000");
//        proxyList.add("45.139.111.146:8000");
//        for (String proxy:proxyList){
//            webDrivers.add(initialization(proxy));
//        }
        for (int i = 0; i < 3; i++) {
            webDrivers.add(initialization(""));
        }
        semaphore=new Semaphore(proxyList.size());
    }

    public DataRetrievalService(UserDataCache userDataCache) {
        this.userDataCache = userDataCache;
    }

    @Async
    public void execute(int userId) {

        UserProfileData userProfileData=userDataCache.getUserProfileData(userId);
        userDataCache.removeUserProfileData(userId);
        WebDriver driver=null;
        try {

            semaphore.acquire();
            driver=webDrivers.poll();
            driver.get("https://zhytel.rosfirm.info/" + userProfileData.getCityFotBrowser());
            Thread.sleep(TimeUnit.SECONDS.toMillis(2));
            driver.findElement(By.name("fio")).sendKeys(userProfileData.getSurName()
                    + " " + userProfileData.getName()
                    + " " + userProfileData.getPatronymic());
            Thread.sleep(TimeUnit.SECONDS.toMillis(3));
            driver.findElement(By.name("searchButton")).click();

            String pageSource=driver.getPageSource();
            if(pageSource.contains("По вашему запросу найдено 0 записей")){
                userProfileData.setData("По вашему запросу не было найдено ни одного человека");
            } else if(pageSource.contains("База перегружена") || pageSource.contains("Bad SQL-query")) {
                userProfileData.setData("Источники поиска перегружены, повторите запрос позднее");
            }else {
                WebElement table=driver.findElement(By.tagName("table"));
                List<String>heads=table.findElement(By.className("head")).findElements(By.tagName("td")).stream().map(x->x.getText()).collect(Collectors.toList());
                List<WebElement>elementData=table.findElements(By.className("data"));
                elementData.remove(0);
                List<List<WebElement>>webElementsList=elementData.stream().map(x->x.findElements(By.tagName("td"))).collect(Collectors.toList());

                StringBuilder stringBuilder=new StringBuilder();
                int a=0;
                for (List<WebElement>we:webElementsList){
                    stringBuilder.append(++a+")");
                    List<String>dataList=we.stream().map(x->x.getText()).collect(Collectors.toList());
                    for (int j = 0; j < dataList.size(); j++) {
                        stringBuilder.append(heads.get(j)+": " + dataList.get(j) + "\n");
                    }
                    stringBuilder.append("\n");
                }

                userProfileData.setData(stringBuilder.toString());
            }

        }catch (InterruptedException ex){

        }catch (Error|Exception ex){
            userProfileData.setData("Источники поиска перегружены, повторите запрос позднее");
        }

        userProfileData.setAnswerFormed(true);

        System.out.println("Работа выполнена");
        webDrivers.add(driver);
        semaphore.release();


    }

    private WebDriver initialization(String host)  {


        Proxy proxy=new Proxy().setHttpProxy(host).
                setFtpProxy(host).
                setSslProxy(host).setProxyType(Proxy.ProxyType.MANUAL);

        ChromeOptions options=new ChromeOptions();
//            options.setProxy(proxy);
//            options.addArguments("--user-data-dir=C:\\Users\\RET\\AppData\\Local\\Google\\Telegram\\" + id);

        options.setHeadless(true);
        options.addArguments("--disable-blink-features=AutomationControlled");
        ChromeDriver driver=new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(60, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(60,TimeUnit.SECONDS);

        driver.get("https://www.google.com/");


        return driver;

    }

}

