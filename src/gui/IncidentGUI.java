package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
import javax.swing.table.DefaultTableModel;

import login.AuthService;
import login.Coloc;
import incidents.Incident;
import incidents.IncidentService;

public class IncidentGUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private AuthService authService;
    private IncidentService incidentService;
    
    private JTextField titreField;
    private JTextArea descriptionArea;
    private JComboBox<String> typeCombo;
    private JButton signalerButton;
    private JButton marquerEnCoursButton;
    private JButton resoudreButton;
    private JTable incidentsTable;
    private DefaultTableModel tableModel;
    private JTextArea resolutionArea;
    
    public IncidentGUI(AuthService authService) {
        this.authService = authService;
        
        // Configuration de la fenêtre
        setTitle("Gestion des Incidents");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Création du panel principal avec BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel pour le signalement d'incidents
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Signaler un incident"));
        
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
        String[] types = {"PANNE", "VOISINAGE", "LITIGE", "AUTRE"};
        typeCombo = new JComboBox<>(types);
        
        signalerButton = new JButton("Signaler");
        
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
        
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(signalerButton, gbc);
        
        // Panel pour la liste des incidents
        JPanel listPanel = new JPanel(new BorderLayout(10, 10));
        listPanel.setBorder(BorderFactory.createTitledBorder("Liste des incidents"));
        
        // Création du tableau
        String[] columnNames = {"ID", "Titre", "Type", "Date", "Créateur", "Statut"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        incidentsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(incidentsTable);
        
        // Panel pour les actions sur les incidents
        JPanel actionPanel = new JPanel(new BorderLayout(10, 10));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        marquerEnCoursButton = new JButton("Marquer en cours");
        resoudreButton = new JButton("Résoudre");
        buttonPanel.add(marquerEnCoursButton);
        buttonPanel.add(resoudreButton);
        
        JPanel resolutionPanel = new JPanel(new BorderLayout(5, 5));
        resolutionPanel.setBorder(BorderFactory.createTitledBorder("Résolution"));
        resolutionArea = new JTextArea(3, 20);
        resolutionArea.setLineWrap(true);
        JScrollPane resScrollPane = new JScrollPane(resolutionArea);
        resolutionPanel.add(new JLabel("Description de la résolution:"), BorderLayout.NORTH);
        resolutionPanel.add(resScrollPane, BorderLayout.CENTER);
        
        actionPanel.add(buttonPanel, BorderLayout.NORTH);
        actionPanel.add(resolutionPanel, BorderLayout.CENTER);
        
        listPanel.add(scrollPane, BorderLayout.CENTER);
        listPanel.add(actionPanel, BorderLayout.SOUTH);
        
        // Ajout des panels au panel principal
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(listPanel, BorderLayout.CENTER);
        
        // Ajout du panel principal à la fenêtre
        add(mainPanel);
        
        // Ajout des écouteurs d'événements
        signalerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                signalerIncident();
            }
        });
        
        marquerEnCoursButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                marquerIncidentEnCours();
            }
        });
        
        resoudreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resoudreIncident();
            }
        });
        
        // Chargement initial des données
        chargerIncidents();
    }
    
    private void signalerIncident() {
        try {
            String titre = titreField.getText();
            String description = descriptionArea.getText();
            String type = (String) typeCombo.getSelectedItem();
            
            if (titre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez entrer un titre", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Coloc colocActuel = authService.getColocActuel();
            if (colocActuel == null) {
                JOptionPane.showMessageDialog(this, "Vous devez être connecté pour signaler un incident", 
                                             "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            IncidentService.creerIncident(colocActuel.getColocation(), titre, description, type, colocActuel.getIdentifiant());
            
            // Réinitialisation des champs
            titreField.setText("");
            descriptionArea.setText("");
            typeCombo.setSelectedIndex(0);
            
            // Mise à jour de l'affichage
            chargerIncidents();
            
            JOptionPane.showMessageDialog(this, "Incident signalé avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur: " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void marquerIncidentEnCours() {
        int selectedRow = incidentsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un incident", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Récupérer l'ID de l'incident sélectionné
        String idIncident = tableModel.getValueAt(selectedRow, 0).toString();
        
        Coloc colocActuel = authService.getColocActuel();
        if (colocActuel == null) {
            JOptionPane.showMessageDialog(this, "Vous devez être connecté pour gérer un incident", 
                                         "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        boolean success = IncidentService.marquerIncidentEnCours(colocActuel.getColocation(), idIncident);
        
        if (success) {
            JOptionPane.showMessageDialog(this, "Incident marqué comme en cours", "Succès", JOptionPane.INFORMATION_MESSAGE);
            chargerIncidents();
        } else {
            JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour de l'incident", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void resoudreIncident() {
        int selectedRow = incidentsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un incident", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String resolution = resolutionArea.getText();
        if (resolution.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer une description de la résolution", 
                                         "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Récupérer l'ID de l'incident sélectionné
        String idIncident = tableModel.getValueAt(selectedRow, 0).toString();
        
        Coloc colocActuel = authService.getColocActuel();
        if (colocActuel == null) {
            JOptionPane.showMessageDialog(this, "Vous devez être connecté pour résoudre un incident", 
                                         "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        boolean success = IncidentService.resoudreIncident(colocActuel.getColocation(), idIncident, resolution);
        
        if (success) {
            JOptionPane.showMessageDialog(this, "Incident résolu avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
            resolutionArea.setText("");
            chargerIncidents();
        } else {
            JOptionPane.showMessageDialog(this, "Erreur lors de la résolution de l'incident", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void chargerIncidents() {
        // Effacer le tableau
        tableModel.setRowCount(0);
        
        try {
            Coloc colocActuel = authService.getColocActuel();
            if (colocActuel == null) {
                return;
            }
            
            List<Incident> incidents = IncidentService.getIncidentsParColocation(colocActuel.getColocation());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            
            for (Incident incident : incidents) {
                Object[] row = {
                    incident.getId(),
                    incident.getTitre(),
                    incident.getType(),
                    sdf.format(incident.getDateCreation()),
                    incident.getCreateur(),
                    incident.getStatut()
                };
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des incidents: " + e.getMessage(), 
                                         "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
