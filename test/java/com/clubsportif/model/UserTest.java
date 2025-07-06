package com.clubsportif.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private User user;
    private LocalDateTime now;

    @BeforeEach
    public void setUp() {
        now = LocalDateTime.now();
        user = new User(
                "johndoe",
                "hashed_password",
                User.Role.adherent,
                "Doe",
                "John",
                "john.doe@example.com",
                "0600000000",
                "123 Rue Exemple",
                LocalDate.of(1990, 1, 1),  // 1 Jan 1990
                now.toLocalDate(),
                now,
                false
        );
    }

    @Test
    public void testUserInitialization() {
        assertEquals("johndoe", user.getUsername());
        assertEquals("hashed_password", user.getPasswordHash());
        assertEquals(User.Role.adherent, user.getRole());
        assertEquals("Doe", user.getLastname());
        assertEquals("John", user.getName());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals("0600000000", user.getTelephone());
        assertEquals("123 Rue Exemple", user.getAdresse());
        assertEquals(now.toLocalDate(), user.getDateInscription());
        assertEquals(now, user.getDateModification());
        assertFalse(user.isArchived());
    }

    @Test
    public void testSettersAndGetters() {
        user.setLastname("Martin");
        assertEquals("Martin", user.getLastname());

        user.setRole(User.Role.admin);
        assertEquals(User.Role.admin, user.getRole());

        user.setArchived(true);
        assertTrue(user.isArchived());

        user.setEmail("new.email@example.com");
        assertEquals("new.email@example.com", user.getEmail());
    }

    @Test
    public void testAddPaiementAndInscription() {
        Paiement paiement = new Paiement(); // à implémenter avec un constructeur ou builder plus tard
        Inscription inscription = new Inscription(); // idem

        user.getPaiements().add(paiement);
        user.getInscriptions().add(inscription);

        assertEquals(1, user.getPaiements().size());
        assertEquals(1, user.getInscriptions().size());
    }
}
