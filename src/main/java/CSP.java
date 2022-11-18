import java.util.*;

public class CSP {
    /***
     * ATTRIBUTS
     */
    //Le nombres de variables que le CSP possède
    private int nbVariable;

    //Le nombres de valeurs que peut prendre une variable
    private int nbValeurs;

    //Le nombres de contraintes qu'a le CSP
    private int nbContraintes;

    //Nombre de couple de valeur pour une contraintes (entre 0 et 1)
    private double durete;

    //Nombre de contraintes dans un CSP (entre 0 et 1)
    private double densite;

    //Liste des varibles du CSP.
    private List<Variable> variables;

    //Liste des liens que les variables ont entres-elles.
    private List<Contrainte> contraintes;

    public CSP(int _nbVariables, int _nbValeurs, double _durete, double _densite)
    {
        nbVariable = _nbVariables;
        nbValeurs = _nbValeurs;
        durete = _durete;
        densite = _densite;
        variables = new ArrayList<>();
        contraintes = new ArrayList<>();
        nbContraintes = (nbVariable * (nbVariable - 1) )/ 2;
        for(int i = 1; i <= nbVariable; i++)
        {
            variables.add(new Variable(i, nbValeurs));
        }

        //On crée d'abord toutes les contraintes
        List<Variable> listTemp = new ArrayList<>(variables);
        listTemp.remove(0);
        for(int i = 0; i < variables.size()-1; i++)
        {
            for (Variable variable : listTemp) {
                contraintes.add(new Contrainte(variables.get(i), variable, durete));
            }
            listTemp.remove(0);
        }
        //On supprime maintenant les contraintes en fonction de la densité pour avoir un CSP aléatoire
        int nbConRemove = (int) (contraintes.size() - Math.ceil(contraintes.size()*densite));
        for(int i = 0; i < nbConRemove; i++)
        {
            Random r = new Random();
            contraintes.remove(r.nextInt(contraintes.size()));
            nbContraintes--;
        }
    }

    public CSP(int _nbVariables, int _nbValeurs)
    {
        nbVariable = _nbVariables;
        nbValeurs = _nbValeurs;
        durete = 1;
        densite = 1;
        variables = new ArrayList<>();
        nbContraintes = (nbVariable * (nbVariable - 1) )/ 2;
        for(int i = 1; i <= nbVariable; i++)
        {
            variables.add(new Variable(i, nbValeurs));
        }
    }

    public boolean test(Variable var, int valeur, List<Integer> assign)
    {
        if (!assign.isEmpty() || !var.getDomaine().contains(valeur)) {
            for (Contrainte c : contraintes) {
                for (int i = 0; i< assign.size(); i++) {
                    if (c.getX().getNumero() == i+1 && c.getY().getNumero() == var.getNumero()) {
                        Pair<Integer, Integer> temp = new Pair<>(assign.get(i), valeur);
                        if (!c.getCouples().contains(temp)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public boolean conflit(Variable var, int valeur,int k ,  List<Integer> assign)
    {
        List<Integer> tempList = assign.subList(0, k);
        if (!assign.isEmpty() || !var.getDomaine().contains(valeur)) {
            for (Contrainte c : contraintes) {
                for (int i = 0; i< tempList.size(); i++) {
                    if (c.getX().getNumero() == i+1 && c.getY().getNumero() == var.getNumero()) {
                        Pair<Integer, Integer> temp = new Pair<>(tempList.get(i), valeur);
                        if (!c.getCouples().contains(temp)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public void revise(int k, int i,int x,List<Integer> assign, Map<Integer, Map<Integer, List<Integer>>> historique)
    {
        List<Integer> temp = new ArrayList<>(assign);
        temp.add(i-1, x);
        List<Integer> temp2 = new ArrayList<>();
        List<Integer> domaineCopy = new ArrayList<>(variables.get(k-1).getDomaine());
        for(Integer value: variables.get(k-1).getDomaine())
        {
            if(!test(variables.get(k-1), value, temp)) {
                domaineCopy.remove(value);
                temp2.add(value);
            }

        }
        variables.get(k-1).setDomaine(domaineCopy);
        Map<Integer, List<Integer>> temp3 = historique.get(k);
        temp3.put(i, temp2);
        historique.put(k, temp3);
    }

    public Map<Integer, List<Integer>> generateParent()
    {
        Map<Integer, List<Integer>> parents = new HashMap<>();
        for(Contrainte contrainte: contraintes)
        {
            if(parents.containsKey(contrainte.getY().getNumero()))
            {
                parents.get(contrainte.getY().getNumero()).add(contrainte.getX().getNumero());
            }else {
                List<Integer> temp = new ArrayList<>();
                temp.add(contrainte.getX().getNumero());
                parents.put(contrainte.getY().getNumero(), temp);
            }
        }
        return parents;
    }

    /***
     * GETTER AND SETTER
     */
    public List<Variable> getVariables() {
        return variables;
    }

    public List<Contrainte> getContraintes() {
        return contraintes;
    }

    public int getNbVariable() {
        return nbVariable;
    }

    public void setContraintes(List<Contrainte> contraintes) {
        this.contraintes = contraintes;
    }

    public void setNbVariable(int nbVariable) {
        this.nbVariable = nbVariable;
    }

    public int getNbValeurs() {
        return nbValeurs;
    }

    public void setNbValeurs(int nbValeurs) {
        this.nbValeurs = nbValeurs;
    }

    public int getNbContraintes() {
        return nbContraintes;
    }

    public void setNbContraintes(int nbContraintes) {
        this.nbContraintes = nbContraintes;
    }

    public double getDurete() {
        return durete;
    }

    public void setDurete(int durete) {
        this.durete = durete;
    }

    public double getDensite() {
        return densite;
    }

    public void setDensite(int densite) {
        this.densite = densite;
    }
}
