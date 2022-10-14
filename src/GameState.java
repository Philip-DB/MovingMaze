/**
 * The GameState class handles storing information pertaining to the current state of the game. Such as current turn and if the game is running.
 * It also performs certain functions that alter the state of the game and its objects.
 * These functions include moving players, ending the game, collecting relics, checking if the game has been won
 * and in some cases displaying the board and scoreboard.
 * @author Philip de Bruyn
 */

public class GameState {

    private Adventurer[] adventurers = new Adventurer[4];
    private int currentTurnIndex;
    private int K;
    boolean gameIsRunning;
    boolean isMoving;

    /**
     * A Constructor that is used to create the gameState object and initializes all players , the starting turn and the number of relics to collect
     * as well as the  {@code gameIsRunning} variable.
     * @param a {@code Adventurer} Player Green
     * @param b {@code Adventurer} Player Yellow
     * @param c {@code Adventurer} Player Red
     * @param d {@code Adventurer} Player Blue
     * @param numRelic an {@code Integer} that holds how many relics must be collected for the game to be won.
     */
    public GameState(Adventurer a, Adventurer b, Adventurer c, Adventurer d, int numRelic) {
        adventurers[0] = a;
        adventurers[1] = b;
        adventurers[2] = c;
        adventurers[3] = d;
        currentTurnIndex = 0;
        K = numRelic;
        isMoving = false;
        gameIsRunning = true;
    }

    /**
     * Marks the isMoving boolean variables as true
     */
    public void startMoving() {
        isMoving = true;
    }

    /**
     * Marks the isMoving boolean variables as false
     */
    public void stopMoving() {
        isMoving = false;
    }

    /**
     * returns whether the players are currently moving or not
     * @return true of the players are moving or false if they are busy sliding.
     */
    public boolean isMoving() {
        return isMoving;
    }

    /**
     * Instance method that returns the current Adventurer object whose turn it currently is.
     * @return an {@code Adventurer} object
     */
    public Adventurer getCurrentTurn() {
        return adventurers[currentTurnIndex];
    }

    /**
     * An instance method that ends the game and prints out the scoreboard
     */
    public void endGame() {

        gameIsRunning = false;

        printScoreBoard();
        System.exit(0);

    }

    /**
     * An instance method that moves the current player into the direction entered by the user if legal.
     * @param dir A {@code Char} that represents the direction that the adventurer wishes to move in
     * @param maze A {@code Tile} 2D array that represents the maze of tiles.
     * @see TileMaze for more information on the 2D array of Tiles.
     * @see Tile for more information on Tiles.
     */
    public void movePlayer(char dir, Tile[][] maze) {

        int currentRow = getCurrentTurn().getCurrentRow(); // get the row and column adventurer is in
        int currentCol = getCurrentTurn().getCurrentCol();



        switch (dir) {
            case 'n': {


                maze[currentRow - 1][currentCol].setAdventurers(getCurrentTurn());
                maze[currentRow][currentCol].removeAdventurer(getCurrentTurn());

                getCurrentTurn().setCurrentRow(currentRow - 1);
                getCurrentTurn().setCurrentCol(currentCol);
                StdOut.println(" Moving north.");
                break;
            }
            case 'e': {


                maze[currentRow][currentCol + 1].setAdventurers(getCurrentTurn());
                maze[currentRow][currentCol].removeAdventurer(getCurrentTurn());

                getCurrentTurn().setCurrentRow(currentRow);
                getCurrentTurn().setCurrentCol(currentCol + 1);
                StdOut.println(" Moving east.");
                break;
            }
            case 's': {

                maze[currentRow + 1][currentCol].setAdventurers(getCurrentTurn());
                maze[currentRow][currentCol].removeAdventurer(getCurrentTurn());

                getCurrentTurn().setCurrentRow(currentRow + 1);
                getCurrentTurn().setCurrentCol(currentCol);
                StdOut.println(" Moving south.");
                break;
            }
            case 'w': {

                maze[currentRow][currentCol - 1].setAdventurers(getCurrentTurn());
                maze[currentRow][currentCol].removeAdventurer(getCurrentTurn());

                getCurrentTurn().setCurrentRow(currentRow);
                getCurrentTurn().setCurrentCol(currentCol - 1);
                StdOut.println(" Moving west.");
                break;
            }


        }


    }

