import aima.search.framework.*;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;
import pracBusquedaLocal.DistribucioBoard;
import pracBusquedaLocal.Heuristica;
import pracBusquedaLocal.PracSuccesorFunction;
import pracBusquedaLocal.pracGoalTest;

import java.util.*;

public class Test {

    public static void main (String[] args) {

        DistribucioBoard estado_inicial = new DistribucioBoard(100, 10, 1, 1234,1, 5, 640, "greedy");
        estado_inicial.setUseHeuristic(2);
        boolean[] operations = {true, true, true, true, true, true};
        estado_inicial.setAllowedOps(operations);
        estado_inicial.imprimir_assignacions();
        estado_inicial.imprimir_benefici();
        estado_inicial.imprimir_metriques();
        System.out.println();
        System.out.println("##------------------------------------------------##");
        System.out.println();

        Problem p = new Problem(estado_inicial, new PracSuccesorFunction(), new pracGoalTest(), new Heuristica());

        List<Successor> children =  p.getSuccessorFunction().getSuccessors(estado_inicial);

        for (Successor child : children) {
            ((DistribucioBoard) child.getState()).imprimir_metriques();
            System.out.println();
        }

    }


}