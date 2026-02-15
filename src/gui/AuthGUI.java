package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import login.AuthService;

public class AuthGUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField identifiantField;
    private JPasswordField passwordField;
    private JTextField idColocationField;
    private JButton loginButton;
    private JButton creerColocationButton;
    private JButton rejoindreColocationButton;
    private AuthService authService;
    
    public AuthGUI(AuthService authService) {
        this.authService = authService;
        
        // Configuration de la fenêtre
        setTitle("Gestion de Colocation - Authentification");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Création des composants
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel identifiantLabel = new JLabel("Identifiant:");
        identifiantField = new JTextField(20);
        
        JLabel passwordLabel = new JLabel("Mot de passe:");
        passwordField = new JPasswordField(20);
        
        JLabel idColocationLabel = new JLabel("ID Colocation:");
        idColocationField = new JTextField(20);
        
        loginButton = new JButton("Se connecter");
        creerColocationButton = new JButton("Créer une colocation");
        rejoindreColocationButton = new JButton("Rejoindre une colocation");
        
        // Ajout des composants au panel
        panel.add(identifiantLabel);
        panel.add(identifiantField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(idColocationLabel);
        panel.add(idColocationField);
        panel.add(new JLabel("")); // Espace vide
        panel.add(loginButton);
        panel.add(new JLabel("")); // Espace vide
        panel.add(creerColocationButton);
        panel.add(new JLabel("")); // Espace vide
        panel.add(rejoindreColocationButton);
        
        // Ajout du panel à la fenêtre
        add(panel);
        
        // Ajout des écouteurs d'événements
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        
        creerColocationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                creerColocation();
            }
        });
        
        rejoindreColocationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rejoindreColocation();
            }
        });
    }
    
    private void login() {
        String identifiant = identifiantField.getText();
        String password = new String(passwordField.getPassword());
        
        if (identifiant.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        boolean success = authService.login(identifiant, password);
        
        if (success) {
            JOptionPane.showMessageDialog(this, "Connexion réussie", "Succès", JOptionPane.INFORMATION_MESSAGE);
            ouvrirMenuPrincipal();
        } else {
            JOptionPane.showMessageDialog(this, "Identifiants incorrects", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void creerColocation() {
        String identifiant = identifiantField.getText();
        String password = new String(passwordField.getPassword());
        String idColocation = idColocationField.getText();
        
        if (identifiant.isEmpty() || password.isEmpty() || idColocation.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        boolean success = authService.creerColocation(idColocation, identifiant, password);
        
        if (success) {
            JOptionPane.showMessageDialog(this, "Colocation créée avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
            ouvrirMenuPrincipal();
        } else {
            JOptionPane.showMessageDialog(this, "Erreur lors de la création de la colocation", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void rejoindreColocation() {
        String identifiant = identifiantField.getText();
        String password = new String(passwordField.getPassword());
        String idColocation = idColocationField.getText();
        
        if (identifiant.isEmpty() || password.isEmpty() || idColocation.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        boolean success = authService.enregistrer(identifiant, password, idColocation);
        
        if (success) {
            JOptionPane.showMessageDialog(this, "Vous avez rejoint la colocation avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
            // Connexion automatique après l'enregistrement
            if (authService.login(identifiant, password)) {
                ouvrirMenuPrincipal();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout à la colocation", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void ouvrirMenuPrincipal() {
        this.dispose(); // Fermer la fenêtre d'authentification
        MainMenuGUI mainMenu = new MainMenuGUI(authService);
        mainMenu.setVisible(true);
    }
}
