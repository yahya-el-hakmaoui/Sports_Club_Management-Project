package com.clubsportif.ui.admin;

import com.clubsportif.model.Activity;
import com.clubsportif.service.ActivityService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.List;

public class ActivityAdminPanel extends JPanel {
    private JTextField searchField;
    private DefaultTableModel tableModel;
    private JTable table;
    private ActivityService activityService = new ActivityService();

    public ActivityAdminPanel() {
        setLayout(new BorderLayout(10, 10));

        // Top panel: Add button + search bar + search button + edit button
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JButton addButton = new JButton("Ajouter");
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        addButton.setPreferredSize(new Dimension(100, 40));
        addButton.setBackground(new Color(40, 167, 69)); // Vert Bootstrap
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setToolTipText("Ajouter une activité");

        // ActionListener pour afficher le panel CreateActivity intégré
        addButton.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window instanceof JFrame) {
                JFrame frame = (JFrame) window;
                // Affiche CreateActivityPanel dans une JDialog modale
                JDialog dialog = new JDialog(frame, "Ajouter une activité", true);
                dialog.setContentPane(new CreateActivityPanel(() -> {
                    dialog.dispose();
                    loadActivities(null); // Refresh la liste après ajout
                }));
                dialog.pack();
                dialog.setLocationRelativeTo(frame);
                dialog.setVisible(true);
            }
        });

        searchField = new JTextField(18);
        searchField.setFont(new Font("Segoe UI", Font.BOLD, 16));
        searchField.setPreferredSize(new Dimension(200, 36));

        // Permettre la recherche avec la touche Entrée
        searchField.addActionListener(this::searchActivities);

        JButton searchButton = new JButton("Rechercher");
        searchButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        searchButton.setPreferredSize(new Dimension(120, 40));
        searchButton.setBackground(new Color(255, 255, 255));
        searchButton.setFocusPainted(false);
        searchButton.setToolTipText("Rechercher");

        searchButton.addActionListener(this::searchActivities);

        // Nouveau bouton "Éditer activité"
        JButton editButton = new JButton("Éditer activité");
        editButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        editButton.setPreferredSize(new Dimension(150, 40));
        editButton.setBackground(new Color(255, 140, 0)); // Orange
        editButton.setForeground(Color.WHITE);
        editButton.setFocusPainted(false);
        editButton.setToolTipText("Éditer l'activité sélectionnée");

        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une activité à éditer.", "Aucune sélection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int activityId = (int) tableModel.getValueAt(selectedRow, 0);
            Activity activity = activityService.getActivityById(activityId);

            // Correction ici : récupérer le Frame parent pour le JDialog
            Window window = SwingUtilities.getWindowAncestor(this);
            Frame parentFrame = window instanceof Frame ? (Frame) window : null;
            JDialog dialog = new JDialog(parentFrame, "Éditer activité", true);
            dialog.setLayout(new BorderLayout());
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

            JButton modifierBtn = new JButton("Modifier activité");
            modifierBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
            modifierBtn.setBackground(new Color(100, 149, 237));
            modifierBtn.setForeground(Color.WHITE);
            modifierBtn.setFocusPainted(false);

            JButton gererInscrBtn = new JButton("Gérer inscriptions");
            gererInscrBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
            gererInscrBtn.setBackground(new Color(60, 179, 113));
            gererInscrBtn.setForeground(Color.WHITE);
            gererInscrBtn.setFocusPainted(false);

            JButton supprimerBtn = new JButton("Supprimer activité");
            supprimerBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
            supprimerBtn.setBackground(new Color(220, 53, 69));
            supprimerBtn.setForeground(Color.WHITE);
            supprimerBtn.setFocusPainted(false);

            // Actions à compléter selon la logique métier
            modifierBtn.addActionListener(ev -> {
                // Fenêtre de modification d'activité
                JDialog editDialog = new JDialog(dialog, "Modifier l'activité", true);
                editDialog.setLayout(new BorderLayout());
                JPanel formPanel = new JPanel(new GridBagLayout());
                formPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(8, 8, 8, 8);
                gbc.anchor = GridBagConstraints.WEST;
                gbc.fill = GridBagConstraints.HORIZONTAL;

                // Nom
                gbc.gridx = 0; gbc.gridy = 0;
                formPanel.add(new JLabel("Nom de l'activité:"), gbc);
                JTextField nomField = new JTextField(activity.getNom(), 18);
                gbc.gridx = 1;
                formPanel.add(nomField, gbc);

                // Tarif
                gbc.gridx = 0; gbc.gridy++;
                formPanel.add(new JLabel("Tarif (DH):"), gbc);
                JTextField tarifField = new JTextField(activity.getTarif() != null ? activity.getTarif().toString() : "", 18);
                gbc.gridx = 1;
                formPanel.add(tarifField, gbc);

                // Max participants
                gbc.gridx = 0; gbc.gridy++;
                formPanel.add(new JLabel("Max participants:"), gbc);
                JTextField maxParticipantsField = new JTextField(activity.getMaxParticipants() != null ? activity.getMaxParticipants().toString() : "", 18);
                gbc.gridx = 1;
                formPanel.add(maxParticipantsField, gbc);

                // Description
                gbc.gridx = 0; gbc.gridy++;
                formPanel.add(new JLabel("Description:"), gbc);
                JTextArea descriptionArea = new JTextArea(activity.getDescription() != null ? activity.getDescription() : "", 4, 18);
                descriptionArea.setLineWrap(true);
                descriptionArea.setWrapStyleWord(true);
                JScrollPane descScroll = new JScrollPane(descriptionArea);
                gbc.gridx = 1;
                formPanel.add(descScroll, gbc);

                // Bouton enregistrer
                gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
                gbc.anchor = GridBagConstraints.CENTER;
                JButton saveButton = new JButton("Enregistrer");
                saveButton.setBackground(new Color(40, 167, 69));
                saveButton.setForeground(Color.WHITE);
                saveButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
                saveButton.setFocusPainted(false);
                formPanel.add(saveButton, gbc);

                saveButton.addActionListener(evt -> {
                    String nom = nomField.getText().trim();
                    String tarifStr = tarifField.getText().trim();
                    String maxStr = maxParticipantsField.getText().trim();
                    String desc = descriptionArea.getText().trim();

                    if (nom.isEmpty() || tarifStr.isEmpty() || maxStr.isEmpty()) {
                        JOptionPane.showMessageDialog(editDialog, "Veuillez remplir tous les champs obligatoires.", "Erreur", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    try {
                        BigDecimal tarif = new BigDecimal(tarifStr);
                        int max = Integer.parseInt(maxStr);

                        activity.setNom(nom);
                        activity.setTarif(tarif);
                        activity.setMaxParticipants(max);
                        activity.setDescription(desc);

                        activityService.updateActivity(activity);

                        JOptionPane.showMessageDialog(editDialog, "Activité modifiée avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                        loadActivities(null);
                        editDialog.dispose();
                        dialog.dispose();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(editDialog, "Erreur lors de la modification : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                });

                editDialog.add(formPanel, BorderLayout.CENTER);
                editDialog.pack();
                editDialog.setLocationRelativeTo(dialog);
                editDialog.setVisible(true);
            });

            gererInscrBtn.addActionListener(ev -> {
                // Ouvre une fenêtre modale avec InscriptionsActivityAdminPanel, même taille que ActivityAdminPanel
                Window parentWindow = SwingUtilities.getWindowAncestor(this);
                Frame parentFrameInscriptions = parentWindow instanceof Frame ? (Frame) parentWindow : null;
                JDialog inscriptionsDialog = new JDialog(parentFrameInscriptions, "Gérer inscriptions", true);
                InscriptionsActivityAdminPanel panel = new InscriptionsActivityAdminPanel(activity);
                panel.setPreferredSize(ActivityAdminPanel.this.getSize());
                inscriptionsDialog.setContentPane(panel);
                inscriptionsDialog.pack();
                inscriptionsDialog.setSize(ActivityAdminPanel.this.getWidth() > 0 ? ActivityAdminPanel.this.getWidth() : 800,
                                           ActivityAdminPanel.this.getHeight() > 0 ? ActivityAdminPanel.this.getHeight() : 600);
                inscriptionsDialog.setLocationRelativeTo(ActivityAdminPanel.this);
                inscriptionsDialog.setVisible(true);
            });

            supprimerBtn.addActionListener(ev -> {
                int confirm = JOptionPane.showConfirmDialog(dialog, "Voulez-vous vraiment supprimer cette activité ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    activityService.deleteActivityById(activityId);
                    loadActivities(null);
                    dialog.dispose();
                }
            });

            buttonPanel.add(modifierBtn);
            buttonPanel.add(gererInscrBtn);
            buttonPanel.add(supprimerBtn);

            dialog.add(buttonPanel, BorderLayout.CENTER);
            dialog.pack();
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        });

        topPanel.add(addButton);
        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(editButton);

        add(topPanel, BorderLayout.NORTH);

        // Table avec colonne ID en premier
        String[] columns = {"ID", "Nom", "Tarif", "Adhérents", "Description"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(28);

        // Rendre la table scrollable si la liste est longue
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);

        // Remplir la table au chargement
        loadActivities(null);
    }

    private void searchActivities(ActionEvent e) {
        String search = searchField.getText().trim();
        loadActivities(search.isEmpty() ? null : search);
    }

    private void loadActivities(String search) {
        tableModel.setRowCount(0);
        List<Activity> activities = activityService.getAllActivities();
        com.clubsportif.service.InscriptionService inscriptionService = new com.clubsportif.service.InscriptionService();
        for (Activity activity : activities) {
            if (search != null && !activity.getNom().toLowerCase().contains(search.toLowerCase())) {
                continue;
            }
            // Compte le nombre d'inscriptions actives pour cette activité
            long adherents = inscriptionService.countActiveInscriptionsForActivity(activity);
            Integer max = activity.getMaxParticipants();
            String adherentsStr = (max != null && max > 0) ? (adherents + "/" + max) : String.valueOf(adherents);
            BigDecimal tarif = activity.getTarif();
            tableModel.addRow(new Object[]{
                activity.getActivityId(),
                activity.getNom(),
                tarif != null ? tarif + " DH" : "",
                adherentsStr,
                activity.getDescription()
            });
        }
    }

    // --- Panel interne pour création d'activité ---
    static class CreateActivityPanel extends JPanel {
        private JLabel idLabel;
        private JTextField nomField;
        private JTextField tarifField;
        private JTextField maxParticipantsField;
        private JTextArea descriptionArea;
        private Runnable onSuccess;

        public CreateActivityPanel(Runnable onSuccess) {
            this.onSuccess = onSuccess;
            setLayout(new BorderLayout(10, 10));
            setPreferredSize(new Dimension(400, 340));

            JPanel formPanel = new JPanel();
            formPanel.setLayout(new GridBagLayout());
            formPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(8, 8, 8, 8);
            gbc.anchor = GridBagConstraints.WEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // ID généré automatiquement (affiché en haut, non éditable)
            gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1;
            formPanel.add(new JLabel("ID (auto):"), gbc);
            idLabel = new JLabel(getNextActivityIdText());
            idLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            gbc.gridx = 1;
            formPanel.add(idLabel, gbc);

            // Nom
            gbc.gridx = 0; gbc.gridy++;
            formPanel.add(new JLabel("Nom de l'activité:"), gbc);
            nomField = new JTextField(18);
            gbc.gridx = 1;
            formPanel.add(nomField, gbc);

            // Tarif
            gbc.gridx = 0; gbc.gridy++;
            formPanel.add(new JLabel("Tarif (DH):"), gbc);
            tarifField = new JTextField(18);
            gbc.gridx = 1;
            formPanel.add(tarifField, gbc);

            // Max participants
            gbc.gridx = 0; gbc.gridy++;
            formPanel.add(new JLabel("Max participants:"), gbc);
            maxParticipantsField = new JTextField(18);
            gbc.gridx = 1;
            formPanel.add(maxParticipantsField, gbc);

            // Description
            gbc.gridx = 0; gbc.gridy++;
            formPanel.add(new JLabel("Description:"), gbc);
            descriptionArea = new JTextArea(4, 18);
            descriptionArea.setLineWrap(true);
            descriptionArea.setWrapStyleWord(true);
            JScrollPane descScroll = new JScrollPane(descriptionArea);
            gbc.gridx = 1;
            formPanel.add(descScroll, gbc);

            // Bouton de confirmation
            gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            JButton confirmButton = new JButton("Créer l'activité");
            confirmButton.setBackground(new Color(40, 167, 69));
            confirmButton.setForeground(Color.WHITE);
            confirmButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
            confirmButton.setFocusPainted(false);
            formPanel.add(confirmButton, gbc);

            // Action du bouton
            confirmButton.addActionListener(e -> {
                String nom = nomField.getText().trim();
                String tarifStr = tarifField.getText().trim();
                String maxStr = maxParticipantsField.getText().trim();
                String desc = descriptionArea.getText().trim();

                if (nom.isEmpty() || tarifStr.isEmpty() || maxStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs obligatoires.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    BigDecimal tarif = new BigDecimal(tarifStr);
                    int max = Integer.parseInt(maxStr);

                    Activity activity = new Activity(nom, tarif, max, desc);
                    ActivityService service = new ActivityService();
                    service.createActivity(activity);

                    JOptionPane.showMessageDialog(this, "Activité créée avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);

                    if (onSuccess != null) onSuccess.run();

                    // Fermer la fenêtre si dans un JDialog
                    Window window = SwingUtilities.getWindowAncestor(this);
                    if (window instanceof JDialog) {
                        window.dispose();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la création : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            });

            add(formPanel, BorderLayout.CENTER);
        }

        // Cherche le dernier id et affiche (id+1), ou 1 si la table est vide
        private String getNextActivityIdText() {
            ActivityService activityService = new ActivityService();
            List<Activity> activities = activityService.getAllActivities();
            if (activities == null || activities.isEmpty()) {
                return "1";
            }
            int maxId = 0;
            for (Activity a : activities) {
                if (a.getActivityId() > maxId) {
                    maxId = a.getActivityId();
                }
            }
            return String.valueOf(maxId + 1);
        }
    }
}
