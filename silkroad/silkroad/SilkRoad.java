/**
 * SilkRoad.java
 * Clase principal del The Silk Road … with Robots!.
 * Permite crear una ruta.
 * @author MELO-ROZO
 * @Ciclo 1
 */
public class SilkRoad{
    private int length;
    private final SilkDisplay display;

    /**
     * Crea una nueva ruta de seda.
     * @param longitud total de la ruta (número de posiciones).
     */
    public SilkRoad(int length){
        this.length = length;
        this.display = new SilkDisplay(length);
    }
    
    public void makeVisible(){
    display.makeVisible();
    }

    /**
     * Método rápido para probar que el esqueleto compila.
     */
    public static void main(String[] args){
        SilkRoad road = new SilkRoad(50);  
        road.makeVisible();
   
    }
    
    
    
}