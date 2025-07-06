package com.clubsportif.service;

import com.clubsportif.dao.UserDao;
import com.clubsportif.model.User;
import com.clubsportif.util.PasswordUtils;

// Service pour l'authentification et le contrôle d'accès
public class AuthService {

    private final UserDao userDao;

    // Constructeur du service d'authentification
    public AuthService() {
        this.userDao = new UserDao();
    }

    /**
     * Authentifie un utilisateur avec le nom d'utilisateur et le mot de passe.
     * Retourne l'utilisateur authentifié ou null si échec.
     */
    public User authenticate(String username, String password) {
        // Recherche l'utilisateur par nom d'utilisateur
        User user = userDao.findByUsername(username);
        if (user == null) {
            return null;
        }
        // Hash le mot de passe fourni et compare avec le hash stocké
        String hashed = PasswordUtils.hashPassword(password);
        if (hashed.equals(user.getPasswordHash())) {
            return user;
        }
        return null;
    }

    // Vérifie si l'utilisateur est administrateur
    public boolean isAdmin(User user) {
        return user != null && user.getRole() == User.Role.admin;
    }

    // Vérifie si l'utilisateur est adhérent
    public boolean isAdherent(User user) {
        return user != null && user.getRole() == User.Role.adherent;
    }

    // Vérifie si l'utilisateur est assistant
    public boolean isAssistant(User user) {
        return user != null && user.getRole() == User.Role.assistant;
    }
}