    /**
     * An instance method used to determine if there is a valid path from the current players position to the desired position provided as input
     * @param desiredCol desired column that the player wishes to move to
     * @param desiredRow desired row that the player wishes to move to
     * @param maze A {@code Tile} 2D array that represents the maze of tiles.
     * @return {@code true} if the player can move to that position and false otherwise.
     */
    public boolean isValidPath(int desiredCol,int desiredRow,Tile[][] maze) { // function that determines if there is a path from adventurers current position to the desired tile
        boolean[][] reachable = new boolean[maze.length][maze[0].length]; // create new 2d array ( will all indices start as false ) to hold reachable tiles from current position
        boolean[][] visited = new boolean[maze.length][maze[0].length]; // array to hold positions already visited ( so that recursion does not go in circles )

        int currentRow = getCurrentTurn().getCurrentRow(); // get the players starting position and pass to function as starting point
        int currentCol = getCurrentTurn().getCurrentCol();

        if( desiredCol >= maze[0].length || desiredRow >= maze.length) {
           // StdOut.println(" Cannot move to " + desiredCol + "," + desiredRow + ": no path.");
            return false;
        }




        getReachableTiles(maze,reachable,visited,currentRow,currentCol); // update new reachable array with reachable tiles from current position

        if(reachable[desiredRow][desiredCol]) {
            //StdOut.println(" Moving to " + desiredCol + "," + desiredRow + ".");
            return true;
        } else {
            //StdOut.println(" Cannot move to " + desiredCol + "," + desiredRow + ": no path.");
            return false;
        }


    }


    /**
     * Auxillary method used by #isValidPath to determine which tiles are reachable from the current players position
     * @param maze A {@code Tile} 2D array that represents the maze of tiles.
     * @param reachable A 2D boolean array that will hold the reachable tiles indices.
     * @param visited A 2D boolean array that will hold the tiles already visited by the recursive function.
     * @param currentRow current row that the player is in.
     * @param currentCol current column the player is in.
     */
    public void getReachableTiles(Tile[][] maze, boolean[][] reachable,boolean[][] visited,int currentRow,int currentCol) { // a recursive function that modifies the given 2d boolean array to represent which tiles are accessible from the current tile
        int numRows = maze.length;
        int numCols = maze[0].length; // this is to make code easier to read, we also need 2 vars as the maze is not always square.
        // update all reachable sites from players position recursively



        if (visited[currentRow][currentCol]) {

            return; // if we have already been to this tile, return
        }
        // now we know we aren't out of bounds and haven't been to this tile before, now we can check adjacent tiles recursively

        visited[currentRow][currentCol] = true;
        reachable[currentRow][currentCol] = true;

        if(currentRow != 1) {
            if (maze[currentRow - 1][currentCol].isOpenToSide('s') && maze[currentRow][currentCol].isOpenToSide('n') ) { // check above

                getReachableTiles(maze, reachable, visited, currentRow - 1, currentCol);
            }
        }
        if(currentRow != numRows-1) {
            if (maze[currentRow + 1][currentCol].isOpenToSide('n') && maze[currentRow][currentCol].isOpenToSide('s')) { // below

                getReachableTiles(maze, reachable, visited, currentRow + 1, currentCol);
            }
        }

        if(currentCol != numCols-1) {
            if (maze[currentRow][currentCol + 1].isOpenToSide('w') && maze[currentRow][currentCol].isOpenToSide('e')) { // right

                getReachableTiles(maze, reachable, visited, currentRow, currentCol + 1);
            }
        }

        if(currentCol != 1) {
            if (maze[currentRow][currentCol - 1].isOpenToSide('e') && maze[currentRow][currentCol].isOpenToSide('w')) { // left

                getReachableTiles(maze, reachable, visited, currentRow, currentCol - 1);
            }
        }


    }

    /**
     * Teleports player directly the desired column and row in the maze.
     * @param desiredCol the desired column to teleport player to.
     * @param desiredRow the desired row to teleport player to.
     * @param maze A {@code Tile} 2D array that represents the maze of tiles.
     */

    public void teleportPlayer(int desiredCol,int desiredRow,Tile[][] maze) {
        int currentRow = getCurrentTurn().getCurrentRow(); // get the players current position
        int currentCol = getCurrentTurn().getCurrentCol();

        if(currentCol == desiredCol && currentRow == desiredRow) {
            return;
        }

        maze[desiredRow][desiredCol].setAdventurers(getCurrentTurn()); // put adventurer on new tile
        maze[currentRow][currentCol].removeAdventurer(getCurrentTurn()); // remove from old tile

        getCurrentTurn().setCurrentRow(desiredRow);
        getCurrentTurn().setCurrentCol(desiredCol);


    }


