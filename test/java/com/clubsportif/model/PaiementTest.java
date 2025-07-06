package com.clubsportif.model;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class PaiementTest {

    @Test
    public void testConstructorAndGetters() {
        User user = new User();
        user.setUserId(1);

        LocalDate start = LocalDate.of(2025, 1, 1);
        LocalDate end = LocalDate.of(2025, 12, 31);
        BigDecimal montant = new BigDecimal("150.00");
        BigDecimal montantPartiel = new BigDecimal("50.00");
        LocalDate datePaiement = LocalDate.of(2025, 1, 5);

        Paiement paiement = new Paiement(
                user,
                start,
                end,
                montant,
                Paiement.StatutPaiement.partiel,
                montantPartiel,
                datePaiement
        );

        assertEquals(user, paiement.getUser());
        assertEquals(start, paiement.getPeriodeDebut());
        assertEquals(end, paiement.getPeriodeFin());
        assertEquals(montant, paiement.getMontant());
        assertEquals(Paiement.StatutPaiement.partiel, paiement.getStatut());
        assertEquals(montantPartiel, paiement.getMontantPartiel());
        assertEquals(datePaiement, paiement.getDatePaiement());
    }

    @Test
    public void testSetters() {
        Paiement paiement = new Paiement();

        User user = new User();
        user.setUserId(2);

        paiement.setUser(user);
        assertEquals(user, paiement.getUser());

        LocalDate start = LocalDate.of(2024, 6, 1);
        paiement.setPeriodeDebut(start);
        assertEquals(start, paiement.getPeriodeDebut());

        LocalDate end = LocalDate.of(2025, 5, 31);
        paiement.setPeriodeFin(end);
        assertEquals(end, paiement.getPeriodeFin());

        BigDecimal montant = new BigDecimal("200.00");
        paiement.setMontant(montant);
        assertEquals(montant, paiement.getMontant());

        paiement.setStatut(Paiement.StatutPaiement.paye);
        assertEquals(Paiement.StatutPaiement.paye, paiement.getStatut());

        BigDecimal montantPartiel = new BigDecimal("0.00");
        paiement.setMontantPartiel(montantPartiel);
        assertEquals(montantPartiel, paiement.getMontantPartiel());

        LocalDate datePaiement = LocalDate.of(2024, 6, 15);
        paiement.setDatePaiement(datePaiement);
        assertEquals(datePaiement, paiement.getDatePaiement());
    }
}
