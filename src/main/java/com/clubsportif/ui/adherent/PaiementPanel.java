package com.clubsportif.ui.adherent;

import com.clubsportif.model.Paiement;
import com.clubsportif.model.User;
import com.clubsportif.service.PaiementService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

public class PaiementPanel extends JPanel {
    public PaiementPanel(User user) {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Mes paiements", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        // Ajout de la colonne "Montant partiel"
        String[] columns = {"Montant", "Montant partiel", "Statut", "Période début", "Période fin"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        PaiementService paiementService = new PaiementService();
        List<Paiement> paiements = paiementService.getPaiementsByUser(user);

        // Trier par période de fin décroissante (les plus récentes/actives en premier)
        paiements.sort(Comparator.comparing(Paiement::getPeriodeFin, Comparator.nullsLast(Comparator.reverseOrder())));

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Paiement paiement : paiements) {
            model.addRow(new Object[]{
                paiement.getMontant(),
                paiement.getMontantPartiel(), // Nouvelle colonne
                paiement.getStatut().name(),
                paiement.getPeriodeDebut() != null ? paiement.getPeriodeDebut().format(fmt) : "",
                paiement.getPeriodeFin() != null ? paiement.getPeriodeFin().format(fmt) : ""
            });
        }

        JTable table = new JTable(model);
        table.setRowHeight(28);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }
}
