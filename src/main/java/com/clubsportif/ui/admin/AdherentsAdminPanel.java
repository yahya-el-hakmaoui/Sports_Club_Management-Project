package com.clubsportif.ui.admin;

import com.clubsportif.model.User;
import com.clubsportif.service.UserService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class AdherentsAdminPanel extends JPanel {
    private JTextField searchField;
    private DefaultTableModel tableModel;
    private JTable table;
    private UserService userService = new UserService();
    private boolean showArchived = false;
    private JButton toggleArchiveButton;
    private String currentSearch = null;

    public AdherentsAdminPanel() {
        setLayout(new BorderLayout(10, 10));

        // Top panel: Add button + toggle archive + search bar + search button + edit button
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JButton addButton = new JButton("Ajouter");
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        addButton.setPreferredSize(new Dimension(100, 40));
        addButton.setBackground(new Color(40, 167, 69)); // Vert Bootstrap
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setToolTipText("Ajouter un adhérent");

        // Implémentation du bouton Ajouter
        addButton.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(this);
            Frame parentFrame = window instanceof Frame ? (Frame) window : null;
            JDialog dialog = new JDialog(parentFrame, "Ajouter un adhérent", true);
            dialog.setContentPane(new CreateAdherentPanel(() -> {
                dialog.dispose();
                loadAdherents(currentSearch); // Rafraîchir la liste après ajout
            }));
            dialog.setMinimumSize(new Dimension(600, 520));
            dialog.pack();
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        });

        // Bouton pour basculer entre actifs et archives
        toggleArchiveButton = new JButton("Voir archives");
        toggleArchiveButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        toggleArchiveButton.setPreferredSize(new Dimension(130, 40));
        toggleArchiveButton.setBackground(new Color(100, 149, 237)); // Bleu
        toggleArchiveButton.setForeground(Color.WHITE);
        toggleArchiveButton.setFocusPainted(false);
        toggleArchiveButton.setToolTipText("Afficher les adhérents archivés");

        toggleArchiveButton.addActionListener(e -> {
            showArchived = !showArchived;
            toggleArchiveButton.setText(showArchived ? "Voir actifs" : "Voir archives");
            toggleArchiveButton.setToolTipText(showArchived ? "Afficher les adhérents actifs" : "Afficher les adhérents archivés");
            loadAdherents(currentSearch);
        });

        searchField = new JTextField(18);
        searchField.setFont(new Font("Segoe UI", Font.BOLD, 16));
        searchField.setPreferredSize(new Dimension(200, 36));
        // Recherche sur entrée clavier
        searchField.addActionListener(this::searchAdherents);

        JButton searchButton = new JButton("Rechercher");
        searchButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        searchButton.setPreferredSize(new Dimension(120, 40));
        searchButton.setBackground(new Color(255, 255, 255));
        searchButton.setFocusPainted(false);
        searchButton.setToolTipText("Rechercher");
        // Recherche sur clic bouton
        searchButton.addActionListener(this::searchAdherents);

        JButton editButton = new JButton("Éditer adhérent");
        editButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        editButton.setPreferredSize(new Dimension(170, 40));
        editButton.setBackground(new Color(255, 140, 0)); // Orange
        editButton.setForeground(Color.WHITE);
        editButton.setFocusPainted(false);
        editButton.setToolTipText("Éditer l'adhérent sélectionné");

        // ActionListener à compléter plus tard
        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner un adhérent à éditer.", "Aucune sélection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int userId = (int) tableModel.getValueAt(selectedRow, 0);
            User user = userService.getUserById(userId);

            Window window = SwingUtilities.getWindowAncestor(this);
            Frame parentFrame = window instanceof Frame ? (Frame) window : null;
            JDialog dialog = new JDialog(parentFrame, "Éditer adhérent", true);
            dialog.setLayout(new BorderLayout());
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridLayout(5, 1, 10, 10));
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

            JButton modifierBtn = new JButton("Modifier adhérent");
            modifierBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
            modifierBtn.setBackground(new Color(100, 149, 237));
            modifierBtn.setForeground(Color.WHITE);
            modifierBtn.setFocusPainted(false);

            // Bouton archiver/activer selon l'état de l'adhérent
            String archiverLabel = user.isArchived() ? "Activer adhérent" : "Archiver adhérent";
            Color archiverColor = user.isArchived() ? new Color(40, 167, 69) : new Color(255, 193, 7);
            JButton archiverBtn = new JButton(archiverLabel);
            archiverBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
            archiverBtn.setBackground(archiverColor);
            archiverBtn.setForeground(Color.WHITE);
            archiverBtn.setFocusPainted(false);

            JButton gererInscrBtn = new JButton("Gérer inscriptions");
            gererInscrBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
            gererInscrBtn.setBackground(new Color(60, 179, 113));
            gererInscrBtn.setForeground(Color.WHITE);
            gererInscrBtn.setFocusPainted(false);

            JButton gererPaiementsBtn = new JButton("Gérer paiements");
            gererPaiementsBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
            gererPaiementsBtn.setBackground(new Color(70, 130, 180));
            gererPaiementsBtn.setForeground(Color.WHITE);
            gererPaiementsBtn.setFocusPainted(false);

            JButton supprimerBtn = new JButton("Supprimer adhérent");
            supprimerBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
            supprimerBtn.setBackground(new Color(220, 53, 69));
            supprimerBtn.setForeground(Color.WHITE);
            supprimerBtn.setFocusPainted(false);

            // Implémentation du bouton Modifier adhérent avec changement de mot de passe
            modifierBtn.addActionListener(ev -> {
                JDialog editDialog = new JDialog(dialog, "Modifier l'adhérent", true);
                editDialog.setLayout(new BorderLayout());
                JPanel formPanel = new JPanel(new GridBagLayout());
                formPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(8, 8, 8, 8);
                gbc.anchor = GridBagConstraints.WEST;
                gbc.fill = GridBagConstraints.HORIZONTAL;

                Dimension fieldSize = new Dimension(340, 32);

                gbc.gridx = 0; gbc.gridy = 0;
                formPanel.add(new JLabel("Nom d'utilisateur:"), gbc);
                JTextField usernameField = new JTextField(user.getUsername());
                usernameField.setPreferredSize(fieldSize);
                gbc.gridx = 1;
                formPanel.add(usernameField, gbc);

                gbc.gridx = 0; gbc.gridy++;
                formPanel.add(new JLabel("Nouveau mot de passe:"), gbc);
                JPasswordField passwordField = new JPasswordField();
                passwordField.setPreferredSize(fieldSize);
                gbc.gridx = 1;
                formPanel.add(passwordField, gbc);

                gbc.gridx = 0; gbc.gridy++;
                formPanel.add(new JLabel("Nom:"), gbc);
                JTextField nomField = new JTextField(user.getLastname());
                nomField.setPreferredSize(fieldSize);
                gbc.gridx = 1;
                formPanel.add(nomField, gbc);

                gbc.gridx = 0; gbc.gridy++;
                formPanel.add(new JLabel("Prénom:"), gbc);
                JTextField prenomField = new JTextField(user.getName());
                prenomField.setPreferredSize(fieldSize);
                gbc.gridx = 1;
                formPanel.add(prenomField, gbc);

                gbc.gridx = 0; gbc.gridy++;
                formPanel.add(new JLabel("Email:"), gbc);
                JTextField emailField = new JTextField(user.getEmail() != null ? user.getEmail() : "");
                emailField.setPreferredSize(fieldSize);
                gbc.gridx = 1;
                formPanel.add(emailField, gbc);

                gbc.gridx = 0; gbc.gridy++;
                formPanel.add(new JLabel("Téléphone:"), gbc);
                JTextField telephoneField = new JTextField(user.getTelephone() != null ? user.getTelephone() : "");
                telephoneField.setPreferredSize(fieldSize);
                gbc.gridx = 1;
                formPanel.add(telephoneField, gbc);

                gbc.gridx = 0; gbc.gridy++;
                formPanel.add(new JLabel("Adresse:"), gbc);
                JTextField adresseField = new JTextField(user.getAdresse() != null ? user.getAdresse() : "");
                adresseField.setPreferredSize(fieldSize);
                gbc.gridx = 1;
                formPanel.add(adresseField, gbc);

                gbc.gridx = 0; gbc.gridy++;
                formPanel.add(new JLabel("Date de naissance (YYYY-MM-DD):"), gbc);
                JTextField dateNaissanceField = new JTextField(user.getDateNaissance() != null ? user.getDateNaissance().toString() : "");
                dateNaissanceField.setPreferredSize(fieldSize);
                gbc.gridx = 1;
                formPanel.add(dateNaissanceField, gbc);

                gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
                gbc.anchor = GridBagConstraints.CENTER;
                JButton saveButton = new JButton("Enregistrer");
                saveButton.setBackground(new Color(40, 167, 69));
                saveButton.setForeground(Color.WHITE);
                saveButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
                saveButton.setFocusPainted(false);
                formPanel.add(saveButton, gbc);

                saveButton.addActionListener(evt -> {
                    String username = usernameField.getText().trim();
                    String password = new String(passwordField.getPassword());
                    String nom = nomField.getText().trim();
                    String prenom = prenomField.getText().trim();
                    String email = emailField.getText().trim();
                    String telephone = telephoneField.getText().trim();
                    String adresse = adresseField.getText().trim();
                    String dateNaissanceStr = dateNaissanceField.getText().trim();

                    if (username.isEmpty() || nom.isEmpty() || prenom.isEmpty()) {
                        JOptionPane.showMessageDialog(editDialog, "Veuillez remplir tous les champs obligatoires.", "Erreur", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    java.time.LocalDate dateNaissance = null;
                    if (!dateNaissanceStr.isEmpty()) {
                        try {
                            dateNaissance = java.time.LocalDate.parse(dateNaissanceStr);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(editDialog, "Format de date de naissance invalide. Utilisez YYYY-MM-DD.", "Erreur", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }

                    user.setUsername(username);
                    if (!password.isEmpty()) {
                        user.setPasswordHash(com.clubsportif.util.PasswordUtils.hashPassword(password));
                    }
                    user.setLastname(nom);
                    user.setName(prenom);
                    user.setEmail(email);
                    user.setTelephone(telephone);
                    user.setAdresse(adresse);
                    user.setDateNaissance(dateNaissance);

                    userService.updateUser(user);

                    JOptionPane.showMessageDialog(editDialog, "Adhérent modifié avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    loadAdherents(currentSearch);
                    editDialog.dispose();
                    dialog.dispose();
                });

                editDialog.add(formPanel, BorderLayout.CENTER);
                editDialog.pack();
                editDialog.setLocationRelativeTo(dialog);
                editDialog.setVisible(true);
            });

            archiverBtn.addActionListener(ev -> {
                if (user.isArchived()) {
                    int confirm = JOptionPane.showConfirmDialog(dialog, "Voulez-vous activer cet adhérent ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        user.setArchived(false);
                        userService.updateUser(user);
                        loadAdherents(currentSearch);
                        dialog.dispose();
                    }
                } else {
                    int confirm = JOptionPane.showConfirmDialog(dialog, "Voulez-vous vraiment archiver cet adhérent ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        user.setArchived(true);
                        userService.updateUser(user);
                        loadAdherents(currentSearch);
                        dialog.dispose();
                    }
                }
            });

            gererInscrBtn.addActionListener(ev -> {
                Window parentWindow = SwingUtilities.getWindowAncestor(this);
                Frame parentFrameInscriptions = parentWindow instanceof Frame ? (Frame) parentWindow : null;
                JDialog inscriptionsDialog = new JDialog(parentFrameInscriptions, "Gérer inscriptions", true);
                inscriptionsDialog.setContentPane(new InscriptionsAdherentAdminPanel(user));
                inscriptionsDialog.setSize(this.getWidth() > 0 ? this.getWidth() : 800, this.getHeight() > 0 ? this.getHeight() : 600);
                inscriptionsDialog.setLocationRelativeTo(this);
                inscriptionsDialog.setVisible(true);
            });

            gererPaiementsBtn.addActionListener(ev -> {
                // Utilise un nom de variable différent pour éviter le conflit
                Window paiementWindow = SwingUtilities.getWindowAncestor(this);
                Frame parentFramePaiement = paiementWindow instanceof Frame ? (Frame) paiementWindow : null;
                JDialog paiementDialog = new JDialog(parentFramePaiement, "Gérer paiements", true);
                paiementDialog.setContentPane(new PaiementsAdherentAdminPanel(user));
                paiementDialog.setSize(this.getWidth(), this.getHeight());
                paiementDialog.setLocationRelativeTo(this);
                paiementDialog.setVisible(true);
            });

            supprimerBtn.addActionListener(ev -> {
                int confirm = JOptionPane.showConfirmDialog(dialog, "Voulez-vous vraiment supprimer cet adhérent ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    userService.deleteUser(user);
                    loadAdherents(currentSearch);
                    dialog.dispose();
                }
            });

            buttonPanel.add(modifierBtn);
            buttonPanel.add(archiverBtn);
            buttonPanel.add(gererInscrBtn);
            buttonPanel.add(gererPaiementsBtn);
            buttonPanel.add(supprimerBtn);

            dialog.add(buttonPanel, BorderLayout.CENTER);
            dialog.pack();
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        });

        topPanel.add(addButton);
        topPanel.add(toggleArchiveButton);
        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(editButton);

        add(topPanel, BorderLayout.NORTH);

        // Colonnes : ID, Username, Nom, Prénom, Email, Téléphone, Statut
        String[] columns = {"ID", "Nom d'utilisateur", "Nom", "Prénom", "Email", "Téléphone", "Statut"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(28);

        // ScrollPane pour la table
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);

        // Charger les adhérents dans la table (par défaut : actifs)
        loadAdherents(null);
    }

    private void searchAdherents(ActionEvent e) {
        String search = searchField.getText().trim();
        currentSearch = search.isEmpty() ? null : search;
        loadAdherents(currentSearch);
    }

    private void loadAdherents(String search) {
        tableModel.setRowCount(0);
        List<User> users = showArchived ? userService.getArchivedUsers() : userService.getNotArchivedUsers();
        for (User user : users) {
            if (user.getRole() == User.Role.adherent) {
                if (search != null) {
                    String searchLower = search.toLowerCase();
                    boolean match = user.getUsername().toLowerCase().contains(searchLower)
                        || user.getLastname().toLowerCase().contains(searchLower)
                        || user.getName().toLowerCase().contains(searchLower)
                        || (user.getEmail() != null && user.getEmail().toLowerCase().contains(searchLower))
                        || (user.getTelephone() != null && user.getTelephone().toLowerCase().contains(searchLower));
                    if (!match) continue;
                }
                tableModel.addRow(new Object[]{
                    user.getUserId(),
                    user.getUsername(),
                    user.getLastname(),
                    user.getName(),
                    user.getEmail(),
                    user.getTelephone(),
                    user.isArchived() ? "Archivé" : "Actif"
                });
            }
        }
    }

    // --- Panel interne pour création d'adhérent ---
    static class CreateAdherentPanel extends JPanel {
        private JTextField usernameField;
        private JPasswordField passwordField;
        private JTextField nomField;
        private JTextField prenomField;
        private JTextField emailField;
        private JTextField telephoneField;
        private JTextField adresseField;
        private JTextField dateNaissanceField;
        private Runnable onSuccess;

        public CreateAdherentPanel(Runnable onSuccess) {
            this.onSuccess = onSuccess;
            setLayout(new BorderLayout(5, 5));
            setPreferredSize(new Dimension(560, 480));

            JPanel formPanel = new JPanel();
            formPanel.setLayout(new GridBagLayout());
            formPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(8, 8, 8, 8);
            gbc.anchor = GridBagConstraints.WEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;

            Dimension fieldSize = new Dimension(340, 32);

            gbc.gridx = 0; gbc.gridy = 0;
            formPanel.add(new JLabel("Nom d'utilisateur:"), gbc);
            usernameField = new JTextField();
            usernameField.setPreferredSize(fieldSize);
            gbc.gridx = 1;
            formPanel.add(usernameField, gbc);

            gbc.gridx = 0; gbc.gridy++;
            formPanel.add(new JLabel("Mot de passe:"), gbc);
            passwordField = new JPasswordField();
            passwordField.setPreferredSize(fieldSize);
            gbc.gridx = 1;
            formPanel.add(passwordField, gbc);

            gbc.gridx = 0; gbc.gridy++;
            formPanel.add(new JLabel("Nom:"), gbc);
            nomField = new JTextField();
            nomField.setPreferredSize(fieldSize);
            gbc.gridx = 1;
            formPanel.add(nomField, gbc);

            gbc.gridx = 0; gbc.gridy++;
            formPanel.add(new JLabel("Prénom:"), gbc);
            prenomField = new JTextField();
            prenomField.setPreferredSize(fieldSize);
            gbc.gridx = 1;
            formPanel.add(prenomField, gbc);

            gbc.gridx = 0; gbc.gridy++;
            formPanel.add(new JLabel("Email:"), gbc);
            emailField = new JTextField();
            emailField.setPreferredSize(fieldSize);
            gbc.gridx = 1;
            formPanel.add(emailField, gbc);

            gbc.gridx = 0; gbc.gridy++;
            formPanel.add(new JLabel("Téléphone:"), gbc);
            telephoneField = new JTextField();
            telephoneField.setPreferredSize(fieldSize);
            gbc.gridx = 1;
            formPanel.add(telephoneField, gbc);

            gbc.gridx = 0; gbc.gridy++;
            formPanel.add(new JLabel("Adresse:"), gbc);
            adresseField = new JTextField();
            adresseField.setPreferredSize(fieldSize);
            gbc.gridx = 1;
            formPanel.add(adresseField, gbc);

            gbc.gridx = 0; gbc.gridy++;
            formPanel.add(new JLabel("Date de nais (YYYY-MM-DD):"), gbc);
            dateNaissanceField = new JTextField();
            dateNaissanceField.setPreferredSize(fieldSize);
            gbc.gridx = 1;
            formPanel.add(dateNaissanceField, gbc);

            gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            JButton confirmButton = new JButton("Créer l'adhérent");
            confirmButton.setBackground(new Color(40, 167, 69));
            confirmButton.setForeground(Color.WHITE);
            confirmButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
            confirmButton.setFocusPainted(false);
            formPanel.add(confirmButton, gbc);

            confirmButton.addActionListener(e -> {
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword());
                String nom = nomField.getText().trim();
                String prenom = prenomField.getText().trim();
                String email = emailField.getText().trim();
                String telephone = telephoneField.getText().trim();
                String adresse = adresseField.getText().trim();
                String dateNaissanceStr = dateNaissanceField.getText().trim();

                if (username.isEmpty() || password.isEmpty() || nom.isEmpty() || prenom.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs obligatoires.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                java.time.LocalDate dateNaissance = null;
                if (!dateNaissanceStr.isEmpty()) {
                    try {
                        dateNaissance = java.time.LocalDate.parse(dateNaissanceStr);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Format de date de naissance invalide. Utilisez YYYY-MM-DD.", "Erreur", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                try {
                    User user = new User();
                    user.setUsername(username);
                    user.setPasswordHash(com.clubsportif.util.PasswordUtils.hashPassword(password)); // Hash du mot de passe
                    user.setLastname(nom);
                    user.setName(prenom);
                    user.setEmail(email);
                    user.setTelephone(telephone);
                    user.setAdresse(adresse);
                    user.setDateNaissance(dateNaissance);
                    user.setRole(User.Role.adherent);
                    user.setArchived(false);
                    user.setDateInscription(java.time.LocalDate.now());

                    UserService service = new UserService();
                    service.createUser(user);

                    JOptionPane.showMessageDialog(this, "Adhérent créé avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);

                    if (onSuccess != null) onSuccess.run();

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
    }
}
