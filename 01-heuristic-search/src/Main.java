
import java.util.*;

import Classes.*;
import IA.TSP2.*;
import aima.search.informed.SimulatedAnnealingSearch;
import pracBusquedaLocal.*;

import aima.basic.XYLocation;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;

import java.time.Duration;
import java.time.Instant;

public class Main {
    public static void main (String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n==== Practica IA - Menu ====\n");
            System.out.println("1) Personalizado (introducir parámetros)");
            System.out.println("2) Tests disponibles");
            System.out.println("3) Salir");
            System.out.print("Selecciona una opción: ");
            String opt = scanner.nextLine().trim();

            if (opt.equals("1")) {
                runPersonalizado(scanner);
            } else if (opt.equals("2")) {
                runTestsMenu(scanner);
            } else if (opt.equals("3") || opt.equalsIgnoreCase("q") || opt.equalsIgnoreCase("exit")) {
                System.out.println("Saliendo.");
                break;
            } else {
                System.out.println("Opción no válida. Intenta de nuevo.");
            }
        }
        scanner.close();
    }

    private static void runPersonalizado(Scanner scanner) {
        try {
            System.out.print("Semilla (int, e.g. 1234): ");
            int seed = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Número de centros de distribución (int): ");
            int nCentres = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Número de gasolineras (int): ");
            int nGasolineres = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Camiones por centro (int, normalmente 1): ");
            int nCamions = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Viatges por camión (int): ");
            int viatges = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Kms máximos por camión (int): ");
            int kms = Integer.parseInt(scanner.nextLine().trim());

            System.out.println("Estrategia inicial (empty / greedy / random) [empty]: ");
            String strategy = scanner.nextLine().trim();
            if (strategy.isEmpty()) strategy = "empty";

            System.out.println("Modo de búsqueda: 0=HillClimbing, 1=SimulatedAnnealing [0]: ");
            String modoStr = scanner.nextLine().trim();
            int modo = modoStr.isEmpty() ? 0 : Integer.parseInt(modoStr);

            // create board
            DistribucioBoard board = new DistribucioBoard(nGasolineres, nCentres, nCamions, seed, 1, viatges, kms, strategy);
            board.setUseHeuristic(2);
            // default allowed ops
            board.setAllowedOps(new boolean[]{true, true, true, true, false});

            System.out.println("\nEstado inicial (beneficio / métricas):");
            board.imprimir_benefici();
            board.imprimir_metriques();

            executeSearch(board, modo);

        } catch (Exception e) {
            System.out.println("Error leyendo parámetros: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void runTestsMenu(Scanner scanner) {
        System.out.println("Tests disponibles:");
        System.out.println("1) run_experiment1 ");
        System.out.println("2) test3 (varias configuraciones de Simulated Annealing)");
        System.out.println("3) test4 (experimentos de escalado)");
        System.out.print("Selecciona test o 'b' para volver: ");
        String t = scanner.nextLine().trim();
        if (t.equals("1")) {
            System.out.println("Ejecutando run_experiment1()");
            run_experiment1();

        } else if (t.equals("2")) {
            System.out.println("Ejecutando test3 con board por defecto (100 gasolineras, 10 centros)");
            DistribucioBoard b = new DistribucioBoard(100, 10, 1, 1234, 1, 5, 640, "empty");
            b.setUseHeuristic(2);
            test3(b);

        } else if (t.equals("3")) {
            System.out.println("Ejecutando test4 (serie de tamaños)");
            test4();
        } else {
            System.out.println("Volviendo al menu.");
        }
    }

    private static void executeSearch(DistribucioBoard estado_inicial, int modo) {
        try {
            Search alg;
            Problem p;
            if (modo == 1) {
                alg = new SimulatedAnnealingSearch(5000, 1, 5, 0.1);
                p = new Problem(estado_inicial, new SuccesorAnealing(), new pracGoalTest(), new Heuristica());
            } else {
                alg = new HillClimbingSearch();
                p = new Problem(estado_inicial, new PracSuccesorFunction(), new pracGoalTest(), new Heuristica());
            }

            Instant inicio = Instant.now();
            SearchAgent agent = new SearchAgent(p, alg);
            Instant fin = Instant.now();

            printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());

            DistribucioBoard goal = (DistribucioBoard) alg.getGoalState();
            if (goal == null) {
                System.out.println("No se obtuvo estado objetivo (goal == null)");
            } else {
                System.out.println("Resultado final:");
                goal.imprimir_benefici();
                goal.imprimir_metriques();
            }
            Duration duration = Duration.between(inicio, fin);
            System.out.println("Tiempo de búsqueda: " + duration.toMillis() + " ms");

        } catch (Exception e) {
            System.out.println("Error durante la búsqueda: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void run_experiment1() {
        boolean[] operations = {true, true, false, false, false};
        for (int i = 2; i < operations.length; ++i) {
            operations[i] = true;
            DistribucioBoard ini = new DistribucioBoard(100, 10, 1, 1234, 5, 5, 640, "greedy");
            ini.setUseHeuristic(2);
            ini.setAllowedOps(operations);
            ini.imprimir_benefici();
            ini.imprimir_metriques();
            System.out.println("----------------------");

            Search alg = new HillClimbingSearch();

            Problem p = new Problem(ini, new PracSuccesorFunction(), new pracGoalTest(), new Heuristica());
            try {
                SearchAgent ag = new SearchAgent(p, alg);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            DistribucioBoard goal = (DistribucioBoard) alg.getGoalState();
            if (goal != null) {
                goal.imprimir_benefici();
                goal.imprimir_metriques();
            }

            operations[i] = false;
            System.out.println();
        }


    }

    private static void runHillClimbing(DistribucioBoard board, boolean prints) {
        System.out.println("\nHillClimbing  -->");
        try {
            Problem problem =  new Problem(board, new PracSuccesorFunction(), new pracGoalTest(), new Heuristica());
            Search search =  new HillClimbingSearch();
            SearchAgent agent = new SearchAgent(problem,search);

            System.out.println();
            if (prints) {
                printActions(agent.getActions());
                printInstrumentation(agent.getInstrumentation());
                DistribucioBoard goal = (DistribucioBoard) search.getGoalState();
                if (goal != null) goal.imprimir_benefici();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printInstrumentation(Properties properties) {
        Iterator keys = properties.keySet().iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            String property = properties.getProperty(key);
            System.out.println(key + " : " + property);
        }
    }

    private static void printActions(List<?> actions) {
        //evita errors de convertir la classe a string
        for (Object action : actions) {
            System.out.println(action.toString());
        }
    }

    private static void test3(DistribucioBoard estado_inicial) {
        //mirar
        Integer steps[] = {5000, 10000, 50000, 75000, 100000, 150000, 200000, 300000, 500000, 1000000, 2000000};
        for (int i = 0; i < 11; ++i) {
            double res = 0;
            for (int j = 0; j < 10; ++j) { //probar 10 vegades
                Search alg = new SimulatedAnnealingSearch(steps[i], 1, 5, 0.5);
                Problem p = new Problem(estado_inicial, new SuccesorAnealing(), new pracGoalTest(), new Heuristica());
                try {
                    SearchAgent agent = new SearchAgent(p, alg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                DistribucioBoard goal = (DistribucioBoard) alg.getGoalState();
                if (goal != null) res = res + (double) goal.get_benefici();
            }
            System.out.println("------------------------");
            System.out.println("mitjana steps " + steps[i] + " : " + res / 10);
        }


        Integer K[] = {1, 5, 25, 100};
        Double lambda[] = {1.0, 0.1, 0.01};
        for (int k = 0; k < 4; ++k) {
            for (int i = 0; i < 3; ++i) {
                double res = 0;
                for (int j = 0; j < 10; ++j) { //probar 10 vegades
                    Search alg = new SimulatedAnnealingSearch(5000, 1, K[k], lambda[i]);
                    Problem p = new Problem(estado_inicial, new SuccesorAnealing(), new pracGoalTest(), new Heuristica());
                    try {
                        SearchAgent agent = new SearchAgent(p, alg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    DistribucioBoard goal = (DistribucioBoard) alg.getGoalState();
                    if (goal != null) res = res + (double) goal.get_benefici();

                }
                System.out.println("------------------------");
                System.out.println("mitjana k " + K[k] + " i lambda " + lambda[i] + " : " + res / 10);
            }
        }


        Integer K2[] = {15, 20, 25, 30, 35, 40, 45, 50};
        Double lambda2[] = {0.001, 0.0001, 0.00001};
        for (int k = 0; k < 8; ++k) {
            for (int i = 0; i < 3; ++i) {
                double res = 0;
                for (int j = 0; j < 10; ++j) { //probar 10 vegades
                    Search alg = new SimulatedAnnealingSearch(5000, 1, K2[k], lambda2[i]);
                    Problem p = new Problem(estado_inicial, new SuccesorAnealing(), new pracGoalTest(), new Heuristica());
                    try {
                        SearchAgent agent = new SearchAgent(p, alg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    DistribucioBoard goal = (DistribucioBoard) alg.getGoalState();
                    if (goal != null) res = res + (double) goal.get_benefici();

                }
                System.out.println("------------------------");
                System.out.println("mitjana k " + K2[k] + " i lambda " + lambda2[i] + " : " + res / 10);
            }
        }

    }

    private static void test4() {
        // limit experiments to safe sizes to avoid OOM during development
        Integer c[] = {10, 20, 30, 40, 50, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000, 2000, 3000, 4000, 5000};
        Integer g[] = {100, 200, 300, 400, 500, 1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000, 20000, 30000, 40000, 50000};

        for (int i = 0; i < c.length; ++i) {
            DistribucioBoard estado_inicial = new DistribucioBoard(g[i], c[i], 1, 1234, 1, 5, 640, "empty");
            estado_inicial.setUseHeuristic(2);
            // ensure successor functions have allowed operations configured (avoid NPE in successor functions)
//            estado_inicial.setAllowedOps(new boolean[]{true, true, true, true, false});
//            estado_inicial.imprimir_benefici();
//            System.out.println("------------------------");

            // start timer immediately before launching the search
            Instant inicio = Instant.now();
            Search alg = new SimulatedAnnealingSearch(5000, 1, 5, 0.1);
            Problem p = new Problem(estado_inicial, new SuccesorAnealing(), new pracGoalTest(), new Heuristica());
            try {
                SearchAgent agent = new SearchAgent(p, alg);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // stop timer right after the search finishes
            Instant fin = Instant.now();
            DistribucioBoard goal = (DistribucioBoard) alg.getGoalState();
            Duration duration = Duration.between(inicio, fin);
            System.out.println("------------------------");
            System.out.println("Simulated anealing");
            if (goal == null) {
                System.out.println("No goal found by search (goal is null).\n");
                System.out.println("El temps d'execució de " + c[i] + " centres i " + g[i] + " gasolineras : " + duration.toMillis() + " ms");
            } else {
                double res = (double) goal.get_benefici();
                System.out.println("El benefici és : " + res);
                System.out.println("El temps d'execució de " + c[i] + " centres i " + g[i] + " gasolineras : " + duration.toMillis() + " ms");
            }
        }

        for (int i = 0; i < c.length; ++i) {
            DistribucioBoard estado_inicial = new DistribucioBoard(g[i], c[i], 1, 1234, 1, 5, 640, "empty");
            estado_inicial.setUseHeuristic(2);
            // ensure successor functions have allowed operations configured (avoid NPE in successor functions)
//            estado_inicial.setAllowedOps(new boolean[]{true, true, true, true, false});
//            estado_inicial.imprimir_benefici();
//            System.out.println("------------------------");

            // measure time around the search
            Instant inicio = Instant.now();
            Search alg = new HillClimbingSearch();
            Problem p = new Problem(estado_inicial, new PracSuccesorFunction(), new pracGoalTest(), new Heuristica());
            try {
                SearchAgent agent = new SearchAgent(p, alg);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Instant fin = Instant.now();
            DistribucioBoard goal = (DistribucioBoard) alg.getGoalState();
            Duration duration = Duration.between(inicio, fin);
            System.out.println("------------------------");
            System.out.println("Hillclimbing");
            if (goal == null) {
                System.out.println("No goal found by search (goal is null).\n");
                System.out.println("El temps d'execució de " + c[i] + " centres i " + g[i] + " gasolineras : " + duration.toMillis() + " ms");
            } else {
                double res = (double) goal.get_benefici();
                System.out.println("El benefici és : " + res);
                System.out.println("El temps d'execució de " + c[i] + " centres i " + g[i] + " gasolineras : " + duration.toMillis() + " ms");
            }
        }

    }



    }
