package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
import javax.swing.table.DefaultTableModel;
import java.util.Calendar;

import login.AuthService;
import login.Coloc;
import reservation.Reservation;
import reservation.ReservationService;

public class ReservationGUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private AuthService authService;
    
    private JTextField titreField;
    private JTextArea descriptionArea;
    private JComboBox<String> espaceCombo;
    private JSpinner dateDebutSpinner;
    private JSpinner dateFinSpinner;
    private JButton reserverButton;
    private JButton supprimerButton;
    private JTable reservationsTable;
    private DefaultTableModel tableModel;
    
    public ReservationGUI(AuthService authService) {
        this.authService = authService;
        
        // Configuration de la fenêtre
        setTitle("Système de Réservation");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Création du panel principal avec BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel pour l'ajout de réservations
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Réserver un espace commun"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JLabel titreLabel = new JLabel("Titre:");
        titreField = new JTextField(20);
        
        JLabel descriptionLabel = new JLabel("Description:");
        descriptionArea = new JTextArea(3, 20);
        descriptionArea.setLineWrap(true);
        JScrollPane descScrollPane = new JScrollPane(descriptionArea);
        
        JLabel espaceLabel = new JLabel("Espace:");
        String[] espaces = {"SALLE_DE_BAIN", "MACHINE_A_LAVER", "SALON", "CUISINE", "AUTRE"};
        espaceCombo = new JComboBox<>(espaces);
        
        JLabel dateDebutLabel = new JLabel("Date de début:");
        SpinnerDateModel dateDebutModel = new SpinnerDateModel();
        dateDebutSpinner = new JSpinner(dateDebutModel);
        JSpinner.DateEditor dateDebutEditor = new JSpinner.DateEditor(dateDebutSpinner, "dd/MM/yyyy HH:mm");
        dateDebutSpinner.setEditor(dateDebutEditor);
        
        JLabel dateFinLabel = new JLabel("Date de fin:");
        SpinnerDateModel dateFinModel = new SpinnerDateModel();
        dateFinSpinner = new JSpinner(dateFinModel);
        JSpinner.DateEditor dateFinEditor = new JSpinner.DateEditor(dateFinSpinner, "dd/MM/yyyy HH:mm");
        dateFinSpinner.setEditor(dateFinEditor);
        
        // Initialiser les dates avec la date actuelle + 1h pour la fin
        Calendar cal = Calendar.getInstance();
        dateDebutSpinner.setValue(cal.getTime());
        cal.add(Calendar.HOUR, 1);
        dateFinSpinner.setValue(cal.getTime());
        
        reserverButton = new JButton("Réserver");
        
        // Ajout des composants avec GridBagLayout
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(titreLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(titreField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        formPanel.add(descriptionLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.gridheight = 2;
        formPanel.add(descScrollPane, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        formPanel.add(espaceLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        formPanel.add(espaceCombo, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        formPanel.add(dateDebutLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        formPanel.add(dateDebutSpinner, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        formPanel.add(dateFinLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        formPanel.add(dateFinSpinner, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(reserverButton, gbc);
        
        // Panel pour la liste des réservations
        JPanel listPanel = new JPanel(new BorderLayout(10, 10));
        listPanel.setBorder(BorderFactory.createTitledBorder("Liste des réservations"));
        
        // Création du tableau
        String[] columnNames = {"ID", "Titre", "Espace", "Date début", "Date fin", "Créateur"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        reservationsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(reservationsTable);
        
        supprimerButton = new JButton("Annuler la réservation");
        
        listPanel.add(scrollPane, BorderLayout.CENTER);
        listPanel.add(supprimerButton, BorderLayout.SOUTH);
        
        // Ajout des panels au panel principal
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(listPanel, BorderLayout.CENTER);
        
        // Ajout du panel principal à la fenêtre
        add(mainPanel);
        
        // Ajout des écouteurs d'événements
        reserverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ajouterReservation();
            }
        });
        
        supprimerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                supprimerReservation();
            }
        });
        
        // Chargement initial des données
        chargerReservations();
    }
    
    private void ajouterReservation() {
        try {
            String titre = titreField.getText();
            String description = descriptionArea.getText();
            String espace = (String) espaceCombo.getSelectedItem();
            Date dateDebut = (Date) dateDebutSpinner.getValue();
            Date dateFin = (Date) dateFinSpinner.getValue();
            
            if (titre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez entrer un titre", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (dateDebut.after(dateFin)) {
                JOptionPane.showMessageDialog(this, "La date de début doit être antérieure à la date de fin", 
                                             "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Coloc colocActuel = authService.getColocActuel();
            if (colocActuel == null) {
                JOptionPane.showMessageDialog(this, "Vous devez être connecté pour faire une réservation", 
                                             "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Reservation reservation = ReservationService.creerReservation(
                colocActuel.getColocation(), 
                titre, 
                description, 
                espace, 
                dateDebut, 
                dateFin, 
                colocActuel.getIdentifiant()
            );
            
            if (reservation == null) {
                JOptionPane.showMessageDialog(this, "Impossible de créer la réservation. L'espace est peut-être déjà réservé pour cette période.", 
                                             "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Réinitialisation des champs
            titreField.setText("");
            descriptionArea.setText("");
            espaceCombo.setSelectedIndex(0);
            
            // Mise à jour de l'affichage
            chargerReservations();
            
            JOptionPane.showMessageDialog(this, "Réservation créée avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur: " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void supprimerReservation() {
        int selectedRow = reservationsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une réservation", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Récupérer l'ID de la réservation sélectionnée
        String idReservation = tableModel.getValueAt(selectedRow, 0).toString();
        
        Coloc colocActuel = authService.getColocActuel();
        if (colocActuel == null) {
            JOptionPane.showMessageDialog(this, "Vous devez être connecté pour annuler une réservation", 
                                         "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        boolean success = ReservationService.supprimerReservation(colocActuel.getColocation(), idReservation);
        
        if (success) {
            JOptionPane.showMessageDialog(this, "Réservation annulée avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
            chargerReservations();
        } else {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'annulation de la réservation", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void chargerReservations() {
        // Effacer le tableau
        tableModel.setRowCount(0);
        
        try {
            Coloc colocActuel = authService.getColocActuel();
            if (colocActuel == null) {
                return;
            }
            
            List<Reservation> reservations = ReservationService.getReservationsParColocation(colocActuel.getColocation());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            
            for (Reservation reservation : reservations) {
                Object[] row = {
                    reservation.getId(),
                    reservation.getTitre(),
                    reservation.getEspace(),
                    sdf.format(reservation.getDateDebut()),
                    sdf.format(reservation.getDateFin()),
                    reservation.getCreateur()
                };
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des réservations: " + e.getMessage(), 
                                         "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
