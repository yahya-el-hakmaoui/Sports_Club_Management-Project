package com.clubsportif.ui.admin;

import javax.swing.*;
import java.awt.*;

public class WelcomeAdminPanel extends JPanel {
    public WelcomeAdminPanel() {
        setLayout(new BorderLayout());
        JLabel label = new JLabel("Bienvenue dans l'espace d'administration", SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 22));
        add(label, BorderLayout.CENTER);
    }
}
