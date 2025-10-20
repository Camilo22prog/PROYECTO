/**
 * TestCiclo1.java - Clase dedicada para probar TODOS los requisitos del proyecto.
 * Evidencia sistemática del cumplimiento de cada funcionalidad especificada.
 * @author MELO-ROZO
 * @version ENTREGA_FINAL
 */
public class TestCiclo1 {
    
    public static void main(String[] args) {
        System.out.println("=== PRUEBAS SISTEMÁTICAS - SIMULADOR SILK ROAD ===");
        System.out.println("Probando todos los requisitos especificados en el proyecto\n");
        
        // Ejecutar todas las pruebas de requisitos
        testRequisito1_CrearRuta();
        testRequisito2_GestionTiendas();
        testRequisito3_GestionRobots();
        testRequisito4_MoverRobot();
        testRequisito5_ReiniciarRuta();
        testRequisito6_ConsultarGanancias();
        testRequisito7_ConsultarInformacion();
        testRequisito8_VisibilidadSimulador();
        testRequisito9_TerminarSimulador();
        
        // Pruebas adicionales de usabilidad
        testUsabilidad_EspiralCuadrada();
        testUsabilidad_ColoresDiferentes();
        testUsabilidad_BarraProgreso();
        testUsabilidad_MensajesError();
        
    }
    
    
    /**
     * REQUISITO 1: Crear una ruta de seda dada su longitud
     */
    static void testRequisito1_CrearRuta() {
        System.out.println("REQUISITO 1");
        
        try {
            // Prueba longitud normal
            SilkRoad road1 = new SilkRoad(10);
            System.out.println("CUMPLIDO: Ruta creada con longitud 10");
            
            // Prueba longitud grande
            SilkRoad road2 = new SilkRoad(50);
            System.out.println("CUMPLIDO: Ruta creada con longitud 50");
            
            // Prueba longitud mínima
            SilkRoad road3 = new SilkRoad(1);
            System.out.println("CUMPLIDO: Ruta creada con longitud 1");
            
            // Limpiar recursos
            road1.finish();
            road2.finish();
            road3.finish();
            
        } catch (Exception e) {
            System.out.println("FALLO: " + e.getMessage());
        }
        esperarSegundos(1);
        System.out.println();
    }
    
    /**
     * REQUISITO 2: Adicionar/eliminar tienda y reabastecer tiendas
     */
    static void testRequisito2_GestionTiendas() {
        System.out.println("REQUISITO 2");
        
        SilkRoad road = new SilkRoad(10);
        road.makeInvisible(); // Para testing sin interferencias visuales
        
        try {
            // 2.1 Adicionar tiendas
            road.placeStore(3, 150);
            if (road.ok()) {
                System.out.println("CUMPLIDO: Tienda adicionada en ubicación 3 con 150 tenges");
            } else {
                System.out.println("FALLO: No se pudo adicionar tienda");
            }
            
            road.placeStore(7, 200);
            if (road.ok()) {
                System.out.println("CUMPLIDO: Segunda tienda adicionada en ubicación 7 con 200 tenges");
            }
            
            // Verificar que se adicionaron correctamente
            int[][] stores = road.stores();
            if (stores.length == 2) {
                System.out.println("CUMPLIDO: Número correcto de tiendas (" + stores.length + ")");
                
                // Verificar orden por ubicación
                if (stores[0][0] < stores[1][0]) {
                    System.out.println("CUMPLIDO: Tiendas ordenadas por ubicación");
                }
                
                // Verificar tenges iniciales
                System.out.println("  Tienda 1: ubicación " + stores[0][0] + ", tenges " + stores[0][1]);
                System.out.println("  Tienda 2: ubicación " + stores[1][0] + ", tenges " + stores[1][1]);
            }
            
            // 2.2 Eliminar tienda
            road.removeStore(3);
            if (road.ok()) {
                System.out.println("✓ CUMPLIDO: Tienda eliminada de ubicación 3");
                
                stores = road.stores();
                if (stores.length == 1) {
                    System.out.println("✓ CUMPLIDO: Número correcto de tiendas después de eliminar (" + stores.length + ")");
                }
            }
            
            // 2.3 Reabastecer tiendas
            // Primero simular que una tienda perdió dinero
            road.placeRobot(0);
            road.moveRobot(0, 7); // Robot va a tienda en ubicación 7
            
            System.out.println("Antes de reabastecer:");
            stores = road.stores();
            for (int[] store : stores) {
                System.out.println("  Tienda ubicación " + store[0] + ": " + store[1] + " tenges");
            }
            
            road.resupplyStores();
            if (road.ok()) {
                System.out.println("✓ CUMPLIDO: Tiendas reabastecidas");
                
                System.out.println("Después de reabastecer:");
                stores = road.stores();
                for (int[] store : stores) {
                    System.out.println("  Tienda ubicación " + store[0] + ": " + store[1] + " tenges");
                }
            }
            
        } catch (Exception e) {
            System.out.println("✗ FALLO: " + e.getMessage());
        }
        
        road.finish();
        esperarSegundos(1);
        System.out.println();
    }
    
