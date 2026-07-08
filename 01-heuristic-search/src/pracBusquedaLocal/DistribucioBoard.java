package pracBusquedaLocal;

import Classes.CentrosDistribucion;
import Classes.Distribucion;
import Classes.Gasolinera;
import Classes.Gasolineras;
import aima.util.Pair;

import java.util.*;


public class DistribucioBoard {
    //static pq compartit entre tots els obj d'aquesta classe
    private static int maxViatges;
    private static int maxKm;
    private static int maxDiposit;
    private static int costKm;
    private static double mitjanaDies;
    private static CentrosDistribucion centresDistribucio;
    private static Gasolineras gasolineres;

    private static int useHeuristic;

    private static int benefIdeal;
    private static boolean[] allowedOps;

    // recorregutCentre[i] = km recorreguts pel camio del centre i
    private int[] recorregutCentre;

    // cada element representa un centre, guarda una llista de les sortides que ha fet i per cada sortida les peticions servides
    private List<List<List<Integer>>> assignacioPeticio;

    private List<Pair> peticionsPendents;
    private List<Pair> peticionsServides;

    //per assignaro a la estructura de dades aixi donada la peticio amb id 785403, sabem que es a 78 54 i es la peticio 03
    private int getIdPeticio(int idG, int idxPeticio) {
        return idG * 100 + idxPeticio;
    }

    public DistribucioBoard(DistribucioBoard b) {
        // Arrays
        this.recorregutCentre = (b.recorregutCentre != null) ? b.recorregutCentre.clone() : null;

        // List<List<List<Integer>>> — deep copy als 3 nivells
        this.assignacioPeticio = new ArrayList<>(b.assignacioPeticio.size());
        for (List<List<Integer>> lvl1 : b.assignacioPeticio) {
            List<List<Integer>> lvl1Copy = new ArrayList<>(lvl1.size());
            for (List<Integer> lvl2 : lvl1) {
                lvl1Copy.add(new ArrayList<>(lvl2));
            }
            this.assignacioPeticio.add(lvl1Copy);
        }

        // List<Pair> — copia els elements
        this.peticionsPendents = new ArrayList<>(b.peticionsPendents.size());
        for (Pair p : b.peticionsPendents) {
            this.peticionsPendents.add(new Pair(p.getFirst(), p.getSecond()));
        }

        this.peticionsServides = new ArrayList<>(b.peticionsServides.size());
        for (Pair p : b.peticionsServides) {
            this.peticionsServides.add(new Pair(p.getFirst(), p.getSecond()));
        }
    }

    public DistribucioBoard(int nGasolineres, int nCentres, int nCamions, int seed, int seedRandom, int viatges, int km, String strategy) {
        gasolineres = new Gasolineras(nGasolineres, seed);
        centresDistribucio = new CentrosDistribucion(nCentres, nCamions, seed);
        maxViatges = viatges;
        maxKm = km;
        maxDiposit = 2;
        costKm = 2;
        nCentres = nCentres * nCamions;
        assignacioPeticio = new ArrayList<>(nCentres);
        for (int i = 0; i < nCentres; i++) {
            assignacioPeticio.add(new ArrayList<>()); // cada posició: llista buida de llistes d'enters
        }
        recorregutCentre = new int[nCentres];
        peticionsPendents = new ArrayList<>();
        peticionsServides = new ArrayList<>();


        for (int i = 0; i < nGasolineres; ++i) {
            Gasolinera gasolinera_actual = gasolineres.get(i);
            ArrayList<Integer> peticions_actuals = gasolinera_actual.getPeticiones();
            for (int j = 0; j < peticions_actuals.size(); ++j) {
                // gasolinera i, peticio j
                peticionsPendents.add(new Pair(i, j));
            }
        }
        benefIdeal = peticionsPendents.size() * 1000; //ho servim tot
        mitjanaDies = getMitjanaDies();
        if (Objects.equals(strategy, "greedy")) solInicialGreedy();
        else if (Objects.equals(strategy, "empty")) {

        } else if (Objects.equals(strategy, "random")) solInicialRandom(seedRandom);
        else System.out.println("Strategy not implemented" + strategy);
    }




    private void SolInicialEmpty() {
        for (int i = 0; i < assignacioPeticio.size(); ++i) {
            assignacioPeticio.get(i).add(new ArrayList<>());
        }
    }

    public void setCostKm(int cost) {costKm = cost;}

