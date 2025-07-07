package com.clubsportif.ui;

import com.clubsportif.model.User;
import com.clubsportif.service.AuthService;
import com.clubsportif.ui.admin.AdminPanel;
import com.clubsportif.ui.adherent.AdherentPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.RoundRectangle2D;

public class LoginPanel extends JPanel {

    private MainFrame mainFrame;
    private AuthService authService;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel messageLabel;
    private JCheckBox showPasswordCheckBox; // Ajout de la case à cocher

    public LoginPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.authService = new AuthService();
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        setBackground(new Color(245, 245, 250));

        // Panel pour logo + titre club (en dehors du carré login)
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0));
        headerPanel.setOpaque(false);

        // Logo musculation (plus beau, haltère stylisé)
        JLabel logoLabel = new JLabel(new Icon() {
            public int getIconWidth() { return 60; }
            public int getIconHeight() { return 60; }
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Barre centrale
                g2.setColor(new Color(80, 80, 80));
                g2.fillRoundRect(x + 18, y + 27, 24, 6, 6, 6);

                // Poids gauche (extérieur)
                g2.setColor(new Color(100, 149, 237));
                g2.fillRoundRect(x + 6, y + 20, 8, 20, 8, 8);
                // Poids gauche (intérieur)
                g2.setColor(new Color(60, 63, 65));
                g2.fillRoundRect(x + 12, y + 23, 6, 14, 6, 6);

                // Poids droite (extérieur)
                g2.setColor(new Color(100, 149, 237));
                g2.fillRoundRect(x + 46, y + 20, 8, 20, 8, 8);
                // Poids droite (intérieur)
                g2.setColor(new Color(60, 63, 65));
                g2.fillRoundRect(x + 42, y + 23, 6, 14, 6, 6);

                // Reflet sur les poids
                g2.setColor(new Color(180, 220, 255, 120));
                g2.fillRoundRect(x + 7, y + 22, 3, 8, 3, 3);
                g2.fillRoundRect(x + 54, y + 22, 3, 8, 3, 3);

                g2.dispose();
            }
        });
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel clubLabel = new JLabel("Club Sportif");
        clubLabel.setFont(new Font("Arial", Font.BOLD, 28));
        clubLabel.setForeground(new Color(60, 63, 65));
        clubLabel.setHorizontalAlignment(SwingConstants.CENTER);

        headerPanel.add(logoLabel);
        headerPanel.add(clubLabel);

        // Panel du formulaire de connexion (carré)
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new LineBorder(new Color(100, 149, 237), 2, true));
        formPanel.setPreferredSize(new Dimension(400, 320));
        formPanel.setOpaque(true);

        JLabel titleLabel = new JLabel("Connexion");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(new Color(100, 149, 237));

        JLabel usernameLabel = new JLabel("Nom d'utilisateur:");
        JLabel passwordLabel = new JLabel("Mot de passe:");

        usernameField = new JTextField(20);
        usernameField.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
        usernameField.setBackground(new Color(245, 248, 255));

        passwordField = new JPasswordField(20);
        passwordField.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
        passwordField.setBackground(new Color(245, 248, 255));

        // Permettre la connexion avec la touche Entrée
        passwordField.addActionListener(this::handleLogin);
        usernameField.addActionListener(this::handleLogin);

        // Ajout de la case à cocher pour afficher/masquer le mot de passe
        showPasswordCheckBox = new JCheckBox("Afficher le mot de passe");
        showPasswordCheckBox.setBackground(Color.WHITE);
        showPasswordCheckBox.setFont(new Font("Arial", Font.PLAIN, 12));
        showPasswordCheckBox.addActionListener(e -> {
            if (showPasswordCheckBox.isSelected()) {
                passwordField.setEchoChar((char)0);
            } else {
                passwordField.setEchoChar('\u2022');
            }
        });

        // Initialiser l'affichage du mot de passe masqué
        passwordField.setEchoChar('\u2022');

        loginButton = new JButton("Se connecter");
        loginButton.setBackground(new Color(100, 149, 237));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setBorder(new LineBorder(new Color(100, 149, 237), 1, true));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        messageLabel = new JLabel();
        messageLabel.setForeground(new Color(220, 20, 60));
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 13));

        loginButton.addActionListener(this::handleLogin);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        formPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        // Ajout de la case à cocher sous le champ mot de passe
        gbc.gridx = 1; gbc.gridy++;
        formPanel.add(showPasswordCheckBox, gbc);

        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        formPanel.add(loginButton, gbc);

        gbc.gridy++;
        formPanel.add(messageLabel, gbc);

        // Placement global : headerPanel au-dessus, formPanel centré
        setLayout(new GridBagLayout());
        GridBagConstraints rootGbc = new GridBagConstraints();
        rootGbc.gridx = 0; rootGbc.gridy = 0; rootGbc.insets = new Insets(0,0,30,0);
        rootGbc.anchor = GridBagConstraints.CENTER;
        add(headerPanel, rootGbc);

        rootGbc.gridy = 1; rootGbc.insets = new Insets(0,0,0,0);
        add(formPanel, rootGbc);
    }

    private void handleLogin(ActionEvent e) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Veuillez saisir vos identifiants.");
            return;
        }

        User user = authService.authenticate(username, password);
        if (user == null) {
            messageLabel.setText("Identifiants incorrects.");
            return;
        }

        // Bloquer la connexion si l'adhérent ou l'assistant est archivé
        if ((user.getRole() == User.Role.adherent || user.getRole() == User.Role.assistant) && user.isArchived()) {
            messageLabel.setText("Votre compte a été archivé. Veuillez contacter l'administration.");
            return;
        }

        messageLabel.setText("");

        switch (user.getRole()) {
            case admin:
            case assistant:
                mainFrame.showPanel("adminPanel", new AdminPanel(user));
                break;
            case adherent:
                mainFrame.showPanel("adherentPanel", new AdherentPanel(user));
                break;
            default:
                messageLabel.setText("Rôle inconnu.");
        }
    }
}
