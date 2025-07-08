package com.clubsportif.ui.admin;

import com.clubsportif.model.User;
import com.clubsportif.service.UserService;
import com.clubsportif.util.PasswordUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class AdherentsAdminPanel extends JPanel {
    private JTextField searchField;
    private DefaultTableModel tableModel;
    private JTable table;
    private UserService userService = new UserService();
    private boolean showArchived = false; // false = non archivés, true = archivés

    public AdherentsAdminPanel() {
        setLayout(new BorderLayout(10, 10));

        // Top panel: Add button + toggle archived + search bar + search button + edit button
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JButton addButton = new JButton("Ajouter");
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        addButton.setPreferredSize(new Dimension(100, 35));
        addButton.setBackground(new Color(40, 167, 69));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setToolTipText("Ajouter un adhérent");

        // ActionListener pour afficher le panel d'ajout
        addButton.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window instanceof JFrame) {
                JFrame frame = (JFrame) window;
                JDialog dialog = new JDialog(frame, "Ajouter un adhérent", true);

                JPanel panel = new JPanel(new GridBagLayout());
                panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(8, 8, 8, 8);
                gbc.anchor = GridBagConstraints.WEST;
                gbc.fill = GridBagConstraints.HORIZONTAL;

                JLabel usernameLabel = new JLabel("Nom d'utilisateur* :");
                JTextField usernameField = new JTextField(16);

                JLabel passwordLabel = new JLabel("Mot de passe* :");
                JPasswordField passwordField = new JPasswordField(16);

                JLabel nomLabel = new JLabel("Nom* :");
                JTextField nomField = new JTextField(16);

                JLabel prenomLabel = new JLabel("Prénom* :");
                JTextField prenomField = new JTextField(16);

                JLabel emailLabel = new JLabel("Email :");
                JTextField emailField = new JTextField(16);

                JLabel telLabel = new JLabel("Téléphone :");
                JTextField telField = new JTextField(16);

                JLabel adresseLabel = new JLabel("Adresse :");
                JTextField adresseField = new JTextField(16);

                JLabel dateNaissanceLabel = new JLabel("Date de naissance (YYYY-MM-DD) :");
                JTextField dateNaissanceField = new JTextField(16);

                // Placement des champs
                int row = 0;
                gbc.gridx = 0; gbc.gridy = row; panel.add(usernameLabel, gbc);
                gbc.gridx = 1; panel.add(usernameField, gbc);

                gbc.gridx = 0; gbc.gridy = ++row; panel.add(passwordLabel, gbc);
                gbc.gridx = 1; panel.add(passwordField, gbc);

                gbc.gridx = 0; gbc.gridy = ++row; panel.add(nomLabel, gbc);
                gbc.gridx = 1; panel.add(nomField, gbc);

                gbc.gridx = 0; gbc.gridy = ++row; panel.add(prenomLabel, gbc);
                gbc.gridx = 1; panel.add(prenomField, gbc);

                gbc.gridx = 0; gbc.gridy = ++row; panel.add(emailLabel, gbc);
                gbc.gridx = 1; panel.add(emailField, gbc);

                gbc.gridx = 0; gbc.gridy = ++row; panel.add(telLabel, gbc);
                gbc.gridx = 1; panel.add(telField, gbc);

                gbc.gridx = 0; gbc.gridy = ++row; panel.add(adresseLabel, gbc);
                gbc.gridx = 1; panel.add(adresseField, gbc);

                gbc.gridx = 0; gbc.gridy = ++row; panel.add(dateNaissanceLabel, gbc);
                gbc.gridx = 1; panel.add(dateNaissanceField, gbc);

                // Bouton d'enregistrement
                JButton saveButton = new JButton("Enregistrer");
                saveButton.setBackground(new Color(40, 167, 69));
                saveButton.setForeground(Color.WHITE);
                saveButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
                saveButton.setFocusPainted(false);

                gbc.gridx = 0; gbc.gridy = ++row; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
                panel.add(saveButton, gbc);

                saveButton.addActionListener(ev -> {
                    String username = usernameField.getText().trim();
                    String password = new String(passwordField.getPassword()).trim();
                    String nom = nomField.getText().trim();
                    String prenom = prenomField.getText().trim();
                    String email = emailField.getText().trim();
                    String tel = telField.getText().trim();
                    String adresse = adresseField.getText().trim();
                    String dateNaissanceStr = dateNaissanceField.getText().trim();

                    if (username.isEmpty() || password.isEmpty() || nom.isEmpty() || prenom.isEmpty()) {
                        JOptionPane.showMessageDialog(dialog, "Veuillez remplir tous les champs obligatoires (*).", "Erreur", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    LocalDate dateNaissance = null;
                    if (!dateNaissanceStr.isEmpty()) {
                        try {
                            dateNaissance = LocalDate.parse(dateNaissanceStr);
                        } catch (DateTimeParseException ex) {
                            JOptionPane.showMessageDialog(dialog, "Format de date de naissance invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }

                    // Hash du mot de passe avec PasswordUtils
                    String hashedPassword = PasswordUtils.hashPassword(password);

                    User adherent = new User();
                    adherent.setUsername(username);
                    adherent.setPasswordHash(hashedPassword);
                    adherent.setRole(User.Role.adherent);
                    adherent.setLastname(nom);
                    adherent.setName(prenom);
                    adherent.setEmail(email);
                    adherent.setTelephone(tel);
                    adherent.setAdresse(adresse);
                    adherent.setDateNaissance(dateNaissance);
                    adherent.setDateInscription(LocalDate.now());
                    adherent.setArchived(false);

                    try {
                        userService.createUser(adherent);
                        JOptionPane.showMessageDialog(dialog, "Adhérent ajouté avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                        dialog.dispose();
                        loadAdherents(null);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(dialog, "Erreur lors de l'ajout : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                });

                dialog.setContentPane(panel);
                dialog.pack();
                dialog.setLocationRelativeTo(frame);
                dialog.setVisible(true);
            }
        });

        // Bouton bleu pour permuter l'affichage archivés/actifs
        JButton toggleArchivedButton = new JButton("Voir Archivés");
        toggleArchivedButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        toggleArchivedButton.setPreferredSize(new Dimension(130, 35));
        toggleArchivedButton.setBackground(new Color(100, 149, 237));
        toggleArchivedButton.setForeground(Color.WHITE);
        toggleArchivedButton.setFocusPainted(false);
        toggleArchivedButton.setToolTipText("Permuter l'affichage des adhérents archivés ou actifs");

        toggleArchivedButton.addActionListener(e -> {
            showArchived = !showArchived;
            toggleArchivedButton.setText(showArchived ? "Voir Actifs" : "Voir Archivés");
            loadAdherents(searchField.getText().trim().isEmpty() ? null : searchField.getText().trim());
        });

        searchField = new JTextField(12); // largeur réduite
        searchField.setFont(new Font("Segoe UI", Font.BOLD, 16));
        searchField.setPreferredSize(new Dimension(120, 35));
        searchField.addActionListener(this::searchAdherents);

        JButton searchButton = new JButton("Rechercher");
        searchButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        searchButton.setPreferredSize(new Dimension(120, 35));
        searchButton.setBackground(Color.WHITE);
        searchButton.setFocusPainted(false);
        searchButton.setToolTipText("Rechercher");
        searchButton.addActionListener(this::searchAdherents);

        JButton editButton = new JButton("Éditer adhérent");
        editButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        editButton.setPreferredSize(new Dimension(160, 35));
        editButton.setBackground(new Color(255, 140, 0));
        editButton.setForeground(Color.WHITE);
        editButton.setFocusPainted(false);
        editButton.setToolTipText("Modifier l'adhérent s��lectionné");

        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner un adhérent à éditer.", "Avertissement", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int userId = (int) tableModel.getValueAt(selectedRow, 0);
            User user = userService.getUserById(userId);
            if (user == null) {
                JOptionPane.showMessageDialog(this, "Adhérent introuvable.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window instanceof JFrame) {
                JFrame frame = (JFrame) window;
                JDialog dialog = new JDialog(frame, "Gestion de l'adhérent", true);

                JPanel buttonPanel = new JPanel();
                buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
                buttonPanel.setBorder(BorderFactory.createEmptyBorder(24, 32, 24, 32));
                buttonPanel.setBackground(Color.WHITE);

                JButton modifierButton = new JButton("Modifier adhérent");
                modifierButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
                modifierButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                modifierButton.setBackground(new Color(100, 149, 237));
                modifierButton.setForeground(Color.WHITE);
                modifierButton.setFocusPainted(false);
                modifierButton.setMaximumSize(new Dimension(220, 40));
                modifierButton.setPreferredSize(new Dimension(220, 40));
                modifierButton.addActionListener(ev -> {
                    // Panel de modification d'adhérent
                    JDialog modifDialog = new JDialog(frame, "Modifier un adhérent", true);

                    JPanel panel = new JPanel(new GridBagLayout());
                    panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
                    GridBagConstraints gbc = new GridBagConstraints();
                    gbc.insets = new Insets(8, 8, 8, 8);
                    gbc.anchor = GridBagConstraints.WEST;
                    gbc.fill = GridBagConstraints.HORIZONTAL;

                    JLabel usernameLabel = new JLabel("Nom d'utilisateur* :");
                    JTextField usernameField = new JTextField(user.getUsername(), 16);

                    JLabel passwordLabel = new JLabel("Nouveau mot de passe :");
                    JPasswordField passwordField = new JPasswordField(16);

                    JLabel nomLabel = new JLabel("Nom* :");
                    JTextField nomField = new JTextField(user.getLastname(), 16);

                    JLabel prenomLabel = new JLabel("Prénom* :");
                    JTextField prenomField = new JTextField(user.getName(), 16);

                    JLabel emailLabel = new JLabel("Email :");
                    JTextField emailField = new JTextField(user.getEmail() != null ? user.getEmail() : "", 16);

                    JLabel telLabel = new JLabel("Téléphone :");
                    JTextField telField = new JTextField(user.getTelephone() != null ? user.getTelephone() : "", 16);

                    JLabel adresseLabel = new JLabel("Adresse :");
                    JTextField adresseField = new JTextField(user.getAdresse() != null ? user.getAdresse() : "", 16);

                    JLabel dateNaissanceLabel = new JLabel("Date de naissance (YYYY-MM-DD) :");
                    JTextField dateNaissanceField = new JTextField(user.getDateNaissance() != null ? user.getDateNaissance().toString() : "", 16);

                    // Placement des champs
                    int row = 0;
                    gbc.gridx = 0; gbc.gridy = row; panel.add(usernameLabel, gbc);
                    gbc.gridx = 1; panel.add(usernameField, gbc);

                    gbc.gridx = 0; gbc.gridy = ++row; panel.add(passwordLabel, gbc);
                    gbc.gridx = 1; panel.add(passwordField, gbc);

                    gbc.gridx = 0; gbc.gridy = ++row; panel.add(nomLabel, gbc);
                    gbc.gridx = 1; panel.add(nomField, gbc);

                    gbc.gridx = 0; gbc.gridy = ++row; panel.add(prenomLabel, gbc);
                    gbc.gridx = 1; panel.add(prenomField, gbc);

                    gbc.gridx = 0; gbc.gridy = ++row; panel.add(emailLabel, gbc);
                    gbc.gridx = 1; panel.add(emailField, gbc);

                    gbc.gridx = 0; gbc.gridy = ++row; panel.add(telLabel, gbc);
                    gbc.gridx = 1; panel.add(telField, gbc);

                    gbc.gridx = 0; gbc.gridy = ++row; panel.add(adresseLabel, gbc);
                    gbc.gridx = 1; panel.add(adresseField, gbc);

                    gbc.gridx = 0; gbc.gridy = ++row; panel.add(dateNaissanceLabel, gbc);
                    gbc.gridx = 1; panel.add(dateNaissanceField, gbc);

                    // Bouton d'enregistrement
                    JButton saveButton = new JButton("Enregistrer");
                    saveButton.setBackground(new Color(100, 149, 237));
                    saveButton.setForeground(Color.WHITE);
                    saveButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
                    saveButton.setFocusPainted(false);

                    gbc.gridx = 0; gbc.gridy = ++row; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
                    panel.add(saveButton, gbc);

                    saveButton.addActionListener(ev2 -> {
                        String username = usernameField.getText().trim();
                        String password = new String(passwordField.getPassword()).trim();
                        String nom = nomField.getText().trim();
                        String prenom = prenomField.getText().trim();
                        String email = emailField.getText().trim();
                        String tel = telField.getText().trim();
                        String adresse = adresseField.getText().trim();
                        String dateNaissanceStr = dateNaissanceField.getText().trim();

                        if (username.isEmpty() || nom.isEmpty() || prenom.isEmpty()) {
                            JOptionPane.showMessageDialog(modifDialog, "Veuillez remplir tous les champs obligatoires (*).", "Erreur", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        LocalDate dateNaissance = null;
                        if (!dateNaissanceStr.isEmpty()) {
                            try {
                                dateNaissance = LocalDate.parse(dateNaissanceStr);
                            } catch (DateTimeParseException ex) {
                                JOptionPane.showMessageDialog(modifDialog, "Format de date de naissance invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        }

                        user.setUsername(username);
                        if (!password.isEmpty()) {
                            user.setPasswordHash(PasswordUtils.hashPassword(password));
                        }
                        user.setLastname(nom);
                        user.setName(prenom);
                        user.setEmail(email);
                        user.setTelephone(tel);
                        user.setAdresse(adresse);
                        user.setDateNaissance(dateNaissance);

                        try {
                            userService.updateUser(user);
                            JOptionPane.showMessageDialog(modifDialog, "Adhérent modifié avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                            modifDialog.dispose();
                            dialog.dispose();
                            loadAdherents(null);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(modifDialog, "Erreur lors de la modification : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                        }
                    });

                    modifDialog.setContentPane(panel);
                    modifDialog.pack();
                    modifDialog.setLocationRelativeTo(frame);
                    modifDialog.setVisible(true);
                });

                // Nouveau bouton Archiver/Restaurer
                JButton archiverButton = new JButton(user.isArchived() ? "Restaurer adhérent" : "Archiver adhérent");
                archiverButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
                archiverButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                archiverButton.setBackground(new Color(70, 130, 180));
                archiverButton.setForeground(Color.WHITE);
                archiverButton.setFocusPainted(false);
                archiverButton.setMaximumSize(new Dimension(220, 40));
                archiverButton.setPreferredSize(new Dimension(220, 40));
                archiverButton.addActionListener(ev -> {
                    boolean toArchive = !user.isArchived();
                    String action = toArchive ? "archiver" : "restaurer";
                    int confirm = JOptionPane.showConfirmDialog(dialog,
                            "Voulez-vous vraiment " + action + " cet adhérent ?",
                            (toArchive ? "Archiver" : "Restaurer") + " l'adhérent",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE);
                    if (confirm == JOptionPane.YES_OPTION) {
                        user.setArchived(toArchive);
                        try {
                            userService.updateUser(user);
                            JOptionPane.showMessageDialog(dialog,
                                    toArchive ? "Adhérent archivé." : "Adhérent restauré.",
                                    "Information", JOptionPane.INFORMATION_MESSAGE);
                            dialog.dispose();
                            loadAdherents(null);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(dialog, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });

                JButton inscriptionButton = new JButton("Gérer les inscriptions");
                inscriptionButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
                inscriptionButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                inscriptionButton.setBackground(new Color(60, 179, 113));
                inscriptionButton.setForeground(Color.WHITE);
                inscriptionButton.setFocusPainted(false);
                inscriptionButton.setMaximumSize(new Dimension(220, 40));
                inscriptionButton.setPreferredSize(new Dimension(220, 40));
                inscriptionButton.addActionListener(ev -> {
                    // À compléter : ouvrir la gestion des inscriptions pour cet adhérent
                    JOptionPane.showMessageDialog(dialog, "Gestion des inscriptions à implémenter.", "Info", JOptionPane.INFORMATION_MESSAGE);
                });

                JButton paiementButton = new JButton("Gérer les paiements");
                paiementButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
                paiementButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                paiementButton.setBackground(new Color(255, 193, 7));
                paiementButton.setForeground(Color.WHITE);
                paiementButton.setFocusPainted(false);
                paiementButton.setMaximumSize(new Dimension(220, 40));
                paiementButton.setPreferredSize(new Dimension(220, 40));
                paiementButton.addActionListener(ev -> {
                    // À compléter : ouvrir la gestion des paiements pour cet adhérent
                    JOptionPane.showMessageDialog(dialog, "Gestion des paiements à implémenter.", "Info", JOptionPane.INFORMATION_MESSAGE);
                });

                JButton supprimerButton = new JButton("Supprimer adhérent");
                supprimerButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
                supprimerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                supprimerButton.setBackground(new Color(220, 53, 69));
                supprimerButton.setForeground(Color.WHITE);
                supprimerButton.setFocusPainted(false);
                supprimerButton.setMaximumSize(new Dimension(220, 40));
                supprimerButton.setPreferredSize(new Dimension(220, 40));
                supprimerButton.addActionListener(ev -> {
                    int confirm = JOptionPane.showConfirmDialog(dialog,
                            "Êtes-vous sûr de vouloir supprimer cet adhérent ?",
                            "Confirmation de suppression",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE);
                    if (confirm == JOptionPane.YES_OPTION) {
                        try {
                            userService.deleteUser(user);
                            JOptionPane.showMessageDialog(dialog, "Adhérent supprimé.", "Suppression", JOptionPane.INFORMATION_MESSAGE);
                            dialog.dispose();
                            loadAdherents(null);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(dialog, "Erreur lors de la suppression : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });

                buttonPanel.add(modifierButton);
                buttonPanel.add(Box.createVerticalStrut(18));
                buttonPanel.add(archiverButton);
                buttonPanel.add(Box.createVerticalStrut(18));
                buttonPanel.add(inscriptionButton);
                buttonPanel.add(Box.createVerticalStrut(18));
                buttonPanel.add(paiementButton);
                buttonPanel.add(Box.createVerticalStrut(18));
                buttonPanel.add(supprimerButton);

                dialog.setContentPane(buttonPanel);
                dialog.pack();
                dialog.setLocationRelativeTo(frame);
                dialog.setVisible(true);
            }
        });

        topPanel.add(addButton);
        topPanel.add(toggleArchivedButton);
        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(editButton);

        add(topPanel, BorderLayout.NORTH);

        // Table avec colonnes principales (username déplacé entre ID et Nom)
        String[] columns = {"ID", "Nom d'utilisateur", "Nom", "Prénom", "Email", "Téléphone", "Archivée"};
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

        loadAdherents(null);
    }

    private void searchAdherents(ActionEvent e) {
        String search = searchField.getText().trim();
        loadAdherents(search.isEmpty() ? null : search);
    }

    private void loadAdherents(String search) {
        tableModel.setRowCount(0);
        List<User> users = userService.getAllUsers();
        for (User user : users) {
            if (user.getRole() != User.Role.adherent) continue;
            if (user.isArchived() != showArchived) continue;
            if (search != null) {
                String nom = user.getLastname() != null ? user.getLastname().toLowerCase() : "";
                String prenom = user.getName() != null ? user.getName().toLowerCase() : "";
                String email = user.getEmail() != null ? user.getEmail().toLowerCase() : "";
                String username = user.getUsername() != null ? user.getUsername().toLowerCase() : "";
                if (!nom.contains(search.toLowerCase()) && !prenom.contains(search.toLowerCase())
                        && !email.contains(search.toLowerCase()) && !username.contains(search.toLowerCase())) {
                    continue;
                }
            }
            tableModel.addRow(new Object[]{
                user.getUserId(),
                user.getUsername(),
                user.getLastname(),
                user.getName(),
                user.getEmail(),
                user.getTelephone(),
                user.isArchived() ? "Oui" : "Non"
            });
        }
    }
}
