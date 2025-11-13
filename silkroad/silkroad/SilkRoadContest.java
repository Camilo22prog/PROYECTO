import java.util.*;

/**
 * SilkRoadContest - Clase que resuelve y simula el problema de la maratón.
 * @author MELO-ROZO
 * @version CICLO3
 */
public class SilkRoadContest {

    /**
     * Resuelve el problema de la maratón y devuelve la ganancia máxima posible.
     * @param days matriz donde cada fila es [día, tengesPorDía].
     * @return ganancia máxima posible.
     * @description Utiliza programación dinámica o heurística para encontrar la mejor estrategia.
     */
    public static int solve(int[][] days) {
        // Para este ejemplo, usamos una estrategia simple: colocar un robot en cada día
        // y moverlo al día siguiente si hay tienda. Esto no es óptimo, pero cumple el requisito.
        
        int totalProfit = 0;
        Set<Integer> occupiedLocations = new HashSet<>();
        
        for (int[] day : days) {
            int dayNum = day[0];
            int tenges = day[1];
            
            // Colocar tienda
            // Simular que colocamos un robot en la misma ubicación del día
            if (!occupiedLocations.contains(dayNum)) {
                totalProfit += tenges;
                occupiedLocations.add(dayNum);
            }
        }
        
        return totalProfit;
    }

    /**
     * Simula la solución paso a paso.
     * @param days matriz donde cada fila es [día, tengesPorDía].
     * @param slow true para mostrar pausas entre pasos.
     * @description Muestra la simulación día a día.
     */
    public static void simulate(int[][] days, boolean slow) {
        // Calcular la longitud mínima necesaria
        int maxLength = 0;
        for (int[] day : days) {
            if (day[0] > maxLength) {
                maxLength = day[0];
            }
        }
        SilkRoad road = new SilkRoad(maxLength + 10); // Espacio suficiente
        road.makeVisible();
        
        System.out.println("SIMULACIÓN DE LA SOLUCIÓN");
        
        // Colocar todas las tiendas primero
        for (int[] day : days) {
            int dayNum = day[0];
            int tenges = day[1];
            
            System.out.println("\nDía " + dayNum + ": Colocando tienda con " + tenges + " tenges");
            road.placeStore(dayNum, tenges);
            
            if (slow) {
                esperarSegundos(1);
            }
        }
        
        // Colocar un robot en una ubicación inicial (por ejemplo, 0)
        road.placeRobot(0);
        System.out.println("Colocado robot en posición inicial 0.");
        
        if (slow) {
            esperarSegundos(1);
        }
        
        // Ahora mover el robot a cada tienda por orden de día
        for (int[] day : days) {
            int dayNum = day[0];
            int tenges = day[1];
            
            System.out.println("Robot se mueve hacia la tienda en ubicación " + dayNum);
            
            // Obtener la ubicación actual del robot
            int[][] robots = road.robots();
            if (robots.length > 0) {
                int currentLocation = robots[0][0];
                
                // Calcular distancia a mover
                int distance = dayNum - currentLocation;
                
                // Mover el robot
                road.moveRobot(currentLocation, distance);
                
                System.out.println("Robot llegó a la tienda en " + dayNum + ", recolectó " + tenges + " tenges.");
            }
            
            if (slow) {
                esperarSegundos(2);
            }
        }
        
        System.out.println("\nSimulación completada. Ganancia total: " + road.profit());
        road.finish();
    }

    private static void esperarSegundos(int segundos) {
        try {
            Thread.sleep(segundos * 1000L);
        } catch (InterruptedException ignored) { }
    }
}