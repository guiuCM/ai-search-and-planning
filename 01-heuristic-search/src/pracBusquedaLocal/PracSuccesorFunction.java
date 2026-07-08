package pracBusquedaLocal;

import Classes.CentrosDistribucion;
import Classes.Gasolineras;
import aima.search.framework.SuccessorFunction;
import aima.search.framework.Successor;
import aima.util.Pair;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.min;

public class PracSuccesorFunction implements SuccessorFunction{

    public List getSuccessors(Object state){
    ArrayList retval = new ArrayList();
    final int MAX_SUCCESSORS = 200; // cap to avoid explosive memory usage when enumerating neighbors
        DistribucioBoard board = (DistribucioBoard) state;
        boolean[] ops = board.getAllowedOps();
        // defensive: if allowed operations not set (some clones may omit this), use sensible defaults
        if (ops == null) {
            ops = new boolean[]{true, true, true, true, false};
        }
        // fem tots els possibles "operadorTreurePeticio(int idxCentre, int idxViatge, int idxPeticio)"
        Gasolineras gasolineres = board.getGasolineres();
        CentrosDistribucion centresDistribucio = board.getCentrosDistribucio();
        int maxV = board.getMaxViatges();
        int maxDi = board.getMaxDiposit();
        // --- dins de getSuccessors(Object state) ---
        if (ops[0]) {
            List<List<List<Integer>>> assignacions = board.getAssignacioPeticio();
            int nCentres = board.getCentrosDistribucio().size();
            for (int c = 0; c < nCentres; c++) {
                int nTrips = assignacions.get(c).size();
                for (int v = 0; v < nTrips; v++) {
                    int nPet = assignacions.get(c).get(v).size();
                    for (int p = 0; p < nPet; p++) {
                        // PRUNE: només si realment es pot treure
                        if (!board.esPotTreure(c, v, p)) continue;

                        DistribucioBoard nb = new DistribucioBoard(board);
                        boolean ok = nb.operadorTreurePeticio(c, v, p);
                        if (ok) {
                            String op = "treure idx_centre=" + c + ", idx_viatge=" + v + ", idx_peticio=" + p + " h: " + board.heuristica_2();
                            retval.add(new Successor(op, nb));
                            if (retval.size() >= MAX_SUCCESSORS) return retval;
                        }
                    }
                }
            }
        }


        // fer el mateix amb afegir peticio: int idxCentre, int viatge, int id_Peticio
        // --- dins de getSuccessors(Object state) ---
        if (ops[1]) {
            List<Pair> pendents = board.getPeticionsPendents();
            int nCentres = board.getCentrosDistribucio().size();
            maxV = board.getMaxViatges();

            for (int i = 0; i < pendents.size(); i++) {
                for (int j = 0; j < nCentres; j++) {
                    // nombre de viatges EXISTENTS en aquest centre
                    int nTrips = board.getNumViatges(j); // <-- afegeix un getter curt (o calcula internament a esPotAfegir)
                    // 1) viatges existents
                    for (int v = 0; v < nTrips; v++) {
                        if (board.esPotAfegir(j, v, pendents.get(i))) {   // <-- PRUNE PRIMER
                            DistribucioBoard nb = new DistribucioBoard(board); // <-- CLONA DESPRÉS
                            if (nb.operadorAfegirPeticio(j, v, i)) {
                                String op = "afegir idx_centre=" + j + ", viatge=" + v + ", id_peticio=" + i + " h: " + board.heuristica_2();
                                retval.add(new Successor(op, nb));
                                if (retval.size() >= MAX_SUCCESSORS) return retval;
                            }
                        }
                    }
                    // 2) viatge nou (com a molt 1)
                    if (nTrips < maxV) {
                        if (board.esPotAfegir(j, nTrips, pendents.get(i))) {   // <-- PRUNE PRIMER
                            DistribucioBoard nb = new DistribucioBoard(board); // <-- CLONA DESPRÉS
                            if (nb.operadorAfegirPeticio(j, nTrips, i)) {
                                String op = "afegir idx_centre=" + j + ", viatge(nou)=" + nTrips + ", id_peticio=" + i + " h: " + board.heuristica_2();
                                retval.add(new Successor(op, nb));
                                if (retval.size() >= MAX_SUCCESSORS) return retval;
                            }
                        }
                    }
                }
            }
        }

        // --- dins de getSuccessors(Object state) ---
        if (ops[2]) {
            List<List<List<Integer>>> assig = board.getAssignacioPeticio();
            int nCentres = board.getCentrosDistribucio().size();
            maxV = board.getMaxViatges();

            for (int cFrom = 0; cFrom < nCentres; cFrom++) {
                int nTripsFrom = assig.get(cFrom).size();
                for (int vFrom = 0; vFrom < nTripsFrom; vFrom++) {
                    int nPet = assig.get(cFrom).get(vFrom).size();
                    for (int pFrom = 0; pFrom < nPet; pFrom++) {

                        for (int cTo = 0; cTo < nCentres; cTo++) {
                            int nTripsTo = assig.get(cTo).size();

                            // 1) provar TOTS els viatges existents al centre destí
                            for (int vTo = 0; vTo < nTripsTo; vTo++) {
                                if (!board.esPotMourePeticio(cFrom, vFrom, pFrom, cTo, vTo)) continue; // PRUNE
                                DistribucioBoard nb = new DistribucioBoard(board);                      // clona DESPRÉS
                                if (nb.operadorMourePeticio(cFrom, cTo, vFrom, vTo, pFrom)) {
                                    String op = "moure cFrom=" + cFrom + " vFrom=" + vFrom + " p=" + pFrom +
                                            " -> cTo=" + cTo + " vTo=" + vTo + " h: " + board.heuristica_2();
                                    retval.add(new Successor(op, nb));
                                    if (retval.size() >= MAX_SUCCESSORS) return retval;
                                }
                            }

                            // 2) si es pot crear viatge nou al centre destí, provar-lo (índex = nTripsTo)
                            if (nTripsTo < maxV) {
                                int vNou = nTripsTo;
                                if (!board.esPotMourePeticio(cFrom, vFrom, pFrom, cTo, vNou)) continue; // PRUNE
                                DistribucioBoard nb = new DistribucioBoard(board);
                                if (nb.operadorMourePeticio(cFrom, cTo, vFrom, vNou, pFrom)) {
                                    String op = "moure cFrom=" + cFrom + " vFrom=" + vFrom + " p=" + pFrom +
                                            " -> cTo=" + cTo + " vNou=" + vNou + " h: " + board.heuristica_2();
                                    retval.add(new Successor(op, nb));
                                    if (retval.size() >= MAX_SUCCESSORS) return retval;
                                }
                            }
                        }

                    }
                }
            }
        }


        //operador swap1 (entre dues peticions servides)

        if (ops[3]) {
            List<List<List<Integer>>> assig = board.getAssignacioPeticio();
            int nCentres = board.getCentrosDistribucio().size();

            for (int c1 = 0; c1 < nCentres; c1++) {
                int nTrips1 = assig.get(c1).size();
                for (int v1 = 0; v1 < nTrips1; v1++) {
                    int nPet1 = assig.get(c1).get(v1).size();
                    for (int p1 = 0; p1 < nPet1; p1++) {

                        for (int c2 = c1; c2 < nCentres; c2++) { // c2=c1 per evitar duplicats simètrics
                            int nTrips2 = assig.get(c2).size();
                            for (int v2 = 0; v2 < nTrips2; v2++) {
                                int nPet2 = assig.get(c2).get(v2).size();
                                for (int p2 = 0; p2 < nPet2; p2++) {

                                    // PRUNE: només si es pot fer el swap
                                    if (!board.esPotSwapPeticions1(c1, v1, p1, c2, v2, p2)) continue;

                                    DistribucioBoard nb = new DistribucioBoard(board); // clona DESPRÉS del filtre
                                    if (nb.operadorSwapPeticions1(c1, v1, p1, c2, v2, p2)) {
                                        String op = "swap c1=" + c1 + " v1=" + v1 + " p1=" + p1 +
                                                "  <->  c2=" + c2 + " v2=" + v2 + " p2=" + p2 + " h: " + board.heuristica_2();
                                        retval.add(new Successor(op, nb));
                                        if (retval.size() >= MAX_SUCCESSORS) return retval;
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }
        //System.out.println("Succ count = " + retval.size());
        //System.out.println("Peticions servides: " + board.getPeticionsPendents().size());
        return retval;
    }

}
