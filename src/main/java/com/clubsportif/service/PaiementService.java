package com.clubsportif.service;

import com.clubsportif.dao.PaiementDao;
import com.clubsportif.model.Paiement;
import com.clubsportif.model.User;
import com.clubsportif.model.Paiement.StatutPaiement;

import java.util.List;

// Service pour la gestion des paiements
public class PaiementService {

    private final PaiementDao paiementDao;

    // Constructeur du service de paiement
    public PaiementService() {
        this.paiementDao = new PaiementDao();
    }

    // Créer un nouveau paiement
    public void createPaiement(Paiement paiement) {
        paiementDao.createPaiement(paiement);
    }

    // Mettre à jour un paiement existant
    public void updatePaiement(Paiement paiement) {
        paiementDao.updatePaiement(paiement);
    }

    // Supprimer un paiement
    public void deletePaiement(Paiement paiement) {
        paiementDao.deletePaiement(paiement);
    }

    // Trouver un paiement par son identifiant
    public Paiement getPaiementById(Integer id) {
        return paiementDao.findPaiementById(id);
    }

    // Récupérer tous les paiements d'un utilisateur
    public List<Paiement> getPaiementsByUser(User user) {
        return paiementDao.findPaiementsByUser(user);
    }

    // Récupérer les paiements selon le statut
    public List<Paiement> getPaiementsByStatut(StatutPaiement statut) {
        return paiementDao.findPaiementsByStatut(statut);
    }

    // Trouver le paiement courant d'un utilisateur pour la période actuelle
    public Paiement getPaiementByUserCurrentPeriod(User user) {
        return paiementDao.findPaiementByUserCurrentPeriod(user);
    }

    // Récupérer la liste de tous les paiements
    public List<Paiement> getAllPaiements() {
        return paiementDao.findAllPaiements();
    }
}

