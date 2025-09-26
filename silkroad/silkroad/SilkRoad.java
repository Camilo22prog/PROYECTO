import java.util.ArrayList;
import java.util.List;

/**
 * SilkRoad.java - Coordinador principal simplificado
 * Solo maneja: coordinación, validación, y estado general
 * @author MELO-ROZO
 * @version CICLO2
 */
public class SilkRoad {
    private int length;
    private final List<Store> stores = new ArrayList<>();
    private final List<Robot> robots = new ArrayList<>();
    private final SilkDisplay display;
    
    // Estado básico
    private int totalProfit = 0;
    private boolean lastOperationOk = true;
    
    // Estados iniciales (para reboot)
    private final List<StoreSnapshot> initialStores = new ArrayList<>();
    private final List<RobotSnapshot> initialRobots = new ArrayList<>();
    
    // Constructor básico (sin parsing)
    public SilkRoad(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("La longitud debe ser positiva.");
        }
        this.length = length;
        this.display = new SilkDisplay(length, stores, robots);
    }
    
    // ============ OPERACIONES PRINCIPALES ============
    
    public void placeStore(int location, int tenges) {
        lastOperationOk = false;
        
        if (!isValidLocation(location) || tenges < 0 || findStoreAt(location) != null) {
            showError("No se puede colocar la tienda en la ubicación " + location);
            return;
        }
        
        Store newStore = new Store(location, tenges);
        stores.add(newStore);
        display.addStore(newStore);
        
        initialStores.add(new StoreSnapshot(location, tenges));
        updateProfitDisplay();
        lastOperationOk = true;
    }
    
    public void placeRobot(int location) {
        lastOperationOk = false;
        
        if (!isValidLocation(location)) {
            showError("Ubicación inválida: " + location);
            return;
        }
        
        Robot newRobot = new Robot(location);
        robots.add(newRobot);
        display.addRobot(newRobot);
        
        initialRobots.add(new RobotSnapshot(robots.size() - 1, location));
        lastOperationOk = true;
    }
    
    public void removeStore(int location) {
        lastOperationOk = false;
        
        Store storeToRemove = findStoreAt(location);
        if (storeToRemove == null) {
            showError("No hay tienda " + location);
            return;
        }
        
        stores.remove(storeToRemove);
        display.removeStore(storeToRemove);
        initialStores.removeIf(s -> s.location == location);
        updateProfitDisplay();
        lastOperationOk = true;
    }
    
    public void removeRobot(int location) {
        lastOperationOk = false;
        
        Robot robotToRemove = findFirstRobotAt(location);
        if (robotToRemove == null) {
            showError("Ubicación inválida" + location);
            return;
        }
        
        robots.remove(robotToRemove);
        display.removeRobot(robotToRemove);
        lastOperationOk = true;
    }
    
    /**
     * Movimiento manual de robot (simplificado)
     */
    public void moveRobot(int location, int meters) {
        lastOperationOk = false;
        
        Robot robot = findFirstRobotAt(location);
        if (robot == null) {
            showError("No hay robot " + location);
            return;
        }
        
        int newLocation = location + meters;
        if (!isValidLocation(newLocation)) {
            showError("El robot se movería fuera de los límites");
            return;
        }
        
        robot.move(meters);
        
        // Procesar transacción si hay tienda
        Store storeAtDestination = findStoreAt(newLocation);
        if (storeAtDestination != null && storeAtDestination.canBeEmptied()) {
            int profit = storeAtDestination.empty();
            robot.addMoney(profit);
            totalProfit += profit;
        }
        
        display.refreshAfterMove();
        updateProfitDisplay();
        updateTopRobotDisplay();
        lastOperationOk = true;
    }
    
    /**
     * NUEVA FUNCIONALIDAD: Habilitar movimientos automaticos en todos los robots
     */
    public void enableMovement(boolean enabled) {
        for (Robot robot : robots) {
            robot.enable(enabled);
        }
        System.out.println("MOVIMIENTO " + (enabled ? "ACTIVADO" : "DESACTIVADO") + " para todos los robots");
    }
    
    /**
     * NUEVA FUNCIONALIDAD: Ejecutar movimientos
     */
    public void executeMovements() {
        lastOperationOk = false;
        
        boolean anyRobotEnabled = robots.stream().anyMatch(Robot::isEnabled);
        if (!anyRobotEnabled) {
            showError("Ningún robot tiene movimientos automaticos habilitados");
            return;
        }
        
        System.out.println("EJECUCIÓN DE MOVIMIENTOS");
        
        for (Robot robot : robots) {
            if (!robot.isEnabled()) continue;
            
            // El robot decide su propio movimiento
            int distance = robot.decideBestMovement(getActiveStores(), length);
            
            if (distance != 0) {
                int newLocation = robot.getLocation() + distance;
                if (isValidLocation(newLocation)) {
                    robot.executeAIMovement(distance);
                    
                    // Procesar transacción
                    Store store = findStoreAt(newLocation);
                    if (store != null && store.canBeEmptied()) {
                        int profit = store.empty();
                        robot.addMoney(profit);
                        totalProfit += profit;
                    }
                    
                    System.out.println("Robot " + robot.getId() + " se movio a la " + distance + " posicion");
                }
            }
        }
        
        display.refreshAfterMove();
        updateProfitDisplay();
        updateTopRobotDisplay();
        lastOperationOk = true;
        System.out.println("MOVIMIENTOS COMPLETADOS");
    }
    
    
    public void returnRobots() {
        for (Robot robot : robots) {
            robot.reset();
        }
        display.refreshAfterMove();
        lastOperationOk = true;
    }
    
    public void reboot() {
        stores.clear();
        robots.clear();
        
        // Restaurar desde snapshots
        for (StoreSnapshot snapshot : initialStores) {
            stores.add(new Store(snapshot.location, snapshot.tenges));
        }
        
        for (RobotSnapshot snapshot : initialRobots) {
            Robot robot = new Robot(snapshot.location);
            robots.add(robot);
        }
        
        totalProfit = 0;
        
        display.clearAll();
        for (Store store : stores) display.addStore(store);
        for (Robot robot : robots) display.addRobot(robot);
        updateProfitDisplay();
        
        lastOperationOk = true;
    }
    
    public void resupplyStores() {
        for (Store store : stores) {
            store.restock();
        }
        display.refreshAfterMove();
        updateProfitDisplay();
        lastOperationOk = true;
    }
    
    // ============ CONSULTAS SIMPLIFICADAS ============
    
    public int profit() { return totalProfit; }
    public boolean ok() { return lastOperationOk; }
    
    public int[][] stores() {
        return stores.stream()
            .sorted((s1, s2) -> Integer.compare(s1.getLocation(), s2.getLocation()))
            .map(s -> new int[]{s.getLocation(), s.getTenges()})
            .toArray(int[][]::new);
    }
    
    public int[][] robots() {
        List<RobotInfo> robotInfos = new ArrayList<>();
        for (int i = 0; i < robots.size(); i++) {
            robotInfos.add(new RobotInfo(robots.get(i).getLocation(), i));
        }
        
        robotInfos.sort((r1, r2) -> {
            int locationCompare = Integer.compare(r1.location, r2.location);
            return locationCompare != 0 ? locationCompare : Integer.compare(r1.id, r2.id);
        });
        
        return robotInfos.stream()
            .map(info -> new int[]{info.location, info.id})
            .toArray(int[][]::new);
    }
    
    /**
     *Consultar estadísticas de tiendas
     */
    public int[][] getStoreEmptyCount() {
        return stores.stream()
            .sorted((s1, s2) -> Integer.compare(s1.getLocation(), s2.getLocation()))
            .map(s -> new int[]{s.getLocation(), s.getTimesEmptied()})
            .toArray(int[][]::new);
    }
    
    /**
     *Consultar historial de robot
     */
    public List<Robot.MovementRecord> getRobotMovementHistory(int robotIndex) {
        if (robotIndex < 0 || robotIndex >= robots.size()) {
            return new ArrayList<>();
        }
        return robots.get(robotIndex).getMovementHistory();
    }
    
    //MÉTODOS DE DISPLAY
    
    public void makeVisible() { display.makeVisible(); }
    public void makeInvisible() { display.makeInvisible(); }
    public void finish() { display.finish(); }
    
    private boolean isValidLocation(int location) {
        return location >= 0 && location < length;
    }
    
    private Store findStoreAt(int location) {
        return stores.stream()
            .filter(s -> s.getLocation() == location)
            .findFirst()
            .orElse(null);
    }
    
    private Robot findFirstRobotAt(int location) {
        return robots.stream()
            .filter(r -> r.getLocation() == location)
            .findFirst()
            .orElse(null);
    }
    
    private List<Store> getActiveStores() {
        return stores.stream()
            .filter(s -> !s.isEmpty())
            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    private void updateProfitDisplay() {
        int maxPossibleProfit = stores.stream()
            .mapToInt(Store::getTenges)
            .sum();
        display.updateProfit(totalProfit, Math.max(maxPossibleProfit, 1));
    }
    
    public void updateTopRobotDisplay() {
        int topRobotIndex = getTopRobotIndex();
        display.setTopRobot(topRobotIndex);
    }
    
    public int getTopRobotIndex() {
        int topIndex = -1;
        int maxMoney = -1;
        
        for (int i = 0; i < robots.size(); i++) {
            if (robots.get(i).getCollectedMoney() > maxMoney) {
                maxMoney = robots.get(i).getCollectedMoney();
                topIndex = i;
            }
        }
        
        return topIndex;
    }
    
    private void showError(String message) {
        display.showError(message);
    }
    
    
    private static class StoreSnapshot {
        final int location, tenges;
        StoreSnapshot(int location, int tenges) {
            this.location = location; this.tenges = tenges;
        }
    }
    
    private static class RobotSnapshot {
        final int id, location;
        RobotSnapshot(int id, int location) {
            this.id = id; this.location = location;
        }
    }
    
    private static class RobotInfo {
        final int location, id;
        RobotInfo(int location, int id) {
            this.location = location; this.id = id;
        }
    }
}