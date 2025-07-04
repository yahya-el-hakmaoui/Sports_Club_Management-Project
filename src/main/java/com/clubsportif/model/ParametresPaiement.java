package com.clubsportif.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

@Entity
@Table(name = "parametres_paiement")
public class ParametresPaiement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "frais_base_annuel", nullable = false, precision = 8, scale = 2)
    private Double fraisBaseAnnuel;

    @Column(name = "frais_base_mensuel", nullable = false, precision = 8, scale = 2)
    private Double fraisBaseMensuel;

    // Constructeur par défaut
    public ParametresPaiement() {}

    // Getters et Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getFraisBaseAnnuel() {
        return fraisBaseAnnuel;
    }

    public void setFraisBaseAnnuel(Double fraisBaseAnnuel) {
        this.fraisBaseAnnuel = fraisBaseAnnuel;
    }

    public Double getFraisBaseMensuel() {
        return fraisBaseMensuel;
    }

    public void setFraisBaseMensuel(Double fraisBaseMensuel) {
        this.fraisBaseMensuel = fraisBaseMensuel;
    }
}
