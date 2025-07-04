package com.clubsportif.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "paiements")
public class Paiement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "paiement_id")
    private Integer paiementId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "periode_debut", nullable = false)
    private LocalDate periodeDebut;

    @Column(name = "periode_fin", nullable = false)
    private LocalDate periodeFin;

    @Column(name = "montant", nullable = false, precision = 8, scale = 2)
    private BigDecimal montant;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false, length = 10)
    private StatutPaiement statut;

    @Column(name = "montant_partiel", precision = 8, scale = 2)
    private BigDecimal montantPartiel;

    @Column(name = "date_paiement")
    private LocalDate datePaiement;

    // Enum pour le statut de paiement
    public enum StatutPaiement {
        paye,
        impaye,
        partiel
    }

    // Constructeur par défaut
    public Paiement() {
    }

    // Getters et setters

    public Integer getPaiementId() {
        return paiementId;
    }

    public void setPaiementId(Integer paiementId) {
        this.paiementId = paiementId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getPeriodeDebut() {
        return periodeDebut;
    }

    public void setPeriodeDebut(LocalDate periodeDebut) {
        this.periodeDebut = periodeDebut;
    }

    public LocalDate getPeriodeFin() {
        return periodeFin;
    }

    public void setPeriodeFin(LocalDate periodeFin) {
        this.periodeFin = periodeFin;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public StatutPaiement getStatut() {
        return statut;
    }

    public void setStatut(StatutPaiement statut) {
        this.statut = statut;
    }

    public BigDecimal getMontantPartiel() {
        return montantPartiel;
    }

    public void setMontantPartiel(BigDecimal montantPartiel) {
        this.montantPartiel = montantPartiel;
    }

    public LocalDate getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(LocalDate datePaiement) {
        this.datePaiement = datePaiement;
    }
}
