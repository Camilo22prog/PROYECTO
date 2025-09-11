import java.util.ArrayList;
import java.util.List;


/**
 * SilkRoad.java - Clase principal del simulador "The Silk Road ... with Robots!".
 * Permite crear una ruta, añadir/eliminar tiendas y robots, moverlos
 * y consultar ganancias.
 * @author MELO-ROZO
 * @version CICLO-3
 */
public class SilkRoad {

    private final int length;
    private final List<Store> stores = new ArrayList<>();
    private final List<Robot> robots = new ArrayList<>();
    private final SilkDisplay display;

    /**
     * Crea una nueva ruta de seda.
     * @param length longitud total de la ruta (nº de posiciones).
     */
    public SilkRoad(int length) {
        this.length = length;
        this.display = new SilkDisplay(length);
    }

    /**
     * Añade una tienda en la posición indicada.
     * @param location posición lógica sobre la espiral.
     * @param tenges   cantidad de dinero inicial de la tienda.
     */
    public void placeStore(int location, int tenges) {
        Store s = new Store(location, tenges);
        stores.add(s);
        display.addStore(s);
    }

    /**
     * Añade un robot en la posición indicada.
     * @param location posición lógica inicial del robot.
     */
    public void placeRobot(int location) {
        Robot r = new Robot(location);
        robots.add(r);
        display.addRobot(r);
    }

    /**
     * Hace visible la ventana del simulador (thread-safe).
     */
    public void makeVisible() {
        display.makeVisible();
    }

    /**
     * Prueba rápida del funcionamiento visual.
     */
    public static void main(String[] args) {
        SilkRoad road = new SilkRoad(20);
        road.makeVisible();
        road.placeStore(5, 100);
        road.placeRobot(2);
        road.placeStore(12, 80);
    }
}