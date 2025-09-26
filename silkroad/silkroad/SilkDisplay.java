import shapes.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * SilkDisplay.java - Display avanzado con efectos visuales.
 * Incluye parpadeo del robot top performer y tiendas diferenciadas.
 * @author MELO-ROZO
 * @version CICLO 2
 */
public class SilkDisplay {

    private final JFrame frame;
    private final shapes.Canvas canvas;
    private final SquareSpiral spiral;
    private final List<Store> stores;
    private final List<Robot> robots;
    private final ProgressBar bar;
    
    // NUEVO: Sistema de parpadeo para robot top performer
    private Timer blinkTimer;
    private int topRobotIndex = -1;
    private boolean blinkState = false;
    
    /**
     * Constructor con inicialización del sistema de parpadeo
     */
    public SilkDisplay(int length, List<Store> stores, List<Robot> robots) {
        this.stores = stores;
        this.robots = robots;
        
        this.canvas = new shapes.Canvas();
        
        this.spiral = new SquareSpiral(length);
        this.spiral.changeColor(Color.GRAY);
        canvas.add(spiral);

        this.bar = new ProgressBar(10, 10, 200, 20, 100);
        canvas.add(bar);

        frame = new JFrame("SilkRoad - Advanced Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(canvas);
        frame.pack();
        
        // NUEVO: Inicializar timer de parpadeo
        initializeBlinkTimer();
    }
    
    /**
     * NUEVO: Inicializa el sistema de parpadeo para robot top performer
     */
    private void initializeBlinkTimer() {
        blinkTimer = new Timer(500, new ActionListener() { // Parpadeo cada 500ms
            @Override
            public void actionPerformed(ActionEvent e) {
                blinkState = !blinkState;
                refreshAfterMove(); // Redibujar para mostrar efecto parpadeo
            }
        });
    }
    
    /**
     * NUEVO: Activa parpadeo para el robot top performer
     */
    public void setTopRobot(int robotIndex) {
        boolean wasBlinking = blinkTimer.isRunning();
        
        if (wasBlinking) {
            blinkTimer.stop();
        }
        
        this.topRobotIndex = robotIndex;
        
        if (robotIndex >= 0 && robotIndex < robots.size()) {
            blinkTimer.start();
            System.out.println("Robot " + robotIndex + " is now the top performer - BLINKING");
        } else {
            System.out.println("No top robot to highlight");
        }
    }
    
    /**
     * NUEVO: Detiene el parpadeo
     */
    public void stopBlinking() {
        if (blinkTimer.isRunning()) {
            blinkTimer.stop();
        }
        topRobotIndex = -1;
        blinkState = false;
    }

    public void makeVisible() {
        SwingUtilities.invokeLater(() -> {
            frame.setVisible(true);
            frame.toFront();
        });
    }

    /**
     * Añade tienda con color diferenciado según estado
     */
    public void addStore(Store s) {
        if (s == null) return;
        
        int[] xy = spiral.toXY(s.getLocation());
        shapes.Rectangle rect = new shapes.Rectangle(xy[0] - 7, xy[1] - 7, 14);
        
        // NUEVO: Color diferenciado para tiendas vacías
        if (s.isEmpty()) {
            rect.changeColor(Color.LIGHT_GRAY); // Tiendas desocupadas grises
        } else {
            rect.changeColor(s.getColor()); // Tiendas con dinero verdes
        }
        
        canvas.add(rect);
        canvas.repaint();
    }

    /**
     * Añade robot con efecto de parpadeo para top performer
     */
    public void addRobot(Robot r) {
        if (r == null) return;
        
        int robotIndex = robots.indexOf(r);
        int[] xy = spiral.toXY(r.getLocation());
        shapes.Circle circ = new shapes.Circle(xy[0], xy[1], 6);
        
        // NUEVO: Efecto parpadeo para robot top performer
        if (robotIndex == topRobotIndex && blinkState) {
            // Durante parpadeo: color amarillo brillante
            circ.changeColor(Color.YELLOW);
        } else if (robotIndex == topRobotIndex) {
            // Top robot en estado normal: color especial (naranja)
            circ.changeColor(Color.ORANGE);
        } else {
            // Robots normales: color original
            circ.changeColor(r.getColor());
        }
        
        canvas.add(circ);
        canvas.repaint();
    }

    public void removeStore(Store s) {
        refreshAll();
    }

    public void removeRobot(Robot r) {
        refreshAll();
    }

    public void refreshAfterMove() {
        refreshAll();
    }

    /**
     * Redibuja todo con efectos visuales avanzados
     */
    private void refreshAll() {
        shapes.Canvas newCanvas = new shapes.Canvas();
        
        newCanvas.add(spiral);
        newCanvas.add(bar);
        
        // Redibujar tiendas con estados diferenciados
        synchronized(stores) {
            for (Store store : stores) {
                int[] xy = spiral.toXY(store.getLocation());
                shapes.Rectangle rect = new shapes.Rectangle(xy[0] - 7, xy[1] - 7, 14);
                
                if (store.isEmpty()) {
                    rect.changeColor(Color.LIGHT_GRAY); // Gris para desocupadas
                } else {
                    rect.changeColor(store.getColor()); // Verde para activas
                }
                
                newCanvas.add(rect);
            }
        }
        
        // Redibujar robots con efecto parpadeo
        synchronized(robots) {
            for (int i = 0; i < robots.size(); i++) {
                Robot robot = robots.get(i);
                int[] xy = spiral.toXY(robot.getLocation());
                shapes.Circle circ = new shapes.Circle(xy[0], xy[1], 6);
                
                if (i == topRobotIndex && blinkState) {
                    circ.changeColor(Color.YELLOW); // Parpadeo amarillo
                } else if (i == topRobotIndex) {
                    circ.changeColor(Color.ORANGE); // Top robot naranja
                } else {
                    circ.changeColor(robot.getColor()); // Color normal
                }
                
                newCanvas.add(circ);
            }
        }
        
        SwingUtilities.invokeLater(() -> {
            frame.getContentPane().removeAll();
            frame.add(newCanvas);
            frame.revalidate();
            frame.repaint();
        });
    }

    public void clearAll() {
        stopBlinking(); // Detener parpadeo al limpiar
        
        shapes.Canvas newCanvas = new shapes.Canvas();
        newCanvas.add(spiral);
        newCanvas.add(bar);
        
        SwingUtilities.invokeLater(() -> {
            frame.getContentPane().removeAll();
            frame.add(newCanvas);
            frame.revalidate();
            frame.repaint();
        });
    }

    public void updateProfit(int current, int max) {
        if (bar == null) return;
        
        if (max < 0) max = 1;
        if (current < 0) current = 0;
        if (current > max) current = max;
        if (max == 0) max = 1;
        
        bar.setMax(max);
        bar.setValue(current);
        canvas.repaint();
    }

    public void showError(String msg) {
        if (frame != null && frame.isVisible() && msg != null && !msg.trim().isEmpty()) {
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(frame, msg, "Error", JOptionPane.ERROR_MESSAGE);
            });
        }
    }

    public void makeInvisible() {
        SwingUtilities.invokeLater(() -> {
            if (frame != null) {
                frame.setVisible(false);
            }
        });
    }

    public void finish() {
        stopBlinking(); // Importante: detener timer antes de cerrar
        
        SwingUtilities.invokeLater(() -> {
            if (frame != null) {
                frame.setVisible(false);
                frame.dispose();
            }
        });
    }
}
