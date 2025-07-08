package com.clubsportif.ui.adherent;

import com.clubsportif.model.User;
import com.clubsportif.model.Inscription;
import com.clubsportif.model.Paiement;
import com.clubsportif.service.InscriptionService;
import com.clubsportif.service.PaiementService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class ProfilPanel extends JPanel {
    private User user;

    public ProfilPanel(User user) {
        this.user = user;
        setLayout(new GridBagLayout());
        setBackground(new Color(245, 245, 250));

        // Panel principal stylisé
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 149, 237), 2, true),
                BorderFactory.createEmptyBorder(24, 32, 24, 32)
        ));

        // Titre
        JLabel title = new JLabel("Informations personnelles");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(100, 149, 237));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(title);
        card.add(Box.createVerticalStrut(18));

        // Infos personnelles
        addInfo(card, "Nom :", user.getLastname());
        addInfo(card, "Prénom :", user.getName());
        addInfo(card, "Nom d'utilisateur :", user.getUsername());
        addInfo(card, "Email :", user.getEmail());
        card.add(Box.createVerticalStrut(18));

        // Nombre total d'inscriptions
        InscriptionService inscriptionService = new InscriptionService();
        List<Inscription> inscriptions = inscriptionService.getAllInscriptionsByUser(user);
        addInfo(card, "Nombre total d'inscriptions :", String.valueOf(inscriptions.size()));
        card.add(Box.createVerticalStrut(18));

        // État du paiement (paiement dont la date de fin > aujourd'hui)
        PaiementService paiementService = new PaiementService();
        List<Paiement> paiements = paiementService.getPaiementsByUser(user);
        LocalDate today = LocalDate.now();
        String etatPaiement = "Aucun paiement actif";
        Color etatColor = new Color(220, 53, 69);

        for (Paiement paiement : paiements) {
            if (paiement.getPeriodeFin() != null && paiement.getPeriodeFin().isAfter(today)) {
                if (paiement.getStatut() == Paiement.StatutPaiement.paye) {
                    etatPaiement = "À jour";
                    etatColor = new Color(40, 167, 69);
                } else if (paiement.getStatut() == Paiement.StatutPaiement.partiel) {
                    etatPaiement = "Paiement partiel";
                    etatColor = new Color(255, 193, 7);
                } else {
                    etatPaiement = "Impayé";
                    etatColor = new Color(220, 53, 69);
                }
                break;
            }
        }

        JLabel etatLabel = new JLabel("État du paiement : " + etatPaiement);
        etatLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        etatLabel.setForeground(etatColor);
        etatLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(etatLabel);

        // Centrage du panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        add(card, gbc);
    }

    private void addInfo(JPanel panel, String label, String value) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 2));
        row.setOpaque(false);
        JLabel l = new JLabel(label);
        l.setFont(new Font("Segoe UI", Font.BOLD, 15));
        l.setForeground(new Color(60, 63, 65));
        JLabel v = new JLabel(value != null ? value : "");
        v.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        v.setForeground(new Color(80, 80, 80));
        row.add(l);
        row.add(v);
        panel.add(row);
    }
}
