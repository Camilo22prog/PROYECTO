package shapes;
import java.awt.*;

/**
 * Shape.java - Clase base para cualquier figura del simulador.
 * @author MELO-ROZO
 * @CICLO-2
 */
public abstract class Shape { 
    protected int x, y;
    protected Color color = Color.BLACK;
    public abstract void draw(Graphics2D g2);
    public void changeColor(Color c){ color = c; }
}