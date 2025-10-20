import shapes.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class SilkDisplay {

    private final JFrame frame;
    private final shapes.Canvas canvas;
    private final SquareSpiral spiral;
    private final List<Store> stores;
    private final List<Robot> robots;
    private final ProgressBar bar;
    
    private Timer blinkTimer;
    private int topRobotIndex = -1;
    private boolean blinkState = false;
    
    /**
     * Constructor del display.
     * @param length longitud de la ruta.
     * @param stores lista de tiendas a mostrar.
     * @param robots lista de robots a mostrar.
     * @description Inicializa el display con elementos visuales.
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
        
        initializeBlinkTimer();
    }
    
    private void initializeBlinkTimer() {
        blinkTimer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                blinkState = !blinkState;
                refreshAfterMove();
            }
        });
    }
    
    /**
     * Configura el robot con más dinero para parpadear.
     * @param robotIndex índice del robot top.
     * @description Activa el efecto de parpadeo para el robot.
     */
    public void setTopRobot(int robotIndex) {
        boolean wasBlinking = blinkTimer.isRunning();
        
        if (wasBlinking) {
            blinkTimer.stop();
        }
        
        this.topRobotIndex = robotIndex;
        
        if (robotIndex >= 0 && robotIndex < robots.size()) {
            blinkTimer.start();
        }
    }
    
    /**
     * Detiene el efecto de parpadeo.
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
     * Añade una tienda al display.
     * @param s tienda a añadir.
     * @description Dibuja un cuadrado en la ubicación de la tienda.
     */
    public void addStore(Store s) {
        if (s == null) return;
        
        int[] xy = spiral.toXY(s.getLocation());
        shapes.Rectangle rect = new shapes.Rectangle(xy[0] - 7, xy[1] - 7, 14);
        
        if (s.isEmpty()) {
            rect.changeColor(Color.LIGHT_GRAY);
        } else {
            rect.changeColor(s.getColor());
        }
        
        canvas.add(rect);
        canvas.repaint();
    }

    /**
     * Añade un robot al display.
     * @param r robot a añadir.
     * @description Dibuja un círculo en la ubicación del robot.
     */
    public void addRobot(Robot r) {
        if (r == null) return;
        
        int robotIndex = robots.indexOf(r);
        int[] xy = spiral.toXY(r.getLocation());
        shapes.Circle circ = new shapes.Circle(xy[0], xy[1], 6);
        
        if (robotIndex == topRobotIndex && blinkState) {
            circ.changeColor(Color.YELLOW);
        } else if (robotIndex == topRobotIndex) {
            circ.changeColor(Color.ORANGE);
        } else {
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

    private void refreshAll() {
        shapes.Canvas newCanvas = new shapes.Canvas();
        
        newCanvas.add(spiral);
        newCanvas.add(bar);
        
        synchronized(stores) {
            for (Store store : stores) {
                int[] xy = spiral.toXY(store.getLocation());
                shapes.Rectangle rect = new shapes.Rectangle(xy[0] - 7, xy[1] - 7, 14);
                
                if (store.isEmpty()) {
                    rect.changeColor(Color.LIGHT_GRAY);
                } else {
                    rect.changeColor(store.getColor());
                }
                
                newCanvas.add(rect);
            }
        }
        
        synchronized(robots) {
            for (int i = 0; i < robots.size(); i++) {
                Robot robot = robots.get(i);
                int[] xy = spiral.toXY(robot.getLocation());
                shapes.Circle circ = new shapes.Circle(xy[0], xy[1], 6);
                
                if (i == topRobotIndex && blinkState) {
                    circ.changeColor(Color.YELLOW);
                } else if (i == topRobotIndex) {
                    circ.changeColor(Color.ORANGE);
                } else {
                    circ.changeColor(robot.getColor());
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
        stopBlinking();
        
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

    /**
     * Actualiza la barra de progreso.
     * @param current valor actual.
     * @param max valor máximo.
     */
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

    /**
     * Muestra un mensaje de error si el display es visible.
     * @param msg mensaje a mostrar.
     */
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
        stopBlinking();
        
        SwingUtilities.invokeLater(() -> {
            if (frame != null) {
                frame.setVisible(false);
                frame.dispose();
            }
        });
    }
}