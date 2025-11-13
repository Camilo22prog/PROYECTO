package shapes;
import java.awt.*;

/**
 * ProgressBar.java - Barra simple (verde) sin números.
 * @author MELO-ROZO
 * @version CICLO-7
 */
public class ProgressBar extends Shape {

    private final int w, h;
    private int value, max;

    public ProgressBar(int x, int y, int w, int h, int max){
        this.x = x; this.y = y; this.w = w; this.h = h; this.max = max;
    }

    public void setValue(int v){ value = v; }
    public void setMax(int m){ max = m; }

    @Override
    public void draw(Graphics2D g2){
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(x, y, w, h);
        g2.setColor(Color.GREEN);
        int filled = (int)((double)value / max * w);
        g2.fillRect(x, y, filled, h);
        g2.setColor(Color.BLACK);
        g2.drawRect(x, y, w, h);
    }
}