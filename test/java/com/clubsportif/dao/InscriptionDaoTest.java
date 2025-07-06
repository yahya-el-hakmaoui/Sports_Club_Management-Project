package com.clubsportif.dao;

import com.clubsportif.model.Activity;
import com.clubsportif.model.Inscription;
import com.clubsportif.model.User;
import com.clubsportif.util.HibernateUtil;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class InscriptionDaoTest {

    private InscriptionDao inscriptionDao;
    private User testUser;
    private Activity testActivity;

    @BeforeAll
    public void setupTestData() {
        inscriptionDao = new InscriptionDao();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            testUser = new User();
            testUser.setUsername("testuser");
            testUser.setPasswordHash("testpassword");
            testUser.setRole(User.Role.valueOf("adherent"));
            testUser.setLastname("Test");
            testUser.setName("User");
            testUser.setDateInscription(LocalDate.now());
            session.persist(testUser);

            testActivity = new Activity();
            testActivity.setNom("Test Activity");
            testActivity.setTarif(BigDecimal.valueOf(50.0));
            session.persist(testActivity);

            tx.commit();
        }
    }

    @AfterEach
    public void cleanupTestData() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            // Delete inscriptions related to testUser or testActivity
            session.createQuery("DELETE FROM Inscription i WHERE i.user = :user OR i.activity = :activity")
                    .setParameter("user", testUser)
                    .setParameter("activity", testActivity)
                    .executeUpdate();

            tx.commit();
        }
    }

    @AfterAll
    public void cleanupUsersAndActivities() {
        // Clean up testUser and testActivity after all tests complete
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            User userToRemove = session.get(User.class, testUser.getUserId());
            if (userToRemove != null) {
                session.remove(userToRemove);
            }

            Activity activityToRemove = session.get(Activity.class, testActivity.getActivityId());
            if (activityToRemove != null) {
                session.remove(activityToRemove);
            }

            tx.commit();
        }
    }

    private void ensureSessionFactoryIsOpen() {
        assertTrue(HibernateUtil.getSessionFactory().isOpen(), "SessionFactory should be open");
    }

    @Test
    public void testCreateAndGetInscription() {
        ensureSessionFactoryIsOpen();

        Inscription inscription = new Inscription(testUser, testActivity, LocalDate.now(), true);
        inscriptionDao.createInscription(inscription);

        assertTrue(inscription.getInscriptionId() > 0);

        Inscription fetched = inscriptionDao.getInscriptionById(inscription.getInscriptionId());
        assertNotNull(fetched);
        assertEquals(testUser.getUserId(), fetched.getUser().getUserId());
        assertEquals(testActivity.getActivityId(), fetched.getActivity().getActivityId());

        // Cleanup handled in @AfterEach
    }

    @Test
    public void testUpdateInscription() {
        ensureSessionFactoryIsOpen();

        Inscription inscription = new Inscription(testUser, testActivity, LocalDate.now(), true);
        inscriptionDao.createInscription(inscription);

        inscription.setActive(false);
        inscriptionDao.updateInscription(inscription);

        Inscription fetched = inscriptionDao.getInscriptionById(inscription.getInscriptionId());
        assertFalse(fetched.isActive());

        // Cleanup handled in @AfterEach
    }

    @Test
    public void testDeactivateAndActivateInscription() {
        ensureSessionFactoryIsOpen();

        Inscription inscription = new Inscription(testUser, testActivity, LocalDate.now(), true);
        inscriptionDao.createInscription(inscription);

        inscriptionDao.deactivateInscription(inscription);
        Inscription deactivated = inscriptionDao.getInscriptionById(inscription.getInscriptionId());
        assertFalse(deactivated.isActive());

        inscriptionDao.activateInscription(deactivated);
        Inscription activated = inscriptionDao.getInscriptionById(inscription.getInscriptionId());
        assertTrue(activated.isActive());

        // Cleanup handled in @AfterEach
    }

    @Test
    public void testGetActiveInscriptionsByUser() {
        ensureSessionFactoryIsOpen();

        Inscription inscription1 = new Inscription(testUser, testActivity, LocalDate.now(), true);
        inscriptionDao.createInscription(inscription1);

        List<Inscription> activeInscriptions = inscriptionDao.getActiveInscriptionsByUser(testUser);
        assertTrue(activeInscriptions.stream().anyMatch(i -> i.getInscriptionId() == inscription1.getInscriptionId()));

        // Cleanup handled in @AfterEach
    }

    @Test
    public void testCountActiveInscriptionsForActivity() {
        ensureSessionFactoryIsOpen();

        Inscription inscription1 = new Inscription(testUser, testActivity, LocalDate.now(), true);
        inscriptionDao.createInscription(inscription1);

        long count = inscriptionDao.countActiveInscriptionsForActivity(testActivity);
        assertTrue(count >= 1);

        // Cleanup handled in @AfterEach
    }
}
