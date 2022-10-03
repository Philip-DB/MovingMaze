/**
 * The Tile class handles the generation of the Tile objects. Tile objects are used to create the game board in the TileMaze class
 * @author Philip de Bruyn
 * @see TileMaze
 */

public class Tile {

    // Variables used to keep track of what sides of the tile are open (north, east , west and south), the relic
    // field ( either has one or null ) and the string array that is the representation of the Tile in terminal mode

    private boolean[] directions = new boolean[4];
    // maybe create an Adventurer class to have an adventurer object
    private Relic relic; // relic object field, if the tile has no relic then it will be null
    private Adventurer[] adventurers = new Adventurer[4]; // a 1D array holding all current adventurers on tile in order Green, Yellow, Red, Blue
    private String[][] pathways = new String[3][7]; // array that will hold pathway strings

    private String lastExitPoint; // a string that holds the sliding indicator of the last position that a tile was slid out


    /**
     * Constructor that takes the tile encoding string and translates in into values for the instance variables of this class
     * It also creates the pathway array that holds the string representation of the tile
     *
     * @param tileEncoding a {@code String} of length 6 that holds the information to create a tile
     */
    public Tile(String tileEncoding) {

        for (int i = 0; i < 4; i++) { // initialize the adventurers array to have no adventurers on the tile
            adventurers[i] = null;
        }
        lastExitPoint = "";

        char[] encodingChars = tileEncoding.toCharArray(); // invoke instance method to create a char array from the string

        for (int i = 0; i < 4; i++) { // loop through the char array

            if (encodingChars[i] == '0') {
                directions[i] = false;
            }

            if (encodingChars[i] == '1') {
                directions[i] = true;
            }
        }
        if (encodingChars[4] == 'x') { // if no relic needs to be on the tile, then make field null
            relic = null;
        } else { // if a relic needs to be on the tile then create it with the color char and num
            int num = Integer.parseInt(String.valueOf(encodingChars[5])); // change the char into an integer
            relic = new Relic(num, encodingChars[4] ); // create relic object
        }
        updateTile();

    }

    /**
     * An instance method that sets the last exit point of the current tile to the sliding indicator.
     * This method is only used on the floating tile, so that it cannot be slid into the last exit point.
     * @param lastExit a {@code String} that represents the last exit point the tile that was slid out
     */
    public void setLastExitPoint(String lastExit) {
        lastExitPoint = lastExit;
    }

    /**
     * An instance method that returns the last exit point of the invoking tile.
     * This is used to compare the sliding position of the current floating tile to the last exit point , and check
     * if they are the same.
     * @return a {@code String} that represents the last exit point.
     */
    public String getLastExitPoint() {
        return lastExitPoint;
    }

