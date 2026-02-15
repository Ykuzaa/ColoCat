package main;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import gui.AuthGUI;
import login.AuthService;
import login.ColocationDatabase;

public class Main {
    public static void main(String[] args) {
        // Définir le look and feel natif du système
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Initialiser les services
        AuthService authService = new AuthService();
        
        // Charger les données
        ColocationDatabase.chargerDepuisFichier();
        
        // Lancer l'interface graphique
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                AuthGUI authGUI = new AuthGUI(authService);
                authGUI.setVisible(true);
            }
        });
    }
}