    // ordenem les peticions per dies creixentment, assignem a centre que estigui a menor distància
    // només assignem una petició per viatge per "donar espai" a l'algorisme
    private void solInicialGreedy() {
        peticionsPendents.sort(Comparator.comparingInt(a -> getNumDiesPeticio(a)));
        int nGasolineres = gasolineres.size();
        int nCentres = centresDistribucio.size();
        int[][] distancies_gasolineres_centres = new  int[nGasolineres][nCentres];
        for (int i = 0; i < nGasolineres; ++i) {
            Gasolinera gasolinera = gasolineres.get(i);
            int x1 = gasolinera.getCoordX();
            int y1 = gasolinera.getCoordY();
            for (int j = 0; j < nCentres; ++j) {
                Distribucion centre = centresDistribucio.get(j);
                int x2 = centre.getCoordX();
                int y2 = centre.getCoordY();
                int dist = getDistance(x1, y1, x2, y2);
                distancies_gasolineres_centres[i][j] = dist;
            }
        }

        for (int i = 0; i < peticionsPendents.size(); ++i) {
            Pair p = peticionsPendents.get(i);
            int id_gasolinera = (int)p.getFirst();
            int id_peticio = (int)p.getSecond();

            int[] distancies = distancies_gasolineres_centres[id_gasolinera];
            int bestCentre = -1;
            int bestDistancia = -1;
            for (int j = 0; j < distancies.length; ++j) {
                int dist_actual = distancies[j];
                if (camio_disponible(j, dist_actual)) {
                    if (bestCentre == -1 || dist_actual < bestDistancia) {
                        bestDistancia = dist_actual;
                        bestCentre = j;
                    }
                }
            }
            if (bestCentre != -1) {
                List<Integer> nou_viatge = new ArrayList<>();
                nou_viatge.add(getIdPeticio(id_gasolinera, id_peticio));
                assignacioPeticio.get(bestCentre).add(nou_viatge);
                recorregutCentre[bestCentre] += 2 * bestDistancia;
                peticionsServides.add(peticionsPendents.get(i));
            }

        }
        for (int i = 0; i < peticionsServides.size(); ++i) {
            peticionsPendents.remove(peticionsServides.get(i));
        }
    }

    // ordenem de manera aleatòria les peticions, les anem assignant al centre actual. quan un centre s'emplena (tant per
    // km com per num de viatges), canviem de centre
    private void solInicialRandom(int seed) {
        Collections.shuffle(peticionsPendents, new Random(seed));

        int idxPeticio = 0;
        int numPeticions = peticionsPendents.size();
        int nCentres = centresDistribucio.size();
        int centreActual = 0;
        int kmCentreActual = 0;
        int viatgeActual = 0;
        assignacioPeticio.get(centreActual).add(new ArrayList<>());
        while (idxPeticio <  numPeticions && centreActual < nCentres) {
            // intentem posar la peticio idxPeticio al centreActual viatge viatgeActual
            int nousKm = kmCentreActual;
            int cx = centresDistribucio.get(centreActual).getCoordX();
            int cy = centresDistribucio.get(centreActual).getCoordY();
            int gasolinera = (int)peticionsPendents.get(idxPeticio).getFirst();
            int peticio = (int)peticionsPendents.get(idxPeticio).getSecond();
            int gx = gasolineres.get(gasolinera).getCoordX();
            int gy = gasolineres.get(gasolinera).getCoordY();
            if (assignacioPeticio.get(centreActual).get(viatgeActual).isEmpty()) {
                //nousKm += 2*dist entre centreActual i gasolinera de la peticio actual
                nousKm += 2*getDistance(cx, cy, gx, gy);
            }
            else {
                //nousKm += dist g1 -> g2 + dist g2 -> c - dist g1 -> c
                int gasolinera1 = (int)getPairFromId(assignacioPeticio.get(centreActual).get(viatgeActual).get(0)).getFirst();
                int g1x = gasolineres.get(gasolinera1).getCoordX();
                int g1y = gasolineres.get(gasolinera1).getCoordY();
                nousKm += getDistance(gx, gy, g1x, g1y) + getDistance(cx, cy, gx, gy) - getDistance(g1x, g1y, cx, cy);
            }

            if (nousKm <= maxKm) {
                assignacioPeticio.get(centreActual).get(viatgeActual).add(getIdPeticio(gasolinera, peticio));
                peticionsServides.add(peticionsPendents.get(idxPeticio));
                kmCentreActual = nousKm;
                ++idxPeticio;
            }
            else {
                viatgeActual++;
                if (viatgeActual < maxViatges) assignacioPeticio.get(centreActual).add(new ArrayList<>());
            }

            // actualitzacions de variables
            if (viatgeActual < 5 && assignacioPeticio.get(centreActual).get(viatgeActual).size() == 2) {
                viatgeActual++;
                if (viatgeActual < maxViatges) assignacioPeticio.get(centreActual).add(new ArrayList<>());
            }
            if (viatgeActual >= maxViatges) {
                recorregutCentre[centreActual] = kmCentreActual;
                centreActual++;
                kmCentreActual = 0;
                viatgeActual = 0;
                if (centreActual < nCentres) assignacioPeticio.get(centreActual).add(new ArrayList<>());
            }
        }

        for (List<List<Integer>> viatgesCentre : assignacioPeticio) {
            viatgesCentre.removeIf(v -> v == null || v.isEmpty());
        }

        for (int i = 0; i < peticionsServides.size(); ++i) {
            peticionsPendents.remove(peticionsServides.get(i));
        }
    }

