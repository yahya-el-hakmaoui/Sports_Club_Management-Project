package com.clubsportif.service;

import com.clubsportif.dao.ActivityDao;
import com.clubsportif.model.Activity;

import java.util.List;

// Service pour la gestion des activités
public class ActivityService {

    private final ActivityDao activityDao;

    // Constructeur du service d'activité
    public ActivityService() {
        this.activityDao = new ActivityDao();
    }

    // Créer une nouvelle activité
    public void createActivity(Activity activity) {
        activityDao.createActivity(activity);
    }

    // Mettre à jour une activité existante
    public Activity updateActivity(Activity activity) {
        return activityDao.updateActivity(activity);
    }

    // Supprimer une activité par son identifiant
    public boolean deleteActivityById(int activityId) {
        return activityDao.deleteActivityById(activityId);
    }

    // Trouver une activité par son identifiant
    public Activity getActivityById(int activityId) {
        return activityDao.findActivityById(activityId);
    }

    // Trouver une activité par son nom
    public Activity getActivityByName(String name) {
        return activityDao.findActivityByName(name);
    }

    // Récupérer toutes les activités
    public List<Activity> getAllActivities() {
        return activityDao.findAllActivities();
    }
}

