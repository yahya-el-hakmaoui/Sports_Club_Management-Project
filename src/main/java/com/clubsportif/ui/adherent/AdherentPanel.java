package com.clubsportif.ui.adherent;

import com.clubsportif.model.User;
import com.clubsportif.ui.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AdherentPanel extends JPanel {
    private User user;

    public AdherentPanel(User user) {
        this.user = user;
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 250));

        // En-tête stylé avec logo user et nom complet
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 18));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 0, 0, 0),
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220))
        ));
        headerPanel.setPreferredSize(new Dimension(100, 70));

        // Icône utilisateur simple (cercle bleu avec tête blanche)
        JLabel iconLabel = new JLabel();
        iconLabel.setPreferredSize(new Dimension(48, 48));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setVerticalAlignment(SwingConstants.CENTER);

        Icon userIcon = new Icon() {
            public int getIconWidth() { return 48; }
            public int getIconHeight() { return 48; }
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(100, 149, 237));
                g2.fillOval(x, y, 48, 48);
                g2.setColor(Color.WHITE);
                g2.fillOval(x + 12, y + 10, 24, 24); // tête
                g2.setColor(new Color(230, 240, 255));
                g2.fillOval(x + 16, y + 28, 16, 10); // épaules
                g2.dispose();
            }
        };
        iconLabel.setIcon(userIcon);

        // Texte de bienvenue avec nom complet
        String nomComplet = (user != null ? user.getLastname() + " " + user.getName() : "");
        JLabel nameLabel = new JLabel("Bonjour, " + nomComplet);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        nameLabel.setForeground(new Color(60, 63, 65));

        headerPanel.add(iconLabel);
        headerPanel.add(nameLabel);

        add(headerPanel, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT);

        // Style général du tabbedPane
        tabbedPane.setBackground(new Color(245, 245, 250));
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 15));
        tabbedPane.setForeground(new Color(60, 63, 65));
        tabbedPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        // Couleur de sélection personnalisée (Java 9+)
        UIManager.put("TabbedPane.selected", new Color(100, 149, 237));
        UIManager.put("TabbedPane.contentAreaColor", Color.WHITE);

        // Ajout des onglets avec icônes
        tabbedPane.addTab("Profil", UIManager.getIcon("FileView.fileIcon"), new ProfilPanel(), "Voir le profil");
        tabbedPane.addTab("Activités", UIManager.getIcon("FileView.directoryIcon"), new ActivityPanel(user), "Voir les activités");
        tabbedPane.addTab("Paiement", UIManager.getIcon("FileView.hardDriveIcon"), new PaiementPanel(), "Gérer les paiements");

        // Largeur personnalisée des onglets (gauche)
        tabbedPane.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI() {
            @Override
            protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics) {
                return 160; // Largeur augmentée
            }
            @Override
            protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight) {
                return 48;
            }
        });

        add(tabbedPane, BorderLayout.CENTER);

        // --- Bouton Déconnexion en bas à gauche, non collé au coin ---
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(245, 245, 250));
        // Ajoute une marge autour du bouton pour qu'il ne soit pas collé au coin
        JPanel logoutWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        logoutWrapper.setBackground(new Color(245, 245, 250));
        logoutWrapper.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 0)); // marge à gauche et en bas

        JButton logoutButton = new JButton("Se déconnecter");
        logoutButton.setBackground(new Color(220, 53, 69));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutButton.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.addActionListener((ActionEvent e) -> {
            // On cherche la JFrame parente pour appeler showPanel
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window instanceof MainFrame) {
                ((MainFrame) window).showPanel(MainFrame.LOGIN_PANEL, new com.clubsportif.ui.LoginPanel((MainFrame) window));
            }
        });

        logoutWrapper.add(logoutButton);
        bottomPanel.add(logoutWrapper, BorderLayout.WEST);
        add(bottomPanel, BorderLayout.SOUTH);
    }
}
