package com.clubsportif.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "activities")
public class Activity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activity_id")
    private int activityId;

    @Column(name = "nom", nullable = false, unique = true, length = 100)
    private String nom;

    @Column(name = "tarif", nullable = false, precision = 8, scale = 2)
    private double tarif;

    @Column(name = "max_participants")
    private Integer maxParticipants;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    // Relation avec inscriptions (1 activité -> plusieurs inscriptions)
    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Inscription> inscriptions = new HashSet<>();

    // Constructeur par défaut requis par Hibernate
    public Activity() {
    }

    // Constructeur avec tous les champs (sauf inscriptions)
    public Activity(String nom, double tarif, Integer maxParticipants, String description) {
        this.nom = nom;
        this.tarif = tarif;
        this.maxParticipants = maxParticipants;
        this.description = description;
    }

    // --- Getters and Setters ---

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getTarif() {
        return tarif;
    }

    public void setTarif(double tarif) {
        this.tarif = tarif;
    }

    public Integer getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(Integer maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Inscription> getInscriptions() {
        return inscriptions;
    }

    public void setInscriptions(Set<Inscription> inscriptions) {
        this.inscriptions = inscriptions;
    }

    // Ajout et suppression pratiques pour maintenir la relation bidirectionnelle

    public void addInscription(Inscription inscription) {
        inscriptions.add(inscription);
        inscription.setActivity(this);
    }

    public void removeInscription(Inscription inscription) {
        inscriptions.remove(inscription);
        inscription.setActivity(null);
    }
}
