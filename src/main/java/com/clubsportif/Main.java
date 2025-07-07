package com.clubsportif;

// model,dao, service, ui imports
import com.clubsportif.model.*;
import com.clubsportif.service.AuthService;
import com.clubsportif.service.UserService;
import com.clubsportif.ui.*;

// swing imports
import javax.swing.SwingUtilities;

// util imports
import com.clubsportif.util.PasswordUtils;
import com.clubsportif.util.HibernateUtil;

// types imports
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        HibernateUtil.getSessionFactory();


        javax.swing.SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });

    }
}
