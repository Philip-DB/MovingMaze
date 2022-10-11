
import java.awt.*;


/**
 * Class that handles the creation and updating of the GUI using the {@code Java Swing} library.
 *
 * @author Philip de Bruyn
 */
public class GUI {

    private Tile[][] maze;

    private Tile floatingTile;
    private GameState gameState;
    private double tileLength;
    private double numRows;
    private double numCols;

    public GUI(GameState gameState, Tile[][] maze,Tile floatingTile) {
        this.maze = maze;
        this.gameState = gameState;
        this.floatingTile = floatingTile;


        numRows = (double) maze.length - 1;
        numCols = (double) maze[0].length - 1;


        if (numRows <= numCols) { // if rows < cols, use rows to determine tile length and a vice versa
            tileLength = 100 / numCols;
        } else {
            tileLength = 100 / numRows;
        }
        // sets up the co-oridinates and canvas size once
        StdDraw.enableDoubleBuffering();
        StdDraw.setCanvasSize(950, 950);
        StdDraw.setXscale(-100, 100);
        StdDraw.setYscale(-100, 100);

    }


    public void drawMaze() { // draws current maze

        drawTitle();
        drawScoreboard();
        drawTiles();
        StdDraw.show();


    }

    private void drawTitle() {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.filledRectangle(0, 0, 100, 100); // clear background and draw title on top


        switch (gameState.getCurrentTurn().getColor()) {
            case 'G':
                StdDraw.setPenColor(StdDraw.GREEN);
                break;
            case 'B':
                StdDraw.setPenColor(StdDraw.BLUE);
                break;
            case 'R':
                StdDraw.setPenColor(StdDraw.RED);
                break;
            case 'Y':
                StdDraw.setPenColor(StdDraw.YELLOW);
                break;
        }
        String moveType;
        if (gameState.isMoving()) {
            moveType = "turn to move";
        } else {
            moveType = "turn to rotate or slide";
        }

        StdDraw.text(0.0, 85.00, gameState.getCurrentTurn().getColorString() + "'s " + moveType);

    }

    private void drawScoreboard() {
        StdDraw.setPenColor(StdDraw.GREEN);
        StdDraw.text(-50.00, -85.00, "Green : " + "\t" + gameState.getAdventurers()[0].getNumRelicCollected());

        StdDraw.setPenColor(StdDraw.YELLOW);
        StdDraw.text(50.00, -85.00, "Yellow : " + "\t" + gameState.getAdventurers()[1].getNumRelicCollected());

        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.text(-50.00, -95.00, "Red : " + "\t" + gameState.getAdventurers()[2].getNumRelicCollected());

        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.text(50.00, -95.00, "Blue : " + "\t" + gameState.getAdventurers()[3].getNumRelicCollected());
    }