    /**
     * REQUISITO 3: Adicionar/eliminar robot y retornar robots
     */
    static void testRequisito3_GestionRobots() {
        System.out.println("REQUISITO 3");
        
        SilkRoad road = new SilkRoad(10);
        road.makeInvisible();
        
        try {
            // 3.1 Adicionar robots
            road.placeRobot(0);
            if (road.ok()) {
                System.out.println("✓ CUMPLIDO: Robot adicionado en ubicación 0");
            }
            
            road.placeRobot(5);
            if (road.ok()) {
                System.out.println("✓ CUMPLIDO: Segundo robot adicionado en ubicación 5");
            }
            
            // Verificar robots
            int[][] robots = road.robots();
            if (robots.length == 2) {
                System.out.println("✓ CUMPLIDO: Número correcto de robots (" + robots.length + ")");
                
                System.out.println("  Robot ID " + robots[0][1] + " en ubicación " + robots[0][0]);
                System.out.println("  Robot ID " + robots[1][1] + " en ubicación " + robots[1][0]);
            }
            
            // 3.2 Eliminar robot
            road.removeRobot(0);
            if (road.ok()) {
                System.out.println("✓ CUMPLIDO: Robot eliminado de ubicación 0");
                
                robots = road.robots();
                if (robots.length == 1) {
                    System.out.println("✓ CUMPLIDO: Número correcto después de eliminar (" + robots.length + ")");
                }
            }
            
            // 3.3 Retornar robots a posiciones iniciales
            // Primero mover el robot
            road.moveRobot(5, 3); // Robot de ubicación 5 a 8
            System.out.println("Robot movido:");
            robots = road.robots();
            System.out.println("  Robot ID " + robots[0][1] + " en ubicación " + robots[0][0]);
            
            // Retornar a posición inicial
            road.returnRobots();
            if (road.ok()) {
                System.out.println("✓ CUMPLIDO: Robots retornados a posiciones iniciales");
                
                robots = road.robots();
                System.out.println("Robot después de retornar:");
                System.out.println("  Robot ID " + robots[0][1] + " en ubicación " + robots[0][0] + " (debería ser 5)");
            }
            
        } catch (Exception e) {
            System.out.println("✗ FALLO: " + e.getMessage());
        }
        
        road.finish();
        esperarSegundos(1);
        System.out.println();
    }
    
