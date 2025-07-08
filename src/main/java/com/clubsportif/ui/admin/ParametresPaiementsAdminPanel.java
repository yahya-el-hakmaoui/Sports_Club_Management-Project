package com.clubsportif.ui.admin;

import com.clubsportif.model.ParametresPaiement;
import com.clubsportif.service.ParametresPaiementService;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

public class ParametresPaiementsAdminPanel extends JPanel {
    private final ParametresPaiementService service = new ParametresPaiementService();
    private JTextField fraisAnnuelField;
    private JTextField fraisMensuelField;
    private ParametresPaiement parametresPaiement;
    private JLabel messageLabel;

    public ParametresPaiementsAdminPanel() {
        setLayout(new BorderLayout());
        JLabel titleLabel = new JLabel("Paramètres de paiement", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(24, 0, 24, 0));
        add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 149, 237), 1, true),
                BorderFactory.createEmptyBorder(32, 32, 32, 32)
        ));

        fraisAnnuelField = new JTextField(12);
        fraisMensuelField = new JTextField(12);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Frais annuel de base (DH) :"), gbc);
        gbc.gridx = 1;
        formPanel.add(fraisAnnuelField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Frais mensuel de base (DH) :"), gbc);
        gbc.gridx = 1;
        formPanel.add(fraisMensuelField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton saveButton = new JButton("Enregistrer");
        saveButton.setBackground(new Color(100, 149, 237));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveButton.addActionListener(e -> saveParametres());
        formPanel.add(saveButton, gbc);

        gbc.gridy = 3;
        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        messageLabel.setForeground(new Color(34, 139, 34));
        formPanel.add(messageLabel, gbc);

        add(formPanel, BorderLayout.CENTER);

        loadParametres();
    }

    private void loadParametres() {
        java.util.List<ParametresPaiement> all = service.getAllParametresPaiement();
        if (!all.isEmpty()) {
            parametresPaiement = all.get(0);
            fraisAnnuelField.setText(parametresPaiement.getFraisBaseAnnuel().toString());
            fraisMensuelField.setText(parametresPaiement.getFraisBaseMensuel().toString());
        } else {
            parametresPaiement = null;
            fraisAnnuelField.setText("");
            fraisMensuelField.setText("");
        }
        messageLabel.setText("");
    }

    private void saveParametres() {
        try {
            BigDecimal annuel = new BigDecimal(fraisAnnuelField.getText().replace(',', '.'));
            BigDecimal mensuel = new BigDecimal(fraisMensuelField.getText().replace(',', '.'));
            if (parametresPaiement == null) {
                parametresPaiement = new ParametresPaiement(annuel, mensuel);
                service.createParametresPaiement(parametresPaiement);
            } else {
                parametresPaiement.setFraisBaseAnnuel(annuel);
                parametresPaiement.setFraisBaseMensuel(mensuel);
                service.updateParametresPaiement(parametresPaiement);
            }
            messageLabel.setForeground(new Color(34, 139, 34));
            messageLabel.setText("Paramètres enregistrés avec succès.");
        } catch (Exception ex) {
            messageLabel.setForeground(new Color(220, 20, 60));
            messageLabel.setText("Erreur lors de l'enregistrement. Vérifiez les valeurs.");
        }
    }
}
