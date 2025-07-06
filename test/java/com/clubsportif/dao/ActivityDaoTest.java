package com.clubsportif.dao;

import com.clubsportif.model.Activity;
import com.clubsportif.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ActivityDaoTest {

    private static ActivityDao activityDao;

    @BeforeAll
    public static void setup() {
        activityDao = new ActivityDao();
    }

    @AfterEach
    public void cleanup() {
        assertFalse(HibernateUtil.getSessionFactory().isClosed(), "SessionFactory should be open");

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.createMutationQuery("DELETE FROM Activity").executeUpdate();
            tx.commit();
        }
    }

    // Do not close SessionFactory in @AfterAll

    @Test
    @Order(1)
    public void testCreateActivity() {
        assertFalse(HibernateUtil.getSessionFactory().isClosed());

        Activity activity = new Activity("Tennis", new BigDecimal("100.00"), 10, "Activité sportive de raquette");
        activityDao.createActivity(activity);

        assertTrue(activity.getActivityId() > 0, "L'identifiant doit être généré.");
    }

    @Test
    @Order(2)
    public void testFindActivityById() {
        assertFalse(HibernateUtil.getSessionFactory().isClosed());

        Activity activity = new Activity("Natation", new BigDecimal("80.00"), 15, "Activité aquatique");
        activityDao.createActivity(activity);

        Activity found = activityDao.findActivityById(activity.getActivityId());
        assertNotNull(found);
        assertEquals("Natation", found.getNom());
    }

    @Test
    @Order(3)
    public void testFindActivityByName() {
        assertFalse(HibernateUtil.getSessionFactory().isClosed());

        Activity activity = new Activity("Basketball", new BigDecimal("90.00"), 20, "Sport d'équipe");
        activityDao.createActivity(activity);

        Activity found = activityDao.findActivityByName("Basketball");
        assertNotNull(found);
        assertEquals(activity.getActivityId(), found.getActivityId());
    }

    @Test
    @Order(4)
    public void testUpdateActivity() {
        assertFalse(HibernateUtil.getSessionFactory().isClosed());

        Activity activity = new Activity("Yoga", new BigDecimal("60.00"), 8, "Activité zen");
        activityDao.createActivity(activity);

        activity.setTarif(new BigDecimal("70.00"));
        activity.setDescription("Activité de relaxation");
        Activity updated = activityDao.updateActivity(activity);

        assertNotNull(updated);
        assertEquals(new BigDecimal("70.00"), updated.getTarif());
        assertEquals("Activité de relaxation", updated.getDescription());
    }

    @Test
    @Order(5)
    public void testDeleteActivityById() {
        assertFalse(HibernateUtil.getSessionFactory().isClosed());

        Activity activity = new Activity("Zumba", new BigDecimal("75.00"), 12, "Activité cardio");
        activityDao.createActivity(activity);

        boolean deleted = activityDao.deleteActivityById(activity.getActivityId());
        assertTrue(deleted);

        Activity found = activityDao.findActivityById(activity.getActivityId());
        assertNull(found);
    }

    @Test
    @Order(6)
    public void testFindAllActivities() {
        assertFalse(HibernateUtil.getSessionFactory().isClosed());

        activityDao.createActivity(new Activity("Boxe", new BigDecimal("100.00"), 10, "Combat"));
        activityDao.createActivity(new Activity("Danse", new BigDecimal("65.00"), 25, "Mouvement rythmique"));

        List<Activity> all = activityDao.findAllActivities();
        assertEquals(2, all.size());
    }
}
