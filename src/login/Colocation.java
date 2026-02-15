package login;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import incidents.Incident;
import reservation.Reservation;

public class Colocation implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private List<Coloc> membres;
    private List<Depense> depenses;
    private List<Incident> incidents;
    private List<Reservation> reservations;

    public Colocation(String id) {
        this.id = id;
        this.membres = new ArrayList<>();
        this.depenses = new ArrayList<>();
        this.incidents = new ArrayList<>();
        this.reservations = new ArrayList<>();
    }

    public String getId() {
        return this.id;
    }

    public List<Coloc> getMembres() {
        return this.membres;
    }
    
    public void ajouterMembre(Coloc membre) {
        for(Coloc c : membres) {
            if(c.getIdentifiant().equals(membre.getIdentifiant())) {
                return;
            }
        }
        this.membres.add(membre);
        membre.setColocation(this);
        ColocationDatabase.sauvegarder();
    }
        
    public List<Depense> getDepenses() {
        return this.depenses;
    }
    
    public List<Incident> getIncidents() {
        return this.incidents;
    }
    
    public void setIncidents(List<Incident> incidents) {
        this.incidents = incidents;
    }
    
    public List<Reservation> getReservations() {
        return this.reservations;
    }
    
    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
}
