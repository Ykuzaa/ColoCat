package login;

import java.util.List;
import java.util.Map;

public class ServiceDepenses {

    private final GestionnaireDepenses gestionnaireDepenses;
    private final AuthService serviceAuth;

    public ServiceDepenses(GestionnaireDepenses gestionnaireDepenses, AuthService serviceAuth) {
        this.gestionnaireDepenses = gestionnaireDepenses;
        this.serviceAuth = serviceAuth;
    }

    // Créer une nouvelle dépense
    public void ajouterDepense(String description, double montant, String categorie) {
        Coloc colocActuel = serviceAuth.getColocActuel();
        if (colocActuel == null) {
            throw new IllegalStateException("Aucun utilisateur connecté");
        }
        Colocation coloc = colocActuel.getColocation();
        GestionnaireDepenses.ajouterDepense(coloc, description, montant, categorie, colocActuel.getIdentifiant());
    }

    // Obtenir les dépenses de la colocation de l'utilisateur connecté
    public List<Depense> obtenirDepensesColocActuelle() {
        Coloc colocActuel = serviceAuth.getColocActuel();
        if (colocActuel == null) throw new IllegalStateException("Aucun utilisateur connecté");

        return GestionnaireDepenses.getDepensesParColocation(colocActuel.getColocation());
    }

    // Obtenir les soldes de la colocation actuelle
    public Map<String, Double> calculerSoldesColocActuelle() {
        Coloc colocActuel = serviceAuth.getColocActuel();
        if (colocActuel == null) throw new IllegalStateException("Aucun utilisateur connecté");

        return GestionnaireDepenses.calculerSoldes(colocActuel.getColocation());
    }

    // Régler une dépense (par ID)
    public boolean reglerDepense(String idDepense) {
        Coloc colocActuel = serviceAuth.getColocActuel();
        if (colocActuel == null) throw new IllegalStateException("Aucun utilisateur connecté");

        return GestionnaireDepenses.reglerDepense(colocActuel.getColocation(), idDepense);
    }

    // Obtenir les stats de dépenses par catégorie
    public Map<String, Double> obtenirDepensesParCategorieColocActuelle() {
        Coloc colocActuel = serviceAuth.getColocActuel();
        if (colocActuel == null) throw new IllegalStateException("Aucun utilisateur connecté");

        return GestionnaireDepenses.obtenirDepensesParCategorie(colocActuel.getColocation());
    }
}
