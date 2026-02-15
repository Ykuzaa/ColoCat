package login;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;
import java.util.Date;

public class GestionnaireDepenses {
    
    // Créer une nouvelle Depense
    public static void ajouterDepense(Colocation colocation, String description, double montant, 
                               String categorie, String idPayeur) {
        String idDepense = UUID.randomUUID().toString(); // Generation d'un id aleatoire automatiquement 
        Depense nouvelleDepense = new Depense(idDepense, description, montant, categorie, idPayeur, new Date());//nouvelle depense 
        colocation.getDepenses().add(nouvelleDepense);
        ColocationDatabase.sauvegarder();
    }

    // Récupérer toutes les Depenses d'une colocation
    public static List<Depense> getDepensesParColocation(Colocation colocation) {
        return colocation.getDepenses();
    }

    // Regler une depense
    public static boolean reglerDepense(Colocation colocation, String idDepense) {
        for(Depense d : colocation.getDepenses()) {
            if(d.getId().equals(idDepense)) {
                d.setReglee(true);
                ColocationDatabase.sauvegarder();
                return true;
            }
        }
        return false;
    }

    // Calculer ce que chaque membre doit dans sa colocation
    public static Map<String, Double> calculerSoldes(Colocation colocation) {
        Map<String, Double> soldes = new HashMap<>();
        List<Coloc> membres = colocation.getMembres();
        List<Depense> depenses = colocation.getDepenses();
        
        // Initialiser les soldes
        for (Coloc membre : membres) {
            soldes.put(membre.getIdentifiant(), 0.0);
        }

        // Calculer les soldes
        for (Depense depense : depenses) {
            if (!depense.estReglee()) {
                double part = depense.getMontant() / membres.size();
                // Le payeur a payé tout le montant, donc on lui crédite (montant - sa part)
                soldes.put(depense.getIdPayeur(), 
                        soldes.get(depense.getIdPayeur()) + (depense.getMontant() - part));
                
                // Les autres membres doivent leur part
                for (Coloc membre : membres) {
                    if (!membre.getIdentifiant().equals(depense.getIdPayeur())) {
                        soldes.put(membre.getIdentifiant(), soldes.get(membre.getIdentifiant()) - part);
                    }
                }
            }
        }

        return soldes;
    }

    // Récupérer les Depenses par categorie
    public static Map<String, Double> obtenirDepensesParCategorie(Colocation colocation) {
        Map<String, Double> parCategorie = new HashMap<>();
        for (Depense depense : colocation.getDepenses()) {
            String categorie = depense.getCategorie();
            double montant = depense.getMontant();
            if (parCategorie.containsKey(categorie)) {
                parCategorie.put(categorie, parCategorie.get(categorie) + montant);
            } else {
                parCategorie.put(categorie, montant);
            }
        }
        return parCategorie;
    }
}
