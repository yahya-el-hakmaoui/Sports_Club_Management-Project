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
        addButton.setPreferredSize(new Dimension(100, 35));
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
        searchField.setPreferredSize(new Dimension(200, 35));

        // Permettre la recherche avec la touche Entrée
        searchField.addActionListener(this::searchActivities);

        JButton searchButton = new JButton("Rechercher");
        searchButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        searchButton.setPreferredSize(new Dimension(120, 35));
        searchButton.setBackground(new Color(255, 255, 255));
        searchButton.setFocusPainted(false);
        searchButton.setToolTipText("Rechercher");

        searchButton.addActionListener(this::searchActivities);

        // Nouveau bouton "Éditer activité" orange
        JButton editButton = new JButton("Éditer activité");
        editButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        editButton.setPreferredSize(new Dimension(160, 35));
        editButton.setBackground(new Color(255, 140, 0)); // Orange
        editButton.setForeground(Color.WHITE);
        editButton.setFocusPainted(false);
        editButton.setToolTipText("Modifier l'activité sélectionnée");

        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une activité à éditer.", "Avertissement", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int activityId = (int) tableModel.getValueAt(selectedRow, 0);
            Activity activity = activityService.getActivityById(activityId);
            if (activity == null) {
                JOptionPane.showMessageDialog(this, "Activité introuvable.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window instanceof JFrame) {
                JFrame frame = (JFrame) window;
                JDialog dialog = new JDialog(frame, "Gestion de l'activité", true);

                // Panel vertical avec les boutons
                JPanel buttonPanel = new JPanel();
                buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
                buttonPanel.setBorder(BorderFactory.createEmptyBorder(24, 32, 24, 32));
                buttonPanel.setBackground(Color.WHITE);

                JButton modifierButton = new JButton("Modifier activité");
                modifierButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
                modifierButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                modifierButton.setBackground(new Color(100, 149, 237));
                modifierButton.setForeground(Color.WHITE);
                modifierButton.setFocusPainted(false);
                modifierButton.setMaximumSize(new Dimension(220, 40));
                modifierButton.setPreferredSize(new Dimension(220, 40));
                modifierButton.addActionListener(ev -> {
                    // Ouvre le panel de modification
                    JDialog modifDialog = new JDialog(frame, "Modifier une activité", true);
                    modifDialog.setContentPane(new CreateActivityPanel(activity, () -> {
                        modifDialog.dispose();
                        dialog.dispose();
                        loadActivities(null);
                    }));
                    modifDialog.pack();
                    modifDialog.setLocationRelativeTo(frame);
                    modifDialog.setVisible(true);
                });

                JButton gererInscrButton = new JButton("Gérer les inscriptions");
                gererInscrButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
                gererInscrButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                gererInscrButton.setBackground(new Color(60, 179, 113));
                gererInscrButton.setForeground(Color.WHITE);
                gererInscrButton.setFocusPainted(false);
                gererInscrButton.setMaximumSize(new Dimension(220, 40));
                gererInscrButton.setPreferredSize(new Dimension(220, 40));
                gererInscrButton.addActionListener(ev -> {
                    // À compléter : ouvrir la gestion des inscriptions pour cette activité
                    JOptionPane.showMessageDialog(dialog, "Gestion des inscriptions à implémenter.", "Info", JOptionPane.INFORMATION_MESSAGE);
                });

                JButton supprimerButton = new JButton("Supprimer activité");
                supprimerButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
                supprimerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                supprimerButton.setBackground(new Color(220, 53, 69));
                supprimerButton.setForeground(Color.WHITE);
                supprimerButton.setFocusPainted(false);
                supprimerButton.setMaximumSize(new Dimension(220, 40));
                supprimerButton.setPreferredSize(new Dimension(220, 40));
                supprimerButton.addActionListener(ev -> {
                    int confirm = JOptionPane.showConfirmDialog(dialog,
                            "Êtes-vous sûr de vouloir supprimer cette activité ?",
                            "Confirmation de suppression",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE);
                    if (confirm == JOptionPane.YES_OPTION) {
                        activityService.deleteActivityById(activityId);
                        JOptionPane.showMessageDialog(dialog, "Activité supprimée.", "Suppression", JOptionPane.INFORMATION_MESSAGE);
                        dialog.dispose();
                        loadActivities(null);
                    }
                });

                buttonPanel.add(modifierButton);
                buttonPanel.add(Box.createVerticalStrut(18));
                buttonPanel.add(gererInscrButton);
                buttonPanel.add(Box.createVerticalStrut(18));
                buttonPanel.add(supprimerButton);

                dialog.setContentPane(buttonPanel);
                dialog.pack();
                dialog.setLocationRelativeTo(frame);
                dialog.setVisible(true);
            }
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
        for (Activity activity : activities) {
            if (search != null && !activity.getNom().toLowerCase().contains(search.toLowerCase())) {
                continue;
            }
            int adherents = 0;
            Integer max = activity.getMaxParticipants();
            try {
                if (activity.getInscriptions() != null) {
                    adherents = activity.getInscriptions().size();
                }
            } catch (Exception ex) {
                adherents = 0;
            }
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
    // Panel interne pour création/modification d'activité
    static class CreateActivityPanel extends JPanel {
        private JLabel idLabel;
        private JTextField nomField;
        private JTextField tarifField;
        private JTextField maxParticipantsField;
        private JTextArea descriptionArea;
        private Runnable onSuccess;
        private Activity activityToEdit;

        // Création
        public CreateActivityPanel(Runnable onSuccess) {
            this(null, onSuccess);
        }

        // Modification
        public CreateActivityPanel(Activity activityToEdit, Runnable onSuccess) {
            this.onSuccess = onSuccess;
            this.activityToEdit = activityToEdit;
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
            idLabel = new JLabel(activityToEdit != null ? String.valueOf(activityToEdit.getActivityId()) : getNextActivityIdText());
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

            // Pré-remplir si modification
            if (activityToEdit != null) {
                nomField.setText(activityToEdit.getNom());
                tarifField.setText(activityToEdit.getTarif() != null ? activityToEdit.getTarif().toString() : "");
                maxParticipantsField.setText(activityToEdit.getMaxParticipants() != null ? activityToEdit.getMaxParticipants().toString() : "");
                descriptionArea.setText(activityToEdit.getDescription() != null ? activityToEdit.getDescription() : "");
            }

            // Bouton de confirmation
            gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            JButton confirmButton = new JButton(activityToEdit == null ? "Créer l'activité" : "Enregistrer les modifications");
            confirmButton.setBackground(activityToEdit == null ? new Color(40, 167, 69) : new Color(255, 140, 0));
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
                    ActivityService service = new ActivityService();

                    if (activityToEdit == null) {
                        Activity activity = new Activity(nom, tarif, max, desc);
                        service.createActivity(activity);
                        JOptionPane.showMessageDialog(this, "Activité créée avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        activityToEdit.setNom(nom);
                        activityToEdit.setTarif(tarif);
                        activityToEdit.setMaxParticipants(max);
                        activityToEdit.setDescription(desc);
                        service.updateActivity(activityToEdit);
                        JOptionPane.showMessageDialog(this, "Activité modifiée avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    }

                    if (onSuccess != null) onSuccess.run();

                    // Fermer la fenêtre si dans un JDialog
                    Window window = SwingUtilities.getWindowAncestor(this);
                    if (window instanceof JDialog) {
                        window.dispose();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la sauvegarde : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
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
