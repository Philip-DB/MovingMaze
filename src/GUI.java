/**
 * Class that handles the creation and updating of the GUI using the {@code StdDraw} library.
 *
 * @author Philip de Bruyn
 */
public class GUI {

    private Tile[][] maze;

    private Position[][] positions;

    private Tile floatingTile;
    private GameState gameState;
    private double tileLength;
    private double numRows;
    private double numCols;

    private int slidingCounter; // keeps track of how many sliding points there are in the slidingPoints array ( also used to enter slidingPoint objects into the array itself as a simple loop could not be imployed

    private SlidingPoint[] slidingPoints; // array of slidingPoint objects

    /**
     * Constructor for the GUI object. It sets up the scaling and canvas size of the GUI as well as instantiating important variables needed for drawing objects.
     *
     * @param gameState    a gameState object that contains vital methods and variables for GUI information.
     * @param maze         A 2D tile array that represents the maze.
     * @param floatingTile a tile that represents the current floating tile.
     */
    public GUI(GameState gameState, Tile[][] maze, Tile floatingTile) {
        this.maze = maze;
        this.gameState = gameState;
        this.floatingTile = floatingTile;


        numRows = (double) maze.length - 1;
        numCols = (double) maze[0].length - 1;

        positions = new Position[maze.length][maze[0].length]; // create array that will hold positions of the tiles in it
        int numEvenRows = (maze.length - 1) / 2;
        int numEvenCols = (maze[0].length - 1) / 2;

        slidingPoints = new SlidingPoint[2 * numEvenRows + 2 * numEvenCols]; // make an array of number of even rows and cols for sliding points
        slidingCounter = 0;

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

    /**
     * Void method that draws the current maze, title and scoreboard onto the GUI window
     */
    public void drawMaze() { // draws current maze

        drawTitle();
        drawScoreboard();
        drawTiles();
        StdDraw.show();


    }

    /**
     * Void method that draws the current title onto the GUI window.
     */
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

    /**
     * Void method that draws the scoreboard onto the GUI window.
     */
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

    /**
     * Void method that draws the maze onto the GUI window
     */
    private void drawTiles() {

        slidingCounter = 0;


        for (int iRow = 1; iRow < maze.length; iRow++) { // before drawing the maze, make sure all text representations are up-to-date

            for (int iCol = 1; iCol < maze[0].length; iCol++) {

                maze[iRow][iCol].updateTile();

            }

        }
        floatingTile.updateTile();

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

                drawSlidePoint(iRow, iCol, xCord, yCord); // used for GUI functionality
                positions[iRow][iCol] = new Position(xCord, yCord, iRow, iCol);

                xCord = xCord + tileLength;
            }

            xCord = 0.0 - tileLength;
            for (int iCol = middleCol - 1; iCol > 0; iCol--) {//loop for cols to the left of middle column
                StdDraw.setPenColor(128, 0, 0);
                StdDraw.filledSquare(xCord, yCord, tileLength / 2);

                drawCurrentTile(xCord, yCord, maze[iRow][iCol]);

                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.square(xCord, yCord, tileLength / 2);
                drawSlidePoint(iRow, iCol, xCord, yCord);
                positions[iRow][iCol] = new Position(xCord, yCord, iRow, iCol);

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
                drawSlidePoint(iRow, iCol, xCord, yCord);
                positions[iRow][iCol] = new Position(xCord, yCord, iRow, iCol);

                xCord = xCord + tileLength;
            }
            xCord = 0.0 - tileLength;
            for (int iCol = middleCol - 1; iCol > 0; iCol--) {//loop for cols to the left of middle column
                StdDraw.setPenColor(128, 0, 0);
                StdDraw.filledSquare(xCord, yCord, tileLength / 2);

                drawCurrentTile(xCord, yCord, maze[iRow][iCol]);

                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.square(xCord, yCord, tileLength / 2);
                drawSlidePoint(iRow, iCol, xCord, yCord);
                positions[iRow][iCol] = new Position(xCord, yCord, iRow, iCol);

                xCord = xCord - tileLength;
            }
            yCord = yCord - tileLength;
        }

