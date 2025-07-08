package com.clubsportif.ui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    // Identifiants des cards
    public static final String LOGIN_PANEL = "loginPanel";

    public MainFrame() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Gestion Club Sportif");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 650);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // On instancie les panels (LoginPanel doit accepter MainFrame pour callback)
        LoginPanel loginPanel = new LoginPanel(this);

        mainPanel.add(loginPanel, LOGIN_PANEL);

        getContentPane().add(mainPanel);

        // Afficher login par défaut
        cardLayout.show(mainPanel, LOGIN_PANEL);
    }

    // Permet de changer la vue principale
    public void showPanel(String panelName, JPanel panelInstance) {
        if (mainPanel.getComponentCount() > 0) {
            // Si panel déjà ajouté, on l'ignore
            for (Component comp : mainPanel.getComponents()) {
                if (panelInstance == comp) {
                    cardLayout.show(mainPanel, panelName);
                    return;
                }
            }
        }
        mainPanel.add(panelInstance, panelName);
        cardLayout.show(mainPanel, panelName);
    }
}
