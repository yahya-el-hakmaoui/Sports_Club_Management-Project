package com.clubsportif.ui.admin;

import com.clubsportif.model.User;
import com.clubsportif.service.UserService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class AssistantAdminPanel extends JPanel {
    private JTextField searchField;
    private DefaultTableModel tableModel;
    private JTable table;
    private UserService userService = new UserService();
    private boolean showArchived = false;
    private JButton toggleArchiveButton;
    private String currentSearch = null;

    public AssistantAdminPanel() {
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JButton addButton = new JButton("Ajouter");
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        addButton.setPreferredSize(new Dimension(100, 40));
        addButton.setBackground(new Color(40, 167, 69));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setToolTipText("Ajouter un assistant");

        // Action à compléter selon la logique métier
        addButton.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(this);
            Frame parentFrame = window instanceof Frame ? (Frame) window : null;
            JDialog dialog = new JDialog(parentFrame, "Ajouter un assistant", true);
            dialog.setContentPane(new CreateAssistantPanel(() -> {
                dialog.dispose();
                loadAssistants(currentSearch); // Rafraîchir la liste après ajout
            }));
            dialog.setMinimumSize(new Dimension(600, 520));
            dialog.pack();
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        });

        toggleArchiveButton = new JButton("Voir archives");
        toggleArchiveButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        toggleArchiveButton.setPreferredSize(new Dimension(130, 40));
        toggleArchiveButton.setBackground(new Color(100, 149, 237));
        toggleArchiveButton.setForeground(Color.WHITE);
        toggleArchiveButton.setFocusPainted(false);
        toggleArchiveButton.setToolTipText("Afficher les assistants archivés");

        toggleArchiveButton.addActionListener(e -> {
            showArchived = !showArchived;
            toggleArchiveButton.setText(showArchived ? "Voir actifs" : "Voir archives");
            toggleArchiveButton.setToolTipText(showArchived ? "Afficher les assistants actifs" : "Afficher les assistants archivés");
            loadAssistants(currentSearch);
        });

        searchField = new JTextField(18);
        searchField.setFont(new Font("Segoe UI", Font.BOLD, 16));
        searchField.setPreferredSize(new Dimension(200, 36));
        searchField.addActionListener(this::searchAssistants);

        JButton searchButton = new JButton("Rechercher");
        searchButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        searchButton.setPreferredSize(new Dimension(120, 40));
        searchButton.setBackground(new Color(255, 255, 255));
        searchButton.setFocusPainted(false);
        searchButton.setToolTipText("Rechercher");
        searchButton.addActionListener(this::searchAssistants);

        JButton editButton = new JButton("Éditer assistant");
        editButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        editButton.setPreferredSize(new Dimension(170, 40));
        editButton.setBackground(new Color(255, 140, 0));
        editButton.setForeground(Color.WHITE);
        editButton.setFocusPainted(false);
        editButton.setToolTipText("Éditer l'assistant sélectionné");

        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner un assistant à éditer.", "Aucune sélection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int userId = (int) tableModel.getValueAt(selectedRow, 0);
            User user = userService.getUserById(userId);

            Window window = SwingUtilities.getWindowAncestor(this);
            Frame parentFrame = window instanceof Frame ? (Frame) window : null;
            JDialog dialog = new JDialog(parentFrame, "Éditer assistant", true);
            dialog.setLayout(new BorderLayout());
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

            JButton modifierBtn = new JButton("Modifier assistant");
            modifierBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
            modifierBtn.setBackground(new Color(100, 149, 237));
            modifierBtn.setForeground(Color.WHITE);
            modifierBtn.setFocusPainted(false);

            String archiverLabel = user.isArchived() ? "Activer assistant" : "Archiver assistant";
            Color archiverColor = user.isArchived() ? new Color(40, 167, 69) : new Color(255, 193, 7);
            JButton archiverBtn = new JButton(archiverLabel);
            archiverBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
            archiverBtn.setBackground(archiverColor);
            archiverBtn.setForeground(Color.WHITE);
            archiverBtn.setFocusPainted(false);

            JButton supprimerBtn = new JButton("Supprimer assistant");
            supprimerBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
            supprimerBtn.setBackground(new Color(220, 53, 69));
            supprimerBtn.setForeground(Color.WHITE);
            supprimerBtn.setFocusPainted(false);

            // Modifier assistant avec changement de mot de passe
            modifierBtn.addActionListener(ev -> {
                JDialog editDialog = new JDialog(dialog, "Modifier l'assistant", true);
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
                formPanel.add(new JLabel("Date de nais (YYYY-MM-DD):"), gbc);
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

                    JOptionPane.showMessageDialog(editDialog, "Assistant modifié avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    loadAssistants(currentSearch);
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
                    int confirm = JOptionPane.showConfirmDialog(dialog, "Voulez-vous activer cet assistant ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        user.setArchived(false);
                        userService.updateUser(user);
                        loadAssistants(currentSearch);
                        dialog.dispose();
                    }
                } else {
                    int confirm = JOptionPane.showConfirmDialog(dialog, "Voulez-vous vraiment archiver cet assistant ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        user.setArchived(true);
                        userService.updateUser(user);
                        loadAssistants(currentSearch);
                        dialog.dispose();
                    }
                }
            });

            supprimerBtn.addActionListener(ev -> {
                int confirm = JOptionPane.showConfirmDialog(dialog, "Voulez-vous vraiment supprimer cet assistant ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    userService.deleteUser(user);
                    loadAssistants(currentSearch);
                    dialog.dispose();
                }
            });

            buttonPanel.add(modifierBtn);
            buttonPanel.add(archiverBtn);
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

        String[] columns = {"ID", "Nom d'utilisateur", "Nom", "Prénom", "Email", "Téléphone", "Statut"};
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

        loadAssistants(null);
    }

    private void searchAssistants(ActionEvent e) {
        String search = searchField.getText().trim();
        currentSearch = search.isEmpty() ? null : search;
        loadAssistants(currentSearch);
    }

    private void loadAssistants(String search) {
        tableModel.setRowCount(0);
        List<User> users = showArchived ? userService.getArchivedUsers() : userService.getNotArchivedUsers();
        for (User user : users) {
            if (user.getRole() == User.Role.assistant) {
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

    static class CreateAssistantPanel extends JPanel {
        private JTextField usernameField;
        private JPasswordField passwordField;
        private JTextField nomField;
        private JTextField prenomField;
        private JTextField emailField;
        private JTextField telephoneField;
        private JTextField adresseField;
        private JTextField dateNaissanceField;
        private Runnable onSuccess;

        public CreateAssistantPanel(Runnable onSuccess) {
            this.onSuccess = onSuccess;
            setLayout(new BorderLayout(10, 10));
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
            JButton confirmButton = new JButton("Créer l'assistant");
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
                    user.setRole(User.Role.assistant);
                    user.setArchived(false);
                    user.setDateInscription(java.time.LocalDate.now());

                    UserService service = new UserService();
                    service.createUser(user);

                    JOptionPane.showMessageDialog(this, "Assistant créé avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);

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
