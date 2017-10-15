import java.awt.*;

public class PhotonTorpedo extends Polygon {

    private static final long serialVersionUID = 1L;

    int gBWidth = GameBoard.boardWidth;
    int gBHeight = GameBoard.boardHeight;

    private double centerX = 0, centerY = 0;

    public static int[] polyXArray = {-3, 3, 3, -3, -3};
    public static int[] polyYArray = {-3, -3, 3, 3, -3};

    private int torpedoWidth = 6, torpedoHeight = 6;

    public boolean onScreen = false;

    private double movingAngle = 0;

    private double xVelocity = 5, yVelocity = 5;

    public PhotonTorpedo(double centerX, double centerY, double movingAngle) {

        super(polyXArray, polyYArray, 5);

        this.centerX = centerX;
        this.centerY = centerY;
        this.movingAngle = movingAngle;

        this.onScreen = true;

        this.setXVelocity(this.torpedoXMovingAngle(movingAngle) * 10);
        this.setYVelocity(this.torpedoYMovingAngle(movingAngle) * 10);

    }

    public double getCenterX() { return centerX; }
    public double getCenterY() { return centerY; }
    public void setCenterX(double centerX) { this.centerX = centerX; }
    public void setCenterY(double centerY) { this.centerY = centerY; }

    public void changeXPosition(double incAmt) { this.centerX += incAmt; }
    public void changeYPosition(double incAmt) { this.centerY += incAmt; }

    public double getXVelocity() { return xVelocity; }
    public double getYVelocity() { return yVelocity; }

    public int getTorpedoWidth() { return torpedoWidth; }
    public int getTorpedoHeight() { return torpedoHeight; }

    public void setMovingAngle(double movingAngle) { this.movingAngle = movingAngle; }
    public double getMovingAngle() { return movingAngle; }

    @Override
    public Rectangle getBounds() {

        return new Rectangle((int)getCenterX() -6, (int)getCenterY()-6, getTorpedoWidth(), getTorpedoHeight());

    }

    public void setXVelocity(double xVelocity) { this.xVelocity = xVelocity; }
    public void setYVelocity(double yVelocity) { this.yVelocity = yVelocity; }

    public double torpedoXMovingAngle(double xMoveAngle) { return (double) (Math.cos(xMoveAngle * Math.PI / 180)); }
    public double torpedoYMovingAngle(double yMoveAngle) { return (double) (Math.sin(yMoveAngle * Math.PI / 180)); }

    public void move(){

        if (this.onScreen){

            this.changeXPosition(this.getXVelocity());

            if (this.getCenterX() < 0 || this.getCenterX() > gBWidth){
                this.onScreen = false;
            }

            this.changeYPosition(this.getYVelocity());

            if (this.getCenterY() < 0 || this.getCenterY() > gBHeight){
                this.onScreen = false;
            }

        }

    }

}
