package com.clubsportif.service;

import com.clubsportif.dao.ParametresPaiementDao;
import com.clubsportif.model.ParametresPaiement;

import java.util.List;

// Service pour la gestion des paramètres de paiement
public class ParametresPaiementService {

    private final ParametresPaiementDao parametresPaiementDao;

    // Constructeur du service de paramètres de paiement
    public ParametresPaiementService() {
        this.parametresPaiementDao = new ParametresPaiementDao();
    }

    // Créer un nouveau paramètre de paiement
    public void createParametresPaiement(ParametresPaiement param) {
        parametresPaiementDao.insertParametresPaiement(param);
    }

    // Mettre à jour un paramètre de paiement existant
    public void updateParametresPaiement(ParametresPaiement param) {
        parametresPaiementDao.updateParametresPaiement(param);
    }

    // Récupérer un paramètre de paiement par son identifiant
    public ParametresPaiement getParametresPaiementById(Integer id) {
        return parametresPaiementDao.getParametresPaiementById(id);
    }

    // Supprimer un paramètre de paiement par entité
    public void deleteParametresPaiement(ParametresPaiement param) {
        parametresPaiementDao.deleteParametresPaiement(param);
    }

    // Supprimer un paramètre de paiement par son identifiant
    public void deleteParametresPaiementById(Integer id) {
        parametresPaiementDao.deleteParametresPaiementById(id);
    }

    // Récupérer tous les paramètres de paiement
    public List<ParametresPaiement> getAllParametresPaiement() {
        return parametresPaiementDao.getAllParametresPaiement();
    }
}

