package reservation;

import java.io.Serializable;
import java.util.Date;

public class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String titre;
    private String description;
    private String espace; // SALLE_DE_BAIN, MACHINE_A_LAVER, SALON, CUISINE, etc.
    private Date dateDebut;
    private Date dateFin;
    private String createur;
    
    public Reservation(String id, String titre, String description, String espace, 
                      Date dateDebut, Date dateFin, String createur) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.espace = espace;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.createur = createur;
    }
    
    // Getters et Setters
    public String getId() {
        return id;
    }
    
    public String getTitre() {
        return titre;
    }
    
    public void setTitre(String titre) {
        this.titre = titre;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getEspace() {
        return espace;
    }
    
    public void setEspace(String espace) {
        this.espace = espace;
    }
    
    public Date getDateDebut() {
        return dateDebut;
    }
    
    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }
    
    public Date getDateFin() {
        return dateFin;
    }
    
    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }
    
    public String getCreateur() {
        return createur;
    }
    
    // Méthodes métier
    public boolean chevauche(Reservation autre) {
        // Vérifie si cette réservation chevauche une autre réservation
        return this.espace.equals(autre.espace) && 
               !(this.dateFin.before(autre.dateDebut) || this.dateDebut.after(autre.dateFin));
    }
    
    public boolean estEnCours() {
        Date maintenant = new Date();
        return !dateDebut.after(maintenant) && !dateFin.before(maintenant);
    }
    
    public boolean estFuture() {
        return dateDebut.after(new Date());
    }
    
    public boolean estPassee() {
        return dateFin.before(new Date());
    }
}