    /**
     * An instance method that updates the {@code pathways} 2D string array that represents the string representation of the tile
     * to match the current state of its variables.
     */
    public void updateTile() {
        initializePathways();
        if (directions[0]) {
            pathways[0][3] = MovingMaze.PATH_NS;
        }
        if (directions[1]) {
            pathways[1][4] = MovingMaze.PATH_EW;
            pathways[1][5] = MovingMaze.PATH_EW;
            pathways[1][6] = MovingMaze.PATH_EW;
        }

        if (directions[2]) {
            pathways[2][3] = MovingMaze.PATH_NS;
        }

        if (directions[3]) {
            pathways[1][0] = MovingMaze.PATH_EW;
            pathways[1][1] = MovingMaze.PATH_EW;
            pathways[1][2] = MovingMaze.PATH_EW;
        }


        if (!directions[0] && directions[1] && !directions[2] && directions[3]) {
            pathways[1][3] = MovingMaze.PATH_EW;
        }
        if (directions[0] && !directions[1] && directions[2] && !directions[3]) {
            pathways[1][3] = MovingMaze.PATH_NS;
        }
        if (!directions[0] && directions[1] && directions[2] && !directions[3]) {
            pathways[1][3] = MovingMaze.PATH_ES;
        }
        if (!directions[0] && !directions[1] && directions[2] && directions[3]) {
            pathways[1][3] = MovingMaze.PATH_SW;
        }
        if (directions[0] && directions[1] && !directions[2] && !directions[3]) {
            pathways[1][3] = MovingMaze.PATH_NE;
        }
        if (directions[0] && !directions[1] && !directions[2] && directions[3]) {
            pathways[1][3] = MovingMaze.PATH_NW;
        }
        if (directions[0] && directions[1] && directions[2] && !directions[3]) {
            pathways[1][3] = MovingMaze.PATH_NES;
        }
        if (directions[0] && !directions[1] && directions[2] && directions[3]) {
            pathways[1][3] = MovingMaze.PATH_NSW;
        }
        if (!directions[0] && directions[1] && directions[2] && directions[3]) {
            pathways[1][3] = MovingMaze.PATH_ESW;
        }
        if (directions[0] && directions[1] && !directions[2] && directions[3]) {
            pathways[1][3] = MovingMaze.PATH_NEW;
        }
        if (directions[0] && directions[1] && directions[2] && directions[3]) {
            pathways[1][3] = MovingMaze.PATH_NESW;
        }

        for (int i = 0; i < 4; i++) {
            if (adventurers[i] != null) {

                switch (adventurers[i].getColor()) { // place adventurer on tile if there is one

                    case 'G':
                        pathways[0][1] = String.valueOf('G');
                        break;
                    case 'Y':
                        pathways[0][5] = String.valueOf('Y');
                        break;
                    case 'R':
                        pathways[2][1] = String.valueOf('R');
                        break;
                    case 'B':
                        pathways[2][5] = String.valueOf('B');
                        break;
                }

            }
        }

        if (hasRelic()) { // if the tile has a relic,and relic is not hidden i.e. it's the first relic, middle must be the relic char

            if (!relic.isHidden()) {
                pathways[1][3] = String.valueOf(relic.getColor());

            }

        }

    }


    /**
     * Method that returns an array that holds the references to the adventurers on that tile
     *
     * @return a 1D array of {@code Adventurer} objects
     */
    public Adventurer[] getAdventurers() {
        return adventurers;
    }

    /**
     * A mutator method that updates the directions that the tile is open to.
     *
     * @param arrDirections is an array that holds the directions the tile should be open to in order of North, East, South, West
     */
    public void setDirections(boolean[] arrDirections) {

        for (int p = 0; p < arrDirections.length; p++) { // loop through new direction array and update old directions

            directions[p] = arrDirections[p]; // update old direction to new direction


        }


    }


    /**
     * Generates a new array with the directions rotated clockwise
     *
     * @return a new {@code boolean} array with the directions rotated clockwise
     */
    public boolean[] rotateTileClockwise() {

        return new boolean[]{directions[3], directions[0], directions[1], directions[2]};
    }

    /**
     * Generates a new array with the directions rotated Counter-Clockwise
     * @return a new {@code boolean} array with the directions rotated Counter-Clockwise
     */
    public boolean[] rotateTileCounterClockwise() {

        return new boolean[]{directions[1], directions[2], directions[3], directions[0]};
    }

    /**
     * An accessor method that gets the direction that the tile is open to
     *
     * @return an array containing the direction that the tile is open to
     */
    public boolean[] getDirections() {
        return directions;
    }

    /**
     * Returns whether the current tile has relic on it
     *
     * @return a boolean that is false if no relic is present and true if a relic is present
     */
    public boolean hasRelic() {

        return relic != null; // if the relic is not null, then return true, if its null, return false


    }

    /**
     * An instance method that gets the relic on the invoking tile ( if it does not have one it returns a null reference )
     * @return A reference to a {@code Relic} object, or {@code null} if not relic is on that tile.
     */
    public Relic getRelic() {
        return relic;
    }

    /**
     * This instance method prints out the string at [iRow][iCol] in the {@code pathways} 2D array
     * This
     * @param iRow the row index of the array that is accessed.
     * @param iCol the column index of the array that is accessed.
     */
    public void printString(int iRow, int iCol) {
        StdOut.print(pathways[iRow][iCol]); // print the part of the array at iRow , iCol
    }

    /**
     * This instance method simply acts as a way to initialize all entries in the 2D {@code pathways} array
     * so that the spaces needed for correct text display are present.
     */
    private void initializePathways() {
        for (int iRow = 0; iRow < 3; iRow++) {

            for (int iCol = 0; iCol < 7; iCol++) {
                pathways[iRow][iCol] = " ";
            }

        }
    }

