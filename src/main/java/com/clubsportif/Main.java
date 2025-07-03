package com.clubsportif;

import com.clubsportif.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .buildSessionFactory();

        Session session = factory.openSession();
        try {
            session.beginTransaction();

            User user = new User();
            user.setName("Alice");
            user.setEmail("alice@example.com");
            user.setBirthDay(LocalDate.parse("2004-05-16"));

            session.persist(user);
            session.getTransaction().commit();

            System.out.println("User saved!");
        } finally {
            session.close();
            factory.close();
        }
    }
}