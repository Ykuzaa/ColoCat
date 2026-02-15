package login;

import java.util.List;

public class AuthService {

    private Coloc colocActuel;

    public AuthService() {
        colocActuel = null;
    }

    // Connexion d'un utilisateur existant
    public boolean login(String identifiant, String password) {
        for (Colocation colocation : ColocationDatabase.getToutesLesColocations()) {
            for (Coloc c : colocation.getMembres()) {
                if (c.getIdentifiant().equals(identifiant) && c.getPassword().equals(password)) {
                    colocActuel = c;
                    colocActuel.setColocation(colocation); // reconstruire le lien
                    return true;
                }
            }
        }
        return false; // mauvais identifiants
    }

    // Enregistrement d'un nouvel utilisateur (1re connexion)
    public boolean enregistrer(String identifiant, String password, String idColocation) {
        // Vérifie si l'identifiant est déjà utilisé
        for (Colocation colocation : ColocationDatabase.getToutesLesColocations()) {
            if(colocation.getId().equals(idColocation)) {
                Coloc nouveauColoc = new Coloc(identifiant, password, colocation);
                colocation.ajouterMembre(nouveauColoc);
                ColocationDatabase.sauvegarder();
                return true;
            }
        }
        return false;
    }
    
    // Création d'une nouvelle colocation
    public boolean creerColocation(String idColocation, String identifiant, String password) {
        // Vérifier si la colocation existe déjà
        for (Colocation colocation : ColocationDatabase.getToutesLesColocations()) {
            if(colocation.getId().equals(idColocation)) {
                return false; // La colocation existe déjà
            }
        }
        
        // Créer la nouvelle colocation
        Colocation nouvelleColocation = new Colocation(idColocation);
        ColocationDatabase.enregistrer(nouvelleColocation);
        
        // Créer le premier membre (administrateur)
        Coloc nouveauColoc = new Coloc(identifiant, password, nouvelleColocation);
        nouvelleColocation.ajouterMembre(nouveauColoc);
        
        // Sauvegarder les changements
        ColocationDatabase.sauvegarder();
        
        // Connecter l'utilisateur
        colocActuel = nouveauColoc;
        
        return true;
    }

    // Retourne le colocataire connecté
    public Coloc getColocActuel() {
        return colocActuel;
    }

    // Déconnexion
    public void logout() {
        colocActuel = null;
    }

    // Vérifie si un utilisateur est connecté
    public boolean isConnected() {
        return colocActuel != null;
    }
}
