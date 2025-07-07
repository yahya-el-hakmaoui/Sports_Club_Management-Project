package com.clubsportif.ui.adherent;

import javax.swing.*;
import java.awt.*;

public class ProfilPanel extends JPanel {
    public ProfilPanel() {
        setLayout(new BorderLayout());
        add(new JLabel("Informations du profil", SwingConstants.CENTER), BorderLayout.CENTER);
    }
}
