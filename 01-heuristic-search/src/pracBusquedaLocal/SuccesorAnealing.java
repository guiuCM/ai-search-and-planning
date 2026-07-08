package pracBusquedaLocal;


import Classes.CentrosDistribucion;
import Classes.Gasolineras;
import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;
import aima.util.Pair;

import java.util.ArrayList;
import java.util.List;


//Es generen succecior amb un operador aleatori i resultat aleatori (la temperatura va reduint l'exploració fins a arribar un bon resultat)
public class SuccesorAnealing implements SuccessorFunction {

    @Override
    public List getSuccessors(Object state) {
        ArrayList retval = new ArrayList();
        DistribucioBoard board = (DistribucioBoard) state;
        //boolean[] ops = board.getAllowedOps();
        int op = (int) (Math.random() * 3); //random value per triar l'operador
        //dona millors valors sense el swap
        //op = 1; //per debugar

        Gasolineras gasolineres = board.getGasolineres();
        int maxV = board.getMaxViatges();
        int maxDi = board.getMaxDiposit();

        List<List<List<Integer>>> assignacions = board.getAssignacioPeticio();
        int nCentres = board.getCentrosDistribucio().size();
        maxV = board.getMaxViatges();
        List<Pair> pendents = board.getPeticionsPendents();



        if(op == 0){//treure

            // pick a random centre that has at least one trip and that trip has at least one peticio
            int attempts = 0;
            boolean found = false;
            int j = -1, v = -1, p = -1;
            while (attempts < 10 && !found) {
                attempts++;
                j = (int) (Math.random() * nCentres);
                List<List<Integer>> trips = assignacions.get(j);
                if (trips == null || trips.isEmpty()) continue;
                v = (int) (Math.random() * trips.size());
                List<Integer> trip = trips.get(v);
                if (trip == null || trip.isEmpty()) continue;
                p = (int) (Math.random() * trip.size());
                found = true;
            }

            if (found) {
                // PRUNE: només si realment es pot treure
                if (board.esPotTreure(j, v, p)) {
                    DistribucioBoard nb = new DistribucioBoard(board);
                    boolean ok = nb.operadorTreurePeticio(j, v, p);
                    if (ok) {
                        String res = "treure idx_centre=" + j + ", idx_viatge=" + v + ", idx_peticio=" + p;
                        retval.add(new Successor(res, nb));
                    }
                }
            }

        } else if (op == 1) {//afegir

            int j = (int) (Math.random() * nCentres); //0-9
            int i = (int) (Math.random() * pendents.size());

            int nTrips = board.getNumViatges(j); // <-- afegeix un getter curt (o calcula internament a esPotAfegir)
            // 1) viatges existents
            for (int t = 0; t < nTrips; t++) {
                if (board.esPotAfegir(j, t, pendents.get(i))) {   // <-- PRUNE PRIMER
                    DistribucioBoard nb = new DistribucioBoard(board); // <-- CLONA DESPRÉS
                    if (nb.operadorAfegirPeticio(j, t, i)) {
                        String res = "afegir idx_centre=" + j + ", viatge=" + t + ", id_peticio=" + i;
                        retval.add(new Successor(res, nb));
                    }
                }
            }
            if (nTrips < maxV)  {
                if (board.esPotAfegir(j, nTrips, pendents.get(i))) {   // <-- PRUNE PRIMER
                    DistribucioBoard nb = new DistribucioBoard(board); // <-- CLONA DESPRÉS
                    if (nb.operadorAfegirPeticio(j, nTrips, i)) {
                        String res = "afegir idx_centre=" + j + ", viatge=" + nTrips + ", id_peticio=" + i;
                        retval.add(new Successor(res, nb));
                    }
                }
            }


        } else if (op == 2) {//moure
            List<List<List<Integer>>> assig = board.getAssignacioPeticio();

            int cFrom = (int) (Math.random() * board.getCentrosDistribucio().size());
            int cTo = (int) (Math.random() * board.getCentrosDistribucio().size());
            int vFrom = (int) (Math.random() * assig.get(cFrom).size());
            int pFrom = -1;
            if (assig != null
                    && assig.get(cFrom) != null
                    && vFrom < assig.get(cFrom).size()
                    && assig.get(cFrom).get(vFrom) != null
                    && !assig.get(cFrom).get(vFrom).isEmpty()) {

                //per arreglar els problemes que donava accedir a una llista buida
                int size = assig.get(cFrom).get(vFrom).size();
                pFrom = (int) (Math.random() * size);

            }

            int nTripsTo = assig.get(cTo).size();


            // 1) provar TOTS els viatges existents al centre destí
            for (int vTo = 0; vTo < nTripsTo; vTo++) {
                if (!board.esPotMourePeticio(cFrom, vFrom, pFrom, cTo, vTo)) continue; // PRUNE
                DistribucioBoard nb = new DistribucioBoard(board);                      // clona DESPRÉS
                if (nb.operadorMourePeticio(cFrom, cTo, vFrom, vTo, pFrom)) {
                    String res = "moure cFrom=" + cFrom + " vFrom=" + vFrom + " p=" + pFrom +
                            " -> cTo=" + cTo + " vTo=" + vTo;
                    retval.add(new Successor(res, nb));
                }
            }

            // 2) si es pot crear viatge nou al centre destí, provar-lo (índex = nTripsTo)
            if (nTripsTo < maxV) {
                int vNou = nTripsTo;
                if (!board.esPotMourePeticio(cFrom, vFrom, pFrom, cTo, vNou)) { // PRUNE
                    DistribucioBoard nb = new DistribucioBoard(board);
                    if (nb.operadorMourePeticio(cFrom, cTo, vFrom, vNou, pFrom)) {
                        String res = "moure cFrom=" + cFrom + " vFrom=" + vFrom + " p=" + pFrom +
                                " -> cTo=" + cTo + " vNou=" + vNou;
                        retval.add(new Successor(res, nb));
                    }
                }
            }



        }else { //op == 3  (swap)
            List<List<List<Integer>>> assig = board.getAssignacioPeticio();

            int c1 = (int) (Math.random() * nCentres); //0-9
            int c2 = (int) (Math.random() * nCentres); //0-9
            int v1= (int) (Math.random() * assig.get(c1).size());
            int v2 = (int) (Math.random() * assig.get(c1).size());
            int p1 = -1;
            if (assig != null
                    && assig.get(c1) != null
                    && v1 < assig.get(c1).size()
                    && assig.get(c1).get(v1) != null
                    && !assig.get(c1).get(v1).isEmpty()) {

                //per arreglar els problemes que donava accedir a una llista buida
                int size = assig.get(c1).get(v1).size();
                p1 = (int) (Math.random() * size);

            }
            int p2 = -1;
            if (assig != null
                    && assig.get(c2) != null
                    && v2 < assig.get(c2).size()
                    && assig.get(c2).get(v2) != null
                    && !assig.get(c2).get(v2).isEmpty()) {

                //per arreglar els problemes que donava accedir a una llista buida
                int size = assig.get(c2).get(v2).size();
                p2 = (int) (Math.random() * size);

            }

            // PRUNE: només si es pot fer el swap
            if (!board.esPotSwapPeticions1 (c1, v1, p1, c2, v2, p2)) {

                DistribucioBoard nb = new DistribucioBoard(board); // clona DESPRÉS del filtre
                if (nb.operadorSwapPeticions1 (c1, v1, p1, c2, v2, p2)) {
                    String res = "swap c1=" + c1 + " v1=" + v1 + " p1=" + p1 +
                            "  <->  c2=" + c2 + " v2=" + v2 + " p2=" + p2;
                    retval.add(new Successor(res, nb));
                }
            }


        }


        return retval;
    }
}