    /**
     * Draws the single tile with its borders. It is used only for the floating tile
     */
    public void drawTile() {
        StdOut.println();
        updateTile();
        StdOut.print(MovingMaze.BORDER_TOPLEFT);
        for (int i = 0; i < 7; i++) {
            StdOut.print(MovingMaze.BORDER_HORI);
        }
        StdOut.print(MovingMaze.BORDER_TOPRIGHT);
        StdOut.println();

        for (int iRow = 0; iRow < 3; iRow++) {
            StdOut.print(MovingMaze.BORDER_VERT);
            for (int iCol = 0; iCol < 7; iCol++) {
                StdOut.print(pathways[iRow][iCol]);
            }
            StdOut.print(MovingMaze.BORDER_VERT);
            StdOut.println();
        }

        StdOut.print(MovingMaze.BORDER_BOTTOMLEFT);
        for (int i = 0; i < 7; i++) {
            StdOut.print(MovingMaze.BORDER_HORI);
        }
        StdOut.print(MovingMaze.BORDER_BOTTOMRIGHT);
        StdOut.println();
        StdOut.println();
    }

    /**
     * An instance method places the {@code Adventurer} onto the tile.
     * This method places the adventurer object into the {@code adventurers} array at the index matching
     * the player's color
     * @param a the {@code Adventurer} object to be inserted into the {@code adventurers} array.
     */
    public void setAdventurers(Adventurer a) { // used to add an adventurer to a tile

        if (a == null) {
            return;
        }

        switch (a.getColor()) {
            case 'G': {
                adventurers[0] = a;
                break;
            }
            case 'Y': {
                adventurers[1] = a;
                break;
            }
            case 'R': {
                adventurers[2] = a;
                break;
            }
            case 'B': {
                adventurers[3] = a;
                break;
            }


        }
    }

    /**
     * An instance method removes the {@code Adventurer} from the tile.
     * This method removes the adventurer object from the {@code adventurers} array at the index matching
     * the player's color by making the reference at that index null.
     * @param a the {@code Adventurer} object reference to be set to null in the {@code adventurers} array.
     */
    public void removeAdventurer(Adventurer a) {

        if(a == null) {
            return;
        }

        switch (a.getColor()) {
            case 'G': {
                adventurers[0] = null;
                break;
            }
            case 'Y': {
                adventurers[1] = null;
                break;
            }
            case 'R': {
                adventurers[2] = null;
                break;
            }
            case 'B': {
                adventurers[3] = null;
                break;
            }


        }
    }

    /**
     * An instance method that checks if the invoking tile is open to the direction provided as a parameter.
     * @param direction a {@code char} that represents the direction that the tile may or may not be open to.
     * @return {@code true} if the tile is open to that {@code direction}
     */
    public boolean isOpenToSide(char direction) {

        switch (direction) {
            case 'n':
                return directions[0];
            case 'e':
                return directions[1];
            case 's':
                return directions[2];
            case 'w':
                return directions[3];
        }
        return false;
    }


    /**
     * The main method within the Tile.java class is used to unit test the class. It provides the constructor with an encoded
     * string in order to test if the constructor creates the tile in the correct specification listed in the encoding.
     * The unit test also tests if the string array representation of the tile is created properly by displaying the tile in
     * the terminal
     *
     * @param args contains the tileEncoding string used to create the tile
     */
    public static void main(String[] args) {

        String tileEncoding = args[0];
        Tile myTile = new Tile(tileEncoding);
        // StdArrayIO.print(myTile.getDirections());
        //StdOut.println(Boolean.toString(myTile.hasRelic()));

        // boolean[] arrDirections = new boolean[4];
        // arrDirections[0] = true;
        // arrDirections[1] = true;
        // arrDirections[2] = true;
        // arrDirections[3] = true;
        // myTile.setDirections( arrDirections);
        // StdArrayIO.print(myTile.getDirections());
        if (myTile.hasRelic()) { // check if un-hiding of relic on tile works
            myTile.relic.unHide();
        }

        myTile.updateTile();
        myTile.drawTile();


    }

}
