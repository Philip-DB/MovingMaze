/**
 * The TileMaze class handles operations performed on the entire game board, such as shifting and rotating tiles.
 * The class also handles printing out the text representation of the maze.
 * @author Philip de Bruyn
 */

public class TileMaze {

    private Tile[][] Maze; // 2d array of tile objects

    /**
     * Creates a TileMaze object, consisting of a 2D array of Tiles, as a main field.
     * This object does not include the floating tile.
     * @param numCols        the number of columns ( width ) the 2D array should be
     * @param numRows        the number of rows ( height ) the 2D array should be
     * @param encodedStrings a 1D array that holds the encodedStrings for the tiles
     */

    public TileMaze(int numCols, int numRows, String[] encodedStrings) {
        int iCount = 0; // variable to keep track of what index in encodedStrings the loop is at
        Maze = new Tile[numRows + 1][numCols + 1]; // create space for a 2D array of numRows x numCols


        for (int iRow = 1; iRow < numRows + 1; iRow++) { // loop through 2D array and create tiles by passing through encoded string

            for (int iCol = 1; iCol < numCols + 1; iCol++) {

                Maze[iRow][iCol] = new Tile(encodedStrings[iCount]);
                iCount++;
            }

        }


    }

    /**
     * An instance method that slides the floating tile object into the position coded for by {@code slidingIndicator }.
     * If the floating tile can legally be slid into the {@code slidingIndicator} position, then it will be slid in.
     * The method returns a Tile object. This Tile is the new floating tile that will replace the old one once it has been slid into
     * the Maze of Tiles.
     *
     * @param slidingIndicator a {@code String} that holds the side and index that the {@code floatingTile} must be slid into.
     * @param floatingTile a {@code Tile} that is the current floating tile that must be slid into the {@code TileMaze}
     * @return a {@code Tile} reference that refers to the new floating tile object
     * @see MovingMaze#isValidSlide(String, Tile)
     */
    public Tile slideIntoMaze(String slidingIndicator, Tile floatingTile) {

        char[] information = slidingIndicator.toCharArray();
        int index = Integer.parseInt(String.valueOf(information[1]));
        char dir = information[0];
        Tile newFloatingTile; // will hold new floating tile reference

        if (dir == 'w') {

            newFloatingTile = Maze[index][Maze[0].length-1];
            for (int iCol = Maze[0].length-1; iCol > 1; iCol--) {

                Maze[index][iCol] = Maze[index][iCol-1];

            }
            Maze[index][1] = floatingTile;
            newFloatingTile.setLastExitPoint("e" + index);

            for (int i = 0;i<4;i++) {
                floatingTile.setAdventurers(newFloatingTile.getAdventurers()[i]);
                newFloatingTile.removeAdventurer(newFloatingTile.getAdventurers()[i]);
            }
            updateAdventurersPosition(Maze);
            return newFloatingTile;

        }

        if (dir == 'e') {

            newFloatingTile = Maze[index][1];
            for (int iCol = 1; iCol < Maze[0].length-1; iCol++) {

                Maze[index][iCol] = Maze[index][iCol+1];

            }
            Maze[index][Maze[0].length-1] = floatingTile;
            newFloatingTile.setLastExitPoint("w" + index);

            for (int i = 0;i<4;i++) {
                floatingTile.setAdventurers(newFloatingTile.getAdventurers()[i]);


                newFloatingTile.removeAdventurer(newFloatingTile.getAdventurers()[i]);
            }
            updateAdventurersPosition(Maze);
            return newFloatingTile;

        }

        if (dir == 'n') {

            newFloatingTile = Maze[Maze.length-1][index];
            for (int iRow = Maze.length-1; iRow > 1; iRow--) {

                Maze[iRow][index] = Maze[iRow-1][index];

            }
            Maze[1][index] = floatingTile;
            newFloatingTile.setLastExitPoint("s" + index);

            for (int i = 0;i<4;i++) {
                floatingTile.setAdventurers(newFloatingTile.getAdventurers()[i]);


                newFloatingTile.removeAdventurer(newFloatingTile.getAdventurers()[i]);
            }
            updateAdventurersPosition(Maze);
            return newFloatingTile;

        }

            // if the program has not returned yet, we are inserting from south

            newFloatingTile = Maze[1][index];
            for (int iRow = 1; iRow < Maze.length-1; iRow++) {

                Maze[iRow][index] = Maze[iRow+1][index];

            }
            Maze[Maze.length-1][index] = floatingTile;
            newFloatingTile.setLastExitPoint("n" + index);

            for (int i = 0;i<4;i++) {
            floatingTile.setAdventurers(newFloatingTile.getAdventurers()[i]);
            newFloatingTile.removeAdventurer(newFloatingTile.getAdventurers()[i]);
            }
             updateAdventurersPosition(Maze);
            return newFloatingTile;




    }

