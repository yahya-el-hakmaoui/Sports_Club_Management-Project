package com.clubsportif.ui.admin;

import com.clubsportif.model.Activity;
import com.clubsportif.model.Inscription;
import com.clubsportif.model.Paiement;
import com.clubsportif.model.User;
import com.clubsportif.model.ParametresPaiement;
import com.clubsportif.service.ActivityService;
import com.clubsportif.service.InscriptionService;
import com.clubsportif.service.PaiementService;
import com.clubsportif.service.ParametresPaiementService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PaiementsAdherentAdminPanel extends JPanel {
    private JTextField searchField;
    private DefaultTableModel tableModel;
    private JTable table;
    private PaiementService paiementService = new PaiementService();
    private User user;
    private String currentSearch = null;

    public PaiementsAdherentAdminPanel(User user) {
        this.user = user;
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 245, 250));

        // Top panel: Add button + search bar + search button
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JButton addButton = new JButton("Ajouter");
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        addButton.setPreferredSize(new Dimension(100, 40));
        addButton.setBackground(new Color(40, 167, 69));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setToolTipText("Ajouter un paiement");

        // Action à compléter selon la logique métier
        addButton.addActionListener(e -> {
            Window parentWindow = SwingUtilities.getWindowAncestor(this);
            Frame parentFrame = parentWindow instanceof Frame ? (Frame) parentWindow : null;
            JDialog addDialog = new JDialog(parentFrame, "Ajouter un paiement", true);
            addDialog.setLayout(new BorderLayout());

            JPanel formPanel = new JPanel(new GridBagLayout());
            formPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(8, 8, 8, 8);
            gbc.anchor = GridBagConstraints.WEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // Durée
            gbc.gridx = 0; gbc.gridy = 0;
            formPanel.add(new JLabel("Durée :"), gbc);
            String[] durees = {"1 mois", "3 mois", "6 mois", "1 an", "2 ans"};
            JComboBox<String> dureeCombo = new JComboBox<>(durees);
            gbc.gridx = 1;
            formPanel.add(dureeCombo, gbc);

            // Montant (affiché, non éditable)
            gbc.gridx = 0; gbc.gridy++;
            formPanel.add(new JLabel("Montant total :"), gbc);
            JTextField montantField = new JTextField();
            montantField.setEditable(false);
            montantField.setBackground(Color.LIGHT_GRAY);
            gbc.gridx = 1;
            formPanel.add(montantField, gbc);

            // Statut
            gbc.gridx = 0; gbc.gridy++;
            formPanel.add(new JLabel("Statut :"), gbc);
            String[] statuts = {"paye", "impaye", "partiel"};
            JComboBox<String> statutCombo = new JComboBox<>(statuts);
            gbc.gridx = 1;
            formPanel.add(statutCombo, gbc);

            // Montant partiel
            gbc.gridx = 0; gbc.gridy++;
            formPanel.add(new JLabel("Montant partiel :"), gbc);
            JTextField montantPartielField = new JTextField();
            gbc.gridx = 1;
            formPanel.add(montantPartielField, gbc);

            // Période début (affiché, non éditable)
            gbc.gridx = 0; gbc.gridy++;
            formPanel.add(new JLabel("Période début :"), gbc);
            JTextField periodeDebutField = new JTextField(LocalDate.now().toString());
            periodeDebutField.setEditable(false);
            periodeDebutField.setBackground(Color.LIGHT_GRAY);
            gbc.gridx = 1;
            formPanel.add(periodeDebutField, gbc);

            // Période fin (affiché, non éditable)
            gbc.gridx = 0; gbc.gridy++;
            formPanel.add(new JLabel("Période fin :"), gbc);
            JTextField periodeFinField = new JTextField();
            periodeFinField.setEditable(false);
            periodeFinField.setBackground(Color.LIGHT_GRAY);
            gbc.gridx = 1;
            formPanel.add(periodeFinField, gbc);

            // Récupération des frais de base avec id = 1
            ParametresPaiementService paramService = new ParametresPaiementService();
            ParametresPaiement parametres = paramService.getParametresPaiementById(1);

            // Inscription et activité de l'utilisateur
            InscriptionService inscriptionService = new InscriptionService();
            ActivityService activityService = new ActivityService();
            List<Inscription> inscriptions = inscriptionService.getActiveInscriptionsByUser(user);
            List<Activity> activities = new ArrayList<>();
            for (Inscription insc : inscriptions) {
                if (insc.isActive() && insc.getActivity() != null) {
                    // Correction LazyInitializationException : recharger l'activité par son id
                    Activity act = activityService.getActivityById(insc.getActivity().getActivityId());
                    if (act != null) {
                        activities.add(act);
                    }
                }
            }

            Runnable updateMontantEtFin = () -> {
                String dureeStr = (String) dureeCombo.getSelectedItem();
                int mois = 1;
                boolean annuel = false;
                if ("3 mois".equals(dureeStr)) mois = 3;
                else if ("6 mois".equals(dureeStr)) mois = 6;
                else if ("1 an".equals(dureeStr)) { mois = 12; annuel = true; }
                else if ("2 ans".equals(dureeStr)) { mois = 24; annuel = true; }
                LocalDate debut = LocalDate.now();
                LocalDate fin = debut.plusMonths(mois).minusDays(1);
                periodeFinField.setText(fin.toString());

                java.math.BigDecimal total = java.math.BigDecimal.ZERO;
                for (Activity act : activities) {
                    if (act.getTarif() != null) {
                        total = total.add(act.getTarif().multiply(java.math.BigDecimal.valueOf(mois)));
                    }
                }
                // Ajout des frais de base selon la durée
                if (parametres != null) {
                    if (annuel) {
                        total = total.add(parametres.getFraisBaseAnnuel());
                    } else {
                        total = total.add(parametres.getFraisBaseMensuel().multiply(java.math.BigDecimal.valueOf(mois)));
                    }
                }
                montantField.setText(total + " DH");
            };

            dureeCombo.addActionListener(ev -> updateMontantEtFin.run());
            updateMontantEtFin.run();

            // Bouton confirmer
            gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            JButton confirmBtn = new JButton("Ajouter");
            confirmBtn.setBackground(new Color(40, 167, 69));
            confirmBtn.setForeground(Color.WHITE);
            confirmBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
            confirmBtn.setFocusPainted(false);
            formPanel.add(confirmBtn, gbc);

            confirmBtn.addActionListener(ev -> {
                String dureeStr = (String) dureeCombo.getSelectedItem();
                int mois = 1;
                if ("3 mois".equals(dureeStr)) mois = 3;
                else if ("6 mois".equals(dureeStr)) mois = 6;
                else if ("1 an".equals(dureeStr)) mois = 12;
                else if ("2 ans".equals(dureeStr)) mois = 24;
                LocalDate debut = LocalDate.now();
                LocalDate fin = debut.plusMonths(mois).minusDays(1);

                java.math.BigDecimal montant = java.math.BigDecimal.ZERO;
                for (Activity act : activities) {
                    if (act.getTarif() != null) {
                        montant = montant.add(act.getTarif().multiply(java.math.BigDecimal.valueOf(mois)));
                    }
                }

                String statutStr = (String) statutCombo.getSelectedItem();
                com.clubsportif.model.Paiement.StatutPaiement statut = com.clubsportif.model.Paiement.StatutPaiement.valueOf(statutStr);

                java.math.BigDecimal montantPartiel = null;
                if (!montantPartielField.getText().trim().isEmpty()) {
                    try {
                        montantPartiel = new java.math.BigDecimal(montantPartielField.getText().trim());
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(addDialog, "Montant partiel invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                LocalDate datePaiement = null;
                if (statut == com.clubsportif.model.Paiement.StatutPaiement.paye || statut == com.clubsportif.model.Paiement.StatutPaiement.partiel) {
                    datePaiement = LocalDate.now();
                }

                com.clubsportif.model.Paiement paiement = new com.clubsportif.model.Paiement(
                    user,
                    debut,
                    fin,
                    montant,
                    statut,
                    montantPartiel,
                    datePaiement
                );

                try {
                    paiementService.createPaiement(paiement);
                    JOptionPane.showMessageDialog(addDialog, "Paiement ajouté avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    addDialog.dispose();
                    loadPaiements(currentSearch);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(addDialog, "Erreur lors de l'ajout : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            });

            addDialog.add(formPanel, BorderLayout.CENTER);
            addDialog.pack();
            addDialog.setLocationRelativeTo(this);
            addDialog.setVisible(true);
        });

        searchField = new JTextField(18);
        searchField.setFont(new Font("Segoe UI", Font.BOLD, 16));
        searchField.setPreferredSize(new Dimension(200, 36));
        searchField.addActionListener(this::searchPaiements);

        JButton searchButton = new JButton("Rechercher");
        searchButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        searchButton.setPreferredSize(new Dimension(120, 40));
        searchButton.setBackground(new Color(255, 255, 255));
        searchButton.setFocusPainted(false);
        searchButton.setToolTipText("Rechercher");
        searchButton.addActionListener(this::searchPaiements);

        JButton editButton = new JButton("Éditer paiement");
        editButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        editButton.setPreferredSize(new Dimension(150, 40));
        editButton.setBackground(new Color(255, 140, 0));
        editButton.setForeground(Color.WHITE);
        editButton.setFocusPainted(false);
        editButton.setToolTipText("Éditer le paiement sélectionné");

        // Action à compléter selon la logique métier
        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner un paiement à éditer.", "Aucune sélection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Integer paiementId = (Integer) tableModel.getValueAt(selectedRow, 0);
            Paiement paiement = paiementService.getPaiementById(paiementId);
            if (paiement == null) {
                JOptionPane.showMessageDialog(this, "Paiement introuvable.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Window parentWindow = SwingUtilities.getWindowAncestor(this);
            Frame parentFrame = parentWindow instanceof Frame ? (Frame) parentWindow : null;
            JDialog editDialog = new JDialog(parentFrame, "Éditer paiement", true);
            editDialog.setLayout(new BorderLayout());

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridLayout(2, 1, 10, 10));
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

            JButton modifierBtn = new JButton("Modifier paiement");
            modifierBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
            modifierBtn.setBackground(new Color(100, 149, 237));
            modifierBtn.setForeground(Color.WHITE);
            modifierBtn.setFocusPainted(false);

            JButton supprimerBtn = new JButton("Supprimer paiement");
            supprimerBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
            supprimerBtn.setBackground(new Color(220, 53, 69));
            supprimerBtn.setForeground(Color.WHITE);
            supprimerBtn.setFocusPainted(false);

            // Action bouton Modifier paiement
            modifierBtn.addActionListener(ev -> {
                JDialog modifDialog = new JDialog(editDialog, "Modifier paiement", true);
                modifDialog.setLayout(new BorderLayout());
                JPanel formPanel = new JPanel(new GridBagLayout());
                formPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(8, 8, 8, 8);
                gbc.anchor = GridBagConstraints.WEST;
                gbc.fill = GridBagConstraints.HORIZONTAL;

                gbc.gridx = 0; gbc.gridy = 0;
                formPanel.add(new JLabel("Statut :"), gbc);
                String[] statuts = {"paye", "impaye", "partiel"};
                JComboBox<String> statutCombo = new JComboBox<>(statuts);
                statutCombo.setSelectedItem(paiement.getStatut().name());
                gbc.gridx = 1;
                formPanel.add(statutCombo, gbc);

                gbc.gridx = 0; gbc.gridy++;
                formPanel.add(new JLabel("Montant partiel :"), gbc);
                JTextField montantPartielField = new JTextField(
                    paiement.getMontantPartiel() != null ? paiement.getMontantPartiel().toString() : ""
                );
                gbc.gridx = 1;
                formPanel.add(montantPartielField, gbc);

                gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
                gbc.anchor = GridBagConstraints.CENTER;
                JButton saveBtn = new JButton("Enregistrer");
                saveBtn.setBackground(new Color(40, 167, 69));
                saveBtn.setForeground(Color.WHITE);
                saveBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
                saveBtn.setFocusPainted(false);
                formPanel.add(saveBtn, gbc);

                saveBtn.addActionListener(evt -> {
                    String statutStr = (String) statutCombo.getSelectedItem();
                    Paiement.StatutPaiement statut = Paiement.StatutPaiement.valueOf(statutStr);

                    BigDecimal montantPartiel = null;
                    String montantPartielTxt = montantPartielField.getText().trim();
                    if (!montantPartielTxt.isEmpty()) {
                        try {
                            montantPartiel = new BigDecimal(montantPartielTxt);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(modifDialog, "Montant partiel invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }

                    paiement.setStatut(statut);
                    paiement.setMontantPartiel(montantPartiel);

                    // Met à jour la date de paiement si statut paye ou partiel
                    if (statut == Paiement.StatutPaiement.paye || statut == Paiement.StatutPaiement.partiel) {
                        paiement.setDatePaiement(LocalDate.now());
                    } else {
                        paiement.setDatePaiement(null);
                    }

                    paiementService.updatePaiement(paiement);
                    JOptionPane.showMessageDialog(modifDialog, "Paiement modifié avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    loadPaiements(currentSearch);
                    modifDialog.dispose();
                    editDialog.dispose();
                });

                modifDialog.add(formPanel, BorderLayout.CENTER);
                modifDialog.pack();
                modifDialog.setLocationRelativeTo(editDialog);
                modifDialog.setVisible(true);
            });

            // Action bouton Supprimer paiement
            supprimerBtn.addActionListener(ev -> {
                int confirm = JOptionPane.showConfirmDialog(editDialog, "Voulez-vous vraiment supprimer ce paiement ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    paiementService.deletePaiement(paiement);
                    loadPaiements(currentSearch);
                    editDialog.dispose();
                }
            });

            buttonPanel.add(modifierBtn);
            buttonPanel.add(supprimerBtn);

            editDialog.add(buttonPanel, BorderLayout.CENTER);
            editDialog.pack();
            editDialog.setLocationRelativeTo(this);
            editDialog.setVisible(true);
        });

        topPanel.add(addButton);
        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(editButton);

        add(topPanel, BorderLayout.NORTH);

        // Colonnes : ID, Période début, Période fin, Montant, Statut, Montant partiel, Date paiement
        String[] columns = {"ID", "Début", "Fin", "Montant", "Statut", "Montant partiel", "Date paiement"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(28);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);

        loadPaiements(null);
    }

    private void searchPaiements(ActionEvent e) {
        String search = searchField.getText().trim();
        currentSearch = search.isEmpty() ? null : search;
        loadPaiements(currentSearch);
    }

    private void loadPaiements(String search) {
        tableModel.setRowCount(0);
        List<Paiement> paiements = paiementService.getPaiementsByUser(user);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (Paiement paiement : paiements) {
            // Recherche sur période, statut, montant, etc.
            if (search != null) {
                String searchLower = search.toLowerCase();
                boolean match =
                        (paiement.getPeriodeDebut() != null && paiement.getPeriodeDebut().toString().contains(searchLower)) ||
                        (paiement.getPeriodeFin() != null && paiement.getPeriodeFin().toString().contains(searchLower)) ||
                        (paiement.getStatut() != null && paiement.getStatut().toString().toLowerCase().contains(searchLower)) ||
                        (paiement.getMontant() != null && paiement.getMontant().toString().contains(searchLower)) ||
                        (paiement.getMontantPartiel() != null && paiement.getMontantPartiel().toString().contains(searchLower)) ||
                        (paiement.getDatePaiement() != null && paiement.getDatePaiement().toString().contains(searchLower));
                if (!match) continue;
            }
            tableModel.addRow(new Object[]{
                paiement.getPaiementId(),
                paiement.getPeriodeDebut() != null ? paiement.getPeriodeDebut().format(fmt) : "",
                paiement.getPeriodeFin() != null ? paiement.getPeriodeFin().format(fmt) : "",
                paiement.getMontant() != null ? paiement.getMontant() : BigDecimal.ZERO,
                paiement.getStatut() != null ? paiement.getStatut().name() : "",
                paiement.getMontantPartiel() != null ? paiement.getMontantPartiel() : "",
                paiement.getDatePaiement() != null ? paiement.getDatePaiement().format(fmt) : ""
            });
        }
    }
}
