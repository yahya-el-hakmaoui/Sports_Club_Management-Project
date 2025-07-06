package com.clubsportif.model;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ActivityTest {

    private Activity activity;

    @BeforeEach
    void setUp() {
        activity = new Activity();
    }

    @Test
    void testGettersAndSetters() {
        String nom = "Tennis";
        BigDecimal tarif = new BigDecimal("50.00");
        Integer maxParticipants = 20;
        String description = "Activité sportive de Tennis";

        activity.setNom(nom);
        activity.setTarif(tarif);
        activity.setMaxParticipants(maxParticipants);
        activity.setDescription(description);

        assertEquals(nom, activity.getNom());
        assertEquals(tarif, activity.getTarif());
        assertEquals(maxParticipants, activity.getMaxParticipants());
        assertEquals(description, activity.getDescription());
    }

    @Test
    void testInscriptionsManagement() {
        Inscription inscription1 = new Inscription();
        Inscription inscription2 = new Inscription();

        // Initialement, la liste d'inscriptions est vide
        assertTrue(activity.getInscriptions().isEmpty());

        // Ajouter une inscription
        activity.addInscription(inscription1);
        assertTrue(activity.getInscriptions().contains(inscription1));
        assertEquals(activity, inscription1.getActivity());

        // Ajouter une autre inscription
        activity.addInscription(inscription2);
        assertEquals(2, activity.getInscriptions().size());

        // Supprimer une inscription
        activity.removeInscription(inscription1);
        assertFalse(activity.getInscriptions().contains(inscription1));
        assertNull(inscription1.getActivity());
        assertEquals(1, activity.getInscriptions().size());
    }

    @Test
    void testDefaultConstructor() {
        Activity defaultActivity = new Activity();
        assertNotNull(defaultActivity);
        assertTrue(defaultActivity.getInscriptions().isEmpty());
    }

    @Test
    void testFullConstructor() {
        String nom = "Basketball";
        BigDecimal tarif = new BigDecimal("75.50");
        Integer maxParticipants = 15;
        String description = "Basketball pour tous";

        Activity act = new Activity(nom, tarif, maxParticipants, description);

        assertEquals(nom, act.getNom());
        assertEquals(tarif, act.getTarif());
        assertEquals(maxParticipants, act.getMaxParticipants());
        assertEquals(description, act.getDescription());
    }
}
