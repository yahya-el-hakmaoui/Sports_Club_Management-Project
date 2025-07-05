package com.clubsportif.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import com.clubsportif.model.User;
import com.clubsportif.model.Activity;
import com.clubsportif.model.Inscription;
import com.clubsportif.model.Paiement;
import com.clubsportif.model.ParametresPaiement;

public class HibernateUtil {
    private static SessionFactory sessionFactory;

    static {
        try {
            Configuration configuration = new Configuration();

            // Charger le fichier de configuration hibernate.cfg.xml
            configuration.configure("hibernate.cfg.xml");

            // Enregistrer les classes annotées
            configuration.addAnnotatedClass(User.class);
            configuration.addAnnotatedClass(Activity.class);
            configuration.addAnnotatedClass(Inscription.class);
            configuration.addAnnotatedClass(Paiement.class);
            configuration.addAnnotatedClass(ParametresPaiement.class);

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build();

            sessionFactory = configuration.buildSessionFactory(serviceRegistry);

        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    // Retourne la session factory singleton
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    // Fermer la session factory proprement
    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