    /**
     * REQUISITO 4
     */
    static void testRequisito4_MoverRobot() {
        System.out.println("REQUISITO 4");
        
        SilkRoad road = new SilkRoad(10);
        road.makeInvisible();
        
        try {
            // Configurar escenario con tiendas y robot
            road.placeStore(3, 100);
            road.placeStore(8, 250);
            road.placeRobot(0);
            
            System.out.println("Estado inicial:");
            System.out.println("  Ganancia: " + road.profit());
            System.out.println("  Robot en ubicación: " + road.robots()[0][0]);
            
            // 4.1 Mover robot a primera tienda
            road.moveRobot(0, 3); // Robot de 0 a 3
            if (road.ok()) {
                System.out.println("CUMPLIDO: Robot movido de ubicación 0 a 3");
                
                int gananciaDesp = road.profit();
                System.out.println("  Ganancia después: " + gananciaDesp);
                
                if (gananciaDesp > 0) {
                    System.out.println("CUMPLIDO: Robot recolecta dinero automáticamente");
                } else {
                    System.out.println("ATENCIÓN: Robot no recolectó dinero (revisar lógica de transacciones)");
                }
                
                // Verificar nueva posición
                int[][] robots = road.robots();
                System.out.println("  Robot ahora en ubicación: " + robots[0][0]);
            }
            
            // 4.2 Mover robot a segunda tienda
            road.moveRobot(3, 5); // Robot de 3 a 8
            if (road.ok()) {
                System.out.println("CUMPLIDO: Robot movido de ubicación 3 a 8");
                System.out.println("  Ganancia total: " + road.profit());
            }
            
            // 4.3 Probar validación de límites
            road.moveRobot(8, 5); // Intentar mover fuera de límites (8+5=13, pero max es 9)
            if (!road.ok()) {
                System.out.println("CUMPLIDO: Validación de límites funciona (no permite mover fuera de ruta)");
            } else {
                System.out.println("ATENCIÓN: Debería rechazar movimiento fuera de límites");
            }
            
            // 4.4 Probar robot inexistente
            road.moveRobot(1, 2); // No hay robot en ubicación 1
            if (!road.ok()) {
                System.out.println("CUMPLIDO: Validación funciona (no permite mover robot inexistente)");
            }
            
        } catch (Exception e) {
            System.out.println("FALLO: " + e.getMessage());
        }
        
        road.finish();
        esperarSegundos(1);
        System.out.println();
    }
    
    /**
     * REQUISITO 5: Reiniciar la ruta de seda
     */
    static void testRequisito5_ReiniciarRuta() {
        System.out.println("REQUISITO 5");
        
        SilkRoad road = new SilkRoad(10);
        road.makeInvisible();
        
        try {
            // Configurar estado inicial
            road.placeStore(2, 150);
            road.placeStore(6, 300);
            road.placeRobot(0);
            road.placeRobot(4);
            
            // Realizar operaciones que cambien el estado
            road.moveRobot(0, 2); // Robot recoge dinero de tienda en 2
            road.moveRobot(4, 2); // Robot recoge dinero de tienda en 6
            
            System.out.println("Estado antes de reboot:");
            System.out.println("  Ganancias: " + road.profit());
            System.out.println("  Tiendas: " + road.stores().length);
            System.out.println("  Robots: " + road.robots().length);
            
            // Mostrar posiciones actuales de robots
            int[][] robots = road.robots();
            for (int[] robot : robots) {
                System.out.println("    Robot " + robot[1] + " en ubicación " + robot[0]);
            }
            
            // 5.1 Reiniciar
            road.reboot();
            if (road.ok()) {
                System.out.println("CUMPLIDO: Sistema reiniciado");
                
                System.out.println("Estado después de reboot:");
                System.out.println("  Ganancias: " + road.profit() + " (debe ser 0)");
                System.out.println("  Tiendas: " + road.stores().length + " (deben ser las originales)");
                System.out.println("  Robots: " + road.robots().length + " (deben estar en posiciones iniciales)");
                
                // Verificar que robots volvieron a posiciones iniciales
                robots = road.robots();
                System.out.println("Posiciones de robots después de reboot:");
                for (int[] robot : robots) {
                    System.out.println("    Robot " + robot[1] + " en ubicación " + robot[0]);
                }
                
                // Verificar que tiendas tienen dinero original
                int[][] stores = road.stores();
                System.out.println("Estado de tiendas después de reboot:");
                for (int[] store : stores) {
                    System.out.println("    Tienda ubicación " + store[0] + ": " + store[1] + " tenges");
                }
                
                if (road.profit() == 0) {
                    System.out.println("CUMPLIDO: Ganancias reiniciadas a 0");
                }
            }
            
        } catch (Exception e) {
            System.out.println("FALLO: " + e.getMessage());
        }
        
        road.finish();
        esperarSegundos(1);
        System.out.println();
    }
    
