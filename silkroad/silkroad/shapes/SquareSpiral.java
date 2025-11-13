package shapes;
import java.awt.*;
import java.util.*;

/**
 * Dibuja la ruta como espiral cuadrada y convierte
 * posiciones lógicas en coordenadas de pantalla.
 * @author MELO-ROZO
 * @CASO 2
 */
public class SquareSpiral extends Line {
    private final int length;
    private final java.util.List<int[]> pts = new ArrayList<>();

    public SquareSpiral(int length){
        super(0, 0, 0, 0);
        this.length = length;
        generate();
    }

    private void generate(){
        int x = 0, y = 0, dx = 1, dy = 0, seg = 1, step = 0;
        for (int i = 0; i <= length; i++){
            pts.add(new int[]{x, y});
            x += dx; y += dy; step++;
            if (step == seg){
                step = 0;
                int tmp = dx; dx = -dy; dy = tmp;
                if (dy == 0) seg++;
            }
        }
    }

    @Override
    public void draw(Graphics2D g2){
        g2.setColor(color);
        for (int i = 1; i < pts.size(); i++){
            int[] a = pts.get(i - 1), b = pts.get(i);
            g2.drawLine(scale(a[0]), scale(a[1]),
                        scale(b[0]), scale(b[1]));
        }
    }

    public int[] toXY(int loc){
        int[] p = pts.get(Math.min(loc, length));
        return new int[]{scale(p[0]), scale(p[1])};
    }

    private int scale(int v){ return 300 + v * 20; }
}