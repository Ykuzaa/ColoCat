package evenements;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class EvenementService {
    private static int compteurId = 1; // on l'utilise pour créer des identifiants
                                       // uniques des événements
    private List<Evenement> evenements;
    
    public EvenementService() {
        this.evenements = new ArrayList<>();
        // Ajouter quelques événements de test
        ajouterEvenementTest();
    }
    
    private void ajouterEvenementTest() {
        // Événements de test pour démonstration
        Date maintenant = new Date();
        Date demain = new Date(maintenant.getTime() + 86400000); // +24h
        Date aprèsDemain = new Date(maintenant.getTime() + 172800000); // +48h
        
        creerEvenement("Repas partagé", "Repas italien préparé par Marie", maintenant, demain, "admin", "REPAS");
        creerEvenement("Nettoyage salon", "Grand ménage du salon et de la cuisine", demain, aprèsDemain, "user", "TACHE_MENAGERE");
        creerEvenement("Soirée jeux", "Soirée jeux de société, chacun apporte une boisson", aprèsDemain, new Date(aprèsDemain.getTime() + 18000000), "admin", "SOIREE");
    }
    
    public Evenement creerEvenement(String titre, String description, Date dateDebut, Date dateFin, String createur, String type) {
        Evenement nouvelEvenement = new Evenement(compteurId++, titre, description, dateDebut, dateFin, createur, type);
        evenements.add(nouvelEvenement);
        return nouvelEvenement;
    }
    
    public boolean supprimerEvenement(int id) {
        return evenements.removeIf(e -> e.getId() == id);
    }
    
    public List<Evenement> getTousEvenements() {
        return new ArrayList<>(evenements);
    }
    
    public List<Evenement> getEvenementsParType(String type) {
        return evenements.stream()
                .filter(e -> e.getType().equals(type))
                .collect(Collectors.toList());
    }
    
    public List<Evenement> getEvenementsParCreateur(String createur) {
        return evenements.stream()
                .filter(e -> e.getCreateur().equals(createur))
                .collect(Collectors.toList());
    }
    
    public List<Evenement> getEvenementsFuturs() {
        Date maintenant = new Date();
        return evenements.stream()
                .filter(e -> e.getDateDebut().after(maintenant))
                .collect(Collectors.toList());
    }
    
    public Evenement getEvenementParId(int id) {
        return evenements.stream()
                .filter(e -> e.getId() == id)
                .findFirst()
                .orElse(null);
    }
    
    public boolean modifierEvenement(int id, String titre, String description, Date dateDebut, Date dateFin, String type) {
        Evenement evenement = getEvenementParId(id);
        if (evenement != null) {
            evenement.setTitre(titre);
            evenement.setDescription(description);
            evenement.setDateDebut(dateDebut);
            evenement.setDateFin(dateFin);
            evenement.setType(type);
            return true;
        }
        return false;
    }
}
