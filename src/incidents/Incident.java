package incidents;

import java.io.Serializable;
import java.util.Date;

public class Incident implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String titre;
    private String description;
    private String type; // PANNE, VOISINAGE, LITIGE, AUTRE
    private String createur;
    private Date dateCreation;
    private String statut; // SIGNALE, EN_COURS, RESOLU
    private String resolution;
    private Date dateResolution;
    
    public Incident(String id, String titre, String description, String type, String createur) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.type = type;
        this.createur = createur;
        this.dateCreation = new Date();
        this.statut = "SIGNALE";
        this.resolution = "";
        this.dateResolution = null;
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
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getCreateur() {
        return createur;
    }
    
    public Date getDateCreation() {
        return dateCreation;
    }
    
    public String getStatut() {
        return statut;
    }
    
    public void setStatut(String statut) {
        this.statut = statut;
    }
    
    public String getResolution() {
        return resolution;
    }
    
    public void setResolution(String resolution) {
        this.resolution = resolution;
    }
    
    public Date getDateResolution() {
        return dateResolution;
    }
    
    public void setDateResolution(Date dateResolution) {
        this.dateResolution = dateResolution;
    }
    
    // Méthodes métier
    public void marquerEnCours() {
        this.statut = "EN_COURS";
    }
    
    public void resoudre(String resolution) {
        this.statut = "RESOLU";
        this.resolution = resolution;
        this.dateResolution = new Date();
    }
    
    public boolean estResolu() {
        return "RESOLU".equals(this.statut);
    }
}
