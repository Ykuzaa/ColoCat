package evenements;

import java.util.Date; // les dates des événements

public class Evenement {
    private int id;
    private String titre;
    private String description;
    private Date dateDebut;
    private Date dateFin;
    private String createur;
    private String type; // le type d'événement, par exemple :
                         //"REPAS", "SOIREE", "COURSES", "TACHE_MENAGERE", etc.
    
    public Evenement(int id, String titre, String description, Date dateDebut, Date dateFin, String createur, String type) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.createur = createur;
        this.type = type;
    }
    
    // Requetes (Getters) et Commandes (Setters)
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
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
    
    public void setCreateur(String createur) {
        this.createur = createur;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
}
