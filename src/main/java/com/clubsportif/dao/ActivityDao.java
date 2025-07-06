package com.clubsportif.dao;

import com.clubsportif.model.Activity;
import com.clubsportif.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class ActivityDao {

    /**
     * Crée une nouvelle activité dans la base de données.
     * @param activity l'activité à créer
     */
    public void createActivity(Activity activity) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(activity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    /**
     * Met à jour une activité existante.
     * @param activity l'activité modifiée
     * @return l'activité attachée à la session après mise à jour
     */
    public Activity updateActivity(Activity activity) {
        Transaction transaction = null;
        Activity updatedActivity = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            updatedActivity = (Activity) session.merge(activity);
            transaction.commit();
            return updatedActivity;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    /**
     * Supprime une activité par son identifiant.
     * @param activityId l'identifiant de l'activité à supprimer
     * @return true si suppression réussie, false si l'activité n'existe pas
     */
    public boolean deleteActivityById(int activityId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Activity activity = session.get(Activity.class, activityId);
            if (activity == null) {
                return false;
            }
            session.remove(activity);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    /**
     * Récupère une activité par son identifiant.
     * @param activityId l'identifiant de l'activité
     * @return l'activité ou null si non trouvée
     */
    public Activity findActivityById(int activityId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Activity.class, activityId);
        }
    }

    /**
     * Récupère une activité par son nom.
     * @param name le nom de l'activité
     * @return l'activité ou null si non trouvée
     */
    public Activity findActivityByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Activity WHERE nom = :name";
            Query<Activity> query = session.createQuery(hql, Activity.class);
            query.setParameter("name", name);
            return query.uniqueResult();
        }
    }

    /**
     * Récupère la liste de toutes les activités.
     * @return liste des activités
     */
    public List<Activity> findAllActivities() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Activity ORDER BY nom";
            Query<Activity> query = session.createQuery(hql, Activity.class);
            return query.list();
        }
    }
}
