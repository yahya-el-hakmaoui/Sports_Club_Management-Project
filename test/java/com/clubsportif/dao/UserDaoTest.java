package com.clubsportif.dao;

import com.clubsportif.model.User;
import com.clubsportif.model.User.Role;
import com.clubsportif.util.HibernateUtil;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserDaoTest {

    private UserDao userDao;

    @BeforeAll
    public void setup() {
        userDao = new UserDao();
    }

    @AfterEach
    public void cleanup() {
        // Optionally clear out users created during tests if needed
        // HibernateUtil.getSessionFactory().getCurrentSession().clear(); // Optional if needed
    }

    // Remove @AfterAll that was closing the factory early

    @Test
    public void testCreateFindDeleteUser() {
        assertTrue(HibernateUtil.getSessionFactory().isOpen(), "SessionFactory should be open");

        User user = new User();
        user.setUsername("testuser");
        user.setPasswordHash("hashedpwd");
        user.setRole(Role.adherent);
        user.setNom("Dupont");
        user.setPrenom("Jean");
        user.setEmail("jean.dupont@example.com");
        user.setDateInscription(LocalDate.now());
        user.setArchived(false);

        userDao.createUser(user);
        assertTrue(user.getUserId() > 0, "User ID should be generated");

        User found = userDao.findById(user.getUserId());
        assertNotNull(found);
        assertEquals("testuser", found.getUsername());

        userDao.deleteUser(found);
        assertNull(userDao.findById(user.getUserId()));
    }

    @Test
    public void testUpdateUser() {
        assertTrue(HibernateUtil.getSessionFactory().isOpen(), "SessionFactory should be open");

        User user = new User();
        user.setUsername("updateuser");
        user.setPasswordHash("pwd");
        user.setRole(Role.adherent);
        user.setNom("Martin");
        user.setPrenom("Paul");
        user.setDateInscription(LocalDate.now());
        user.setArchived(false);

        userDao.createUser(user);
        assertTrue(user.getUserId() > 0);

        user.setNom("MartinUpdated");
        userDao.updateUser(user);

        User updated = userDao.findById(user.getUserId());
        assertEquals("MartinUpdated", updated.getNom());

        userDao.deleteUser(updated);
    }

    @Test
    public void testFindAllAndArchivedNotArchived() {
        assertTrue(HibernateUtil.getSessionFactory().isOpen(), "SessionFactory should be open");

        User user1 = new User();
        user1.setUsername("user1");
        user1.setPasswordHash("pwd1");
        user1.setRole(Role.adherent);
        user1.setNom("UserOne");
        user1.setPrenom("PrenomOne");
        user1.setDateInscription(LocalDate.now());
        user1.setArchived(false);

        User user2 = new User();
        user2.setUsername("user2");
        user2.setPasswordHash("pwd2");
        user2.setRole(Role.adherent);
        user2.setNom("UserTwo");
        user2.setPrenom("PrenomTwo");
        user2.setDateInscription(LocalDate.now());
        user2.setArchived(true);

        userDao.createUser(user1);
        userDao.createUser(user2);

        List<User> allUsers = userDao.findAll();
        assertTrue(allUsers.size() >= 2);

        List<User> archivedUsers = userDao.findArchived();
        assertTrue(archivedUsers.stream().anyMatch(u -> u.getUserId() == user2.getUserId()));

        List<User> notArchivedUsers = userDao.findNotArchived();
        assertTrue(notArchivedUsers.stream().anyMatch(u -> u.getUserId() == user1.getUserId()));

        userDao.deleteUser(user1);
        userDao.deleteUser(user2);
    }

    @Test
    public void testFindByNameAndLastname() {
        assertTrue(HibernateUtil.getSessionFactory().isOpen(), "SessionFactory should be open");

        User user = new User();
        user.setUsername("searchuser");
        user.setPasswordHash("pwd");
        user.setRole(Role.adherent);
        user.setNom("Lemoine");
        user.setPrenom("Sylvie");
        user.setDateInscription(LocalDate.now());
        user.setArchived(false);

        userDao.createUser(user);
        assertTrue(user.getUserId() > 0);

        List<User> byName = userDao.findByName("lemo");
        assertTrue(byName.stream().anyMatch(u -> u.getUserId() == user.getUserId()));

        List<User> byLastname = userDao.findByLastname("sylv");
        assertTrue(byLastname.stream().anyMatch(u -> u.getUserId() == user.getUserId()));

        userDao.deleteUser(user);
    }
}
