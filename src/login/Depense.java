package login;
import java.io.Serializable;
import java.util.Date;

public class Depense implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id; // Identifiant de la depense generee automatiquement 
    private String description; // Description de la depense
    private double montant;
    private String categorie;
    private String idPayeur; // Identifiant du colocataire qui a payé
    private Date date;
    private boolean regle; // Identifiant qui montre si la depense est paye par tout les colocataires 

    public Depense(String id, String description, double montant, String categorie, String idPayeur, Date date) {
        this.id = id;
        this.description = description;
        this.montant = montant;
        this.categorie = categorie;
        this.idPayeur = idPayeur;
        this.date = date;
        this.regle = false;
    }

    // Getters et Setters
    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public double getMontant() {
        return montant;
    }

    public String getCategorie() {
        return categorie;
    }

    public String getIdPayeur() {
        return idPayeur;
    }

    public Date getDate() {
        return date;
    }

    public boolean estReglee() {
        return regle;
    }

    public void setReglee(boolean regle) {
        this.regle = regle;
    }
}
