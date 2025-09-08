/**
 * SilkRoad.java
 * Clase principal del The Silk Road … with Robots!.
 * Permite crear una ruta.
 * @author MELO-ROZO
 * @Ciclo 1
 */
public class SilkRoad{
    private int length;

    /**
     * Crea una nueva ruta de seda.
     * @param longitud total de la ruta (número de posiciones).
     */
    public SilkRoad(int length){
        this.length = length;
    }

    /**
     * Método rápido para probar que el esqueleto compila.
     */
    public static void main(String[] args){
        SilkRoad road = new SilkRoad(100);
        System.out.println("SilkRoad creada con longitud de: " + road.length);
    }
}