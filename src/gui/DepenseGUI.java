package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Map;
import javax.swing.table.DefaultTableModel;

import login.AuthService;
import login.Coloc;
import login.Depense;
import login.ServiceDepenses;

public class DepenseGUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private AuthService authService;
    private ServiceDepenses serviceDepenses;
    
    private JTextField descriptionField;
    private JTextField montantField;
    private JComboBox<String> categorieCombo;
    private JButton ajouterButton;
    private JButton reglerButton;
    private JTable depensesTable;
    private DefaultTableModel tableModel;
    private JPanel statsPanel;
    
    public DepenseGUI(AuthService authService, ServiceDepenses serviceDepenses) {
        this.authService = authService;
        this.serviceDepenses = serviceDepenses;
        
        // Configuration de la fenêtre
        setTitle("Gestion des Dépenses");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Création du panel principal avec BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel pour l'ajout de dépenses
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Ajouter une dépense"));
        
        JLabel descriptionLabel = new JLabel("Description:");
        descriptionField = new JTextField(20);
        
        JLabel montantLabel = new JLabel("Montant (€):");
        montantField = new JTextField(10);
        
        JLabel categorieLabel = new JLabel("Catégorie:");
        String[] categories = {"Loyer", "Courses", "Factures", "Abonnements", "Autre"};
        categorieCombo = new JComboBox<>(categories);
        
        ajouterButton = new JButton("Ajouter");
        
        formPanel.add(descriptionLabel);
        formPanel.add(descriptionField);
        formPanel.add(montantLabel);
        formPanel.add(montantField);
        formPanel.add(categorieLabel);
        formPanel.add(categorieCombo);
        formPanel.add(new JLabel(""));
        formPanel.add(ajouterButton);
        
        // Panel pour la liste des dépenses
        JPanel listPanel = new JPanel(new BorderLayout(10, 10));
        listPanel.setBorder(BorderFactory.createTitledBorder("Liste des dépenses"));
        
        // Création du tableau
        String[] columnNames = {"Description", "Montant", "Catégorie", "Payé par", "Date", "Réglée"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        depensesTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(depensesTable);
        
        reglerButton = new JButton("Marquer comme réglée");
        
        listPanel.add(scrollPane, BorderLayout.CENTER);
        listPanel.add(reglerButton, BorderLayout.SOUTH);
        
        // Panel pour les statistiques
        statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Statistiques"));
        
        // Ajout des panels au panel principal
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(listPanel, BorderLayout.CENTER);
        mainPanel.add(statsPanel, BorderLayout.EAST);
        
        // Ajout du panel principal à la fenêtre
        add(mainPanel);
        
        // Ajout des écouteurs d'événements
        ajouterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ajouterDepense();
            }
        });
        
        reglerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reglerDepense();
            }
        });
        
        // Chargement initial des données
        chargerDepenses();
        mettreAJourStats();
    }
    
    private void ajouterDepense() {
        try {
            String description = descriptionField.getText();
            double montant = Double.parseDouble(montantField.getText());
            String categorie = (String) categorieCombo.getSelectedItem();
            
            if (description.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez entrer une description", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            serviceDepenses.ajouterDepense(description, montant, categorie);
            
            // Réinitialisation des champs
            descriptionField.setText("");
            montantField.setText("");
            categorieCombo.setSelectedIndex(0);
            
            // Mise à jour de l'affichage
            chargerDepenses();
            mettreAJourStats();
            
            JOptionPane.showMessageDialog(this, "Dépense ajoutée avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer un montant valide", "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur: " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void reglerDepense() {
        int selectedRow = depensesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une dépense", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Récupérer l'ID de la dépense sélectionnée
        String idDepense = (String) tableModel.getValueAt(selectedRow, 0); // Supposons que l'ID est dans la première colonne
        
        boolean success = serviceDepenses.reglerDepense(idDepense);
        
        if (success) {
            JOptionPane.showMessageDialog(this, "Dépense marquée comme réglée", "Succès", JOptionPane.INFORMATION_MESSAGE);
            chargerDepenses();
            mettreAJourStats();
        } else {
            JOptionPane.showMessageDialog(this, "Erreur lors du règlement de la dépense", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void chargerDepenses() {
        // Effacer le tableau
        tableModel.setRowCount(0);
        
        try {
            List<Depense> depenses = serviceDepenses.obtenirDepensesColocActuelle();
            
            for (Depense depense : depenses) {
                Object[] row = {
                    depense.getDescription(),
                    depense.getMontant() + " €",
                    depense.getCategorie(),
                    depense.getIdPayeur(),
                    depense.getDate().toString(),
                    depense.estReglee() ? "Oui" : "Non"
                };
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des dépenses: " + e.getMessage(), 
                                         "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void mettreAJourStats() {
        statsPanel.removeAll();
        
        try {
            // Soldes
            Map<String, Double> soldes = serviceDepenses.calculerSoldesColocActuelle();
            JPanel soldesPanel = new JPanel(new GridLayout(0, 1));
            soldesPanel.setBorder(BorderFactory.createTitledBorder("Soldes"));
            
            for (Map.Entry<String, Double> entry : soldes.entrySet()) {
                String membre = entry.getKey();
                double solde = entry.getValue();
                String texte = membre + ": " + String.format("%.2f", solde) + " €";
                JLabel label = new JLabel(texte);
                if (solde > 0) {
                    label.setForeground(Color.GREEN);
                } else if (solde < 0) {
                    label.setForeground(Color.RED);
                }
                soldesPanel.add(label);
            }
            
            // Dépenses par catégorie
            Map<String, Double> parCategorie = serviceDepenses.obtenirDepensesParCategorieColocActuelle();
            JPanel categoriesPanel = new JPanel(new GridLayout(0, 1));
            categoriesPanel.setBorder(BorderFactory.createTitledBorder("Par catégorie"));
            
            for (Map.Entry<String, Double> entry : parCategorie.entrySet()) {
                String categorie = entry.getKey();
                double montant = entry.getValue();
                String texte = categorie + ": " + String.format("%.2f", montant) + " €";
                categoriesPanel.add(new JLabel(texte));
            }
            
            statsPanel.add(soldesPanel);
            statsPanel.add(Box.createVerticalStrut(20));
            statsPanel.add(categoriesPanel);
            
            statsPanel.revalidate();
            statsPanel.repaint();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour des statistiques: " + e.getMessage(), 
                                         "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
