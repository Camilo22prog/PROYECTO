import java.awt.Color;
import java.util.*;

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
    
    /**
     * Mueve al robot una cantidad de metros.
     * @param meters metros a mover (puede ser negativo).
     * @description Registra el movimiento en el historial.
     */
    public void move(int meters) { 
        int oldLocation = location;
        location += meters;
        movementHistory.add(new MovementRecord(oldLocation, location, meters, false));
    }
    
    /**
     * Mueve al robot a una ubicación específica.
     * @param newLocation nueva ubicación del robot.
     * @description Registra el movimiento en el historial.
     */
    public void moveTo(int newLocation) {
        int oldLocation = location;
        location = newLocation;
        movementHistory.add(new MovementRecord(oldLocation, newLocation, newLocation - oldLocation, false));
    }
    
    /**
     * Añade dinero recolectado al robot.
     * @param amount cantidad de dinero a añadir.
     * @description Actualiza el historial de movimientos con la ganancia.
     */
    public void addMoney(int amount) {
        collectedMoney += amount;
        if (!movementHistory.isEmpty()) {
            MovementRecord lastMove = movementHistory.get(movementHistory.size() - 1);
            lastMove.addProfit(amount);
        }
    }
    
    /**
     * Reinicia la posición del robot a su ubicación inicial.
     * @description No afecta el dinero recolectado.
     */
    public void reset() { 
        location = initLocation;
    }
    
    /**
     * Reinicia el dinero recolectado del robot a 0.
     * @description No afecta la posición.
     */
    public void resetMoney() {
        collectedMoney = 0;
    }
    
    /**
     * Habilita o deshabilita el movimiento automático del robot.
     * @param enabled true para habilitar, false para deshabilitar.
     */
    public void enable(boolean enabled) {
        this.Enabled = enabled;
    }
    
    public boolean isEnabled() {
        return Enabled;
    }
    
    /**
     * Decide el mejor movimiento basado en tiendas disponibles.
     * @param availableStores lista de tiendas con dinero disponibles.
     * @param maxLocation límite máximo de la ruta.
     * @return distancia a mover (0 si no hay movimiento óptimo).
     * @description Calcula el movimiento que maximiza ganancia/distance.
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
            
            if (targetLocation < 0 || targetLocation >= maxLocation) {
                continue;
            }
            
            double score = (double) store.getTenges() / (Math.abs(distance) + 1);
            
            if (score > bestScore) {
                bestScore = score;
                bestDistance = distance;
            }
        }
        
        return bestDistance;
    }
    
    /**
     * Ejecuta un movimiento automático y lo registra.
     * @param distance metros a mover.
     * @description Solo se ejecuta si el robot está habilitado.
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
    
    /**
     * Limpia el historial de movimientos del robot.
     */
    public void clearHistory() {
        movementHistory.clear();
    }
    
    /**
     * Retorna el número total de movimientos registrados.
     * @return cantidad de movimientos.
     */
    public int getTotalMovements() {
        return movementHistory.size();
    }
    
    /**
     * Retorna el número de movimientos automáticos.
     * @return cantidad de movimientos automáticos.
     */
    public int getMovements() {
        return (int) movementHistory.stream().filter(MovementRecord::isMovement).count();
    }
    
    /**
     * Retorna el número de movimientos manuales.
     * @return cantidad de movimientos manuales.
     */
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