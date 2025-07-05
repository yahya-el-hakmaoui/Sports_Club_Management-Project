package com.clubsportif.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(unique = true)
    private String email;

    private String telephone;

    private String adresse;

    @Column(name = "date_naissance")
    @Temporal(TemporalType.DATE)
    private Date dateNaissance;

    @Column(name = "date_inscription", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dateInscription;

    @Column(name = "date_modification")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateModification;

    @Column(nullable = false)
    private boolean archived = false;

    // Relations

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Paiement> paiements = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Inscription> inscriptions = new HashSet<>();

    public enum Role {
        admin, adherent, assistant
    }

    // ======= Constructeurs =======

    public User() {
        // Constructeur sans argument requis par Hibernate
    }

    public User(String username, String passwordHash, Role role, String nom, String prenom,
                String email, String telephone, String adresse, Date dateNaissance,
                Date dateInscription, Date dateModification, boolean archived) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.adresse = adresse;
        this.dateNaissance = dateNaissance;
        this.dateInscription = dateInscription;
        this.dateModification = dateModification;
        this.archived = archived;
    }

    // ======= Getters et Setters =======

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public Date getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(Date dateInscription) {
        this.dateInscription = dateInscription;
    }

    public Date getDateModification() {
        return dateModification;
    }

    public void setDateModification(Date dateModification) {
        this.dateModification = dateModification;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public Set<Paiement> getPaiements() {
        return paiements;
    }

    public void setPaiements(Set<Paiement> paiements) {
        this.paiements = paiements;
    }

    public Set<Inscription> getInscriptions() {
        return inscriptions;
    }

    public void setInscriptions(Set<Inscription> inscriptions) {
        this.inscriptions = inscriptions;
    }
}
