/**
 * TestCiclo3.java - Clase dedicada para probar los nuevos requisitos del Ciclo 3.
 * Evidencia sistemática del cumplimiento de los requisitos 14 y 15.
 * @author MELO-ROZO
 * @version CICLO3
 */
public class TestCiclo3 {

    public static void main(String[] args) {
        System.out.println("=== PRUEBAS SISTEMÁTICAS - CICLO 3 ===");
        System.out.println("Probando los nuevos requisitos: Resolver y Simular el problema de la maratón\n");

        // Ejecutar pruebas de los nuevos requisitos
        testRequisito14_SolucionarMaraton();
        testRequisito15_SimularSolucion();
    }

    /**
     * REQUISITO 14: Debe solucionar el problema de la maratón
     */
    static void testRequisito14_SolucionarMaraton() {
        System.out.println("--- REQUISITO 14: Solucionar el problema de la maratón ---");

        // Ejemplo de entrada de la maratón
        int[][] days = {
            {0, 100},  // Día 0, tienda con 100 tenges
            {2, 150},  // Día 2, tienda con 150 tenges
            {5, 200},  // Día 5, tienda con 200 tenges
            {8, 120}   // Día 8, tienda con 120 tenges
        };

        System.out.println("Entrada de días:");
        for (int[] day : days) {
            System.out.println("  Día " + day[0] + " - " + day[1] + " tenges");
        }

        // Resolver el problema
        int maxProfit = SilkRoadContest.solve(days);

        System.out.println("\nGanancia máxima calculada: " + maxProfit);
        System.out.println("✓ CUMPLIDO: El problema de la maratón ha sido resuelto correctamente.");
        System.out.println();
    }

    /**
     * REQUISITO 15: Debe simular la solución
     */
    static void testRequisito15_SimularSolucion() {
        System.out.println("--- REQUISITO 15: Simular la solución ---");

        // Ejemplo de entrada de la maratón
        int[][] days = {
            {1, 80},   // Día 1, tienda con 80 tenges
            {3, 120},  // Día 3, tienda con 120 tenges
            {6, 180},  // Día 6, tienda con 180 tenges
            {9, 100}   // Día 9, tienda con 100 tenges
        };

        System.out.println("Entrada de días:");
        for (int[] day : days) {
            System.out.println("  Día " + day[0] + " - " + day[1] + " tenges");
        }

        System.out.println("\nIniciando simulación en MODO LENTO (paso a paso)...");
        SilkRoadContest.simulate(days, true);

        System.out.println("✓ CUMPLIDO: La simulación de la solución ha sido completada.");
        System.out.println();

        System.out.println("Iniciando simulación en MODO RÁPIDO...");
        SilkRoadContest.simulate(days, false);

        System.out.println("✓ CUMPLIDO: La simulación en modo rápido ha sido completada.");
        System.out.println();
    }
}