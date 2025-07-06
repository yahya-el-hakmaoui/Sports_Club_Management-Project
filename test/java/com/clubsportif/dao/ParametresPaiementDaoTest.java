package com.clubsportif.dao;

import com.clubsportif.model.ParametresPaiement;
import com.clubsportif.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ParametresPaiementDaoTest {

    private ParametresPaiementDao dao;
    private Integer testId;

    @BeforeAll
    public void setup() {
        dao = new ParametresPaiementDao();
        // Optional: force SessionFactory initialization
        HibernateUtil.getSessionFactory();
    }

    @AfterEach
    public void cleanUpTestData() {
        // Clean up test data after each test, but do NOT close the SessionFactory
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.createQuery("DELETE FROM ParametresPaiement WHERE fraisBaseAnnuel IN (:f1, :f2)")
                    .setParameter("f1", new BigDecimal("300.00"))
                    .setParameter("f2", new BigDecimal("350.00"))
                    .executeUpdate();
            tx.commit();
        } catch (Exception e) {
            System.err.println("Erreur de nettoyage : " + e.getMessage());
        }
        testId = null; // Reset testId after each test to ensure isolation
    }

    @Test
    @Order(1)
    void testInsertParametresPaiement() {
        assertTrue(HibernateUtil.getSessionFactory().isOpen(), "SessionFactory should be open");
        ParametresPaiement param = new ParametresPaiement(
                new BigDecimal("300.00"),
                new BigDecimal("30.00")
        );

        dao.insertParametresPaiement(param);
        assertNotNull(param.getParametresPaiementId());

        testId = param.getParametresPaiementId();
    }

    @Test
    @Order(2)
    void testGetParametresPaiementById() {
        assertTrue(HibernateUtil.getSessionFactory().isOpen(), "SessionFactory should be open");
        // Insert a record if not present
        if (testId == null) {
            ParametresPaiement param = new ParametresPaiement(
                    new BigDecimal("300.00"),
                    new BigDecimal("30.00")
            );
            dao.insertParametresPaiement(param);
            testId = param.getParametresPaiementId();
        }
        ParametresPaiement found = dao.getParametresPaiementById(testId);
        assertNotNull(found);
        assertEquals(new BigDecimal("300.00"), found.getFraisBaseAnnuel());
    }

    @Test
    @Order(3)
    void testUpdateParametresPaiement() {
        assertTrue(HibernateUtil.getSessionFactory().isOpen(), "SessionFactory should be open");
        // Insert a record if not present
        if (testId == null) {
            ParametresPaiement param = new ParametresPaiement(
                    new BigDecimal("300.00"),
                    new BigDecimal("30.00")
            );
            dao.insertParametresPaiement(param);
            testId = param.getParametresPaiementId();
        }
        ParametresPaiement toUpdate = dao.getParametresPaiementById(testId);
        assertNotNull(toUpdate);
        toUpdate.setFraisBaseAnnuel(new BigDecimal("350.00"));

        dao.updateParametresPaiement(toUpdate);
        ParametresPaiement updated = dao.getParametresPaiementById(testId);
        assertEquals(new BigDecimal("350.00"), updated.getFraisBaseAnnuel());
    }

    @Test
    @Order(4)
    void testGetAllParametresPaiement() {
        assertTrue(HibernateUtil.getSessionFactory().isOpen(), "SessionFactory should be open");
        // Insert a record to ensure at least one exists
        ParametresPaiement param = new ParametresPaiement(
                new BigDecimal("300.00"),
                new BigDecimal("30.00")
        );
        dao.insertParametresPaiement(param);
        List<ParametresPaiement> list = dao.getAllParametresPaiement();
        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

    @Test
    @Order(5)
    void testDeleteParametresPaiement() {
        assertTrue(HibernateUtil.getSessionFactory().isOpen(), "SessionFactory should be open");
        // Insert a record if not present
        if (testId == null) {
            ParametresPaiement param = new ParametresPaiement(
                    new BigDecimal("300.00"),
                    new BigDecimal("30.00")
            );
            dao.insertParametresPaiement(param);
            testId = param.getParametresPaiementId();
        }
        ParametresPaiement toDelete = dao.getParametresPaiementById(testId);
        assertNotNull(toDelete);

        dao.deleteParametresPaiement(toDelete);

        ParametresPaiement shouldBeNull = dao.getParametresPaiementById(testId);
        assertNull(shouldBeNull);
    }
}