    /**
     * A Static method of the {@code TileMaze} class that updates all the adventurer's current position to match their location
     * in the 2D array field
     *
     * 
     * @param Maze  a 2D array of {@code Tile} that represents the Maze of Tiles.
     * @see #Maze
     * @see Adventurer#setCurrentRow(int)
     * @see Adventurer#setCurrentCol(int)
     */
    private static void updateAdventurersPosition(Tile[][] Maze) {
        //loop through every tile in the array and update its adventurers positions
        for(int iRow = 1;iRow < Maze.length;iRow++) {

            for (int iCol = 1;iCol< Maze[0].length;iCol++) {

                for(int i = 0;i<4;i++) {

                    if(Maze[iRow][iCol].getAdventurers()[i] != null) { // if there is an adventurer of this color 0 = green , 1 = yellow etc.
                        Maze[iRow][iCol].getAdventurers()[i].setCurrentRow(iRow);
                        Maze[iRow][iCol].getAdventurers()[i].setCurrentCol(iCol);
                    }
                }

            }

        }
    }

    public void createAdventurer( Adventurer a) {
        Maze[a.getCurrentRow()][a.getCurrentCol()].setAdventurers(a);
    }

    /**
     * Instance method that prints the current text representation of the TileMaze to the terminal
     */
    public void drawTextMaze() {

        for (int iRow = 1; iRow < Maze.length; iRow++) { // before drawing the maze, make sure all text representations are up-to-date

            for (int iCol = 1; iCol < Maze[0].length; iCol++) {

                Maze[iRow][iCol].updateTile();

            }

        }

        StdOut.println(); // print a line before the maze
        StdOut.print("     "); // make space for first number
        for (int i = 1; i < Maze[0].length; i++) {
            StdOut.print(i + "       ");
        }
        StdOut.println();// go down a line for first border of the maze

        StdOut.print(" " + MovingMaze.BORDER_TOPLEFT);
        for (int i = 1; i < Maze[0].length; i++) { // for each tile, draw the top borders

            for (int k = 0; k < 7; k++) {
                StdOut.print(MovingMaze.BORDER_HORI);


            }
            if (i == Maze[0].length - 1) {
                StdOut.print(MovingMaze.BORDER_TOPRIGHT); // if its the last column, print a top right
            } else {
                StdOut.print(MovingMaze.BORDER_TOPEDGE); // if its not, print a top edge
            }

        }
        StdOut.println();
        int iCounter = 1;
        // now we draw 3 rows of each tile, and then a bottom row and we do that for how many rows of tiles we have
        for (int iRowObject = 1; iRowObject < Maze.length; iRowObject++) { // loop through the rows of tiles

            for (int iRowTile = 0; iRowTile < 3; iRowTile++) { // loop through the rows of each tile
                if (iRowTile == 1) {
                    StdOut.print(iRowObject);
                    StdOut.print(MovingMaze.BORDER_VERT);
                } else {
                    StdOut.print(" " + MovingMaze.BORDER_VERT); // start each non-ending or start row with a vert border
                }
                for (int iColObject = 1; iColObject < Maze[iRowObject].length; iColObject++) { // loop through each column of tiles

                    for (int iColTile = 0; iColTile < 7; iColTile++) {// loop through the columns of the tiles to print pathways

                        Maze[iRowObject][iColObject].printString(iRowTile, iColTile); // print the bit of the tile

                    }

                    StdOut.print(MovingMaze.BORDER_VERT); // each middle row ends in a vert border

                }
                if (iRowTile == 1) {
                    StdOut.print(iRowObject);
                }
                StdOut.println(); // next row of the tiles

            }
            if (iRowObject == Maze.length - 1) { // draw bottom row of the tile
                StdOut.print(" " + MovingMaze.BORDER_BOTTOMLEFT);// if this is the last row of tiles, start the bottom row with bottom left
            } else {
                StdOut.print(" " + MovingMaze.BORDER_LEFTEDGE);
            }
            for (int i = 1; i < Maze[0].length; i++) { // for each tile, draw the bottom borders

                for (int k = 0; k < 7; k++) {
                    StdOut.print(MovingMaze.BORDER_HORI);


                }
                if (i == Maze[0].length - 1 && iRowObject == Maze.length - 1) { // if last tile of last row, print bottom right
                    StdOut.print(MovingMaze.BORDER_BOTTOMRIGHT);
                }

                if (i == Maze[0].length - 1 && iRowObject != Maze.length - 1) {
                    StdOut.print(MovingMaze.BORDER_RIGHTEDGE);
                }

                if (i != Maze[0].length - 1 && iRowObject != Maze.length - 1) { // if its not the last tile, and not last row
                    StdOut.print(MovingMaze.BORDER_MIDDLE);
                }

                if (i != Maze[0].length - 1 && iRowObject == Maze.length - 1) { // if not last column, but is last row
                    StdOut.print(MovingMaze.BORDER_BOTTOMEDGE);
                }


            }
            StdOut.println();

        }

        StdOut.print("     ");
        for (int i = 1; i < Maze[0].length; i++) {
            StdOut.print(i + "       ");
        }
        StdOut.println();

    }

    public Tile[][] getMaze() {
        return Maze;
    }

    /**
     * Main method of the TileMaze class is used to unit test the class and its functions
     *
     * @param args {@code Integer}
     */
    public static void main(String[] args) {
        int iRows = Integer.parseInt(args[0]);
        int iCols = Integer.parseInt(args[1]);
        String[] encodedStrings = {"0110xx", "0101xx", "0011xx", "1110xx", "1010xx", "1011xx", "1100xx", "1111xx", "1001xx"};
        TileMaze myMaze = new TileMaze(iRows, iCols, encodedStrings); // create TileMaze of tiles with the above encoded values
        //myMaze.drawTiles();
        myMaze.drawTextMaze();


    }
}
