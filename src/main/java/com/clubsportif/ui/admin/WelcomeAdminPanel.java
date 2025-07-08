package com.clubsportif.ui.admin;

import com.clubsportif.service.UserService;
import com.clubsportif.service.ActivityService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class WelcomeAdminPanel extends JPanel {
    private JLabel adherentCountLabel;
    private JLabel activityCountLabel;
    private final UserService userService = new UserService();
    private final ActivityService activityService = new ActivityService();

    public WelcomeAdminPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 250));

        JLabel titleLabel = new JLabel("Bienvenue dans l'espace d'administration", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(24, 0, 24, 0));
        add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);

        JPanel boxPanel = new JPanel(new GridLayout(1, 2, 40, 0));
        boxPanel.setOpaque(false);

        // Box Adhérents
        JPanel adherentBox = new JPanel();
        adherentBox.setBackground(Color.WHITE);
        adherentBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 149, 237), 2, true),
                BorderFactory.createEmptyBorder(24, 32, 24, 32)
        ));
        adherentBox.setLayout(new BoxLayout(adherentBox, BoxLayout.Y_AXIS));
        JLabel adherentTitle = new JLabel("Adhérents");
        adherentTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        adherentTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        adherentCountLabel = new JLabel();
        adherentCountLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        adherentCountLabel.setForeground(new Color(100, 149, 237));
        adherentCountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        adherentBox.add(adherentTitle);
        adherentBox.add(Box.createVerticalStrut(12));
        adherentBox.add(adherentCountLabel);

        // Box Activités
        JPanel activityBox = new JPanel();
        activityBox.setBackground(Color.WHITE);
        activityBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 179, 113), 2, true),
                BorderFactory.createEmptyBorder(24, 32, 24, 32)
        ));
        activityBox.setLayout(new BoxLayout(activityBox, BoxLayout.Y_AXIS));
        JLabel activityTitle = new JLabel("Activités");
        activityTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        activityTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        activityCountLabel = new JLabel();
        activityCountLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        activityCountLabel.setForeground(new Color(60, 179, 113));
        activityCountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        activityBox.add(activityTitle);
        activityBox.add(Box.createVerticalStrut(12));
        activityBox.add(activityCountLabel);

        boxPanel.add(adherentBox);
        boxPanel.add(activityBox);

        centerPanel.add(boxPanel);

        add(centerPanel, BorderLayout.CENTER);

        // Bouton Rafraîchir
        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        JButton refreshButton = new JButton("Rafraîchir");
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        refreshButton.setBackground(new Color(100, 149, 237));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshButton.setBorder(BorderFactory.createEmptyBorder(8, 24, 8, 24));
        refreshButton.addActionListener(this::refreshCounts);
        bottomPanel.add(refreshButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // Initialisation des compteurs
        refreshCounts(null);
    }

    private void refreshCounts(ActionEvent e) {
        // Compte les utilisateurs non archivés dont le rôle est "adherent"
        long adherentCount = userService.getNotArchivedUsers().stream()
                .filter(u -> u.getRole() == com.clubsportif.model.User.Role.adherent)
                .count();
        adherentCountLabel.setText(String.valueOf(adherentCount));

        // Compte toutes les activités
        int activityCount = activityService.getAllActivities().size();
        activityCountLabel.setText(String.valueOf(activityCount));
    }
}
