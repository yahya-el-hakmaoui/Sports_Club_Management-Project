package com.clubsportif.dao;

import com.clubsportif.model.Paiement;
import com.clubsportif.model.User;
import com.clubsportif.model.Paiement.StatutPaiement;
import com.clubsportif.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PaiementDaoTest {

    private static User testUser;
    private static PaiementDao paiementDao;

    @BeforeAll
    static void setup() {
        assertTrue(HibernateUtil.getSessionFactory().isOpen(), "SessionFactory should be open in setup");
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            // Clean up any existing user with the same username to avoid unique constraint violation
            User existing = session.createQuery("from User u where u.username = :username", User.class)
                    .setParameter("username", "junit_user")
                    .uniqueResult();
            if (existing != null) {
                // Remove related paiements first due to foreign key constraints
                List<Paiement> paiements = session.createQuery("from Paiement p where p.user.userId = :userId", Paiement.class)
                        .setParameter("userId", existing.getUserId())
                        .list();
                for (Paiement p : paiements) {
                    session.remove(p);
                }
                session.remove(existing);
                session.flush();
            }

            testUser = new User();
            testUser.setUsername("junit_user");
            testUser.setPasswordHash("securepassword");
            testUser.setRole(User.Role.adherent);
            testUser.setLastname("Dupont");
            testUser.setName("Jean");
            testUser.setEmail("junit@example.com");
            testUser.setTelephone("0612345678");
            testUser.setAdresse("12 rue des tests");
            testUser.setDateNaissance(LocalDate.of(1990, 5, 10));
            testUser.setDateInscription(LocalDate.now());
            testUser.setDateModification(LocalDateTime.now());
            testUser.setArchived(false);

            session.persist(testUser);

            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw e;
        }
        paiementDao = new PaiementDao();
    }

    @AfterEach
    void cleanupAfterEach() {
        assertTrue(HibernateUtil.getSessionFactory().isOpen(), "SessionFactory should be open during cleanup");
        // Remove any Paiements created during tests to keep DB clean
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            // Clean Paiements linked to testUser
            List<Paiement> paiements = session.createQuery("from Paiement p where p.user.userId = :userId", Paiement.class)
                    .setParameter("userId", testUser.getUserId())
                    .list();
            for (Paiement p : paiements) {
                session.remove(p);
            }
            tx.commit();
        }
    }

    // Removed @AfterAll shutdown() method to keep SessionFactory open until all tests finish.

    @Test
    void testCreatePaiement() {
        assertTrue(HibernateUtil.getSessionFactory().isOpen(), "SessionFactory should be open");

        Paiement paiement = new Paiement();
        paiement.setUser(testUser);
        paiement.setPeriodeDebut(LocalDate.of(2025, 1, 1));
        paiement.setPeriodeFin(LocalDate.of(2025, 12, 31));
        paiement.setMontant(new BigDecimal("150.00"));
        paiement.setStatut(StatutPaiement.paye);
        paiement.setMontantPartiel(null);
        paiement.setDatePaiement(LocalDate.now());

        paiementDao.createPaiement(paiement);

        assertNotNull(paiement.getPaiementId());
    }

    @Test
    void testUpdatePaiement() {
        assertTrue(HibernateUtil.getSessionFactory().isOpen(), "SessionFactory should be open");

        Paiement paiement = new Paiement();
        paiement.setUser(testUser);
        paiement.setPeriodeDebut(LocalDate.of(2025, 1, 1));
        paiement.setPeriodeFin(LocalDate.of(2025, 12, 31));
        paiement.setMontant(new BigDecimal("150.00"));
        paiement.setStatut(StatutPaiement.impaye);
        paiement.setMontantPartiel(null);
        paiement.setDatePaiement(null);

        // Insert for update
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(paiement);
            tx.commit();
        }

        paiement.setStatut(StatutPaiement.partiel);
        paiement.setMontantPartiel(new BigDecimal("50.00"));
        paiement.setDatePaiement(LocalDate.now());

        paiementDao.updatePaiement(paiement);

        Paiement fetched;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            fetched = session.get(Paiement.class, paiement.getPaiementId());
        }

        assertEquals(StatutPaiement.partiel, fetched.getStatut());
        assertEquals(new BigDecimal("50.00"), fetched.getMontantPartiel());
    }

    @Test
    void testDeletePaiement() {
        assertTrue(HibernateUtil.getSessionFactory().isOpen(), "SessionFactory should be open");

        Paiement paiement = new Paiement();
        paiement.setUser(testUser);
        paiement.setPeriodeDebut(LocalDate.of(2025, 1, 1));
        paiement.setPeriodeFin(LocalDate.of(2025, 12, 31));
        paiement.setMontant(new BigDecimal("150.00"));
        paiement.setStatut(StatutPaiement.paye);
        paiement.setDatePaiement(LocalDate.now());

        // Insert for delete
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(paiement);
            tx.commit();
        }

        paiementDao.deletePaiement(paiement);

        Paiement fetched;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            fetched = session.get(Paiement.class, paiement.getPaiementId());
        }
        assertNull(fetched);
    }

    @Test
    void testFindPaiementById() {
        assertTrue(HibernateUtil.getSessionFactory().isOpen(), "SessionFactory should be open");

        Paiement paiement = new Paiement();
        paiement.setUser(testUser);
        paiement.setPeriodeDebut(LocalDate.of(2025, 6, 1));
        paiement.setPeriodeFin(LocalDate.of(2025, 6, 30));
        paiement.setMontant(new BigDecimal("50.00"));
        paiement.setStatut(StatutPaiement.paye);
        paiement.setDatePaiement(LocalDate.now());

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(paiement);
            tx.commit();
        }

        Paiement fetched = paiementDao.findPaiementById(paiement.getPaiementId());
        assertNotNull(fetched);
        assertEquals(paiement.getPaiementId(), fetched.getPaiementId());
    }

    @Test
    void testFindPaiementsByUser() {
        assertTrue(HibernateUtil.getSessionFactory().isOpen(), "SessionFactory should be open");

        Paiement paiement1 = new Paiement();
        paiement1.setUser(testUser);
        paiement1.setPeriodeDebut(LocalDate.of(2025, 1, 1));
        paiement1.setPeriodeFin(LocalDate.of(2025, 1, 31));
        paiement1.setMontant(new BigDecimal("30.00"));
        paiement1.setStatut(StatutPaiement.paye);

        Paiement paiement2 = new Paiement();
        paiement2.setUser(testUser);
        paiement2.setPeriodeDebut(LocalDate.of(2025, 2, 1));
        paiement2.setPeriodeFin(LocalDate.of(2025, 2, 28));
        paiement2.setMontant(new BigDecimal("30.00"));
        paiement2.setStatut(StatutPaiement.impaye);

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(paiement1);
            session.persist(paiement2);
            tx.commit();
        }

        List<Paiement> paiements = paiementDao.findPaiementsByUser(testUser);
        assertTrue(paiements.size() >= 2);
    }

    @Test
    void testFindPaiementsByStatut() {
        assertTrue(HibernateUtil.getSessionFactory().isOpen(), "SessionFactory should be open");

        Paiement paiement1 = new Paiement();
        paiement1.setUser(testUser);
        paiement1.setPeriodeDebut(LocalDate.of(2025, 3, 1));
        paiement1.setPeriodeFin(LocalDate.of(2025, 3, 31));
        paiement1.setMontant(new BigDecimal("40.00"));
        paiement1.setStatut(StatutPaiement.partiel);

        Paiement paiement2 = new Paiement();
        paiement2.setUser(testUser);
        paiement2.setPeriodeDebut(LocalDate.of(2025, 4, 1));
        paiement2.setPeriodeFin(LocalDate.of(2025, 4, 30));
        paiement2.setMontant(new BigDecimal("40.00"));
        paiement2.setStatut(StatutPaiement.partiel);

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(paiement1);
            session.persist(paiement2);
            tx.commit();
        }

        List<Paiement> paiements = paiementDao.findPaiementsByStatut(StatutPaiement.partiel);
        assertTrue(paiements.size() >= 2);
    }

    @Test
    void testFindPaiementByUserCurrentPeriod() {
        assertTrue(HibernateUtil.getSessionFactory().isOpen(), "SessionFactory should be open");

        LocalDate today = LocalDate.now();

        Paiement paiement = new Paiement();
        paiement.setUser(testUser);
        paiement.setPeriodeDebut(today.minusDays(1));
        paiement.setPeriodeFin(today.plusDays(1));
        paiement.setMontant(new BigDecimal("100.00"));
        paiement.setStatut(StatutPaiement.paye);
        paiement.setDatePaiement(today);

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(paiement);
            tx.commit();
        }

        Paiement fetched = paiementDao.findPaiementByUserCurrentPeriod(testUser);
        assertNotNull(fetched);
        assertEquals(paiement.getPaiementId(), fetched.getPaiementId());
    }

    @Test
    void testFindAllPaiements() {
        assertTrue(HibernateUtil.getSessionFactory().isOpen(), "SessionFactory should be open");

        Paiement paiement1 = new Paiement();
        paiement1.setUser(testUser);
        paiement1.setPeriodeDebut(LocalDate.of(2025, 7, 1));
        paiement1.setPeriodeFin(LocalDate.of(2025, 7, 31));
        paiement1.setMontant(new BigDecimal("80.00"));
        paiement1.setStatut(StatutPaiement.paye);
        paiement1.setDatePaiement(LocalDate.now());

        Paiement paiement2 = new Paiement();
        paiement2.setUser(testUser);
        paiement2.setPeriodeDebut(LocalDate.of(2025, 8, 1));
        paiement2.setPeriodeFin(LocalDate.of(2025, 8, 31));
        paiement2.setMontant(new BigDecimal("90.00"));
        paiement2.setStatut(StatutPaiement.impaye);
        paiement2.setDatePaiement(null);

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(paiement1);
            session.persist(paiement2);
            tx.commit();
        }

        List<Paiement> allPaiements = paiementDao.findAllPaiements();
        assertNotNull(allPaiements);
        assertTrue(allPaiements.stream().anyMatch(p -> p.getPaiementId().equals(paiement1.getPaiementId())));
        assertTrue(allPaiements.stream().anyMatch(p -> p.getPaiementId().equals(paiement2.getPaiementId())));
    }
}
