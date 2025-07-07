package com.clubsportif.ui.admin;

import com.clubsportif.model.ParametresPaiement;
import com.clubsportif.service.ParametresPaiementService;
import com.clubsportif.service.AuthService;
import com.clubsportif.model.User;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

public class ParametresPaiementAdminPanel extends JPanel {
    private final ParametresPaiementService service = new ParametresPaiementService();
    private ParametresPaiement parametres;

    private JTextField annuelField;
    private JTextField mensuelField;
    private JButton saveButton;

    public ParametresPaiementAdminPanel() {
        setLayout(new BorderLayout());
        JLabel title = new JLabel("Paramètres de paiement", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Récupérer ou créer le paramètre id=1
        parametres = service.getParametresPaiementById(1);
        if (parametres == null) {
            parametres = new ParametresPaiement(BigDecimal.ZERO, BigDecimal.ZERO);
            service.createParametresPaiement(parametres);
            parametres = service.getAllParametresPaiement().get(0); // recharge pour avoir l'ID généré
        }

        // Champ Frais Annuel
        gbc.gridx = 0; gbc.gridy = 0;
        centerPanel.add(new JLabel("Frais base annuel (DH):"), gbc);
        annuelField = new JTextField(parametres.getFraisBaseAnnuel().toPlainString(), 12);
        gbc.gridx = 1;
        centerPanel.add(annuelField, gbc);

        // Champ Frais Mensuel
        gbc.gridx = 0; gbc.gridy = 1;
        centerPanel.add(new JLabel("Frais base mensuel (DH):"), gbc);
        mensuelField = new JTextField(parametres.getFraisBaseMensuel().toPlainString(), 12);
        gbc.gridx = 1;
        centerPanel.add(mensuelField, gbc);

        // Bouton sauvegarder
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        saveButton = new JButton("Enregistrer");
        saveButton.setBackground(new Color(40, 167, 69));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        saveButton.setFocusPainted(false);
        centerPanel.add(saveButton, gbc);

        saveButton.addActionListener(e -> {
            try {
                BigDecimal annuel = new BigDecimal(annuelField.getText().trim());
                BigDecimal mensuel = new BigDecimal(mensuelField.getText().trim());

                // Demande d'authentification avant modification
                JPanel authPanel = new JPanel(new GridLayout(2, 2, 8, 8));
                JTextField usernameField = new JTextField();
                JPasswordField passwordField = new JPasswordField();
                authPanel.add(new JLabel("Nom d'utilisateur:"));
                authPanel.add(usernameField);
                authPanel.add(new JLabel("Mot de passe:"));
                authPanel.add(passwordField);

                int result = JOptionPane.showConfirmDialog(
                        this,
                        authPanel,
                        "Authentification requise",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

                if (result == JOptionPane.OK_OPTION) {
                    String username = usernameField.getText().trim();
                    String password = new String(passwordField.getPassword());
                    AuthService authService = new AuthService();
                    User user = authService.authenticate(username, password);

                    if (user == null || (!user.getRole().toString().equalsIgnoreCase("admin") && !user.getRole().toString().equalsIgnoreCase("assistant"))) {
                        JOptionPane.showMessageDialog(this, "Authentification échouée ou droits insuffisants.", "Erreur", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    parametres.setFraisBaseAnnuel(annuel);
                    parametres.setFraisBaseMensuel(mensuel);

                    service.updateParametresPaiement(parametres);

                    JOptionPane.showMessageDialog(this, "Paramètres enregistrés avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(centerPanel, BorderLayout.CENTER);
    }
}
