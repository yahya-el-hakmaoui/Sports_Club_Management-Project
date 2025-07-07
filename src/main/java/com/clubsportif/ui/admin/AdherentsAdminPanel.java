package com.clubsportif.ui.admin;

import javax.swing.*;
import java.awt.*;

public class AdherentsAdminPanel extends JPanel {
    public AdherentsAdminPanel() {
        setLayout(new BorderLayout());
        JLabel label = new JLabel("Gestion des adhérents", SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 22));
        add(label, BorderLayout.CENTER);
    }
}
