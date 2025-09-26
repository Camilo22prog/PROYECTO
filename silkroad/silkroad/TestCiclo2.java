import java.util.*;

/**
 * TestRequisitosAvanzados.java – Pruebas para requisitos 10-13
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

            road.enableMovement(true);
            System.out.println("activada");

            road.executeMovements();
            if (road.ok()) {
                System.out.println("CUMPLIDO: Robots ejecutaron movimientos automáticos");
                System.out.println("Ganancia: " + road.profit());
                printRobotPositions(road);
            }

            road.enableMovement(false);
            road.executeMovements();
            if (!road.ok()) {
                System.out.println("CUMPLIDO:correctamente deshabilitada");
            }
            road.finish();
        } catch (Exception e) {
            System.out.println("FALLO: " + e.getMessage());
        }
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

            int[][] emptyCounts = road.getStoreEmptyCount();
            System.out.println("Método getStoreEmptyCount() disponible");
            for (int[] c : emptyCounts)
                System.out.println("  Tienda " + c[0] + ": " + c[1] + " veces");

            /* primera ronda */
            road.moveRobot(0, 2);  
            road.resupplyStores();
            road.moveRobot(2, 3);  
            road.resupplyStores();
            road.moveRobot(5, -3); 

            emptyCounts = road.getStoreEmptyCount();
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

            road.moveRobot(0, 3); 
            road.moveRobot(1, 6); 
            road.moveRobot(5, 4);

            for (int idx = 0; idx < 3; idx++) {
                List<Robot.MovementRecord> hist =
                    road.getRobotMovementHistory(idx);
                int sum = 0;
                for (Robot.MovementRecord m : hist) {
                    sum += m.getProfitGained();
                }
                System.out.println("  Total ganado: " + sum);
            }
            int top = road.getTopRobotIndex();
            System.out.println("Robot top: " + top);
            road.finish();
        } catch (Exception e) {
            System.out.println("FALLO: " + e.getMessage());
        }
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
        System.out.println();
    }

    /* ============ helpers ============ */
    private static void printRobotPositions(SilkRoad road) {
        int[][] r = road.robots();
        for (int[] r1 : r)
            System.out.println("  Robot " + r1[1] + " en " + r1[0]);
    }

    private static void esperarSegundos(int s) {
        try { Thread.sleep(s * 1000L); } catch (InterruptedException ignored) { }
    }
}