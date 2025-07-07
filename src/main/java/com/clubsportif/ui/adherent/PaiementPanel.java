package com.clubsportif.ui.adherent;

import javax.swing.*;
import java.awt.*;

public class PaiementPanel extends JPanel {
    public PaiementPanel() {
        setLayout(new BorderLayout());
        add(new JLabel("Gestion des paiements", SwingConstants.CENTER), BorderLayout.CENTER);
    }
}
