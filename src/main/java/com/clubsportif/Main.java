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

    public static void insert(Session session, String name, String email){
        session.beginTransaction();

        User user = new User();
        user.setName(name);
        user.setEmail(email);

        session.persist(user);
        session.getTransaction().commit();

        System.out.println("User saved!");
    }

    public static User retrieveById(Session session, int id){
        session.beginTransaction();

        User user = session.get(User.class, id);
        session.getTransaction().commit();

        return user;
    }

    public static User retrieveByEmail(Session session, String email){
        session.beginTransaction();

        User user = session.createQuery("FROM User WHERE email = :email", User.class)
                .setParameter("email", email)
                .uniqueResult();
        session.getTransaction().commit();

        return user;
    }

    public static User updateName(Session session, User user, String newName){
        session.beginTransaction();

        user.setName(newName);
        session.merge(user);
        session.getTransaction().commit();

        return user;
    }

    public static User delete(Session session, User user){
        session.beginTransaction();


        if (user != null) {
            session.remove(user); // Hibernate 6+
        }
        session.getTransaction().commit();

        return user;
    }


    public static void main(String[] args) {
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .buildSessionFactory();

        Session session = factory.openSession();


        JFrame frame = new JFrame("Club Sportif");
        frame.setSize(700, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton button = new JButton("Clique-moi");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "Tu as clique ici");
            }
        });
        frame.getContentPane().add(button);

        frame.setVisible(true);




        try {
            //insert(session, "Alice5", "alice5@example.com");
            //System.out.println(retrieveById(session, 1).getName());
            //System.out.println(retrieveByEmail(session, "salim@gmail.com").getName());
            //updateName(session, retrieveByEmail(session, "salim@gmail.com"), "notSalime");
            //delete(session, retrieveById(session, 19));

        } finally {
            session.close();
            factory.close();
        }
    }
}