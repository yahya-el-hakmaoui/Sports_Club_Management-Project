package com.clubsportif.service;

import com.clubsportif.dao.InscriptionDao;
import com.clubsportif.model.Inscription;
import com.clubsportif.model.User;
import com.clubsportif.model.Activity;

import java.util.List;

// Service pour la gestion des inscriptions
public class InscriptionService {

    private final InscriptionDao inscriptionDao;

    // Constructeur du service d'inscription
    public InscriptionService() {
        this.inscriptionDao = new InscriptionDao();
    }

    // Créer une nouvelle inscription
    public void createInscription(Inscription inscription) {
        inscriptionDao.createInscription(inscription);
    }

    // Récupérer une inscription par son identifiant
    public Inscription getInscriptionById(int id) {
        return inscriptionDao.getInscriptionById(id);
    }

    // Mettre à jour une inscription existante
    public void updateInscription(Inscription inscription) {
        inscriptionDao.updateInscription(inscription);
    }

    // Supprimer une inscription définitivement
    public void deleteInscription(Inscription inscription) {
        inscriptionDao.deleteInscription(inscription);
    }

    // Obtenir la liste des inscriptions actives d'un utilisateur
    public List<Inscription> getActiveInscriptionsByUser(User user) {
        return inscriptionDao.getActiveInscriptionsByUser(user);
    }

    // Obtenir la liste des inscriptions pour une activité donnée
    public List<Inscription> getInscriptionsByActivity(Activity activity) {
        return inscriptionDao.getInscriptionsByActivity(activity);
    }

    // Compter le nombre d'inscrits actifs pour une activité
    public long countActiveInscriptionsForActivity(Activity activity) {
        return inscriptionDao.countActiveInscriptionsForActivity(activity);
    }

    // Désactiver une inscription (désinscrire sans supprimer)
    public void deactivateInscription(Inscription inscription) {
        inscriptionDao.deactivateInscription(inscription);
    }

    // Activer une inscription (réinscrire)
    public void activateInscription(Inscription inscription) {
        inscriptionDao.activateInscription(inscription);
    }
}