    /**
     * This function moves the player to the new tile without printing anything to the terminal. Its main use is for API testing
     *
     * @param dir A {@code Char} indicating the direction the player wants to move in
     * @param maze A {@code Tile} 2D array that represents the maze of tiles.
     */
    public void movePlayerNoText(char dir, Tile[][] maze) {

        int currentRow = getCurrentTurn().getCurrentRow(); // get the row and column adventurer is in
        int currentCol = getCurrentTurn().getCurrentCol();

        switch (dir) {
            case 'n': {


                maze[currentRow - 1][currentCol].setAdventurers(getCurrentTurn());
                maze[currentRow][currentCol].removeAdventurer(getCurrentTurn());

                getCurrentTurn().setCurrentRow(currentRow - 1);
                getCurrentTurn().setCurrentCol(currentCol);

                break;
            }
            case 'e': {


                maze[currentRow][currentCol + 1].setAdventurers(getCurrentTurn());
                maze[currentRow][currentCol].removeAdventurer(getCurrentTurn());

                getCurrentTurn().setCurrentRow(currentRow);
                getCurrentTurn().setCurrentCol(currentCol + 1);

                break;
            }
            case 's': {

                maze[currentRow + 1][currentCol].setAdventurers(getCurrentTurn());
                maze[currentRow][currentCol].removeAdventurer(getCurrentTurn());

                getCurrentTurn().setCurrentRow(currentRow + 1);
                getCurrentTurn().setCurrentCol(currentCol);

                break;
            }
            case 'w': {

                maze[currentRow][currentCol - 1].setAdventurers(getCurrentTurn());
                maze[currentRow][currentCol].removeAdventurer(getCurrentTurn());

                getCurrentTurn().setCurrentRow(currentRow);
                getCurrentTurn().setCurrentCol(currentCol - 1);

                break;
            }


        }

    }

