import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class Main {


   /** public static void main(String[] args) {

        CSP c = generateurCSP(100,5,0.5,0.5, 1).get(0);
        System.out.println("Generation des reines effectuée");
        System.out.println("BackTracking\n");
        long debut = System.currentTimeMillis();
        System.out.println(BJ(c));
        long fin = System.currentTimeMillis();
        double second = (fin-debut) /1000F;
        System.out.println("Temps d'éxécution : "+second);


    }**/

    public static void main(String[] args) {
        List<Double> temps = new ArrayList<>();
        int nbVariable = 100;
        int nbValeur = 5;
        double durete = 0.5;
        double densite = 0.5;
        int nb = 30;
        List<CSP> listCsp = generateurCSP(nbVariable, nbValeur, durete, densite, nb);
        System.out.println("Génération de "+ nb + " CSP aléatoire avec " + nbVariable + " variables de " + nbValeur + " valeurs, " + durete + " de durete et " + densite + " densité");
        for(CSP csp: listCsp)
        {
            long debut = System.currentTimeMillis();
            BT(csp);
            long fin = System.currentTimeMillis();
            double second = (fin-debut) /1000F;
            temps.add(second);
        }
        double temp = 0;
        for(Double d : temps)
            temp += d;
        double moyenne = temp/temps.size();
        System.out.println("Moyenne de calcul d'un CSP : " + moyenne);

    }
   public static List<CSP> generateurCSP(int nbVariable, int nbValeur, double durete, double densite, int nb)
   {
        List<CSP> list = new ArrayList<>();
        for(int i = 0; i < nb; i++)
        {
            list.add(new CSP(nbVariable, nbValeur, durete, densite));
        }
        return list;
   }

   public static CSP generateNQueenProblem(int nbQueen)
   {
        CSP QueenProblem = new CSP(nbQueen, nbQueen);
       //On crée d'abord toutes les contraintes
       List<Variable> listTemp = new ArrayList<>(QueenProblem.getVariables());
       listTemp.remove(0);
       List<Contrainte> contraintes = new ArrayList<>();
       for(int i = 0; i < QueenProblem.getVariables().size()-1; i++)
       {
           for (Variable variable : listTemp) {
               List<Pair<Integer, Integer>> couples = new ArrayList<>();
               for(Integer x: QueenProblem.getVariables().get(i).getDomaine())
               {
                   for (Integer y: variable.getDomaine())
                   {
                       if(!(x.equals(y)) && !(y.equals(x + (variable.getNumero() -
                               QueenProblem.getVariables().get(i).getNumero()))) && !(y.equals(x -
                               (variable.getNumero() - QueenProblem.getVariables().get(i).getNumero()))))
                       {
                           couples.add(new Pair<>(x, y));
                       }
                   }
               }
               contraintes.add(new Contrainte(QueenProblem.getVariables().get(i), variable, couples));
           }
           listTemp.remove(0);
       }
       QueenProblem.setContraintes(contraintes);
       return QueenProblem;
   }

   public static List<Integer> BT(CSP csp)
   {
        int i = 1;
        List<Integer> domaine = csp.getVariables().get(0).getDomaine();
        List<Integer> assign = new ArrayList<>();
        int x = 0;
        while(1 <= i && i <= csp.getNbVariable())
        {
            boolean ok = false;
            while(!ok && !domaine.isEmpty())
            {
                x = domaine.get(0);
                domaine.remove(Integer.valueOf(x));
                if(csp.test(csp.getVariables().get(i-1), x, assign)) {
                    ok = true;
                }
            }
            if(!ok) {
                i--;
                if(!(i-1 < 0))
                    domaine = csp.getVariables().get(i-1).getDomaine();
            }
            else {
                if (assign.size() < i) {
                    assign.add(x);
                } else {
                    assign.set(i - 1, x);
                }
                i++;
                if (i <= csp.getNbVariable())
                    domaine = csp.getVariables().get(i - 1).resetDomaine();
            }
        }
        if(i == 0) return null; else return assign;

   }

    public static List<Integer> BJ(CSP csp)
    {
        int i = 1;
        List<Integer> domaine = csp.getVariables().get(0).getDomaine();
        List<Integer> assign = new ArrayList<>();
        List<Integer> coupable = new ArrayList<>();;//pointeur sur coupable
        coupable.add(i-1,0);
        int x = 0;
        while(1 <= i && i <= csp.getNbVariable())
        {
            boolean ok = false;
            while(!ok && !domaine.isEmpty())
            {
                x = domaine.get(0);
                domaine.remove(Integer.valueOf(x));
                boolean consistant = true;
                int k = 1;
                while(k < i && consistant)
                {
                    if(k>coupable.get(i-1))
                        coupable.set(i-1, k);
                    if(csp.conflit(csp.getVariables().get(i-1), x, k, assign)) {
                        //System.out.println(k);
                        consistant = false;
                    }
                    else
                        k++;
                }
                if(consistant)
                    ok = true;
            }
            if(!ok) {
                i = coupable.get(i-1);
                if(!(i-1 < 0))
                    domaine = csp.getVariables().get(i-1).getDomaine();
            }
            else
            {
                if(assign.size() < i) {
                    assign.add(x);
                }else {
                    assign.set(i-1,x);
                }
                i++;
                if(i <= csp.getNbVariable())
                    domaine = csp.getVariables().get(i-1).resetDomaine();
                coupable.add(i-1,0);
            }
            if(i>=28)
                System.out.println(i);
        }
        if(i == 0) return null; else return assign;
    }

    public static List<Integer> FC(CSP csp){
        int i = 1;
        List<Integer> domaine = csp.getVariables().get(0).getDomaine();
        List<Integer> assign = new ArrayList<>();
        Map<Integer, Map<Integer, List<Integer>>> historique = new HashMap<>();
        for(int j = 1; j <= csp.getNbVariable(); j++)
            historique.put(j, new HashMap<>());


        int x = 0;
        while( 1 <= i && i <= csp.getNbVariable())
        {
            boolean ok = false;
            while( !ok && !domaine.isEmpty())
            {
                x = domaine.get(0);
                domaine.remove(Integer.valueOf(x));
                boolean domaineVide = false;
                for(int k = i + 1; k <= csp.getNbVariable(); k++)
                {
                    csp.revise(k, i, x, assign, historique);
                    if(csp.getVariables().get(k-1).getDomaine().isEmpty())
                        domaineVide = true;
                }
                if(domaineVide)
                {
                    //TODO: Restaurer chaque domaine avant le choix de la valeur x
                    

                }else
                    ok = true;
            }
            if(!ok)
            {
                //TODO: Restaurer chaque domaine avant le choix de la valeur x
                i--;
            }else {
                if(assign.size() < i) {
                    assign.add(x);
                }else {
                    assign.set(i-1,x);
                }
                i++;
                if(i <= csp.getNbVariable())
                    domaine = csp.getVariables().get(i-1).resetDomaine();
            }
        }
        return assign;
    }



}
