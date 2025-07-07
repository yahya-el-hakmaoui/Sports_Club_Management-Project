package com.clubsportif.ui.adherent;

import com.clubsportif.model.User;
import com.clubsportif.model.Inscription;
import com.clubsportif.model.Activity;
import com.clubsportif.service.InscriptionService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ActivityPanel extends JPanel {
    private User user;

    public ActivityPanel(User user) {
        this.user = user;
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Mes activités", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        String[] columns = {"Nom de l'activité", "Tarif", "Description", "Active"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(28);

        // Récupérer les inscriptions actives et inactives de l'utilisateur
        InscriptionService inscriptionService = new InscriptionService();
        List<Inscription> inscriptions = inscriptionService.getActiveInscriptionsByUser(user);
        // Ajoute aussi les inscriptions inactives si besoin
        // (Supposons qu'il existe une méthode getAllInscriptionsByUser sinon à adapter)
        // List<Inscription> inscriptions = inscriptionService.getAllInscriptionsByUser(user);

        for (Inscription inscription : inscriptions) {
            Activity activity = inscription.getActivity();
            model.addRow(new Object[]{
                activity.getNom(),
                activity.getTarif(),
                activity.getDescription(),
                inscription.isActive() ? "Oui" : "Non"
            });
        }

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }
}
