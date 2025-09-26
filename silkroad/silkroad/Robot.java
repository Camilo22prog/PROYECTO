import java.awt.Color;
import java.util.*;

/**
 * Robot.java - Robot inteligente que puede decidir sus propios movimientos
 * @author MELO-ROZO  
 * @version CICLO2
 */
public class Robot {
    private final int id;
    private int location;
    private final int initLocation;
    private final Color color;
    private int collectedMoney;
    private boolean Enabled = false;
    private List<MovementRecord> movementHistory = new ArrayList<>();
    
    public Robot(int id, int location) {
        this.id = id;
        this.location = location;
        this.initLocation = location;
        this.collectedMoney = 0;
        this.color = generateColorFromId(id);
    }
    
    public Robot(int location) {
        this(getNextId(), location);
    }
    
    public int getLocation() { return location; }
    public Color getColor() { return color; }
    public int getId() { return id; }
    public int getInitLocation() { return initLocation; }
    public int getCollectedMoney() { return collectedMoney; }
    
    public void move(int meters) { 
        int oldLocation = location;
        location += meters;
        // Registrar movimiento en historial
        movementHistory.add(new MovementRecord(oldLocation, location, meters, false));
    }
    
    public void moveTo(int newLocation) {
        int oldLocation = location;
        location = newLocation;
        movementHistory.add(new MovementRecord(oldLocation, newLocation, newLocation - oldLocation, false));
    }
    
    public void addMoney(int amount) {
        collectedMoney += amount;
        // Actualizar último movimiento con ganancia
        if (!movementHistory.isEmpty()) {
            MovementRecord lastMove = movementHistory.get(movementHistory.size() - 1);
            lastMove.addProfit(amount);
        }
    }
    
    public void reset() { 
        location = initLocation;
    }
    
    public void resetMoney() {
        collectedMoney = 0;
    }
    
    // NUEVAS RESPONSABILIDADES
    public void enable(boolean enabled) {
        this.Enabled = enabled;
    }
    
    public boolean isEnabled() {
        return Enabled;
    }
    
    /**
     * El robot decide su mejor movimiento basado en tiendas disponibles
     * @param availableStores lista de tiendas con dinero disponible
     * @param maxLocation límite máximo de la ruta
     * @return distancia a mover (0 si no hay buen movimiento)
     */
    public int decideBestMovement(List<Store> availableStores, int maxLocation) {
        if (!Enabled || availableStores.isEmpty()) {
            return 0;
        }
        
        int bestDistance = 0;
        double bestScore = 0;
        
        for (Store store : availableStores) {
            if (store.isEmpty()) continue;
            
            int targetLocation = store.getLocation();
            int distance = targetLocation - this.location;
            
            // Validar que el movimiento es posible
            if (targetLocation < 0 || targetLocation >= maxLocation) {
                continue;
            }
            
            // Calcular puntuación (ganancia vs distancia)
            double score = (double) store.getTenges() / (Math.abs(distance) + 1);
            
            if (score > bestScore) {
                bestScore = score;
                bestDistance = distance;
            }
        }
        
        return bestDistance;
    }
    
    /**
     * Ejecuta movimiento y registra como tal
     */
    public void executeAIMovement(int distance) {
        if (!Enabled) return;
        
        int oldLocation = location;
        location += distance;
        movementHistory.add(new MovementRecord(oldLocation, location, distance, true)); 
    }
    
    
    public List<MovementRecord> getMovementHistory() {
        return new ArrayList<>(movementHistory);
    }
    
    public void clearHistory() {
        movementHistory.clear();
    }
    
    public int getTotalMovements() {
        return movementHistory.size();
    }
    
    public int getMovements() {
        return (int) movementHistory.stream().filter(MovementRecord::isMovement).count();
    }
    
    public int getManualMovements() {
        return getTotalMovements() - getMovements();
    }
    
    private Color generateColorFromId(int robotId) {
        Color[] robotColors = {
            Color.getHSBColor(0.55f, 1f, 0.9f),
            Color.RED, Color.BLACK, new Color(0, 100, 0),
            new Color(139, 69, 19), new Color(75, 0, 130),
            Color.ORANGE, Color.PINK, new Color(128, 0, 128), new Color(0, 128, 128)
        };
        return robotColors[robotId % robotColors.length];
    }
    
    private static int nextId = 0;
    private static int getNextId() { return nextId++; }
    public static void resetIdCounter() { nextId = 0; }
    

    
    
    public static class MovementRecord {
        private final int fromLocation;
        private final int toLocation;
        private final int distance;
        private final boolean isMovement;
        private int profitGained = 0;
        private final long timestamp;
        
        public MovementRecord(int from, int to, int distance, boolean isauto) {
            this.fromLocation = from;
            this.toLocation = to;
            this.distance = distance;
            this.isMovement = isauto;
            this.timestamp = System.currentTimeMillis();
        }
        
        public void addProfit(int profit) { this.profitGained += profit; }
        public int getFromLocation() { return fromLocation; }
        public int getToLocation() { return toLocation; }
        public int getDistance() { return distance; }
        public boolean isMovement() { return isMovement; }
        public int getProfitGained() { return profitGained; }
        public long getTimestamp() { return timestamp; }
        
    }
}