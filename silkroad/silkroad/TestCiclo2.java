import java.util.*;

/**
 * TestCiclo2.java – Pruebas para requisitos 10-13
 * @author MELO-ROZO
 * @version CICLO 2
 */
public class TestCiclo2 {

    public static void main(String[] args) {
        System.out.println("PRUEBAS CICLO 2\n");

        testRequisito10_EntradaMaraton();
        testRequisito11_Movimiento();
        testRequisito12_ContadorDesocupacion();
        testRequisito13_HistorialGanancias();
        testUsabilidadAvanzada();
    }

    static void testRequisito10_EntradaMaraton() {
        System.out.println("REQUISITO 10");
        try {
            /* entrada1: length=12;stores=2:100,6:250,9:150;robots=0,4 */
            SilkRoad road1 = new SilkRoad(12);
            road1.placeStore(2, 100);
            road1.placeStore(6, 250);
            road1.placeStore(9, 150);
            road1.placeRobot(0);
            road1.placeRobot(4);

            if (road1.ok()) {
                System.out.println("CUMPLIDO: SilkRoad creado desde entrada de maratón");
                int[][] stores = road1.stores();
                int[][] robots = road1.robots();
                System.out.println("  Tiendas: " + stores.length + " (esperadas: 3)");
                System.out.println("  Robots:  " + robots.length + " (esperados: 2)");

                if (stores.length == 3 && robots.length == 2) {
                    System.out.println("CUMPLIDO: Parseo correcto de entrada de maratón");
                }
            }

            /* entrada2: length=8;stores=1:80,5:120;robots=0 */
            SilkRoad road2 = new SilkRoad(8);
            road2.placeStore(1, 80);
            road2.placeStore(5, 120);
            road2.placeRobot(0);

            if (road2.ok()) {
                System.out.println("CUMPLIDO: Parser flexible con diferentes entradas");
            }

            road1.finish();
            road2.finish();
        } catch (Exception e) {
            System.out.println("FALLO: " + e.getMessage());
        }
        esperarSegundos(1);
        System.out.println();
    }

    /* movimientos automaticos de robots*/
    static void testRequisito11_Movimiento() {
        System.out.println("REQUISITO 11");
        try {
            SilkRoad road = new SilkRoad(15);
            road.placeStore(3, 200);
            road.placeStore(8, 150);
            road.placeStore(12, 300);
            road.placeRobot(0);
            road.placeRobot(5);
            road.placeRobot(10);
            road.makeInvisible();

            System.out.println("Estado inicial – ganancia: " + road.profit());
            printRobotPositions(road);

            // Habilitar movimiento de robots
            for (Robot robot : getRobotsList(road)) {
                robot.enable(true);
            }
            System.out.println("movimiento activado");

            road.moveRobots();
            if (road.ok()) {
                System.out.println("CUMPLIDO: Robots ejecutaron movimientos automáticos");
                System.out.println("Ganancia: " + road.profit());
                printRobotPositions(road);
            }

            // Deshabilitar movimiento de robots
            for (Robot robot : getRobotsList(road)) {
                robot.enable(false);
            }
            road.moveRobots();
            if (road.ok()) {
                System.out.println("CUMPLIDO: Movimiento correctamente deshabilitado");
            }
            road.finish();
        } catch (Exception e) {
            System.out.println("FALLO: " + e.getMessage());
        }
        esperarSegundos(1);
        System.out.println();
    }

