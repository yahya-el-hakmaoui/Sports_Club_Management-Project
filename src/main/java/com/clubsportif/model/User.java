
package com.clubsportif.model;

import jakarta.persistence.*;

import java.time.LocalDate;


@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    @Column(name = "birth_day")
    private LocalDate birthDay;

    public User(){
    }

    public User(String name, String email, LocalDate birthDay) {
        this.name = name;
        this.email = email;
        this.birthDay = birthDay;
    }

    public Long getId() {return id;}
    public void setId(Long id) { this.id=id;}

    public String getName() {return name;}
    public void setName(String name) { this.name=name;}

    public String getEmail() {return email;}
    public void setEmail(String email) { this.email=email;}

    public LocalDate getBirthDay() {return birthDay;}
    public void setBirthDay(LocalDate birthDay) {this.birthDay=birthDay;}


}
