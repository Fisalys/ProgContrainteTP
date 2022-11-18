import java.util.ArrayList;
import java.util.List;

public class Variable {
    //Permet d'identifier la variables
    private int numero;
    private int nbValeurs;
    //Les valeurs que la variables peut prendre
    private List<Integer> domaine;


    public Variable(int _numero, int _nbValeurs)
    {
        numero = _numero;
        nbValeurs = _nbValeurs;
        domaine = new ArrayList<>();
        for(int i = 1; i <= _nbValeurs; i++)
        {
            domaine.add(i);
        }
    }

    public List<Integer> resetDomaine()
    {
        domaine = new ArrayList<>();
        for(int i = 1; i <= nbValeurs; i++)
        {
            domaine.add(i);
        }
        return domaine;
    }

    public List<Integer> getDomaine() {
        return domaine;
    }

    public int getNumero() {
        return numero;
    }

    public void setDomaine(List<Integer> domaine) {
        this.domaine = domaine;
    }

    @Override
    public String toString() {
        return "Variable{" +
                "numero=" + numero +
                '}';
    }
}
