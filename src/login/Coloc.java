package login;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

public class Coloc implements Serializable {
    private static final long serialVersionUID = 1L;
    private String identifiant;
    private String password;
    private transient Colocation colocation;
    
    public Coloc(String identifiant, String password, Colocation colocation) {
        this.identifiant = identifiant;
        this.password = password;
        this.colocation = colocation;
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public Colocation getColocation() {
        return this.colocation;
    }
    
    public void setColocation(Colocation colocation) {
        this.colocation = colocation;
    }   	
}