    public void setAllowedOps(boolean[] ops) {
        allowedOps = ops;
    }

    public boolean[] getAllowedOps() { return allowedOps; }


    private void imprimir_vector(int[] v) {
        for (int i = 0; i < v.length; ++i)
            System.out.println(i + ": " + v[i]);
    }

    private boolean camio_disponible(int id_centre, int nova_dist) {
        if (recorregutCentre[id_centre] + 2*nova_dist > maxKm) return false;
        if (assignacioPeticio.get(id_centre).size() == maxViatges) return false;
        return true;
    }

    private void imprimir_peticions(List<Pair> peticions) {
        for (int i = 0; i < peticions.size(); ++i) {
            System.out.println("  Gasolinera " + peticions.get(i).getFirst() + ", Petició " +
                    peticions.get(i).getSecond() + "(" + getNumDiesPeticio(peticions.get(i)) + " dies)");
        }
    }

    private void imprimir_distancies(int[][] distancies_gasolineres_centres) {
        for (int i = 0; i < distancies_gasolineres_centres.length; ++i) {
            for (int j = 0; j < distancies_gasolineres_centres[i].length; ++j) {
                System.out.println("Dist gasolinera " + i + " centre " + j + ": " + distancies_gasolineres_centres[i][j]);
            }
        }
    }

    public void imprimir_assignacions() {
        for (int i = 0; i < assignacioPeticio.size(); ++i) {
            System.out.println("  Assignacions centre " + i + " (total: " + recorregutCentre[i] + " km)");
            for (int j = 0; j < assignacioPeticio.get(i).size(); ++j) {
                System.out.println("    Viatge " + j);
                for (int k = 0; k < assignacioPeticio.get(i).get(j).size(); ++k) {
                    int idPeticio =  assignacioPeticio.get(i).get(j).get(k);
                    Pair p = getPairFromId(idPeticio);
                    System.out.println("      Gasolinera " + p.getFirst() + " petició " + p.getSecond() + " (" + getNumDiesPeticio(p) + " dies)");
                }
            }
        }
    }


    public void imprimir_benefici(){
        double total = get_benefici();
        System.out.println(' ');
        System.out.println("El total és: " + total);
    }

    public double get_benefici(){
        double total = 0;
        int[] peticiones = getPeticionesCompletadas();
        for (int i = 0; i < peticiones.length; i++){
            if (peticiones[i] == 0) {
                total += 1.02*1000; //102%
            }
            else total += ((100-Math.pow(2, peticiones[i]))*1000)/100;
        }
        total -= getKmTotales() * 2; //cada km costa 2
        return total;
    }


    public Gasolineras getGasolineres() {
        return gasolineres;
    }

    public CentrosDistribucion getCentrosDistribucio() {
        return centresDistribucio;
    }

    public int[] getRecorregutCentre() {
        return recorregutCentre;
    }

    public List< List< List<Integer> >> getAssignacioPeticio() {
        return assignacioPeticio;
    }

    public int getCost() {
        int sumKm = 0;
        for (int i = 0; i < recorregutCentre.length; ++i) {
            sumKm += recorregutCentre[i];
        }
        return sumKm * 2;
    }

