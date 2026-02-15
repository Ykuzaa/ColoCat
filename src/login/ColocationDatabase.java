package login;
import java.io.*;
import java.util.*;

public class ColocationDatabase {
    private static final String FICHIER = "colocation.ser";
    private static Map<String, Colocation> colocations = new HashMap<>();
    
    static {
        chargerDepuisFichier();
    }
    
    public static Colocation getColocation(String id) {
        return colocations.get(id);
    }
    
    public static void enregistrer(Colocation colocation) {
        colocations.putIfAbsent(colocation.getId(), colocation);
    }
    
    public static void sauvegarder() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FICHIER))) {
            out.writeObject(colocations);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @SuppressWarnings("unchecked")
    public static void chargerDepuisFichier() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FICHIER))) {
            colocations = (Map<String, Colocation>) in.readObject();
            for (Colocation c : colocations.values()) {
                for (Coloc coloc : c.getMembres()) {
                    coloc.setColocation(c);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            colocations = new HashMap<>();
        }
    }
    
    public static Collection<Colocation> getToutesLesColocations() {
        return colocations.values();
    }
}
