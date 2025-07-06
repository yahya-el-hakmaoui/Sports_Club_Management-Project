package com.clubsportif.dao;

import com.clubsportif.model.User;
import com.clubsportif.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class UserDao {

    // Créer un nouvel utilisateur
    public void createUser(User user) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(user);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    // Mettre à jour un utilisateur existant
    public void updateUser(User user) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(user);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    // Supprimer un utilisateur
    public void deleteUser(User user) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.remove(session.contains(user) ? user : session.merge(user));
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    // Trouver un utilisateur par ID
    public User findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(User.class, id);
        }
    }

    // Récupérer tous les utilisateurs
    public List<User> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM User";
            Query<User> query = session.createQuery(hql, User.class);
            return query.getResultList();
        }
    }

    // Récupérer les utilisateurs archivés
    public List<User> findArchived() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM User WHERE archived = true";
            Query<User> query = session.createQuery(hql, User.class);
            return query.getResultList();
        }
    }

    // Récupérer les utilisateurs non archivés
    public List<User> findNotArchived() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM User WHERE archived = false";
            Query<User> query = session.createQuery(hql, User.class);
            return query.getResultList();
        }
    }

    // Rechercher un utilisateur par nom (partiel, insensible à la casse)
    public List<User> findByName(String namePart) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM User WHERE LOWER(lastname) LIKE :name";
            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("name", "%" + namePart.toLowerCase() + "%");
            return query.getResultList();
        }
    }

    // Rechercher un utilisateur par prénom (partiel, insensible à la casse)
    public List<User> findByLastname(String lastnamePart) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM User WHERE LOWER(name) LIKE :lastname";
            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("lastname", "%" + lastnamePart.toLowerCase() + "%");
            return query.getResultList();
        }
    }

    // Rechercher un utilisateur par nom d'utilisateur (exact, insensible à la casse)
    public User findByUsername(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM User WHERE LOWER(username) = :username";
            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("username", username.toLowerCase());
            return query.uniqueResult();
        }
    }
}