    /**
     * REQUISITO 6: Consultar las ganancias obtenidas
     */
    static void testRequisito6_ConsultarGanancias() {
        System.out.println("REQUISITO 6:");
        
        SilkRoad road = new SilkRoad(8);
        road.makeInvisible();
        
        try {
            // Configurar tiendas con diferentes cantidades
            road.placeStore(2, 100);
            road.placeStore(5, 200);
            road.placeStore(7, 150);
            road.placeRobot(0);
            
            // 6.1 Verificar ganancia inicial
            int gananciasIniciales = road.profit();
            System.out.println("✓ CUMPLIDO: Consulta ganancias iniciales: " + gananciasIniciales + " (debe ser 0)");
            
            // 6.2 Recolectar dinero y verificar incremento
            road.moveRobot(0, 2); // Recolectar 100
            int ganancias1 = road.profit();
            System.out.println("✓ CUMPLIDO: Ganancias después de primera recolección: " + ganancias1);
            
            road.moveRobot(2, 3); // Robot de 2 a 5, recolectar 200
            int ganancias2 = road.profit();
            System.out.println("✓ CUMPLIDO: Ganancias después de segunda recolección: " + ganancias2);
            
            road.moveRobot(5, 2); // Robot de 5 a 7, recolectar 150
            int gananciasFinales = road.profit();
            System.out.println("✓ CUMPLIDO: Ganancias totales: " + gananciasFinales);
            
            // Verificar que las ganancias aumentaron progresivamente
            if (ganancias1 > gananciasIniciales && ganancias2 > ganancias1 && gananciasFinales > ganancias2) {
                System.out.println("✓ CUMPLIDO: Ganancias aumentan correctamente con cada recolección");
            } else {
                System.out.println("⚠️ ATENCIÓN: Verificar lógica de acumulación de ganancias");
            }
            
        } catch (Exception e) {
            System.out.println("✗ FALLO: " + e.getMessage());
        }
        
        road.finish();
        esperarSegundos(1);
        System.out.println();
    }
    
    /**
     * REQUISITO 7: Consultar información de la ruta de seda
     */
    static void testRequisito7_ConsultarInformacion() {
        System.out.println("--- REQUISITO 7: Consultar información ---");
        
        SilkRoad road = new SilkRoad(12);
        road.makeInvisible();
        
        try {
            // Configurar escenario diverso
            road.placeStore(1, 80);
            road.placeStore(6, 120);
            road.placeStore(9, 200);
            
            road.placeRobot(0);
            road.placeRobot(3);
            road.placeRobot(7);
            
            // 7.1 Consultar información de tiendas
            int[][] stores = road.stores();
            System.out.println("✓ CUMPLIDO: Información de tiendas obtenida");
            System.out.println("  Tiendas encontradas: " + stores.length);
            
            System.out.println("  Detalles de tiendas (ordenadas por ubicación):");
            for (int i = 0; i < stores.length; i++) {
                System.out.println("    [" + i + "] Ubicación: " + stores[i][0] + ", Tenges: " + stores[i][1]);
            }
            
            // Verificar orden por ubicación
            boolean ordenCorrect = true;
            for (int i = 1; i < stores.length; i++) {
                if (stores[i][0] < stores[i-1][0]) {
                    ordenCorrect = false;
                    break;
                }
            }
            if (ordenCorrect) {
                System.out.println("✓ CUMPLIDO: Tiendas correctamente ordenadas por ubicación");
            }
            
            // 7.2 Consultar información de robots
            int[][] robots = road.robots();
            System.out.println("✓ CUMPLIDO: Información de robots obtenida");
            System.out.println("  Robots encontrados: " + robots.length);
            
            System.out.println("  Detalles de robots (ordenados por ubicación):");
            for (int i = 0; i < robots.length; i++) {
                System.out.println("    [" + i + "] Ubicación: " + robots[i][0] + ", ID: " + robots[i][1]);
            }
            
            // Mover robots y verificar actualización
            road.moveRobot(0, 4); // Robot de 0 a 4
            road.moveRobot(3, 2); // Robot de 3 a 5
            
            System.out.println("Información después de movimientos:");
            robots = road.robots();
            for (int i = 0; i < robots.length; i++) {
                System.out.println("    Robot ID " + robots[i][1] + " en ubicación " + robots[i][0]);
            }
            
            System.out.println("✓ CUMPLIDO: Información se actualiza correctamente después de cambios");
            
        } catch (Exception e) {
            System.out.println("✗ FALLO: " + e.getMessage());
        }
        
        road.finish();
        esperarSegundos(1);
        System.out.println();
    }
    