    private void drawTiles() {

        for (int iRow = 1; iRow < maze.length; iRow++) { // before drawing the maze, make sure all text representations are up-to-date

            for (int iCol = 1; iCol < maze[0].length; iCol++) {

                maze[iRow][iCol].updateTile();

            }

        }

        int middleRow = (int) (Math.ceil(numRows / 2));
        int middleCol = (int) (Math.ceil(numCols / 2)); // get the middle ( where we start printing )
        double xCord = 0.0;
        double yCord = 0.0;

        for (int iRow = middleRow; iRow > 0; iRow--) { // loop for rows above middle point and including it
            xCord = 0.0;
            for (int iCol = middleCol; iCol < maze[0].length; iCol++) {//loop for cols to the right of middle column ( including middle )
                StdDraw.setPenColor(128, 0, 0);
                StdDraw.filledSquare(xCord, yCord, tileLength / 2);

                drawCurrentTile(xCord, yCord, maze[iRow][iCol]);

                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.square(xCord, yCord, tileLength / 2);

                xCord = xCord + tileLength;
            }

            xCord = 0.0 - tileLength;
            for (int iCol = middleCol - 1; iCol > 0; iCol--) {//loop for cols to the left of middle column
                StdDraw.setPenColor(128, 0, 0);
                StdDraw.filledSquare(xCord, yCord, tileLength / 2);

                drawCurrentTile(xCord, yCord, maze[iRow][iCol]);

                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.square(xCord, yCord, tileLength / 2);

                xCord = xCord - tileLength;
            }
            yCord = yCord + tileLength;
        }
        xCord = 0.0;
        yCord = 0.0 - tileLength;
        for (int iRow = middleRow + 1; iRow < maze.length; iRow++) { // loop for rows below middle point
            xCord = 0.0;
            for (int iCol = middleCol; iCol < maze[0].length; iCol++) {//loop for cols to the right of middle column ( including middle )
                StdDraw.setPenColor(128, 0, 0);
                StdDraw.filledSquare(xCord, yCord, tileLength / 2);

                drawCurrentTile(xCord, yCord, maze[iRow][iCol]);

                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.square(xCord, yCord, tileLength / 2);

                xCord = xCord + tileLength;
            }
            xCord = 0.0 - tileLength;
            for (int iCol = middleCol - 1; iCol > 0; iCol--) {//loop for cols to the left of middle column
                StdDraw.setPenColor(128, 0, 0);
                StdDraw.filledSquare(xCord, yCord, tileLength / 2);

                drawCurrentTile(xCord, yCord, maze[iRow][iCol]);

                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.square(xCord, yCord, tileLength / 2);

                xCord = xCord - tileLength;
            }
            yCord = yCord - tileLength;
        }

        xCord = -80.00;
        yCord = 0;

        StdDraw.setPenColor(128, 0, 0);
        StdDraw.filledSquare(xCord,yCord,tileLength/2);

        drawCurrentTile(xCord,yCord,floatingTile);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.square(xCord,yCord,tileLength/2);
    }

    private void drawCurrentTile(double xCord, double yCord, Tile tile) {
        double block = tileLength / 10; // divide tile into grid if 10 squares ( first block centered at xCord, yCord)
        boolean[] directions = tile.getDirections();
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.filledSquare(xCord, yCord, 2 * block);

        if (directions[0]) {
            StdDraw.filledRectangle(xCord, yCord + (3 * block), 2 * block, 2 * block);

        }

        if (directions[1]) {
            StdDraw.filledRectangle(xCord + (3 * block), yCord, 2 * block, 2 * block);
        }

        if (directions[2]) {
            StdDraw.filledRectangle(xCord, yCord - (3 * block), 2 * block, 2 * block);
        }

        if (directions[3]) {
            StdDraw.filledRectangle(xCord - +(3 * block), yCord, 2 * block, 2 * block);
        }

        // now draw relics and adventurers
        Adventurer[] adventurers = tile.getAdventurers();
        for (int i = 0; i < 4; i++) {
            if (adventurers[i] != null) {

                switch (adventurers[i].getColor()) { // place adventurer on tile if there is one

                    case 'G':

                        StdDraw.setPenColor(StdDraw.GREEN);
                        StdDraw.filledCircle(xCord,yCord,1.5*block);
                        break;
                    case 'Y':

                        StdDraw.setPenColor(StdDraw.YELLOW);
                        StdDraw.filledCircle(xCord,yCord,1.5*block);
                        break;
                    case 'R':

                        StdDraw.setPenColor(StdDraw.RED);
                        StdDraw.filledCircle(xCord,yCord,1.5*block);
                        break;
                    case 'B':
                        StdDraw.setPenColor(StdDraw.BLUE);
                        StdDraw.filledCircle(xCord,yCord,1.5*block);
                        break;
                }

            }
        }

        if (tile.hasRelic()) {

            if (!tile.getRelic().isHidden()) {

                switch (tile.getRelic().getColor()) {
                    case 'g': {
                        StdDraw.setPenColor(StdDraw.GREEN);
                        StdDraw.filledSquare(xCord,yCord,block);
                        break;
                    }
                    case 'b': {
                        StdDraw.setPenColor(StdDraw.BLUE);
                        StdDraw.filledSquare(xCord,yCord,block);
                        break;
                    }
                    case 'r': {
                        StdDraw.setPenColor(StdDraw.RED);
                        StdDraw.filledSquare(xCord,yCord,block);
                        break;
                    }
                    case 'y': {
                        StdDraw.setPenColor(StdDraw.YELLOW);
                        StdDraw.filledSquare(xCord,yCord,block);
                        break;
                    }
                }

            }

        }


    }

    public static void main(String[] args) {

    }


}
