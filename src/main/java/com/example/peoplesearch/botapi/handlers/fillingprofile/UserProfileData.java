package com.example.peoplesearch.botapi.handlers.fillingprofile;


/**
 * Данные анкеты пользователя
 */
public class UserProfileData {

    private String City;
    private String CityFotBrowser;
    private String SurName;
    private String Name;
    private String Patronymic;

    private String Data;

    private  volatile boolean isAnswerFormed;

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getCityFotBrowser() {
        return CityFotBrowser;
    }

    public void setCityFotBrowser(String cityFotBrowser) {
        CityFotBrowser = cityFotBrowser;
    }

    public String getSurName() {
        return SurName;
    }

    public void setSurName(String surName) {
        SurName = surName;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPatronymic() {
        return Patronymic;
    }

    public void setPatronymic(String patronymic) {
        Patronymic = patronymic;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public boolean isAnswerFormed() {
        return isAnswerFormed;
    }

    public void setAnswerFormed(boolean answerFormed) {
        isAnswerFormed = answerFormed;
    }

    @Override
    public String toString() {
        return "UserProfileData{" +
                "City='" + City + '\'' +
                ", SurName='" + SurName + '\'' +
                ", Name='" + Name + '\'' +
                '}';
    }
}