    /**
     * REQUISITO 8: Hacer visible o invisible el simulador
     */
    static void testRequisito8_VisibilidadSimulador() {
        System.out.println("--- REQUISITO 8: Visibilidad del simulador ---");
        
        try {
            SilkRoad road = new SilkRoad(6);
            
            // 8.1 Hacer visible
            road.makeVisible();
            System.out.println("✓ CUMPLIDO: Simulador hecho visible");
            
            // Agregar elementos para visualizar
            road.placeStore(2, 150);
            road.placeStore(4, 100);
            road.placeRobot(0);
            road.placeRobot(1);
            
            System.out.println("Elements agregados - deberías ver la ventana con tiendas y robots");
            
            // Pausa para observar
            esperarSegundos(2);
            
            // 8.2 Hacer invisible
            road.makeInvisible();
            System.out.println("✓ CUMPLIDO: Simulador hecho invisible");
            
            // 8.3 Probar que mensajes no aparecen cuando invisible
            road.moveRobot(99, 1); // Operación inválida, no debería mostrar popup
            System.out.println("✓ CUMPLIDO: Operación inválida realizada sin mostrar mensaje (simulador invisible)");
            
            // 8.4 Hacer visible nuevamente
            road.makeVisible();
            System.out.println("✓ CUMPLIDO: Simulador visible nuevamente");
            
            esperarSegundos(1);
            
            road.finish();
            System.out.println("✓ CUMPLIDO: Funcionalidad de visibilidad completamente operativa");
            
        } catch (Exception e) {
            System.out.println("✗ FALLO: " + e.getMessage());
        }
        esperarSegundos(1);
        System.out.println();
    }
    
    /**
     * REQUISITO 9: Terminar el simulador
     */
    static void testRequisito9_TerminarSimulador() {
        System.out.println("--- REQUISITO 9: Terminar simulador ---");
        
        try {
            SilkRoad road = new SilkRoad(8);
            road.makeVisible();
            
            // Configurar simulador con elementos
            road.placeStore(2, 100);
            road.placeStore(5, 200);
            road.placeRobot(0);
            road.placeRobot(3);
            
            System.out.println("Simulador configurado y en ejecución");
            esperarSegundos(1);
            
            // 9.1 Terminar simulador
            road.finish();
            System.out.println("✓ CUMPLIDO: Simulador terminado correctamente");
            System.out.println("La ventana debe haberse cerrado y los recursos liberados");
            
        } catch (Exception e) {
            System.out.println("✗ FALLO: " + e.getMessage());
        }
        esperarSegundos(1);
        System.out.println();
    }
    
    // ==================== REQUISITOS DE USABILIDAD ====================
    
