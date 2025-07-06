package com.clubsportif.dao;

import com.clubsportif.model.Paiement;
import com.clubsportif.model.User;
import com.clubsportif.model.Paiement.StatutPaiement;
import com.clubsportif.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.List;

public class PaiementDao {

    /**
     * Enregistre un nouveau paiement dans la base.
     * @param paiement objet Paiement à insérer
     */
    public void createPaiement(Paiement paiement) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(paiement);  // Persist pour insert
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    /**
     * Met à jour un paiement existant.
     * @param paiement objet Paiement modifié
     */
    public void updatePaiement(Paiement paiement) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(paiement);  // Merge pour update
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    /**
     * Supprime un paiement de la base.
     * @param paiement objet Paiement à supprimer
     */
    public void deletePaiement(Paiement paiement) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(paiement);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    /**
     * Recherche un paiement par son identifiant.
     * @param id identifiant du paiement
     * @return Paiement ou null si non trouvé
     */
    public Paiement findPaiementById(Integer id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Paiement.class, id);
        }
    }

    /**
     * Récupère tous les paiements d'un utilisateur donné.
     * @param user utilisateur concerné
     * @return liste des paiements de l'utilisateur
     */
    public List<Paiement> findPaiementsByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Paiement p WHERE p.user = :user ORDER BY p.periodeDebut DESC";
            Query<Paiement> query = session.createQuery(hql, Paiement.class);
            query.setParameter("user", user);
            return query.list();
        }
    }

    /**
     * Recherche les paiements selon le statut.
     * @param statut statut du paiement (paye, impaye, partiel)
     * @return liste des paiements avec ce statut
     */
    public List<Paiement> findPaiementsByStatut(StatutPaiement statut) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Paiement p WHERE p.statut = :statut ORDER BY p.periodeDebut DESC";
            Query<Paiement> query = session.createQuery(hql, Paiement.class);
            query.setParameter("statut", statut);
            return query.list();
        }
    }


    public Paiement findPaiementByUserCurrentPeriod(User user) {
        LocalDate today = LocalDate.now();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Paiement p WHERE p.user = :user AND :today BETWEEN p.periodeDebut AND p.periodeFin";
            Query<Paiement> query = session.createQuery(hql, Paiement.class);
            query.setParameter("user", user);
            query.setParameter("today", today);
            return query.uniqueResult();
        }
    }

    /**
     * Récupère la liste de tous les paiements.
     * @return liste de tous les paiements
     */
    public List<Paiement> findAllPaiements() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Paiement p ORDER BY p.periodeDebut DESC";
            Query<Paiement> query = session.createQuery(hql, Paiement.class);
            return query.list();
        }
    }
}