    /**
     * An instance method that checks whether a player can move from the current tile to a destination tile.
     * This functionality was built into my other methods, however for API testing, this method was created to test the
     * subroutines using the same logic in my other methods.
     * @param dir A {@code Char} indicating what direction the player wants to move in.
     * @param currentTile The {@code Tile} that the adventurer is currently located on.
     * @param destinationTile The {@code Tile} that the adventurer would like to travel to.
     * @return {@code true} if the adventurer can move to the destination {@code Tile} .
     */
    public static boolean canMoveInDirection(char dir, Tile currentTile, Tile destinationTile) {
        char oppDir = ' ';

        if (dir == 'n') {

            oppDir = 's';
        }
        if (dir == 'e') {

            oppDir = 'w';
        }
        if (dir == 's') {

            oppDir = 'n';
        }
        if (dir == 'w') {

            oppDir = 'e';
        }

        if (currentTile.isOpenToSide(dir) && destinationTile.isOpenToSide(oppDir)) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * Static method that checks if the player can move in the direction provided based on their current position in the maze.
     * As a side effect, it also sends a String to StdOut to indicate if the player
     * moved, and if not , why he could not move
     * @param dir a {@code Char } that dictates what direction the player wants to move in
     * @param maze a {@code Tile} 2D array that represents the Maze of Tiles
     * @return {@code true} if the player is able to move in that direction.
     */
    public boolean isValidPlayerMove(char dir, Tile[][] maze) {

        String direction = "";
        char oppDir = ' ';
        if (dir == 'n') {
            direction = "north";
            oppDir = 's';
        }
        if (dir == 'e') {
            direction = "east";
            oppDir = 'w';
        }
        if (dir == 's') {
            direction = "south";
            oppDir = 'n';
        }
        if (dir == 'w') {
            direction = "west";
            oppDir = 'e';
        }
        //check if the tile that the adventurer is currently on, is open to the direction they wish to travel in
        if (maze[getCurrentTurn().getCurrentRow()][getCurrentTurn().getCurrentCol()].isOpenToSide(dir)) {

            switch (dir) {
                case 'n': {
                    if (getCurrentTurn().getCurrentRow() - 1 < 1) {
                        StdOut.println(" Cannot move " + direction + ": off the board.");
                        return false;
                    }
                    break;
                }
                case 'e': {
                    if (getCurrentTurn().getCurrentCol() + 1 > maze[0].length - 1) {
                        StdOut.println(" Cannot move " + direction + ": off the board.");
                        return false;
                    }
                    break;
                }
                case 's': {
                    if (getCurrentTurn().getCurrentRow() + 1 > maze.length - 1) {
                        StdOut.println(" Cannot move " + direction + ": off the board.");
                        return false;
                    }

                    break;
                }
                case 'w': {
                    if (getCurrentTurn().getCurrentCol() - 1 < 1) {
                        StdOut.println(" Cannot move " + direction + ": off the board.");
                        return false;
                    }

                    break;
                }


            }

            switch (oppDir) { // check if the tile in that direction is also open to that opposite direction

                case 's': {

                    if (!maze[getCurrentTurn().getCurrentRow() - 1][getCurrentTurn().getCurrentCol()].isOpenToSide(oppDir)) {
                        StdOut.println(" Cannot move " + direction + ": no path.");
                        return false;
                    }
                    break;

                }

                case 'w': {

                    if (!maze[getCurrentTurn().getCurrentRow()][getCurrentTurn().getCurrentCol() + 1].isOpenToSide(oppDir)) {
                        StdOut.println(" Cannot move " + direction + ": no path.");
                        return false;
                    }
                    break;

                }

                case 'e': {

                    if (!maze[getCurrentTurn().getCurrentRow()][getCurrentTurn().getCurrentCol() - 1].isOpenToSide(oppDir)) {
                        StdOut.println(" Cannot move " + direction + ": no path.");
                        return false;
                    }
                    break;

                }

                case 'n': {

                    if (!maze[getCurrentTurn().getCurrentRow() + 1][getCurrentTurn().getCurrentCol()].isOpenToSide(oppDir)) {
                        StdOut.println(" Cannot move " + direction + ": no path.");
                        return false;
                    }
                    break;

                }

            }


        } else {

            StdOut.println(" Cannot move " + direction + ": no path.");
            return false;
        }
        return true; // if no issues found then return true

    }

    /**
     * Instance method that checks if the player can move in the direction provided based on their current position in the maze.
     * This method is different to the method referenced below as it does not print any strings.
     * @param dir a {@code Char } that dictates what direction the player wants to move in
     * @param maze a {@code Tile} 2D array that represents the Maze of Tiles
     * @return {@code true} if the player is able to move in that direction.
     * @see #isValidPlayerMove(char, Tile[][])
     */
    public boolean isValidPlayerMoveWithoutText(char dir, Tile[][] maze) {


        char oppDir = ' ';
        if (dir == 'n') {

            oppDir = 's';
        }
        if (dir == 'e') {

            oppDir = 'w';
        }
        if (dir == 's') {

            oppDir = 'n';
        }
        if (dir == 'w') {

            oppDir = 'e';
        }
        //check if the tile that the adventurer is currently on, is open to the direction they wish to travel in
        if (maze[getCurrentTurn().getCurrentRow()][getCurrentTurn().getCurrentCol()].isOpenToSide(dir)) {

            switch (dir) {
                case 'n': {
                    if (getCurrentTurn().getCurrentRow() - 1 < 1) {

                        return false;
                    }
                    break;
                }
                case 'e': {
                    if (getCurrentTurn().getCurrentCol() + 1 > maze[0].length - 1) {

                        return false;
                    }
                    break;
                }
                case 's': {
                    if (getCurrentTurn().getCurrentRow() + 1 > maze.length - 1) {

                        return false;
                    }

                    break;
                }
                case 'w': {
                    if (getCurrentTurn().getCurrentCol() - 1 < 1) {

                        return false;
                    }

                    break;
                }


            }

            switch (oppDir) { // check if the tile in that direction is also open to that opposite direction

                case 's': {

                    if (!maze[getCurrentTurn().getCurrentRow() - 1][getCurrentTurn().getCurrentCol()].isOpenToSide(oppDir)) {

                        return false;
                    }
                    break;

                }

                case 'w': {

                    if (!maze[getCurrentTurn().getCurrentRow()][getCurrentTurn().getCurrentCol() + 1].isOpenToSide(oppDir)) {

                        return false;
                    }
                    break;

                }

                case 'e': {

                    if (!maze[getCurrentTurn().getCurrentRow()][getCurrentTurn().getCurrentCol() - 1].isOpenToSide(oppDir)) {

                        return false;
                    }
                    break;

                }

                case 'n': {

                    if (!maze[getCurrentTurn().getCurrentRow() + 1][getCurrentTurn().getCurrentCol()].isOpenToSide(oppDir)) {

                        return false;
                    }
                    break;

                }

            }


        } else {


            return false;
        }
        return true; // if no issues found then return true

    }

    /**
     * An instance method used to swap the to next adventurers turn in the sequence : Green, Yellow, Red and Blue, cyclically.
     */
    public void nextTurn() {
        StdOut.println("End of " + getCurrentTurn().getColorString() + "'s turn.");
        printScoreBoard();

        if (currentTurnIndex != 3) {
            currentTurnIndex++;
        } else {
            currentTurnIndex = 0;
        }


    }

    /**
     * An instance method that prints the current scores of the adventurers as well as the total amount of relics they must collect.
     */
    private void printScoreBoard() {

        StdOut.println("Relics collected /" + K + ":");
        StdOut.println("- Green  " + adventurers[0].getNumRelicCollected());
        StdOut.println("- Yellow " + adventurers[1].getNumRelicCollected());
        StdOut.println("- Red    " + adventurers[2].getNumRelicCollected());
        StdOut.println("- Blue   " + adventurers[3].getNumRelicCollected());

    }

    /**
     * An accessor method that returns the array of adventurers playing the game.
     * @return a reference to a 1D {@code Adventurer} array.
     */
    public Adventurer[] getAdventurers() {
        return adventurers;
    }


    /**
     * An instance method that checks if any adventurers is in a winning position and state ( meaning they have collected all relics )
     * @return {@code true} if the player won the game
     */
    public boolean ifWon() {
        if (getCurrentTurn().getNumRelicCollected() == K && getCurrentTurn().getCurrentRow() == getCurrentTurn().getStartingRow() && getCurrentTurn().getCurrentCol() == getCurrentTurn().getStartingCol()) {
            // check if the current player is in a winning position
            return true;
        }
        return false;
    }

    /**
     * An instance method that is used to collect relics during the moving phase. After a move is executed,
     * this method is called. The current player who is moving is compared to the tile they are on. If a relic that can be picked up by that player
     * is on their current tile, they collect the relic, and the next one is displayed. The updated board is printed, whether a relic is picked up or not.
     * If a relic is collected, the current players turn ends, and the score board is printed
     * @param maze a 2D array of {@code Tile} that represents the Maze of Tiles.
     * @param TileMaze a {@code TileMaze} object that represents the Maze as a whole.
     * @param floatingTile a {@code Tile} object that represents the current floating tile.
     * @return {@code true} if a relic was collected
     * @see #movePlayer(char, Tile[][])
     * @see TileMaze#drawTextMaze()
     */
    public boolean movementRelicCollection(Tile[][] maze, TileMaze TileMaze, Tile floatingTile) {
        // allows the moving player to pick up relics and in doing so will end their turn
        int iRow = getCurrentTurn().getCurrentRow(); //
        int iCol = getCurrentTurn().getCurrentCol();
        int relicsCollected = getCurrentTurn().getNumRelicCollected();
        boolean allCollected = false;


        if (maze[iRow][iCol].hasRelic()) { // if the adventurer is on a tile with a relic, proceed

            if (maze[iRow][iCol].getRelic().getRelicNum() == relicsCollected + 1 && maze[iRow][iCol].getRelic().getColor() == Character.toLowerCase(getCurrentTurn().getColor()) && !maze[iRow][iCol].getRelic().isHidden()) {
                // if the adventurer is on a tile with a match relic, pick it up, unhide the next and end turn

                getCurrentTurn().incNumRelicCollected(); // pick it up
                maze[iRow][iCol].getRelic().Hide(); // pick it up

                // now we un-hide next relic, we do this by looping through the maze and finding the next relic of that color
                unHideNextRelic(getCurrentTurn(), maze, floatingTile);

                // check if player has collected all relics
                if (getCurrentTurn().getNumRelicCollected() == K) { // if the player has collected all their relics, we also end turn but add more text
                    allCollected = true;

                }
                TileMaze.drawTextMaze(); // draw updated maze and afterwards we print needed messages
                floatingTile.drawTile();
                StdOut.println(getCurrentTurn().getColorString() + " has collected a relic.");
                if (allCollected) {
                    StdOut.println(getCurrentTurn().getColorString() + " has all their relics.");
                }

                nextTurn();
                return true;


            } else { // if the tile has a relic, but it doesnt match the next one to be collected, we just draw and move on. Players move does not end
                TileMaze.drawTextMaze();
                floatingTile.drawTile();
                return false;
            }

        } else { // the tile that the adventurer is on does not have a relic, so we just print maze and move on. The players move does not end
            TileMaze.drawTextMaze();
            floatingTile.drawTile();
            return false;
        }


    }

    /**
     * An instance method that is used to collect relics during the sliding and rotating phase. After a tile is rotated and slid into the maze
     * this method is called. All players current tile is checked to see if a relic collected should be triggered. If a relic is collected,
     * the players turn does not end. If a relic is collected, the next relic for that player is shown. After all tiles that hold players are checked
     * for relic collection, the updated board is printed
     *
     * @param maze a 2D array of {@code Tile} that represents the Maze of Tiles.
     * @param TileMaze  a {@code TileMaze} object that represents the Maze as a whole.
     * @param floatingTile a {@code Tile} object that represents the current floating tile.
     *
     * @see TileMaze#drawTextMaze()
     * @see TileMaze#slideIntoMaze(String, Tile)
     */
    public void wrapAroundRelicCollection(Tile[][] maze, TileMaze TileMaze, Tile floatingTile) {
        // what we do here, is go through all adventurers, check if they are on a tile that they can pick up a relic on. If so, we pick it up, but dont end their turn

        for (int iPlayer = 0; iPlayer < 4; iPlayer++) { // check for each adventurer
            int iRow = adventurers[iPlayer].getCurrentRow();
            int iCol = adventurers[iPlayer].getCurrentCol();
            int relicsCollected = adventurers[iPlayer].getNumRelicCollected();


            if (maze[iRow][iCol].hasRelic()) { // if the current player is on a tile with a relic, proceed to checks

                if (maze[iRow][iCol].getRelic().getRelicNum() == relicsCollected + 1 && maze[iRow][iCol].getRelic().getColor() == Character.toLowerCase(adventurers[iPlayer].getColor()) && !maze[iRow][iCol].getRelic().isHidden()) {
                // if the player is on a tile with a relic that must be collected, collect it and report its collection and scoreboard

                    adventurers[iPlayer].incNumRelicCollected(); // pick it up
                    maze[iRow][iCol].getRelic().Hide(); // hide it

                    // now we un-hide next relic, we do this by looping through the maze and finding the next relic of that color
                    unHideNextRelic(adventurers[iPlayer], maze, floatingTile);
                    // print scoreboard
                    StdOut.println(adventurers[iPlayer].getColorString() + " collected a relic.");
                    if(adventurers[iPlayer].getNumRelicCollected() == K) {
                        StdOut.println(adventurers[iPlayer].getColorString() + " has all their relics.");
                    }
                    printScoreBoard();

                }


            }

        }

    }

    /**
     * An instance method used to un-hide the next relic that the adventurer should collect.
     * @param player an {@code Adventurer} object of which the next relic should be unlocked
     * @param maze a 2D array of {@code Tile} that represents the Maze of Tiles.
     * @param floatingTile a {@code Tile} object that represents the current floating tile.
     */

    public void unHideNextRelic(Adventurer player, Tile[][] maze, Tile floatingTile) {

        if (floatingTile.hasRelic()) {
            if (floatingTile.getRelic().getColor() == Character.toLowerCase(player.getColor()) && floatingTile.getRelic().getRelicNum() == player.getNumRelicCollected() + 1) {
                floatingTile.getRelic().unHide();
                return;
            }
        }

        for (int iRow = 1; iRow < maze.length; iRow++) { // loop through maze and unhide next relic of the given player

            for (int iCol = 1; iCol < maze[0].length; iCol++) {
                if (maze[iRow][iCol].hasRelic()) {

                    if (maze[iRow][iCol].getRelic().getColor() == Character.toLowerCase(player.getColor()) && maze[iRow][iCol].getRelic().getRelicNum() == player.getNumRelicCollected() + 1) {
                        maze[iRow][iCol].getRelic().unHide();

                    }


                }


            }

        }

    }



}