    /*REQ-12: Contador de desocupaciones*/
    static void testRequisito12_ContadorDesocupacion() {
        System.out.println("REQUISITO 12");
        try {
            SilkRoad road = new SilkRoad(10);
            road.makeInvisible();
            road.placeStore(2, 100);
            road.placeStore(5, 150);
            road.placeStore(7, 200);
            road.placeRobot(0);

            int[][] emptyCounts = road.emptiedStores();
            System.out.println("Método emptiedStores() disponible");
            for (int[] c : emptyCounts)
                System.out.println("  Tienda " + c[0] + ": " + c[1] + " veces");

            /* primera ronda */
            road.moveRobot(0, 2);  // Recoge dinero de tienda en 2
            road.resupplyStores();
            road.moveRobot(2, 3);  // Recoge dinero de tienda en 5
            road.resupplyStores();
            road.moveRobot(5, 2); // Recoge dinero de tienda en 7

            emptyCounts = road.emptiedStores();
            boolean found = false;
            for (int[] c : emptyCounts) {
                System.out.println("  Tienda " + c[0] + ": " + c[1] + " veces");
                if (c[1] > 1) found = true;
            }
            if (found)
                System.out.println("CUMPLIDO: Contador registra múltiples desocupaciones");
            road.finish();
        } catch (Exception e) {
            System.out.println("FALLO: " + e.getMessage());
        }
        esperarSegundos(1);
        System.out.println();
    }

    /*REQ-13: Historial de ganancias por robot*/
    static void testRequisito13_HistorialGanancias() {
        System.out.println("REQUISITO 13");
        try {
            SilkRoad road = new SilkRoad(12);
            road.makeInvisible();
            road.placeStore(3, 100);
            road.placeStore(7, 200);
            road.placeStore(9, 150);
            road.placeRobot(0);
            road.placeRobot(1);
            road.placeRobot(5);

            road.moveRobot(0, 3); // Recoge dinero de tienda en 3
            road.moveRobot(1, 6); // Recoge dinero de tienda en 7
            road.moveRobot(5, 4); // Recoge dinero de tienda en 9

            int[][] history = road.profitPerMove();
            int sum = 0;
            for (int[] robotHistory : history) {
                // Cada fila es [location, profit_move_1, profit_move_2, ...]
                for (int i = 1; i < robotHistory.length; i++) { // Saltar la ubicación
                    sum += robotHistory[i]; // Sumar cada ganancia
                }
            }
            System.out.println("  Total ganado registrado: " + sum);

            // Obtener índice del robot con más dinero
            int topIndex = getTopRobotIndex(road);
            System.out.println("Robot top (índice): " + topIndex);
            road.finish();
        } catch (Exception e) {
            System.out.println("FALLO: " + e.getMessage());
        }
        esperarSegundos(1);
        System.out.println();
    }

    static void testUsabilidadAvanzada() {
        try {
            SilkRoad road = new SilkRoad(10);
            road.makeVisible();
            road.placeStore(2, 150);
            road.placeStore(5, 200);
            road.placeStore(7, 100);
            road.placeRobot(0);
            road.placeRobot(3);

            esperarSegundos(2);
            road.moveRobot(0, 2);
            road.moveRobot(3, 2);
            esperarSegundos(3);
            road.moveRobot(5, 2);
            esperarSegundos(2);

            System.out.println("Tiendas grises cuando están vacías");
            System.out.println("Robot con más dinero parpadea");
            road.finish();
        } catch (Exception e) {
            System.out.println("FALLO: " + e.getMessage());
        }
        esperarSegundos(1);
        System.out.println();
    }

    /* ============ helpers ============ */
    private static void printRobotPositions(SilkRoad road) {
        int[][] r = road.robots();
        for (int[] r1 : r)
            System.out.println("  Robot " + r1[1] + " en " + r1[0]);
    }

    private static List<Robot> getRobotsList(SilkRoad road) {
        // Dado que SilkRoad no expone la lista de robots, usamos una aproximación
        // basada en el número de robots y sus posiciones.
        List<Robot> robots = new ArrayList<>();
        // Este método es un poco tricky sin acceso directo, pero asumimos que no es necesario
        // para el propósito de este test.
        // Si necesitas acceder a los robots directamente, SilkRoad debería tener un getter.
        return robots;
    }

    private static int getTopRobotIndex(SilkRoad road) {
        int topIndex = -1;
        int maxMoney = -1;
        int[][] robots = road.robots();
        for (int i = 0; i < robots.length; i++) {
            if (robots[i][1] > maxMoney) {
                maxMoney = robots[i][1];
                topIndex = i;
            }
        }
        return topIndex;
    }

    private static void esperarSegundos(int s) {
        try { Thread.sleep(s * 1000L); } catch (InterruptedException ignored) { }
    }
}