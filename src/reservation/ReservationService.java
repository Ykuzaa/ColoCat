package reservation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import login.Colocation;
import login.ColocationDatabase;

public class ReservationService {
    
    // Créer une nouvelle réservation
    public static Reservation creerReservation(Colocation colocation, String titre, String description, 
                                             String espace, Date dateDebut, Date dateFin, String createur) {
        // Vérifier si la réservation est possible (pas de chevauchement)
        if (!estReservationPossible(colocation, espace, dateDebut, dateFin)) {
            return null; // Réservation impossible (chevauchement)
        }
        
        String idReservation = UUID.randomUUID().toString();
        Reservation nouvelleReservation = new Reservation(idReservation, titre, description, 
                                                        espace, dateDebut, dateFin, createur);
        
        // Ajouter la réservation à la colocation
        if (colocation.getReservations() == null) {
            colocation.setReservations(new ArrayList<>());
        }
        colocation.getReservations().add(nouvelleReservation);
        
        // Sauvegarder les changements
        ColocationDatabase.sauvegarder();
        
        return nouvelleReservation;
    }
    
    // Vérifier si une réservation est possible (pas de chevauchement)
    public static boolean estReservationPossible(Colocation colocation, String espace, 
                                               Date dateDebut, Date dateFin) {
        if (colocation.getReservations() == null) {
            return true; // Aucune réservation existante
        }
        
        // Créer une réservation temporaire pour vérifier les chevauchements
        Reservation temp = new Reservation("temp", "", "", espace, dateDebut, dateFin, "");
        
        // Vérifier s'il y a des chevauchements avec les réservations existantes
        for (Reservation r : colocation.getReservations()) {
            if (temp.chevauche(r)) {
                return false; // Chevauchement détecté
            }
        }
        
        return true; // Pas de chevauchement
    }
    
    // Récupérer toutes les réservations d'une colocation
    public static List<Reservation> getReservationsParColocation(Colocation colocation) {
        return colocation.getReservations() != null ? colocation.getReservations() : new ArrayList<>();
    }
    
    // Récupérer une réservation par son ID
    public static Reservation getReservationParId(Colocation colocation, String idReservation) {
        if (colocation.getReservations() == null) {
            return null;
        }
        
        return colocation.getReservations().stream()
                .filter(r -> r.getId().equals(idReservation))
                .findFirst()
                .orElse(null);
    }
    
    // Supprimer une réservation
    public static boolean supprimerReservation(Colocation colocation, String idReservation) {
        if (colocation.getReservations() == null) {
            return false;
        }
        
        boolean supprime = colocation.getReservations().removeIf(r -> r.getId().equals(idReservation));
        if (supprime) {
            ColocationDatabase.sauvegarder();
        }
        return supprime;
    }
    
    // Récupérer les réservations par espace
    public static List<Reservation> getReservationsParEspace(Colocation colocation, String espace) {
        if (colocation.getReservations() == null) {
            return new ArrayList<>();
        }
        
        return colocation.getReservations().stream()
                .filter(r -> r.getEspace().equals(espace))
                .collect(Collectors.toList());
    }
    
    // Récupérer les réservations par créateur
    public static List<Reservation> getReservationsParCreateur(Colocation colocation, String createur) {
        if (colocation.getReservations() == null) {
            return new ArrayList<>();
        }
        
        return colocation.getReservations().stream()
                .filter(r -> r.getCreateur().equals(createur))
                .collect(Collectors.toList());
    }
    
    // Récupérer les réservations futures
    public static List<Reservation> getReservationsFutures(Colocation colocation) {
        if (colocation.getReservations() == null) {
            return new ArrayList<>();
        }
        
        return colocation.getReservations().stream()
                .filter(Reservation::estFuture)
                .collect(Collectors.toList());
    }
    
    // Récupérer les réservations en cours
    public static List<Reservation> getReservationsEnCours(Colocation colocation) {
        if (colocation.getReservations() == null) {
            return new ArrayList<>();
        }
        
        return colocation.getReservations().stream()
                .filter(Reservation::estEnCours)
                .collect(Collectors.toList());
    }
    
    // Récupérer les réservations pour une période donnée
    public static List<Reservation> getReservationsPourPeriode(Colocation colocation, 
                                                             Date debut, Date fin) {
        if (colocation.getReservations() == null) {
            return new ArrayList<>();
        }
        
        return colocation.getReservations().stream()
                .filter(r -> !r.getDateFin().before(debut) && !r.getDateDebut().after(fin))
                .collect(Collectors.toList());
    }
}