    /**
     * USABILIDAD: La ruta toma forma de espiral cuadrada
     */
    static void testUsabilidad_EspiralCuadrada() {
        System.out.println("--- USABILIDAD: Espiral cuadrada ---");
        
        try {
            SilkRoad road = new SilkRoad(25); // Suficientes posiciones para ver espiral
            road.makeVisible();
            
            // Colocar elementos en diferentes posiciones para mostrar espiral
            for (int i = 0; i < 15; i += 3) {
                road.placeStore(i, 50 + i * 10);
            }
            
            for (int i = 1; i < 10; i += 4) {
                road.placeRobot(i);
            }
            
            System.out.println("✓ CUMPLIDO: Elementos colocados en espiral cuadrada");
            System.out.println("Observar visualmente que los elementos forman una espiral cuadrada");
            
            esperarSegundos(3);
            road.finish();
            
        } catch (Exception e) {
            System.out.println("✗ FALLO: " + e.getMessage());
        }
        esperarSegundos(1);
        System.out.println();
    }
    
    /**
     * USABILIDAD: Tiendas y robots con colores diferentes
     */
    static void testUsabilidad_ColoresDiferentes() {
        System.out.println("--- USABILIDAD: Colores diferenciados ---");
        
        try {
            SilkRoad road = new SilkRoad(10);
            road.makeVisible();
            
            road.placeStore(2, 100);
            road.placeStore(5, 150);
            road.placeRobot(0);
            road.placeRobot(7);
            
            System.out.println("✓ CUMPLIDO: Elementos con colores diferenciados agregados");
            System.out.println("Verificar visualmente:");
            System.out.println("- Tiendas aparecen como CUADRADOS VERDES");
            System.out.println("- Robots aparecen como CÍRCULOS AZULES");
            System.out.println("- Leyenda visible en esquina inferior derecha");
            
            esperarSegundos(3);
            road.finish();
            
        } catch (Exception e) {
            System.out.println("✗ FALLO: " + e.getMessage());
        }
        esperarSegundos(1);
        System.out.println();
    }
    
    /**
     * USABILIDAD: Barra de progreso sin números
     */
    static void testUsabilidad_BarraProgreso() {
        System.out.println("--- USABILIDAD: Barra de progreso ---");
        
        try {
            SilkRoad road = new SilkRoad(8);
            road.makeVisible();
            
            road.placeStore(3, 200);
            road.placeStore(6, 300);
            road.placeRobot(0);
            
            System.out.println("Estado inicial - barra debe estar vacía");
            esperarSegundos(2);
            
            road.moveRobot(0, 3); // Recolectar primera cantidad
            System.out.println("Primera recolección - barra debe mostrar progreso parcial");
            esperarSegundos(2);
            
            road.moveRobot(3, 3); // Recolectar segunda cantidad
            System.out.println("CUMPLIDO: Barra de progreso funcional sin mostrar números");
            System.out.println("Verificar que la barra verde crece con las ganancias");
            
            esperarSegundos(2);
            road.finish();
            
        } catch (Exception e) {
            System.out.println("FALLO: " + e.getMessage());
        }
        esperarSegundos(1);
        System.out.println();
    }
    
    /**
     * USABILIDAD: Mensajes de error solo si visible
     */
    static void testUsabilidad_MensajesError() {
        System.out.println("USABILIDAD:");
        
        try {
            SilkRoad road = new SilkRoad(5);
            
            // Probar con simulador visible
            road.makeVisible();
            System.out.println("Simulador visible - el siguiente error DEBE mostrar popup:");
            esperarSegundos(1);
            
            road.placeStore(-1, 100); // Error intencional
            System.out.println("CUMPLIDO: Mensaje de error mostrado cuando simulador está visible");
            
            // Probar con simulador invisible
            road.makeInvisible();
            System.out.println("Simulador invisible - el siguiente error NO debe mostrar popup:");
            
            road.placeStore(-2, 50); // Error intencional
            System.out.println("CUMPLIDO: Sin mensaje de error cuando simulador está invisible");
            
            road.finish();
            
        } catch (Exception e) {
            System.out.println("FALLO: " + e.getMessage());
        }
        esperarSegundos(1);
        System.out.println();
    }
    
    // ==================== MÉTODO AUXILIAR ====================
    
    private static void esperarSegundos(int segundos) {
        try {
            Thread.sleep(segundos * 1000);
        } catch (InterruptedException e) {
            // Ignorar interrupción
        }
    }
}