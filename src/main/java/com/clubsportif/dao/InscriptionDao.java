package com.clubsportif.dao;

import com.clubsportif.model.Inscription;
import com.clubsportif.model.User;
import com.clubsportif.model.Activity;
import com.clubsportif.util.HibernateUtil;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class InscriptionDao {

    // ➤ Enregistrer une nouvelle inscription
    public void createInscription(Inscription inscription) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(inscription); // Ne pas utiliser saveOrUpdate
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    // ➤ Récupérer une inscription par son ID
    public Inscription getInscriptionById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Inscription.class, id);
        }
    }

    // ➤ Mettre à jour une inscription existante
    public void updateInscription(Inscription inscription) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(inscription); // merge est préféré à saveOrUpdate
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    // ➤ Supprimer une inscription définitivement
    public void deleteInscription(Inscription inscription) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.remove(inscription);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    // ➤ Liste des inscriptions actives d'un utilisateur
    public List<Inscription> getActiveInscriptionsByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Inscription i WHERE i.user = :user AND i.active = true";
            Query<Inscription> query = session.createQuery(hql, Inscription.class);
            query.setParameter("user", user);
            return query.list();
        }
    }

    // ➤ Liste des inscriptions pour une activité donnée
    public List<Inscription> getInscriptionsByActivity(Activity activity) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Inscription i WHERE i.activity = :activity";
            Query<Inscription> query = session.createQuery(hql, Inscription.class);
            query.setParameter("activity", activity);
            return query.list();
        }
    }

    // ➤ Compter le nombre d'inscrits actifs pour une activité (utile pour la limite max)
    public long countActiveInscriptionsForActivity(Activity activity) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT COUNT(i) FROM Inscription i WHERE i.activity = :activity AND i.active = true";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("activity", activity);
            return query.uniqueResult();
        }
    }

    // ➤ Désinscrire (désactiver) une inscription sans la supprimer
    public void deactivateInscription(Inscription inscription) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            inscription.setActive(false);
            session.merge(inscription);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    // ➤ inscrire (activer) une inscription
    public void activateInscription(Inscription inscription) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            inscription.setActive(true);
            session.merge(inscription);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }
}
