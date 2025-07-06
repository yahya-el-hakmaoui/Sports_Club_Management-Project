package com.clubsportif.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class ParametresPaiementTest {

    @Test
    void testConstructorAndGetters() {
        BigDecimal fraisAnnuel = new BigDecimal("120.00");
        BigDecimal fraisMensuel = new BigDecimal("15.00");

        ParametresPaiement params = new ParametresPaiement(fraisAnnuel, fraisMensuel);

        assertEquals(fraisAnnuel, params.getFraisBaseAnnuel(), "Frais base annuel should match");
        assertEquals(fraisMensuel, params.getFraisBaseMensuel(), "Frais base mensuel should match");
    }

    @Test
    void testSettersAndGetters() {
        ParametresPaiement params = new ParametresPaiement();

        BigDecimal fraisAnnuel = new BigDecimal("200.00");
        BigDecimal fraisMensuel = new BigDecimal("25.00");

        params.setFraisBaseAnnuel(fraisAnnuel);
        params.setFraisBaseMensuel(fraisMensuel);

        assertEquals(fraisAnnuel, params.getFraisBaseAnnuel());
        assertEquals(fraisMensuel, params.getFraisBaseMensuel());
    }

    @Test
    void testIdSetterGetter() {
        ParametresPaiement params = new ParametresPaiement();

        params.setParametresPaiementId(10);
        assertEquals(10, params.getParametresPaiementId());
    }
}
