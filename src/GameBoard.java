import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class GameBoard extends JFrame {

    public static int boardWidth = 1000;
    public static int boardHeight = 800;

    public static boolean keyHeld = false;

    public static int keyHeldCode;

    public static ArrayList<PhotonTorpedo> torpedoes = new ArrayList<>();

    String thrustFile = "file:./src/sounds/thrust.au";
    String laseFile = "file:./src/sounds/laser.aiff";

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

                    playSoundEffect(thrustFile);

                    keyHeldCode = e.getKeyCode();
                    keyHeld = true;


                } else if (e.getKeyCode() == 83) {
                    System.out.println("Backward");
                    keyHeldCode = e.getKeyCode();
                    keyHeld = true;


                } else if (e.getKeyCode() == 65) {
                    System.out.println("Rotate Left");
                    keyHeldCode = e.getKeyCode();
                    keyHeld = true;


                } else if (e.getKeyCode() == 68) {
                    System.out.println("Rotate Right");
                    keyHeldCode = e.getKeyCode();
                    keyHeld = true;


                } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    System.out.println("Shoot");

                    playSoundEffect(laseFile);

                    torpedoes.add(new PhotonTorpedo(GameDrawingPanel.theShip.getShipNoseX(),
                            GameDrawingPanel.theShip.getShipNoseY(),
                            GameDrawingPanel.theShip.getRotationAngle()));
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

    public static void playSoundEffect(String soundToPlay) {

        URL soundLocation;

        try {

            soundLocation = new URL(soundToPlay);

            Clip clip = AudioSystem.getClip();
            AudioInputStream inputStream;

            inputStream = AudioSystem.getAudioInputStream(soundLocation);

            clip.open(inputStream);
            clip.loop(0);
            clip.start();
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e1) {

            e1.printStackTrace();

        }

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

    public ArrayList<Rock> rocks = new ArrayList<>();

    int[] polyXArray = Rock.sPolyXArray;
    int[] polyYArray = Rock.sPolyYArray;

    static SpaceShip theShip = new SpaceShip();

    int width = GameBoard.boardWidth;
    int height = GameBoard.boardHeight;

    public GameDrawingPanel() {

        for (int i = 0; i < 10; i++) {

            int randomStartXPos = (int) (Math.random() * (GameBoard.boardWidth - 40) + 1);
            int randomStartYPos = (int) (Math.random() * (GameBoard.boardHeight - 40) + 1);

            rocks.add(new Rock(Rock.getPolyXArray(randomStartXPos), Rock.getPolyYArray(randomStartYPos), 13, randomStartXPos, randomStartYPos));

            Rock.rocks = rocks;
        }

    }

    public void paint(Graphics g) {

        Graphics2D graphicSettings = (Graphics2D) g;

        AffineTransform identity = new AffineTransform();

        graphicSettings.setColor(Color.BLACK);
        graphicSettings.fillRect(0, 0, getWidth(), getHeight());

        graphicSettings.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        graphicSettings.setPaint(Color.WHITE);

        for (Rock rock : rocks) {

            if (rock.onScreen) {
                rock.move(theShip, GameBoard.torpedoes);
                graphicSettings.draw(rock);
            }

        }

        if (GameBoard.keyHeld && GameBoard.keyHeldCode == 68) {

            theShip.increaseRotationAngle();

            System.out.println("Ship Angle: " + theShip.getRotationAngle());


        } else if (GameBoard.keyHeld && GameBoard.keyHeldCode == 65) {

            theShip.decreaseRotationAngle();

            System.out.println("Ship Angle: " + theShip.getRotationAngle());


        } else if (GameBoard.keyHeld && GameBoard.keyHeldCode == 87) {

            theShip.setMovingAngle(theShip.getRotationAngle());

            theShip.increaseXVelocity(theShip.shipXMoveAngle(theShip.getMovingAngle()) * 0.1);
            theShip.increaseYVelocity(theShip.shipYMoveAngle(theShip.getMovingAngle()) * 0.1);


        } else if (GameBoard.keyHeld && GameBoard.keyHeldCode == 83) {

            theShip.setMovingAngle(theShip.getRotationAngle());

            theShip.decreaseXVelocity(theShip.shipXMoveAngle(theShip.getMovingAngle()) * 0.1);
            theShip.decreaseYVelocity(theShip.shipYMoveAngle(theShip.getMovingAngle()) * 0.1);
        }

        theShip.move();

        graphicSettings.setTransform(identity);

        graphicSettings.translate(theShip.getXCenter(), theShip.getYCenter());

        graphicSettings.rotate(Math.toRadians(theShip.getRotationAngle()));

        graphicSettings.draw(theShip);

        for (PhotonTorpedo torpedo : GameBoard.torpedoes) {

            torpedo.move();
            if (torpedo.onScreen) {

                graphicSettings.setTransform(identity);
                graphicSettings.translate(torpedo.getCenterX(), torpedo.getCenterY());
                graphicSettings.draw(torpedo);

            }

        }

    } // END OF paint

}
