package com.example.peoplesearch.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "user_peoplesearch")
public class UserBot {

    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String firstName;
    private String lastName;
    private String userName;

    public UserBot() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserBot userBot = (UserBot) o;
        return Objects.equals(id, userBot.id) &&
                Objects.equals(firstName, userBot.firstName) &&
                Objects.equals(lastName, userBot.lastName) &&
                Objects.equals(userName, userBot.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, userName);
    }
}
