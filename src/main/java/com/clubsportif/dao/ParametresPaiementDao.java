package com.clubsportif.dao;

import com.clubsportif.model.ParametresPaiement;
import com.clubsportif.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class ParametresPaiementDao {

    /**
     * Enregistre un nouvel enregistrement dans la table parametres_paiement.
     */
    public void insertParametresPaiement(ParametresPaiement param) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(param);  // Utilisation de persist pour nouvelle entité
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    /**
     * Met à jour un enregistrement existant dans la base.
     */
    public void updateParametresPaiement(ParametresPaiement param) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(param);  // merge pour mise à jour
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    /**
     * Récupère un paramètre de paiement par son ID.
     */
    public ParametresPaiement getParametresPaiementById(Integer id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(ParametresPaiement.class, id);
        }
    }

    /**
     * Supprime un paramètre de paiement par entité.
     */
    public void deleteParametresPaiement(ParametresPaiement param) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.remove(param);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    /**
     * Supprime un paramètre de paiement par son ID.
     */
    public void deleteParametresPaiementById(Integer id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            ParametresPaiement param = session.get(ParametresPaiement.class, id);
            if (param != null) {
                session.remove(param);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    /**
     * Récupère tous les enregistrements des paramètres de paiement.
     */
    public List<ParametresPaiement> getAllParametresPaiement() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<ParametresPaiement> query = session.createQuery("FROM ParametresPaiement", ParametresPaiement.class);
            return query.getResultList();
        }
    }

}
