package com.clubsportif.service;

import com.clubsportif.dao.UserDao;
import com.clubsportif.model.User;

import java.util.List;

// Service pour la gestion des utilisateurs
public class UserService {

    private final UserDao userDao;

    // Constructeur du service utilisateur
    public UserService() {
        this.userDao = new UserDao();
    }

    // Créer un nouvel utilisateur
    public void createUser(User user) {
        userDao.createUser(user);
    }

    // Mettre à jour un utilisateur existant
    public void updateUser(User user) {
        userDao.updateUser(user);
    }

    // Supprimer un utilisateur
    public void deleteUser(User user) {
        userDao.deleteUser(user);
    }

    // Trouver un utilisateur par ID
    public User getUserById(int id) {
        return userDao.findById(id);
    }

    // Récupérer tous les utilisateurs
    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    // Récupérer les utilisateurs archivés
    public List<User> getArchivedUsers() {
        return userDao.findArchived();
    }

    // Récupérer les utilisateurs non archivés
    public List<User> getNotArchivedUsers() {
        return userDao.findNotArchived();
    }

    // Rechercher des utilisateurs par nom (partiel, insensible à la casse)
    public List<User> searchUsersByName(String namePart) {
        return userDao.findByName(namePart);
    }

    // Rechercher des utilisateurs par prénom (partiel, insensible à la casse)
    public List<User> searchUsersByLastname(String lastnamePart) {
        return userDao.findByLastname(lastnamePart);
    }
}

