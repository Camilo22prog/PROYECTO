import java.awt.*;

/**
 * Tienda con posición y tenges.
 * @author MELO-ROZO
 * @CICLO-3
 */
public class Store {
    private final int location;
    private final int tenges;
    private final Color color = new Color((int)(Math.random()*0x1000000));

    public Store(int location, int tenges){
        this.location = location;
        this.tenges   = tenges;
    }

    public int getLocation(){ return location; }
    public int getTenges()  { return tenges; }
    public Color getColor() { return color; }
}