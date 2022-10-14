/**
 * Class used to create slidingPoint objects that store the information about the location of the slidingPoints of the maze, such as  the row and column they refer
 * to in the TileMaze and the x and y coordinate of its location on the GUI window, as well as the direction to slide the floating tile in when it is clicked.
 * @author Philip de Bruyn
 */
public class SlidingPoint {

    private String slidingDirection;
    private int row;
    private int col;

    private double xCord;

    private double yCord;

    /**
     * Constructor the slidingPoint object.
     * @param row row of the tile that the slidingPoint object refers to.
     * @param col column of the tile that the slidingPoint object refers to.
     * @param xCord x-coordinate of the slidingPoint on the GUI window.
     * @param yCord y-coordinate of the slidingPoint on the GUI window.
     * @param slidingDirection A {@code string} that determines what side and column/row of the maze the slidingPoint is on and hence where to slide a floating tile in from.
     */
    public SlidingPoint(int row, int col, double xCord,double yCord, String slidingDirection) {
        this.col = col;
        this.row = row;
        this.xCord = xCord;
        this.yCord = yCord;
        this.slidingDirection = slidingDirection;

    }

    /**
     * Returns the slidingString of the invoking slidingPoint object.
     * @return a String that contains direction and row/column of the slidingPoint.
     */
    public String getSlidingDirection() {
        return slidingDirection;
    }

    /**
     * Returns the x-coordinate of the current slidingPoint
     * @return x-coordinate of the current slidingPoint
     */
    public double getxCord() {
        return xCord;
    }

    /**
     * Returns the y-coordinate of the current slidingPoint
     * @return y-coordinate of the current slidingPoint
     */
    public double getyCord() {
        return yCord;
    }



}
