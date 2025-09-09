import java.awt.*;

/**
 * Robot que se mueve por la ruta.
 * @author MELO-ROZO
 * @CICLO-3
 */
public class Robot {
    private int location;
    private final int initLocation;
    private final Color color = new Color((int)(Math.random()*0x1000000));

    public Robot(int location){
        this.location = location;
        this.initLocation = location;
    }

    public int getLocation(){ return location; }
    public Color getColor() { return color; }
    public void move(int meters){ location += meters; }
    public void reset()         { location = initLocation; }
}