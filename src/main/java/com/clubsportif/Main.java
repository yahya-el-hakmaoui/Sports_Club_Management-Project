package com.clubsportif;
// our models
import com.clubsportif.model.*;
// for hibernate
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
// for swing
import javax.swing.*;
import java.awt.event.*;
// for types
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .buildSessionFactory();

        Session session = factory.openSession();

        try {

        } finally {
            session.close();
            factory.close();
        }
    }
}