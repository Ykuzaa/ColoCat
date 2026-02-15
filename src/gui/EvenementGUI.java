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
import evenements.Evenement;
import evenements.EvenementService;

public class EvenementGUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private AuthService authService;
    private EvenementService evenementService;
    
    private JTextField titreField;
    private JTextArea descriptionArea;
    private JComboBox<String> typeCombo;
    private JSpinner dateDebutSpinner;
    private JSpinner dateFinSpinner;
    private JButton ajouterButton;
    private JButton supprimerButton;
    private JTable evenementsTable;
    private DefaultTableModel tableModel;
    
    public EvenementGUI(AuthService authService, EvenementService evenementService) {
        this.authService = authService;
        this.evenementService = evenementService;
        
        // Configuration de la fenêtre
        setTitle("Gestion des Événements");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Création du panel principal avec BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel pour l'ajout d'événements
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Ajouter un événement"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JLabel titreLabel = new JLabel("Titre:");
        titreField = new JTextField(20);
        
        JLabel descriptionLabel = new JLabel("Description:");
        descriptionArea = new JTextArea(3, 20);
        descriptionArea.setLineWrap(true);
        JScrollPane descScrollPane = new JScrollPane(descriptionArea);
        
        JLabel typeLabel = new JLabel("Type:");
        String[] types = {"REPAS", "SOIREE", "COURSES", "TACHE_MENAGERE", "AUTRE"};
        typeCombo = new JComboBox<>(types);
        
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
        
        ajouterButton = new JButton("Ajouter");
        
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
        formPanel.add(typeLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        formPanel.add(typeCombo, gbc);
        
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
        formPanel.add(ajouterButton, gbc);
        
        // Panel pour la liste des événements
        JPanel listPanel = new JPanel(new BorderLayout(10, 10));
        listPanel.setBorder(BorderFactory.createTitledBorder("Liste des événements"));
        
        // Création du tableau
        String[] columnNames = {"ID", "Titre", "Type", "Date début", "Date fin", "Créateur"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        evenementsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(evenementsTable);
        
        supprimerButton = new JButton("Supprimer");
        
        listPanel.add(scrollPane, BorderLayout.CENTER);
        listPanel.add(supprimerButton, BorderLayout.SOUTH);
        
        // Ajout des panels au panel principal
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(listPanel, BorderLayout.CENTER);
        
        // Ajout du panel principal à la fenêtre
        add(mainPanel);
        
        // Ajout des écouteurs d'événements
        ajouterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ajouterEvenement();
            }
        });
        
        supprimerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                supprimerEvenement();
            }
        });
        
        // Chargement initial des données
        chargerEvenements();
    }
    
    private void ajouterEvenement() {
        try {
            String titre = titreField.getText();
            String description = descriptionArea.getText();
            String type = (String) typeCombo.getSelectedItem();
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
            
            String createur = authService.getColocActuel().getIdentifiant();
            
            evenementService.creerEvenement(titre, description, dateDebut, dateFin, createur, type);
            
            // Réinitialisation des champs
            titreField.setText("");
            descriptionArea.setText("");
            typeCombo.setSelectedIndex(0);
            
            // Mise à jour de l'affichage
            chargerEvenements();
            
            JOptionPane.showMessageDialog(this, "Événement ajouté avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur: " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void supprimerEvenement() {
        int selectedRow = evenementsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un événement", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Récupérer l'ID de l'événement sélectionné
        int idEvenement = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
        
        boolean success = evenementService.supprimerEvenement(idEvenement);
        
        if (success) {
            JOptionPane.showMessageDialog(this, "Événement supprimé avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
            chargerEvenements();
        } else {
            JOptionPane.showMessageDialog(this, "Erreur lors de la suppression de l'événement", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void chargerEvenements() {
        // Effacer le tableau
        tableModel.setRowCount(0);
        
        try {
            List<Evenement> evenements = evenementService.getTousEvenements();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            
            for (Evenement evenement : evenements) {
                Object[] row = {
                    evenement.getId(),
                    evenement.getTitre(),
                    evenement.getType(),
                    sdf.format(evenement.getDateDebut()),
                    sdf.format(evenement.getDateFin()),
                    evenement.getCreateur()
                };
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des événements: " + e.getMessage(), 
                                         "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
