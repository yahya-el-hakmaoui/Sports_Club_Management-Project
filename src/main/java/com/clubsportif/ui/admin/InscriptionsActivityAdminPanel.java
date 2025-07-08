package com.clubsportif.ui.admin;

import com.clubsportif.model.Activity;
import com.clubsportif.model.Inscription;
import com.clubsportif.model.User;
import com.clubsportif.service.ActivityService;
import com.clubsportif.service.InscriptionService;
import com.clubsportif.service.UserService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class InscriptionsActivityAdminPanel extends JPanel {
    private JTextField searchField;
    private DefaultTableModel tableModel;
    private JTable table;
    private InscriptionService inscriptionService = new InscriptionService();
    private UserService userService = new UserService();
    private Activity activity;
    private String currentSearch = null;

    public InscriptionsActivityAdminPanel(Activity activity) {
        this.activity = activity;

        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 245, 250));

        // Top panel: Add button + search bar + search button + edit button
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JButton addButton = new JButton("Ajouter");
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        addButton.setPreferredSize(new Dimension(100, 40));
        addButton.setBackground(new Color(40, 167, 69));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setToolTipText("Ajouter une inscription");

        // Ajout d'une inscription à cette activité
        addButton.addActionListener(e -> {
            Window parentWindow = SwingUtilities.getWindowAncestor(this);
            Frame parentFrame = parentWindow instanceof Frame ? (Frame) parentWindow : null;
            JDialog addDialog = new JDialog(parentFrame, "Ajouter une inscription", true);
            addDialog.setLayout(new BorderLayout());
            JPanel formPanel = new JPanel(new GridBagLayout());
            formPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(8, 8, 8, 8);
            gbc.anchor = GridBagConstraints.WEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // Sélection de l'adhérent
            gbc.gridx = 0;
            gbc.gridy = 0;
            formPanel.add(new JLabel("Adhérent:"), gbc);
            JComboBox<String> adherentCombo = new JComboBox<>();
            List<User> adherents = userService.getNotArchivedUsers();
            for (User user : adherents) {
                if (user.getRole() == User.Role.adherent) {
                    adherentCombo.addItem(user.getUserId() + " - " + user.getLastname() + " " + user.getName());
                }
            }
            gbc.gridx = 1;
            formPanel.add(adherentCombo, gbc);

            // Date d'inscription (aujourd'hui par défaut)
            gbc.gridx = 0;
            gbc.gridy++;
            formPanel.add(new JLabel("Date d'inscription (YYYY-MM-DD):"), gbc);
            JTextField dateField = new JTextField(java.time.LocalDate.now().toString());
            dateField.setPreferredSize(new Dimension(180, 28));
            gbc.gridx = 1;
            formPanel.add(dateField, gbc);

            // Statut (active/inactive)
            gbc.gridx = 0;
            gbc.gridy++;
            formPanel.add(new JLabel("Active:"), gbc);
            JCheckBox activeCheck = new JCheckBox();
            activeCheck.setSelected(true);
            gbc.gridx = 1;
            formPanel.add(activeCheck, gbc);

            // Bouton confirmer
            gbc.gridx = 0;
            gbc.gridy++;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            JButton confirmBtn = new JButton("Ajouter");
            confirmBtn.setBackground(new Color(40, 167, 69));
            confirmBtn.setForeground(Color.WHITE);
            confirmBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
            confirmBtn.setFocusPainted(false);
            formPanel.add(confirmBtn, gbc);

            confirmBtn.addActionListener(ev -> {
                String adherentStr = (String) adherentCombo.getSelectedItem();
                String dateStr = dateField.getText().trim();
                boolean active = activeCheck.isSelected();

                if (adherentStr == null || dateStr.isEmpty()) {
                    JOptionPane.showMessageDialog(addDialog, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                java.time.LocalDate dateInscription;
                try {
                    dateInscription = java.time.LocalDate.parse(dateStr);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(addDialog, "Format de date invalide. Utilisez YYYY-MM-DD.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int userId = Integer.parseInt(adherentStr.split(" - ")[0]);
                User selectedUser = userService.getUserById(userId);
                if (selectedUser == null) {
                    JOptionPane.showMessageDialog(addDialog, "Adhérent invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    Inscription inscription = new Inscription(selectedUser, activity, dateInscription, active);
                    inscriptionService.createInscription(inscription);
                    JOptionPane.showMessageDialog(addDialog, "Inscription ajoutée avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    addDialog.dispose();
                    loadInscriptions(currentSearch);
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
        searchField.addActionListener(this::searchInscriptions);

        JButton searchButton = new JButton("Rechercher");
        searchButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        searchButton.setPreferredSize(new Dimension(120, 40));
        searchButton.setBackground(new Color(255, 255, 255));
        searchButton.setFocusPainted(false);
        searchButton.setToolTipText("Rechercher");
        searchButton.addActionListener(this::searchInscriptions);

        JButton editButton = new JButton("Éditer inscription");
        editButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        editButton.setPreferredSize(new Dimension(170, 40));
        editButton.setBackground(new Color(255, 140, 0));
        editButton.setForeground(Color.WHITE);
        editButton.setFocusPainted(false);
        editButton.setToolTipText("Éditer l'inscription sélectionnée");

        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une inscription à éditer.", "Aucune sélection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int inscriptionId = (int) tableModel.getValueAt(selectedRow, 0);
            Inscription inscription = inscriptionService.getInscriptionById(inscriptionId);

            if (inscription == null) {
                JOptionPane.showMessageDialog(this, "Impossible de trouver l'inscription sélectionnée.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Window parentWindow = SwingUtilities.getWindowAncestor(this);
            Frame parentFrame = parentWindow instanceof Frame ? (Frame) parentWindow : null;
            JDialog editDialog = new JDialog(parentFrame, "Éditer inscription", true);
            editDialog.setLayout(new BorderLayout());
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridLayout(2, 1, 10, 10));
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

            // Bouton Activer/Désactiver selon l'état actuel
            String statusLabel = inscription.isActive() ? "Désactiver inscription" : "Activer inscription";
            Color statusColor = inscription.isActive() ? new Color(255, 193, 7) : new Color(40, 167, 69);
            JButton statusBtn = new JButton(statusLabel);
            statusBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
            statusBtn.setBackground(statusColor);
            statusBtn.setForeground(Color.WHITE);
            statusBtn.setFocusPainted(false);

            JButton supprimerBtn = new JButton("Supprimer inscription");
            supprimerBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
            supprimerBtn.setBackground(new Color(220, 53, 69));
            supprimerBtn.setForeground(Color.WHITE);
            supprimerBtn.setFocusPainted(false);

            statusBtn.addActionListener(ev -> {
                if (inscription.isActive()) {
                    int confirm = JOptionPane.showConfirmDialog(editDialog, "Voulez-vous vraiment désactiver cette inscription ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        inscription.setActive(false);
                        inscriptionService.updateInscription(inscription);
                        loadInscriptions(currentSearch);
                        editDialog.dispose();
                    }
                } else {
                    int confirm = JOptionPane.showConfirmDialog(editDialog, "Voulez-vous activer cette inscription ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        inscription.setActive(true);
                        inscriptionService.updateInscription(inscription);
                        loadInscriptions(currentSearch);
                        editDialog.dispose();
                    }
                }
            });

            supprimerBtn.addActionListener(ev -> {
                int confirm = JOptionPane.showConfirmDialog(editDialog, "Voulez-vous vraiment supprimer cette inscription ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    inscriptionService.deleteInscription(inscription);
                    loadInscriptions(currentSearch);
                    editDialog.dispose();
                }
            });

            buttonPanel.add(statusBtn);
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

        // Colonnes : ID, Adhérent, Date inscription, Statut
        String[] columns = {"ID", "Adhérent", "Date inscription", "Statut"};
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

        loadInscriptions(null);
    }

    private void searchInscriptions(ActionEvent e) {
        String search = searchField.getText().trim();
        currentSearch = search.isEmpty() ? null : search;
        loadInscriptions(currentSearch);
    }

    private void loadInscriptions(String search) {
        tableModel.setRowCount(0);
        List<Inscription> inscriptions = inscriptionService.getInscriptionsByActivity(activity);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (Inscription inscription : inscriptions) {
            // Correction LazyInitializationException : charger explicitement l'utilisateur
            int adherentId = inscription.getUser().getUserId();
            User adherent = userService.getUserById(adherentId);
            String adherentName = adherent != null ? (adherent.getLastname() + " " + adherent.getName()) : ("Adhérent #" + adherentId);
            String dateInscription = inscription.getDateInscription() != null ? inscription.getDateInscription().format(fmt) : "";
            String statut = inscription.isActive() ? "Active" : "Inactive";
            if (search != null) {
                String searchLower = search.toLowerCase();
                boolean match =
                        adherentName.toLowerCase().contains(searchLower) ||
                        dateInscription.contains(searchLower) ||
                        statut.toLowerCase().contains(searchLower);
                if (!match) continue;
            }
            tableModel.addRow(new Object[]{
                    inscription.getInscriptionId(),
                    adherentName,
                    dateInscription,
                    statut
            });
        }
    }
}
