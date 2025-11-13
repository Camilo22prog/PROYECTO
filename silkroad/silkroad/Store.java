import java.awt.Color;

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
    
    public int getLocation() { return location; }
    public int getTenges() { return currentTenges; }
    public Color getColor() { return generateStoreColor(); } 
    public boolean isEmpty() { return isEmpty || currentTenges == 0; }
    
    /**
     * Vacía la tienda y retorna el dinero recolectado.
     * @return cantidad de dinero recolectado.
     * @description Marca la tienda como vacía y actualiza estadísticas.
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
     * Reabastece la tienda a su estado original.
     * @description Restaura la cantidad original de tenges.
     */
    public void restock() {
        currentTenges = originalTenges;
        isEmpty = (originalTenges == 0);
    }
    
    /**
     * Verifica si la tienda puede ser vaciada.
     * @return true si la tienda no está vacía y tiene tenges > 0.
     */
    public boolean canBeEmptied() {
        return !isEmpty() && currentTenges > 0;
    }
    
    public int getTimesEmptied() { return timesEmptied; }
    public long getLastEmptiedTime() { return lastEmptiedTime; }
    public int getOriginalTenges() { return originalTenges; }
    
    /**
     * Reinicia las estadísticas de la tienda.
     * @description Pone a 0 veces vaciada y el tiempo de última vaciado.
     */
    public void resetStatistics() {
        timesEmptied = 0;
        lastEmptiedTime = 0;
    }
    
    private Color generateStoreColor() {
        if (isEmpty()) {
            return Color.LIGHT_GRAY;
        } else {
            float intensity = Math.min(1.0f, currentTenges / 200.0f);
            return Color.getHSBColor(0.33f, 1f, 0.5f + intensity * 0.5f);
        }
    }
    
    /**
     * Verifica si la ubicación es válida para esta tienda.
     * @param maxRouteLength longitud máxima de la ruta.
     * @return true si la ubicación está dentro de los límites.
     */
    public boolean isValidLocation(int maxRouteLength) {
        return location >= 0 && location < maxRouteLength;
    }
    
    /**
     * Calcula qué tan atractiva es esta tienda para un robot.
     * @param robotLocation ubicación del robot.
     * @return puntuación basada en tenges y distancia.
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