    private int getDistance(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    public double heuristica_1() {
        int nKm = getKmTotales();
        int precioDeposito = 1000;
        int[] peticiones= getPeticionesCompletadas();

        double total = 0;
        for (int i = 0; i < peticiones.length; i++){
            if (peticiones[i] == 0) {
                total += 1.02*precioDeposito;
            }
            else total += ((100-Math.pow(2, peticiones[i]))*precioDeposito)/100;
        }
        total -= nKm * costKm; //cada km costa 2
        return total;
    }

    public double heuristica_2() {
        return benefIdeal - heuristica_1();
    }

    public double heuristica_3() {
        double value;
        int nKm = getKmTotales();
        int n = peticionsServides.size();

        value = n * (100-Math.pow(2, mitjanaDies))*1000/100;

        value -= nKm * costKm;
        value = benefIdeal - value;
        //System.out.println(value);
        return value;
    }


    private Pair getPairFromId(int id) {
        return new Pair(id/100,id%100);
    }

    private void checkViatgesBuits(int centre) {
        for (int i = 0; i < assignacioPeticio.get(centre).size(); ++i) {
            if (assignacioPeticio.get(centre).get(i).isEmpty()) {
                assignacioPeticio.get(centre).remove(i);
            }
        }
    }

    public boolean esPotAfegir(int idxCentre, int viatge, Pair peticio) {
        if (idxCentre < 0 || idxCentre >= centresDistribucio.size()) return false;
        int nTrips = assignacioPeticio.get(idxCentre).size();
        if (viatge < 0 || viatge > nTrips) return false;              // > nTrips NO, == nTrips és viatge nou

        int idxGas = (int) peticio.getFirst();
        int idxPet = (int) peticio.getSecond();

        // càlcul de km
        int nousKm;
        if (viatge == nTrips) {
            // viatge nou: centre -> gas -> centre
            int cx = centresDistribucio.get(idxCentre).getCoordX();
            int cy = centresDistribucio.get(idxCentre).getCoordY();
            int gx = gasolineres.get(idxGas).getCoordX();
            int gy = gasolineres.get(idxGas).getCoordY();
            nousKm = recorregutCentre[idxCentre] + 2 * getDistance(cx, cy, gx, gy);
            // només es pot crear si encara queda capacitat de viatges
            if (nTrips >= maxViatges) return false;
        } else {
            // viatge existent: usem el càlcul estàndard
            nousKm = getNouRecorregutCentre("afegir", idxCentre, idxGas, viatge);
            // capacitat del dipòsit
            if (assignacioPeticio.get(idxCentre).get(viatge).size() >= maxDiposit) return false;
        }

        // límit de km
        if (nousKm > maxKm) return false;

        return true;
    }

    public boolean operadorAfegirPeticio(int idxCentre, int viatge, int id_Peticio) {
        if (idxCentre < 0 || idxCentre >= centresDistribucio.size()) return false;

        int nTrips = assignacioPeticio.get(idxCentre).size();
        if (viatge < 0 || viatge > nTrips) return false;          // > nTrips mai
        if (nTrips == maxViatges && viatge == nTrips) return false; // no podem crear viatge nou

        int idxPet = (int) peticionsPendents.get(id_Peticio).getSecond();
        int idxGas = (int) peticionsPendents.get(id_Peticio).getFirst();
        int id_peticio = getIdPeticio(idxGas, idxPet);

        int nousKm;
        boolean viatgeNou = (viatge == nTrips);

        if (viatgeNou) {
            // calcular km per viatge nou sense tocar assignacions
            int cx = centresDistribucio.get(idxCentre).getCoordX();
            int cy = centresDistribucio.get(idxCentre).getCoordY();
            int gx = gasolineres.get(idxGas).getCoordX();
            int gy = gasolineres.get(idxGas).getCoordY();
            nousKm = recorregutCentre[idxCentre] + 2 * getDistance(cx, cy, gx, gy);
            if (nousKm > maxKm) return false;

            // crear el viatge i afegir la petició
            List<Integer> diposit = new ArrayList<>();
            diposit.add(id_peticio);
            assignacioPeticio.get(idxCentre).add(diposit);
            servirPeticio(id_Peticio, idxGas, idxPet, idxCentre, nousKm);
            return true;
        } else {
            // viatge existent
            List<Integer> trip = assignacioPeticio.get(idxCentre).get(viatge);
            if (trip.size() >= maxDiposit) return false;

            nousKm = getNouRecorregutCentre("afegir", idxCentre, idxGas, viatge);
            if (nousKm > maxKm) return false;

            trip.add(id_peticio);
            servirPeticio(id_Peticio, idxGas, idxPet, idxCentre, nousKm);
            return true;
        }
    }

    // Recalcula el total de km d'un centre des de zero
    private int recomputeKmCentre(int idxCentre) {
        int km = 0;
        int cx = centresDistribucio.get(idxCentre).getCoordX();
        int cy = centresDistribucio.get(idxCentre).getCoordY();
        List<List<Integer>> trips = assignacioPeticio.get(idxCentre);

        for (List<Integer> trip : trips) {
            if (trip == null || trip.isEmpty()) continue;

            // primera gasolinera del viatge
            int idFirst = trip.get(0);
            int gFirst = (int) getPairFromId(idFirst).getFirst();
            int gxF = gasolineres.get(gFirst).getCoordX();
            int gyF = gasolineres.get(gFirst).getCoordY();

            // cost bàsic: centre -> gFirst -> centre
            km += 2 * getDistance(cx, cy, gxF, gyF);

            // cada gasolinera addicional aporta: dist(g, gFirst) + dist(c, g) - dist(c, gFirst)
            for (int t = 1; t < trip.size(); t++) {
                int id = trip.get(t);
                int g = (int) getPairFromId(id).getFirst();
                int gx = gasolineres.get(g).getCoordX();
                int gy = gasolineres.get(g).getCoordY();
                km += getDistance(gx, gy, gxF, gyF)
                        +  getDistance(cx, cy, gx, gy)
                        -  getDistance(gxF, gyF, cx, cy);
            }
        }
        return km;
    }

    public boolean esPotSwapPeticions1(int c1, int v1, int p1, int c2, int v2, int p2) {
        // índexos bàsics
        if (c1 < 0 || c1 >= assignacioPeticio.size()) return false;
        if (c2 < 0 || c2 >= assignacioPeticio.size()) return false;
        if (v1 < 0 || v1 >= assignacioPeticio.get(c1).size()) return false;
        if (v2 < 0 || v2 >= assignacioPeticio.get(c2).size()) return false;
        if (p1 < 0 || p1 >= assignacioPeticio.get(c1).get(v1).size()) return false;
        if (p2 < 0 || p2 >= assignacioPeticio.get(c2).get(v2).size()) return false;

        // si és exactament el mateix element, no té sentit
        if (c1 == c2 && v1 == v2 && p1 == p2) return false;

        int size1 = assignacioPeticio.get(c1).get(v1).size();
        int size2 = assignacioPeticio.get(c2).get(v2).size();
        if (size1 > maxDiposit || size2 > maxDiposit) return false;

        // Simula swap lleuger i comprova límit km per als dos centres
        int id1 = assignacioPeticio.get(c1).get(v1).get(p1);
        int id2 = assignacioPeticio.get(c2).get(v2).get(p2);

        // fem el swap "in place" de manera segura (desfer després)
        assignacioPeticio.get(c1).get(v1).set(p1, id2);
        assignacioPeticio.get(c2).get(v2).set(p2, id1);

        int km1 = recomputeKmCentre(c1);
        int km2 = (c2 == c1) ? km1 : recomputeKmCentre(c2);

        // desfem el swap de prova
        assignacioPeticio.get(c1).get(v1).set(p1, id1);
        assignacioPeticio.get(c2).get(v2).set(p2, id2);

        return km1 <= maxKm && km2 <= maxKm;
    }

    public boolean operadorTreurePeticio(int idxCentre, int idxViatge, int idxPeticio) {
        if (!esPotTreure(idxCentre, idxViatge, idxPeticio)) return false;

        // dades de la petició a treure
        int idxPeticioPacked = assignacioPeticio.get(idxCentre).get(idxViatge).get(idxPeticio);
        Pair p = getPairFromId(idxPeticioPacked);          // (idxGasolinera, idxPeticioG)
        int idxGasolinera = (int) p.getFirst();
        int idxPeticioG   = (int) p.getSecond();

        int numPeticio    = idxPeticioPacked % 100;
        int id_Peticio    = indexOfPair(peticionsServides, p); // posició a peticionsServides

        // eliminar de l'assignació
        assignacioPeticio.get(idxCentre).get(idxViatge).remove(idxPeticio);
        if (assignacioPeticio.get(idxCentre).get(idxViatge).isEmpty()) {
            assignacioPeticio.get(idxCentre).remove(idxViatge);
        }

        // recalcular km del centre després de treure
        int nousKm = recomputeKmCentre(idxCentre);
        recorregutCentre[idxCentre] = nousKm;

        // actualitzar estructures de peticions
        eliminarPeticio(id_Peticio, idxGasolinera, numPeticio, idxCentre, nousKm);

        return true;
    }

    public boolean esPotTreure(int idxCentre, int idxViatge, int idxPeticio) {
        // Comprovació d’índexos
        if (idxCentre < 0 || idxCentre >= centresDistribucio.size()) return false;
        if (idxViatge < 0 || idxViatge >= assignacioPeticio.get(idxCentre).size()) return false;
        if (idxPeticio < 0 || idxPeticio >= assignacioPeticio.get(idxCentre).get(idxViatge).size()) return false;
        // treure sempre “alleugereix” km, no cal comprovar res més
        return true;
    }

    static int indexOfPair(java.util.List<Pair> l, Pair t) {
        for (int i = 0; i < l.size(); i++) {
            Pair p = l.get(i);
            if (Objects.equals(p.getFirst(), t.getFirst()) &&
                    Objects.equals(p.getSecond(), t.getSecond())) {
                return i;
            }
        }
        return -1;
    }

    public boolean esPotMourePeticio(int cFrom, int vFrom, int pFrom, int cTo, int vTo) {
        // comprovació indexs
        if (cFrom < 0 || cFrom >= assignacioPeticio.size()) return false;
        if (cTo   < 0 || cTo   >= assignacioPeticio.size()) return false;
        if (vFrom < 0 || vFrom >= assignacioPeticio.get(cFrom).size()) return false;
        if (pFrom < 0 || pFrom >= assignacioPeticio.get(cFrom).get(vFrom).size()) return false;

        // límits de viatges al destí
        int nTripsTo = assignacioPeticio.get(cTo).size();
        if (vTo < 0 || vTo > nTripsTo) return false; // == nTripsTo => viatge nou
        if (vTo == nTripsTo) {
            if (nTripsTo >= maxViatges) return false;
        } else {
            if (assignacioPeticio.get(cTo).get(vTo).size() >= maxDiposit) return false;
        }

        List<List<Integer>> fromCopy = new ArrayList<>();
        for (List<Integer> trip : assignacioPeticio.get(cFrom)) fromCopy.add(new ArrayList<>(trip));

        List<List<Integer>> toCopy;
        if (cTo == cFrom) toCopy = fromCopy; // mateix centre
        else {
            toCopy = new ArrayList<>();
            for (List<Integer> trip : assignacioPeticio.get(cTo)) toCopy.add(new ArrayList<>(trip));
        }

        // apliquem el moviment a les còpies
        int id = fromCopy.get(vFrom).remove(pFrom);
        if (fromCopy.get(vFrom).isEmpty()) {
            fromCopy.remove(vFrom);
            // si cTo==cFrom i vTo estava després, el seu índex es pot desplaçar:
            if (cTo == cFrom && vTo > vFrom) vTo--;
        }

        if (vTo == toCopy.size()) {
            List<Integer> nou = new ArrayList<>();
            nou.add(id);
            toCopy.add(nou);
        } else {
            toCopy.get(vTo).add(id); // al final
        }

        // comprovem km sobre còpies
        int kmFrom = recomputeKmCentreOn(fromCopy, cFrom);
        int kmTo   = (cTo == cFrom) ? kmFrom : recomputeKmCentreOn(toCopy, cTo);

        return kmFrom <= maxKm && kmTo <= maxKm;
    }


    public boolean operadorMourePeticio(int cFrom, int cTo, int vFrom, int vTo, int pFrom) {
        if (!esPotMourePeticio(cFrom, vFrom, pFrom, cTo, vTo)) return false;

        boolean sameCenter = (cFrom == cTo);
        if (sameCenter && vFrom == vTo) {
            // moure dins el mateix viatge no té sentit
            return false;
        }

        int id = assignacioPeticio.get(cFrom).get(vFrom).get(pFrom);

        // treure de l'origen
        assignacioPeticio.get(cFrom).get(vFrom).remove(pFrom);
        boolean removedTrip = false;
        if (assignacioPeticio.get(cFrom).get(vFrom).isEmpty()) {
            assignacioPeticio.get(cFrom).remove(vFrom);
            removedTrip = true;

            // si traiem un viatge i destí és al mateix centre,
            // i el destí estava "després", l'índex destí es desplaça cap a l'esquerra
            if (sameCenter && vTo > vFrom) vTo--;

            // si el destí era EXACTAMENT aquest viatge (i l'hem eliminat),
            // ho interpretem com "viatge nou" al final.
            if (sameCenter && vTo == vFrom) {
                vTo = assignacioPeticio.get(cTo).size(); // index del nou viatge (al final)
            }
        }

        // afegir al destí (viatge existent o nou)
        int nTripsTo = assignacioPeticio.get(cTo).size(); // recomptar després de la possible eliminació
        if (vTo == nTripsTo) {
            List<Integer> nou = new ArrayList<>();
            nou.add(id);
            assignacioPeticio.get(cTo).add(nou);
        } else {
            assignacioPeticio.get(cTo).get(vTo).add(id);
        }

        // recalcular km
        int kmFrom = recomputeKmCentre(cFrom);
        int kmTo   = (cTo == cFrom) ? kmFrom : recomputeKmCentre(cTo);

        if (kmFrom > maxKm || kmTo > maxKm) {
            // Treu del destí
            if (vTo == assignacioPeticio.get(cTo).size() - 1 && assignacioPeticio.get(cTo).get(vTo).size() == 1) {
                assignacioPeticio.get(cTo).remove(vTo);
            } else {
                List<Integer> tripTo = assignacioPeticio.get(cTo).get(vTo);
                tripTo.remove(tripTo.size() - 1);
            }
            // Torna a l'origen: si vam eliminar el viatge, el re-creem al final
            if (removedTrip) {
                List<Integer> nou = new ArrayList<>();
                nou.add(id);
                assignacioPeticio.get(cFrom).add(nou);
            } else {
                // si el viatge encara existeix, inserim a la posició (al final també val)
                assignacioPeticio.get(cFrom).get(vFrom).add(id);
            }
            return false;
        }

        recorregutCentre[cFrom] = kmFrom;
        if (cTo != cFrom) recorregutCentre[cTo] = kmTo;

        // netejar viatges buits
        checkViatgesBuits(cFrom);
        if (cTo != cFrom) checkViatgesBuits(cTo);

        return true;
    }

    public boolean operadorSwapPeticions1(int c1, int v1, int p1, int c2, int v2, int p2) {
        if (!esPotSwapPeticions1(c1, v1, p1, c2, v2, p2)) return false;

        // cas simple: mateix centre i mateix viatge -> només permutar posicions
        if (c1 == c2 && v1 == v2) {
            List<Integer> trip = assignacioPeticio.get(c1).get(v1);
            int tmp = trip.get(p1);
            trip.set(p1, trip.get(p2));
            trip.set(p2, tmp);
            int km = recomputeKmCentre(c1);
            recorregutCentre[c1] = km;
            return true;
        }

        // cas general: centres/viatges poden ser diferents
        List<Integer> trip1 = assignacioPeticio.get(c1).get(v1);
        List<Integer> trip2 = assignacioPeticio.get(c2).get(v2);

        int id1 = trip1.get(p1);
        int id2 = trip2.get(p2);

        trip1.set(p1, id2);
        trip2.set(p2, id1);

        int km1 = recomputeKmCentre(c1);
        int km2 = (c2 == c1) ? km1 : recomputeKmCentre(c2);

        if (km1 > maxKm || km2 > maxKm) {
            // revertir
            trip1.set(p1, id1);
            trip2.set(p2, id2);
            return false;
        }

        recorregutCentre[c1] = km1;
        if (c2 != c1) recorregutCentre[c2] = km2;

        return true;
    }

    private int getNouRecorregutCentre(String op, int idxCentre, int idxGasolinera, int viatge) {
        int nousKm = recorregutCentre[idxCentre];
        int x1 = centresDistribucio.get(idxCentre).getCoordX();
        int y1 = centresDistribucio.get(idxCentre).getCoordY();
        int x2 = gasolineres.get(idxGasolinera).getCoordX();
        int y2 = gasolineres.get(idxGasolinera).getCoordY();
        int dist_centre_gasAct = getDistance(x1, y1, x2, y2);

        if ("afegir".equals(op)) {
            int nTrips = assignacioPeticio.get(idxCentre).size();
            if (viatge == nTrips) {
                // viatge nou: centre -> gas -> centre
                return nousKm + 2 * dist_centre_gasAct;
            }
            // viatge existent
            if (assignacioPeticio.get(idxCentre).get(viatge).size() == 0) {
                return nousKm + 2 * dist_centre_gasAct;
            } else if (assignacioPeticio.get(idxCentre).get(viatge).size() == 1) {
                return nousKm + 2 * dist_centre_gasAct;
            } else {
                int idPeticioAnterior = assignacioPeticio.get(idxCentre).get(viatge).getFirst();
                int idxGasAnt = idPeticioAnterior / 100;
                int x3 = gasolineres.get(idxGasAnt).getCoordX();
                int y3 = gasolineres.get(idxGasAnt).getCoordY();
                int dist_centre_gasAnt = getDistance(x1, y1, x3, y3);
                int dist_gasAnt_gasAct = getDistance(x2, y2, x3, y3);
                return nousKm + dist_gasAnt_gasAct + dist_centre_gasAct - dist_centre_gasAnt;
            }
        } else if ("remove".equals(op)) {
            if (assignacioPeticio.get(idxCentre).get(viatge).size() == 1) {
                //no hi ha mes viatges per tant restar viatge eliminat
                nousKm -= 2*dist_centre_gasAct;
            }
            else if (assignacioPeticio.get(idxCentre).get(viatge).size() == 2) {
                int idP1 = assignacioPeticio.get(idxCentre).get(viatge).getFirst();
                int idP2 = assignacioPeticio.get(idxCentre).get(viatge).getLast();
                int idxG1 = idP1 / 100;
                int idxG2 = idP2 / 100;
                int v_x1 = gasolineres.get(idxG1).getCoordX();;
                int v_y1 = gasolineres.get(idxG1).getCoordY();;
                int v_x2 = gasolineres.get(idxG2).getCoordX();;
                int v_y2 = gasolineres.get(idxG2).getCoordY();;
                int kmsViatge = getDistance(x1, y1, v_x1, v_y1) + getDistance(v_x1, v_y1, v_x2, v_y2);
                recorregutCentre[idxCentre] -= kmsViatge;
                if (idxGasolinera == idxG2) {
                    nousKm += getDistance(x1, y1, v_x1, v_y1);
                }
                else {
                    nousKm += getDistance(x1, y1, v_x2, v_y2);
                }
            }
            else {
                System.out.println("Calcul fora possibilitats if: " + assignacioPeticio.get(idxCentre).get(viatge).size());
            }
        } else {
            System.out.println("Operacio erronia");
        }
        return nousKm;
    }

    public int getNumViatges(int centre) {
        if (centre < 0 || centre >= assignacioPeticio.size()) return 0;
        return assignacioPeticio.get(centre).size();
    }

    private void servirPeticio(int id, int idxGasolinera, int idxPeticio, int idxCentre, int nousKm) {
        Pair peticio =  new Pair(idxGasolinera, idxPeticio);
        peticionsPendents.remove(id);
        peticionsServides.add(peticio);
        //actualitzar km al servir peticio
        recorregutCentre[idxCentre] = nousKm;
    }

    private void eliminarPeticio(int id, int idxGasolinera, int idxPeticio, int idxCentre, int nousKm) {
        // treu per índex O(1) i fiable
        if (id >= 0 && id < peticionsServides.size()) {
            peticionsServides.remove(id);
        } else {
            // fallback robust si cal (per si ve -1)
            Pair peticio = new Pair(idxGasolinera, idxPeticio);
            peticionsServides.remove(peticio);
        }
        // afegeix a pendents
        peticionsPendents.add(new Pair(idxGasolinera, idxPeticio));
        // actualitza km
        recorregutCentre[idxCentre] = nousKm;
    }

    // Recalcula km per a un centre però sobre una còpia de llista de viatges (no toca assignacioPeticio)
    private int recomputeKmCentreOn(List<List<Integer>> tripsCopy, int idxCentre) {
        int km = 0;
        int cx = centresDistribucio.get(idxCentre).getCoordX();
        int cy = centresDistribucio.get(idxCentre).getCoordY();

        for (List<Integer> trip : tripsCopy) {
            if (trip == null || trip.isEmpty()) continue;

            int idFirst = trip.get(0);
            int gFirst = (int) getPairFromId(idFirst).getFirst();
            int gxF = gasolineres.get(gFirst).getCoordX();
            int gyF = gasolineres.get(gFirst).getCoordY();

            km += 2 * getDistance(cx, cy, gxF, gyF);

            for (int t = 1; t < trip.size(); t++) {
                int id = trip.get(t);
                int g = (int) getPairFromId(id).getFirst();
                int gx = gasolineres.get(g).getCoordX();
                int gy = gasolineres.get(g).getCoordY();
                km += getDistance(gx, gy, gxF, gyF)
                        +  getDistance(cx, cy, gx, gy)
                        -  getDistance(gxF, gyF, cx, cy);
            }
        }
        return km;
    }

    //getters

    //serà els km que han fet tots els camions en aquest estat
    public int getKmTotales() {
        int km = 0;
        for (int i = 0; i < recorregutCentre.length; ++i) {
            km += recorregutCentre[i];
        }
        return km;
    }

    //serà un array de n peticions completades i el valor són els dies de retràs de la petició
    public int[] getPeticionesCompletadas(){
        int[] diesPeticions = new int[peticionsServides.size()];
        for (int i = 0; i < peticionsServides.size(); ++i) {
            diesPeticions[i] = getNumDiesPeticio(peticionsServides.get(i));
        }
        return diesPeticions;
    }

    public int getNumDiesPeticio(Pair peticio) {
        int id_gasolinera = (int) peticio.getFirst();
        int id_peticio = (int) peticio.getSecond();
        return gasolineres.get(id_gasolinera).getPeticiones().get(id_peticio);
    }

    public int[] getDiesPeticionsServides() {
        int res[] = new int[10];

        for (int i = 0; i < peticionsServides.size(); ++i) {
            int idG = (int) peticionsServides.get(i).getFirst();
            int idP = (int) peticionsServides.get(i).getSecond();
            int dies = gasolineres.get(idG).getPeticiones().get(idP);
            if (dies < 10) ++res[dies];
        }

        return res;
    }

    public double getMitjanaDies() {
        double res = 0;
        int count = 0;
        for (Pair peticionsPendent : peticionsPendents) {
            int idG = (int) peticionsPendent.getFirst();
            int idP = (int) peticionsPendent.getSecond();
            int dies = gasolineres.get(idG).getPeticiones().get(idP);
            ++count;
            res += dies;
        }
        res = res/count;
        return res;
    }

    public int getDies(int id_g, int id_petico) {
        return gasolineres.get(id_g).getPeticiones().get(id_petico);
    }

    public List<Pair> getPeticionsPendents() {
        return peticionsPendents;
    }

    public int getPeticionsServides() {return peticionsServides.size();}

    public void impirmir_kmCentre() {
        System.out.println("KM per centre: ");
        for (int j : recorregutCentre) {
            System.out.print(j + " ");
        }
    }

    public void imprimir_metriques() {
        System.out.println("Peticions servides: " + peticionsServides.size());
        System.out.println("Peticions pendents: " + peticionsPendents.size());
        System.out.println("KM: " + getKmTotales());
        System.out.println("benefici: " + heuristica_1());
        impirmir_kmCentre();
        System.out.println();
    }

    public int getUseHeuristic() {return useHeuristic;}

    //selseccio de quina funcio heuristica utilitzar
    public void setUseHeuristic(int f_id) {useHeuristic = f_id;}

    public int getMaxViatges() { return maxViatges; }

    public int getMaxDiposit() { return maxDiposit; }

}