        xCord = -80.00;
        yCord = -60.00;

        StdDraw.setPenColor(128, 0, 0);
        StdDraw.filledSquare(xCord, yCord, tileLength / 2);

        drawCurrentTile(xCord, yCord, floatingTile);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.square(xCord, yCord, tileLength / 2);
    }

    /**
     * Void method that draws the slidePoint corresponding to the given Row, Column and x,y co-ordinate.
     * @param iRow row of the slidingPoint
     * @param iCol column of the slidingPoint
     * @param xCord x-Coordinate of the tile that the slidingPoint must be drawn near.
     * @param yCord y-Coordinate of the tile that the slidingPoint must be drawn near.
     */
    private void drawSlidePoint(int iRow, int iCol, double xCord, double yCord) { // draws the slidepoint and stores it in a 2d array
        StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
        if (iRow == 1) { // if we are in the top row  ( we draw a slidePoint above each even col )

            if (iCol % 2 == 0) {
                slidingPoints[slidingCounter++] = new SlidingPoint(iRow, iCol, xCord, yCord + tileLength, "n" + iCol);
                StdDraw.filledCircle(xCord, yCord + tileLength, tileLength / 10);

            }


        }

        if (iRow == maze.length - 1) { // if we are in the bottom row, we draw slidePoints below each even col
            if (iCol % 2 == 0) {
                slidingPoints[slidingCounter++] = new SlidingPoint(iRow, iCol, xCord, yCord - tileLength, "s" + iCol);
                StdDraw.filledCircle(xCord, yCord - tileLength, tileLength / 10);
            }


        }

        if (iCol == 1) { // if we are in the left most column

            if (iRow % 2 == 0) {
                slidingPoints[slidingCounter++] = new SlidingPoint(iRow, iCol, xCord - tileLength, yCord, "w" + iRow);
                StdDraw.filledCircle(xCord - tileLength, yCord, tileLength / 10);
            }

        }

        if (iCol == maze[0].length - 1) { // if we are in the right most column

            if (iRow % 2 == 0) {
                slidingPoints[slidingCounter++] = new SlidingPoint(iRow, iCol, xCord + tileLength, yCord, "e" + iRow);
                StdDraw.filledCircle(xCord + tileLength, yCord, tileLength / 10);
            }

        }

    }

    /**
     * Method that determines whether the mouse cursor is inside the Maze section of the GUI and should draw a pathfinder square around a tile.
     * @param xMouse x-Coordinate of the mouse cursor
     * @param yMouse y-Coordinate of the mouse cursor
     * @return true if the mouse cursor is inside the maze section of the GUI
     */
    public boolean canDraw(double xMouse, double yMouse) {

        double halflength = tileLength / 2;
        for (int iRow = 1; iRow < positions.length; iRow++) {

            for (int iCol = 1; iCol < positions[0].length; iCol++) {

                if (xMouse <= positions[iRow][iCol].getxCord() + halflength && xMouse >= positions[iRow][iCol].getxCord() - halflength) { // if x is inside of circle

                    if (yMouse <= positions[iRow][iCol].getyCord() + halflength && yMouse >= positions[iRow][iCol].getyCord() - halflength) { // if x is inside of circle

                        return true;

                    }

                }


            }

        }
        return false;

    }

    /**
     * Returns the position of the tile that is currently being hovered over by the mouse
     * @param xMouse x-Coordinate of the mouse cursor
     * @param yMouse y-Coordinate of the mouse cursor
     * @return a position object if mouse is hovering over a tile and null otherwise.
     */
    public Position getPosition(double xMouse, double yMouse) {

        double halflength = tileLength / 2;
        for (int iRow = 1; iRow < positions.length; iRow++) {

            for (int iCol = 1; iCol < positions[0].length; iCol++) {

                if (xMouse <= positions[iRow][iCol].getxCord() + halflength && xMouse >= positions[iRow][iCol].getxCord() - halflength) { // if x is inside of circle

                    if (yMouse <= positions[iRow][iCol].getyCord() + halflength && yMouse >= positions[iRow][iCol].getyCord() - halflength) { // if x is inside of circle

                        return positions[iRow][iCol];

                    }

                }


            }

        }
        return null;

    }

    /**
     * Void method that draws the green pathfinder square on the currently selected tile if the player can move there and a red square if they cannot move there.
     * @param xMouse x-Coordinate of the mouse cursor
     * @param yMouse y-Coordinate of the mouse cursor
     */
    public void drawPathfinderSquare(double xMouse, double yMouse) {

        if (xMouse <= 50.00 && xMouse >= -50.00) { // check we are actually inside the maze

            if (yMouse >= -50.00 && yMouse <= 50.00) {

                if (canDraw(xMouse, yMouse)) { // if are on a tile , see if we can get there

                    if (canMove(xMouse, yMouse)) {

                        StdDraw.setPenColor(StdDraw.GREEN);
                        StdDraw.square(getPosition(xMouse, yMouse).getxCord(), getPosition(xMouse, yMouse).getyCord(), tileLength / 2);
                    } else {
                        StdDraw.setPenColor(StdDraw.RED);
                        StdDraw.square(getPosition(xMouse, yMouse).getxCord(), getPosition(xMouse, yMouse).getyCord(), tileLength / 2);
                    }


                }

            }

        }
        StdDraw.show();

    }

    /**
     * Method that determines whether the current player can move to the tile specified by the x and y coordinate of the current mouse position.
     * @param xMouse x-Coordinate of the mouse cursor
     * @param yMouse y-Coordinate of the mouse cursor
     * @return true if the player can move to tile currently being hovered on by the mouse cursor
     */
    public boolean canMove(double xMouse, double yMouse) {
        if (gameState.isValidPath(getPosition(xMouse, yMouse).getCol(), getPosition(xMouse, yMouse).getRow(), maze)) {
            // if there is a valid path to the currently highlighted postion, draw a green square
            return true;
        } else {
            return false;
        }
    }

    /**
     * Determines whether a slidingPoint was pressed on the GUI window.
     * @param mouseX x-Coordinate of the mouse cursor
     * @param mouseY y-Coordinate of the mouse cursor
     * @return true if a slidingPoint was pressed.
     */
    public boolean wasSlidingPointPressed(double mouseX, double mouseY) {
        double radius = tileLength / 10;
        for (int i = 0; i < slidingPoints.length; i++) {

            if (mouseX <= slidingPoints[i].getxCord() + radius && mouseX >= slidingPoints[i].getxCord() - radius) { // if x is inside of circle

                if (mouseY <= slidingPoints[i].getyCord() + radius && mouseY >= slidingPoints[i].getyCord() - radius) { // if x is inside of circle

                    return true;

                }

            }


        }
        return false;

    }

    /**
     * Returns the slidingPoint that was pressed. It is used in conjunction with #wasSlidingPointPressed.
     * @param x x-Coordinate of the mouse cursor
     * @param y y-Coordinate of the mouse cursor
     * @return the slidingPoint object from the slidingPoints array that was pressed by the user.
      */
    public SlidingPoint getPoint(double x, double y) {

        double radius = tileLength / 10;
        for (int i = 0; i < slidingPoints.length; i++) {

            if (x <= slidingPoints[i].getxCord() + radius && x >= slidingPoints[i].getxCord() - radius) { // if x is inside of circle

                if (y <= slidingPoints[i].getyCord() + radius && y >= slidingPoints[i].getyCord() - radius) { // if x is inside of circle

                    return slidingPoints[i];

                }

            }


        }
        return null;
    }

    /**
     * Void method that draws the current tile at the given x,y coordinate based on the Tile object's information given as a parameter.
     * @param xCord
     * @param yCord
     * @param tile
     */
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
                        StdDraw.filledCircle(xCord, yCord, 1.5 * block);
                        break;
                    case 'Y':

                        StdDraw.setPenColor(StdDraw.YELLOW);
                        StdDraw.filledCircle(xCord, yCord, 1.5 * block);
                        break;
                    case 'R':

                        StdDraw.setPenColor(StdDraw.RED);
                        StdDraw.filledCircle(xCord, yCord, 1.5 * block);
                        break;
                    case 'B':
                        StdDraw.setPenColor(StdDraw.BLUE);
                        StdDraw.filledCircle(xCord, yCord, 1.5 * block);
                        break;
                }

            }
        }

        if (tile.hasRelic()) {

            if (!tile.getRelic().isHidden()) {

                switch (tile.getRelic().getColor()) {
                    case 'g': {
                        StdDraw.setPenColor(StdDraw.GREEN);
                        StdDraw.filledSquare(xCord, yCord, block);
                        break;
                    }
                    case 'b': {
                        StdDraw.setPenColor(StdDraw.BLUE);
                        StdDraw.filledSquare(xCord, yCord, block);
                        break;
                    }
                    case 'r': {
                        StdDraw.setPenColor(StdDraw.RED);
                        StdDraw.filledSquare(xCord, yCord, block);
                        break;
                    }
                    case 'y': {
                        StdDraw.setPenColor(StdDraw.YELLOW);
                        StdDraw.filledSquare(xCord, yCord, block);
                        break;
                    }
                }

            }

        }

        for (int i = 0; i < 4; i++) {
            if (adventurers[i] != null) {

                if (gameState.getCurrentTurn() == adventurers[i]) {

                    switch (adventurers[i].getColor()) { // place adventurer on tile if there is one

                        case 'G':

                            StdDraw.setPenColor(StdDraw.GREEN);
                            StdDraw.filledCircle(xCord, yCord, 1.5 * block);
                            break;
                        case 'Y':

                            StdDraw.setPenColor(StdDraw.YELLOW);
                            StdDraw.filledCircle(xCord, yCord, 1.5 * block);
                            break;
                        case 'R':

                            StdDraw.setPenColor(StdDraw.RED);
                            StdDraw.filledCircle(xCord, yCord, 1.5 * block);
                            break;
                        case 'B':
                            StdDraw.setPenColor(StdDraw.BLUE);
                            StdDraw.filledCircle(xCord, yCord, 1.5 * block);
                            break;
                    }


                }

            }
        }


    }

    /**
     * Void method that prints the end-screen once a player has won the game.
     */
    public void drawEndScreen() {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.filledRectangle(0, 0, 100, 100);

        switch (gameState.getCurrentTurn().getColor()) { // place adventurer on tile if there is one

            case 'G':

                StdDraw.setPenColor(StdDraw.GREEN);
                break;
            case 'Y':

                StdDraw.setPenColor(StdDraw.YELLOW);
                break;
            case 'R':

                StdDraw.setPenColor(StdDraw.RED);
                break;
            case 'B':
                StdDraw.setPenColor(StdDraw.BLUE);
                break;
        }
        StdDraw.setPenRadius();
        StdDraw.setPenRadius(1);
        StdDraw.text(0.0, 50.00, gameState.getCurrentTurn().getColorString() + " has won!");
        StdDraw.text(0.0, 00.00, "Please press Enter to close this screen");
        StdDraw.show();
        while (true) {
            if (StdDraw.isKeyPressed(10)) { // wait for user to press enter
                System.exit(0);
            }
        }


    }

    /**
     * Setter method that is used to update the new floating tile as a result of rotating or sliding.
     * @param floatingTile the new floating tile.
     */
    public void setFloatingTile(Tile floatingTile) {
        this.floatingTile = floatingTile;
    }

}
