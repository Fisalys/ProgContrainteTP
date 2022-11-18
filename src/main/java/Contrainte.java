import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Contrainte {
    private Variable x;
    private Variable y;
    private List<Pair<Integer, Integer>> couples;

    public Contrainte (Variable _x, Variable _y, double durete)
    {
        x = _x;
        y = _y;
        couples = new ArrayList<>();
        //On crée tout les couple de valeurs entre les deux domaines
        for(int i = 0; i < x.getDomaine().size(); i++)
        {
            for(int j = 0; j < y.getDomaine().size(); j++)
            {
                couples.add(new Pair<>(x.getDomaine().get(i), y.getDomaine().get(j)));
            }
        }
        //On supprime maintenant les couples en fonction de la densité.
        int nbCouRemove = (int) (couples.size() -  Math.ceil(couples.size() * durete));
        for(int i = 0; i < nbCouRemove; i++)
        {
            Random r = new Random();
            couples.remove(r.nextInt(couples.size()));
        }

    }

    public Contrainte(Variable _x, Variable _y, List<Pair<Integer, Integer>> _couples)
    {
        x = _x;
        y = _y;
        couples = _couples;
    }

    public Variable getX() {
        return x;
    }

    public void setX(Variable x) {
        this.x = x;
    }

    public Variable getY() {
        return y;
    }

    public void setY(Variable y) {
        this.y = y;
    }

    public List<Pair<Integer, Integer>> getCouples() {
        return couples;
    }

    public void setCouples(List<Pair<Integer, Integer>> couples) {
        this.couples = couples;
    }

    @Override
    public String toString() {
        return "Contrainte{" +
                "x=" + x +
                ", y=" + y +
                ", couples=" + couples +
                "}\n";
    }
}
