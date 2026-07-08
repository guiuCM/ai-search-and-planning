package pracBusquedaLocal;
import aima.search.framework.HeuristicFunction;



public class Heuristica implements HeuristicFunction {

    public double getHeuristicValue(Object State) {
        if (((DistribucioBoard) State).getUseHeuristic() == 2) {
            return ((DistribucioBoard) State).heuristica_2();
        }
        else if (((DistribucioBoard) State).getUseHeuristic() == 3) {
            return ((DistribucioBoard) State).heuristica_3();
        }
        else return ((DistribucioBoard) State).heuristica_1();
    }
}
