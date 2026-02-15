package incidents;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import login.Colocation;
import login.ColocationDatabase;

public class IncidentService {
    
    // Créer un nouvel incident
    public static Incident creerIncident(Colocation colocation, String titre, String description, 
                                        String type, String createur) {
        String idIncident = UUID.randomUUID().toString();
        Incident nouvelIncident = new Incident(idIncident, titre, description, type, createur);
        
        // Ajouter l'incident à la colocation
        if (colocation.getIncidents() == null) {
            colocation.setIncidents(new ArrayList<>());
        }
        colocation.getIncidents().add(nouvelIncident);
        
        // Sauvegarder les changements
        ColocationDatabase.sauvegarder();
        
        return nouvelIncident;
    }
    
    // Récupérer tous les incidents d'une colocation
    public static List<Incident> getIncidentsParColocation(Colocation colocation) {
        return colocation.getIncidents() != null ? colocation.getIncidents() : new ArrayList<>();
    }
    
    // Récupérer un incident par son ID
    public static Incident getIncidentParId(Colocation colocation, String idIncident) {
        if (colocation.getIncidents() == null) {
            return null;
        }
        
        return colocation.getIncidents().stream()
                .filter(i -> i.getId().equals(idIncident))
                .findFirst()
                .orElse(null);
    }
    
    // Marquer un incident comme étant en cours de traitement
    public static boolean marquerIncidentEnCours(Colocation colocation, String idIncident) {
        Incident incident = getIncidentParId(colocation, idIncident);
        if (incident != null) {
            incident.marquerEnCours();
            ColocationDatabase.sauvegarder();
            return true;
        }
        return false;
    }
    
    // Résoudre un incident
    public static boolean resoudreIncident(Colocation colocation, String idIncident, String resolution) {
        Incident incident = getIncidentParId(colocation, idIncident);
        if (incident != null) {
            incident.resoudre(resolution);
            ColocationDatabase.sauvegarder();
            return true;
        }
        return false;
    }
    
    // Récupérer les incidents par statut
    public static List<Incident> getIncidentsParStatut(Colocation colocation, String statut) {
        if (colocation.getIncidents() == null) {
            return new ArrayList<>();
        }
        
        return colocation.getIncidents().stream()
                .filter(i -> i.getStatut().equals(statut))
                .collect(Collectors.toList());
    }
    
    // Récupérer les incidents par type
    public static List<Incident> getIncidentsParType(Colocation colocation, String type) {
        if (colocation.getIncidents() == null) {
            return new ArrayList<>();
        }
        
        return colocation.getIncidents().stream()
                .filter(i -> i.getType().equals(type))
                .collect(Collectors.toList());
    }
    
    // Récupérer les incidents créés par un utilisateur spécifique
    public static List<Incident> getIncidentsParCreateur(Colocation colocation, String createur) {
        if (colocation.getIncidents() == null) {
            return new ArrayList<>();
        }
        
        return colocation.getIncidents().stream()
                .filter(i -> i.getCreateur().equals(createur))
                .collect(Collectors.toList());
    }
}
