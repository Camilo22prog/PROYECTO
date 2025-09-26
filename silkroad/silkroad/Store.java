import java.awt.Color;

/**
 * Store.java - Tienda inteligente que maneja su propio estado y estadísticas
 * @author MELO-ROZO
 * @version CICLO2
 */
public class Store {
    private final int location;
    private final int originalTenges;  
    private int currentTenges;        
    private final Color color;
    private boolean isEmpty;
    private int timesEmptied = 0;
    private long lastEmptiedTime = 0;
    
    public Store(int location, int tenges) {
        this.location = location;
        this.originalTenges = tenges;
        this.currentTenges = tenges;
        this.isEmpty = (tenges == 0);
        this.color = generateStoreColor();
    }
    
    // MÉTODOS ORIGINALES 
    public int getLocation() { return location; }
    public int getTenges() { return currentTenges; }
    public Color getColor() { return generateStoreColor(); } 
    public boolean isEmpty() { return isEmpty || currentTenges == 0; }
    
    // NUEVAS RESPONSABILIDADES: GESTIÓN DE ESTADO PROPIO
    /**
     * Vacía la tienda cuando un robot recoge el dinero
     * @return cantidad de dinero que tenía la tienda
     */
    public int empty() {
        if (isEmpty()) return 0;
        
        int collectedAmount = currentTenges;
        currentTenges = 0;
        isEmpty = true;
        timesEmptied++;
        lastEmptiedTime = System.currentTimeMillis();
        
        return collectedAmount;
    }
    
    /**
     * Reabastece la tienda a su valor original
     */
    public void restock() {
        currentTenges = originalTenges;
        isEmpty = (originalTenges == 0);
    }
    
    /**
     * Valida si puede ser vaciada por un robot
     */
    public boolean canBeEmptied() {
        return !isEmpty() && currentTenges > 0;
    }
    
    public int getTimesEmptied() { return timesEmptied; }
    public long getLastEmptiedTime() { return lastEmptiedTime; }
    public int getOriginalTenges() { return originalTenges; }
    
    public void resetStatistics() {
        timesEmptied = 0;
        lastEmptiedTime = 0;
    }
    
    /**
     * NUEVO: Genera color dinámico según estado actual
     */
    private Color generateStoreColor() {
        if (isEmpty()) {
            return Color.LIGHT_GRAY;
        } else {
            // Verde más intenso para tiendas con más dinero
            float intensity = Math.min(1.0f, currentTenges / 200.0f);
            return Color.getHSBColor(0.33f, 1f, 0.5f + intensity * 0.5f);
        }
    }
    
    // NUEVAS RESPONSABILIDADES: VALIDACIONES PROPIAS
    /**
     * Valida si la ubicación es válida para esta tienda
     */
    public boolean isValidLocation(int maxRouteLength) {
        return location >= 0 && location < maxRouteLength;
    }
    
    /**
     * Calcula qué tan atractiva es esta tienda para un robot
     * (usado por los robots)
     */
    public double getAttractivenessScore(int robotLocation) {
        if (isEmpty()) return 0;
        
        int distance = Math.abs(location - robotLocation);
        return (double) currentTenges / (distance + 1);
    }
    
    public String getStatusDescription() {
        if (isEmpty()) {
            return String.format("Tienda VACÍA en la ubicación %d (emptied %d times)", 
                location, timesEmptied);
        } else {
            return String.format("Tienda ACTIVA en la ubicación %d with %d/%d tenges (emptied %d times)", 
                location, currentTenges, originalTenges, timesEmptied);
        }
    }
    
    @Override
    public String toString() {
        return getStatusDescription();
    }
}