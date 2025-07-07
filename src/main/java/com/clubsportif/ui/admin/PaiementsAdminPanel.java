package com.clubsportif.ui.admin;

import javax.swing.*;
import java.awt.*;

public class PaiementsAdminPanel extends JPanel {
    public PaiementsAdminPanel() {
        setLayout(new BorderLayout());
        JLabel label = new JLabel("Gestion des paiements", SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 22));
        add(label, BorderLayout.CENTER);
    }
}
