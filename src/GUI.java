
import java.awt.*;



/**
 * Class that handles the creation and updating of the GUI using the {@code Java Swing} library.
 *
 * @author Philip de Bruyn
 */
public class GUI {

    private Tile[][] maze;
    private GameState gameState;
    private double tileLength;

    public GUI(GameState gameState, Tile[][] maze) {
        this.maze = maze;
        this.gameState = gameState;

        if (maze.length <= maze[0].length) { // if rows < cols, use rows to determine tile length and a vice versa
            tileLength = 100 / (double) maze.length;
        } else {
            tileLength = 100 / (double) maze[0].length;
        }


    }


    public void drawMaze() { // draws current maze
        StdDraw.enableDoubleBuffering();
        StdDraw.setCanvasSize(900, 900);
        StdDraw.setXscale(-100, 100);
        StdDraw.setYscale(-100, 100);

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.filledRectangle(1920 / 2, 1080 / 2 + 100, 300, 300); // this will be the area that


        double xCord = -70.00;
        double yCord =  70.00;
        for (int iRows = 1; iRows < maze.length; iRows++) { // loop through the tile maze and create tiles on Canvas

            for(int iCol = 1; iCol < maze[0].length;iCol++) {
                StdDraw.setPenColor(Color.RED);
                StdDraw.filledSquare(xCord,yCord,tileLength/2);
                StdDraw.setPenColor(Color.BLACK);
                StdDraw.square(xCord,yCord,tileLength/2);
            // create tile in here
                xCord = xCord + tileLength;
            }
            // when tile is done
                yCord = yCord + tileLength;

        }



    }

    public static void main(String[] args) {

    }


}
