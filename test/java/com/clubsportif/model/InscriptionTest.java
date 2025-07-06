package com.clubsportif.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InscriptionTest {

    private Inscription inscription;
    private User user;
    private Activity activity;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setUserId(1);
        user.setUsername("john_doe");
        // configure other User fields si nécessaire

        activity = new Activity();
        activity.setActivityId(10);
        activity.setNom("Tennis");
        // configure other Activity fields si nécessaire

        inscription = new Inscription(user, activity, LocalDate.of(2025, 7, 6), true);
    }

    @Test
    public void testGetters() {
        assertEquals(1, inscription.getUser().getUserId());
        assertEquals("john_doe", inscription.getUser().getUsername());

        assertEquals(10, inscription.getActivity().getActivityId());
        assertEquals("Tennis", inscription.getActivity().getNom());

        assertEquals(LocalDate.of(2025, 7, 6), inscription.getDateInscription());

        assertTrue(inscription.isActive());
    }

    @Test
    public void testSetters() {
        inscription.setActive(false);
        assertFalse(inscription.isActive());

        LocalDate newDate = LocalDate.of(2025, 8, 1);
        inscription.setDateInscription(newDate);
        assertEquals(newDate, inscription.getDateInscription());

        User newUser = new User();
        newUser.setUserId(2);
        inscription.setUser(newUser);
        assertEquals(2, inscription.getUser().getUserId());

        Activity newActivity = new Activity();
        newActivity.setActivityId(20);
        inscription.setActivity(newActivity);
    assertEquals(20, inscription.getActivity().getActivityId());
    }
}
