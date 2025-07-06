package com.clubsportif.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import java.math.BigDecimal;

@Entity
@Table(name = "parametres_paiement")
public class ParametresPaiement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "parametres_paiement_id")
    private Integer parametresPaiementId;

    @Column(name = "frais_base_annuel", nullable = false, precision = 8, scale = 2)
    private BigDecimal fraisBaseAnnuel;

    @Column(name = "frais_base_mensuel", nullable = false, precision = 8, scale = 2)
    private BigDecimal fraisBaseMensuel;

    // Constructeur par défaut
    public ParametresPaiement() {}

    // Constructeur avec paramètres
    public ParametresPaiement(BigDecimal fraisBaseAnnuel, BigDecimal fraisBaseMensuel) {
        this.fraisBaseAnnuel = fraisBaseAnnuel;
        this.fraisBaseMensuel = fraisBaseMensuel;
    }

    // Getters et Setters

    public Integer getParametresPaiementId() {
        return parametresPaiementId;
    }

    public void setParametresPaiementId(Integer id) {
        this.parametresPaiementId = id;
    }

    public BigDecimal getFraisBaseAnnuel() {
        return fraisBaseAnnuel;
    }

    public void setFraisBaseAnnuel(BigDecimal fraisBaseAnnuel) {
        this.fraisBaseAnnuel = fraisBaseAnnuel;
    }

    public BigDecimal getFraisBaseMensuel() {
        return fraisBaseMensuel;
    }

    public void setFraisBaseMensuel(BigDecimal fraisBaseMensuel) {
        this.fraisBaseMensuel = fraisBaseMensuel;
    }
}
