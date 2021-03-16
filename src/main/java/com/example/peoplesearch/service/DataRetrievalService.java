package com.example.peoplesearch.service;

import com.example.peoplesearch.botapi.handlers.fillingprofile.UserProfileData;
import com.example.peoplesearch.cache.UserDataCache;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class DataRetrievalService {

    private List<String>proxyList=new CopyOnWriteArrayList<>();
    private UserDataCache userDataCache;
    private Semaphore semaphore;
    private InitializationDriverComponent initializationDriverComponent;

    @PostConstruct
    void initializationWebDriver(){

        //45.154.59.14:8000 // 45.139.111.146:8000
        for (int i = 0; i < 2 ; i++) {
            initializationDriverComponent.initialization();
        }
//        semaphore=new Semaphore(proxyList.size());
    }

    public DataRetrievalService(UserDataCache userDataCache, InitializationDriverComponent initializationDriverComponent) {
        this.userDataCache = userDataCache;
        this.initializationDriverComponent = initializationDriverComponent;
    }

    @Async
    public void execute(int userId) {

        UserProfileData userProfileData=userDataCache.getUserProfileData(userId);
        userDataCache.removeUserProfileData(userId);
        WebDriver driver=initializationDriverComponent.getWebDriver();

        if(driver!=null){

            try {

//           semaphore.acquire();
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
                System.out.println("Работа выполнена");
                initializationDriverComponent.setWebDriver(driver);

            }catch (InterruptedException ex){

            }catch (Error|Exception ex){
                userProfileData.setData("Источники поиска перегружены, повторите запрос позднее");
                driver.quit();
                initializationDriverComponent.initialization();
                System.out.println("Вызов потока");
            }

        }else {
            userProfileData.setData("Источники поиска перегружены, повторите запрос позднее");
        }
        userProfileData.setAnswerFormed(true);

//        semaphore.release();


    }

}

