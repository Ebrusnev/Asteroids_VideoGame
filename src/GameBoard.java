import org.ietf.jgss.GSSManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class GameBoard extends JFrame {

    public static int boardWidth = 1000;
    public static int boardHeight = 800;

    public static boolean keyHeld = false;

    public static int keyHeldCode;

    public static void main(String[] args) {
        new GameBoard();
    }

    public GameBoard() {

        this.setSize(boardWidth, boardHeight);
        this.setTitle("Java Asteroids");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

                if (e.getKeyCode() == 87) {
                    System.out.println("Forward");
                } else if (e.getKeyCode() == 83) {
                    System.out.println("Backward");
                } else if (e.getKeyCode() == 65) {
                    System.out.println("Rotate Left");

                    keyHeldCode = e.getKeyCode();
                    keyHeld = true;
                } else if (e.getKeyCode() == 68) {
                    System.out.println("Rotate Right");

                    keyHeldCode = e.getKeyCode();
                    keyHeld = true;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

                keyHeld = false;

            }
        });

        GameDrawingPanel gamePanel = new GameDrawingPanel();

        this.add(gamePanel, BorderLayout.CENTER);

        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(5);

        executor.scheduleAtFixedRate(new RepaintTheBoard(this), 0L, 20L, TimeUnit.MILLISECONDS);

        this.setVisible(true);

    }

}

class RepaintTheBoard implements Runnable {

    GameBoard theBoard;

    public RepaintTheBoard(GameBoard theBoard) {

        this.theBoard = theBoard;

    }

    @Override
    public void run() {

        theBoard.repaint();

    }
}

@SuppressWarnings("serial")
class GameDrawingPanel extends JComponent {

    public ArrayList<Rock> arrayOfRocks = new ArrayList<>();

    int[] polyXArray = Rock.sPolyXArray;
    int[] polyYArray = Rock.sPolyYArray;

    SpaceShip theShip = new SpaceShip();

    int width = GameBoard.boardWidth;
    int height = GameBoard.boardHeight;

    public GameDrawingPanel() {

        for (int i = 0; i < 15; i++) {

            int randomStartXPos = (int) (Math.random() * (GameBoard.boardWidth - 40) + 1);
            int randomStartYPos = (int) (Math.random() * (GameBoard.boardHeight - 40) + 1);

            arrayOfRocks.add(new Rock(Rock.getPolyXArray(randomStartXPos), Rock.getPolyYArray(randomStartYPos), 13, randomStartXPos, randomStartYPos));

        }

    }

    public void paint(Graphics g) {

        Graphics2D graphicSettings = (Graphics2D) g;

        AffineTransform identity = new AffineTransform();

        graphicSettings.setColor(Color.BLACK);
        graphicSettings.fillRect(0, 0, getWidth(), getHeight());

        graphicSettings.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        graphicSettings.setPaint(Color.WHITE);

        for (Rock rock : arrayOfRocks) {

            rock.move();

            graphicSettings.draw(rock);

        }

        if (GameBoard.keyHeld == true && GameBoard.keyHeldCode == 68){

            SpaceShip.rotationAngle += 10;
        }

        else if(GameBoard.keyHeld == true && GameBoard.keyHeldCode == 65){

            SpaceShip.rotationAngle -= 10;
        }

        theShip.move();

        graphicSettings.setTransform(identity);

        graphicSettings.translate(GameBoard.boardWidth/2, GameBoard.boardHeight/2);

        graphicSettings.rotate(Math.toRadians(SpaceShip.rotationAngle));

        graphicSettings.draw(theShip);

    } //

}
