package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import login.AuthService;
import login.ServiceDepenses;
import evenements.EvenementService;

public class MainMenuGUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private AuthService authService;
    
    private JButton depensesButton;
    private JButton evenementsButton;
    private JButton incidentsButton;
    private JButton reservationsButton;
    private JButton deconnexionButton;
    private JLabel bienvenuLabel;
    
    public MainMenuGUI(AuthService authService) {
        this.authService = authService;
        
        // Configuration de la fenêtre
        setTitle("Gestion de Colocation - Menu Principal");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Création du panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Panel d'en-tête
        JPanel headerPanel = new JPanel(new BorderLayout());
        bienvenuLabel = new JLabel("Bienvenue, " + authService.getColocActuel().getIdentifiant() + 
                                  " - Colocation: " + authService.getColocActuel().getColocation().getId());
        bienvenuLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(bienvenuLabel, BorderLayout.WEST);
        
        deconnexionButton = new JButton("Déconnexion");
        headerPanel.add(deconnexionButton, BorderLayout.EAST);
        
        // Panel des boutons de menu
        JPanel menuPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        
        depensesButton = new JButton("Gestion des Dépenses");
        depensesButton.setFont(new Font("Arial", Font.PLAIN, 14));
        depensesButton.setIcon(UIManager.getIcon("FileView.fileIcon"));
        
        evenementsButton = new JButton("Gestion des Événements");
        evenementsButton.setFont(new Font("Arial", Font.PLAIN, 14));
        evenementsButton.setIcon(UIManager.getIcon("FileView.directoryIcon"));
        
        incidentsButton = new JButton("Gestion des Incidents");
        incidentsButton.setFont(new Font("Arial", Font.PLAIN, 14));
        incidentsButton.setIcon(UIManager.getIcon("OptionPane.warningIcon"));
        
        reservationsButton = new JButton("Système de Réservation");
        reservationsButton.setFont(new Font("Arial", Font.PLAIN, 14));
        reservationsButton.setIcon(UIManager.getIcon("FileView.computerIcon"));
        
        menuPanel.add(depensesButton);
        menuPanel.add(evenementsButton);
        menuPanel.add(incidentsButton);
        menuPanel.add(reservationsButton);
        
        // Ajout des panels au panel principal
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(menuPanel, BorderLayout.CENTER);
        
        // Ajout du panel principal à la fenêtre
        add(mainPanel);
        
        // Ajout des écouteurs d'événements
        depensesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ouvrirGestionDepenses();
            }
        });
        
        evenementsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ouvrirGestionEvenements();
            }
        });
        
        incidentsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ouvrirGestionIncidents();
            }
        });
        
        reservationsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ouvrirSystemeReservation();
            }
        });
        
        deconnexionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deconnexion();
            }
        });
    }
    
    private void ouvrirGestionDepenses() {
        ServiceDepenses serviceDepenses = new ServiceDepenses(null, authService);
        DepenseGUI depenseGUI = new DepenseGUI(authService, serviceDepenses);
        depenseGUI.setVisible(true);
    }
    
    private void ouvrirGestionEvenements() {
        EvenementService evenementService = new EvenementService();
        EvenementGUI evenementGUI = new EvenementGUI(authService, evenementService);
        evenementGUI.setVisible(true);
    }
    
    private void ouvrirGestionIncidents() {
        IncidentGUI incidentGUI = new IncidentGUI(authService);
        incidentGUI.setVisible(true);
    }
    
    private void ouvrirSystemeReservation() {
        ReservationGUI reservationGUI = new ReservationGUI(authService);
        reservationGUI.setVisible(true);
    }
    
    private void deconnexion() {
        authService.logout();
        this.dispose();
        AuthGUI authGUI = new AuthGUI(authService);
        authGUI.setVisible(true);
    }
}
