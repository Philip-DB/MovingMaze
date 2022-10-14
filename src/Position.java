/**
 * Class used to create Position objects that store the information about the location of a tile in the maze, such as its row and column
 * and the x and y coordinate of its location on the GUI window.
 * @author Philip de Bruyn
 */
public class Position {

    private double xCord;
    private double yCord;

    private int iRow;
    private int iCol;

    /**
     * Creates the position object using the x and y coordinates and row and column of its position in the tile maze as parameters.
     * @param xCord x-Coordinate of the mouse cursor
     * @param yCord y-Coordinate of the mouse cursor
     * @param iRow row of the Tile object in the Tile[][] 2D array of tiles.
     * @param iCol column of the Tile object in the Tile[][] 2D array of tiles.
     */
    public Position(double xCord,double yCord,int iRow,int iCol) {
        this.xCord = xCord;
        this.yCord = yCord;
        this.iRow = iRow;
        this.iCol = iCol;
    }

    /**
     * Returns the y-coordinate of the tile.
     * @return y-coordinate of the tile.
     */
    public double getyCord() {
        return yCord;
    }

    /**
     * Returns the x-coordinate of the tile
     * @return x-coordinate of the tile
     */
    public double getxCord() {
        return xCord;
    }

    /**
     * Return column of the tile in the 2D tile maze.
     * @return column of the tile.
     */
    public int getCol() {
        return iCol;
    }

    /**
     * Return row of the tile in the 2D tile maze.
     * @return row of the tile.
     */
    public int getRow() {
        return iRow;
    }
}
