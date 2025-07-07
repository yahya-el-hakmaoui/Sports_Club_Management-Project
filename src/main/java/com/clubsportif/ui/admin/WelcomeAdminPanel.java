package com.clubsportif.ui.admin;

import com.clubsportif.service.UserService;
import com.clubsportif.service.ActivityService;
import com.clubsportif.model.User;

import javax.swing.*;
import java.awt.*;

public class WelcomeAdminPanel extends JPanel {
    public WelcomeAdminPanel() {
        setLayout(new BorderLayout());

        JLabel label = new JLabel("Bienvenue dans l'espace d'administration", SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 22));
        add(label, BorderLayout.NORTH);

        // Récupérer les totaux
        UserService userService = new UserService();
        ActivityService activityService = new ActivityService();

        long adherentCount = userService.getAllUsers().stream()
                .filter(u -> u.getRole() == User.Role.adherent && !u.isArchived())
                .count();
        int activityCount = activityService.getAllActivities().size();

        // Box panel
        JPanel boxPanel = new JPanel(new GridLayout(1, 2, 40, 0));
        boxPanel.setBorder(BorderFactory.createEmptyBorder(40, 80, 40, 80));
        boxPanel.setOpaque(false);

        // Box 1 : Nombre d'adhérents
        JPanel adherentBox = new JPanel();
        adherentBox.setBackground(new Color(100, 149, 237));
        adherentBox.setBorder(BorderFactory.createLineBorder(new Color(60, 63, 65), 2, true));
        adherentBox.setLayout(new BorderLayout());
        JLabel adherentLabel = new JLabel(String.valueOf(adherentCount), SwingConstants.CENTER);
        adherentLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        adherentLabel.setForeground(Color.WHITE);
        JLabel adherentText = new JLabel("Adhérents", SwingConstants.CENTER);
        adherentText.setFont(new Font("Segoe UI", Font.BOLD, 22));
        adherentText.setForeground(Color.WHITE);
        adherentBox.add(adherentLabel, BorderLayout.CENTER);
        adherentBox.add(adherentText, BorderLayout.SOUTH);

        // Box 2 : Nombre d'activités
        JPanel activityBox = new JPanel();
        activityBox.setBackground(new Color(60, 179, 113));
        activityBox.setBorder(BorderFactory.createLineBorder(new Color(60, 63, 65), 2, true));
        activityBox.setLayout(new BorderLayout());
        JLabel activityLabel = new JLabel(String.valueOf(activityCount), SwingConstants.CENTER);
        activityLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        activityLabel.setForeground(Color.WHITE);
        JLabel activityText = new JLabel("Activités", SwingConstants.CENTER);
        activityText.setFont(new Font("Segoe UI", Font.BOLD, 22));
        activityText.setForeground(Color.WHITE);
        activityBox.add(activityLabel, BorderLayout.CENTER);
        activityBox.add(activityText, BorderLayout.SOUTH);

        boxPanel.add(adherentBox);
        boxPanel.add(activityBox);

        add(boxPanel, BorderLayout.CENTER);
    }
}
