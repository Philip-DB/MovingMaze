/* Stellenbosch University CS144 Project 2022
 * MovingMaze.java
 * Name and surname: Philip de Bruyn
 * Student number: 25968548
 */


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Main class of the moving maze game.
 *
 * @author Philip de Bruyn
 */
public class MovingMaze {

    // ==========================================================
    // Constants
    // ==========================================================

    // Move these where you'll use them, or refer to them with e.g. MovingMaze.PATH_NESW

    // ═ ║ ╔ ╗ ╚ ╝ ╠ ╣ ╦ ╩ ╬
    // ─ │ ┌ ┐ └ ┘ ├ ┤ ┬ ┴ ┼

    public static final String PATH_EW = "═";
    public static final String PATH_NS = "║";
    public static final String PATH_ES = "╔";
    public static final String PATH_SW = "╗";
    public static final String PATH_NE = "╚";
    public static final String PATH_NW = "╝";
    public static final String PATH_NES = "╠";
    public static final String PATH_NSW = "╣";
    public static final String PATH_ESW = "╦";
    public static final String PATH_NEW = "╩";
    public static final String PATH_NESW = "╬";

    public static final String BORDER_HORI = "─";
    public static final String BORDER_VERT = "│";
    public static final String BORDER_TOPLEFT = "┌";
    public static final String BORDER_TOPRIGHT = "┐";
    public static final String BORDER_BOTTOMLEFT = "└";
    public static final String BORDER_BOTTOMRIGHT = "┘";
    public static final String BORDER_LEFTEDGE = "├";
    public static final String BORDER_RIGHTEDGE = "┤";
    public static final String BORDER_TOPEDGE = "┬";
    public static final String BORDER_BOTTOMEDGE = "┴";
    public static final String BORDER_MIDDLE = "┼";


    // ==========================================================
    // Main function
    // ==========================================================

    /**
     * The main method of MovingMaze.java. This method runs the MovingMaze appliction and takes 2 arguements. The name of the game board text file
     * and the visual mode, either 'text' or 'gui'.
     *
     * @param args 0: the game board text file name. 1: the visual mode, either 'text' or 'gui'.
     */
    public static void main(String[] args) {
        String fileName = args[0];  // args[0] will contain the filename of the game board file to be loaded.
        String visualMode = args[1]; // args[1] will contain either "text" or "gui".
        int boardWidth; // stores the board width from the game board file
        int boardHeight;// stores the board height from the game board file
        int K; // the number of relics each adventurer needs to collect
        String[] encodedStrings;
        String encodedFloatingTile;


        try { // create all the needed objects

            File file = new File(fileName); // create a file object for use by the scanner
            Scanner scanner = new Scanner(file); // create a scanner and read in my gameboard file
            boardWidth = scanner.nextInt(); // get board width
            boardHeight = scanner.nextInt();
            encodedStrings = new String[boardHeight * boardWidth]; // stores all encoded strings for the tile maze, and does not include the floating tile
            K = scanner.nextInt();
            encodedFloatingTile = scanner.next();
            int iCount = 0;
            while (scanner.hasNext()) {

                encodedStrings[iCount] = scanner.next();
                iCount++;

            }

            TileMaze myMaze = new TileMaze(boardWidth, boardHeight, encodedStrings); // create TileMaze object
            Adventurer playerGreen = new Adventurer('G', 1, 1); // create adventurers and initial locations
            Adventurer playerYellow = new Adventurer('Y', 1, boardWidth);
            Adventurer playerRed = new Adventurer('R', boardHeight, 1);
            Adventurer playerBlue = new Adventurer('B', boardHeight, boardWidth);

            myMaze.createAdventurer(playerGreen);
            myMaze.createAdventurer(playerYellow);
            myMaze.createAdventurer(playerRed);
            myMaze.createAdventurer(playerBlue);


            Tile floatingTile = new Tile(encodedFloatingTile);


            GameState gameState = new GameState(playerGreen, playerYellow, playerRed, playerBlue, K); // create a new gameState object
            Scanner inputReader = new Scanner(System.in); // create a scanner to read input


            // start of text mode
            if (visualMode.equals("text")) {
                StdOut.println("--------------------------------------------------");
                StdOut.println("Moving Maze");
                StdOut.println("Relic goal: " + K);
                StdOut.println("--------------------------------------------------");
                while (gameState.gameIsRunning) {
                    myMaze.drawTextMaze();
                    floatingTile.drawTile();
                    while (true) {
                        StdOut.println("[" + gameState.getCurrentTurn().getColorString() + "]" + " Rotate and slide the floating tile:");
                        StdOut.print(">");
                        String input = inputReader.next();


                        if (input.length() == 2) {
                            if (isValidSlidingString(input)) {
                                if (isValidSlide(input, floatingTile)) {
                                    StdOut.println(" Inserting at " + input + ".");
                                    floatingTile = myMaze.slideIntoMaze(input, floatingTile);
                                    gameState.wrapAroundRelicCollection(myMaze.getMaze(), myMaze, floatingTile);
                                    myMaze.drawTextMaze();
                                    floatingTile.drawTile();
                                    break;
                                }
                            } else {
                                StdOut.println("Invalid input.");
                            }

                        }

                        if (input.equals("quit")) {
                            StdOut.println(" Game has been quit.");
                            gameState.endGame();

                        }

                        if (input.length() > 2) {
                            StdOut.println("Invalid input.");
                        }
                        if(input.length() == 1) {
                            switch (input) {
                                case "r": {
                                    floatingTile.setDirections(floatingTile.rotateTileClockwise());
                                    StdOut.println(" Rotating right.");
                                    myMaze.drawTextMaze();
                                    floatingTile.drawTile();
                                    break;
                                }

                                case "l": {
                                    floatingTile.setDirections(floatingTile.rotateTileCounterClockwise());
                                    StdOut.println(" Rotating left.");
                                    myMaze.drawTextMaze();
                                    floatingTile.drawTile();
                                    break;
                                }

                                default:
                                    StdOut.println("Invalid input.");

                            }
                        }


                    }
                    // player has now rotated and slid the tile in, now we check if any relic collections must be triggered


                    while (true) { // moving adventurer

                        StdOut.println("[" + gameState.getCurrentTurn().getColorString() + "]" + " Move your adventurer:");
                        StdOut.print(">");
                        String input = inputReader.next();



                        if (input.length() == 1) {
                            if(isValidMoveInput(input)) {

                                if (gameState.isValidPlayerMove(input.charAt(0), myMaze.getMaze())) {
                                    gameState.movePlayer(input.charAt(0), myMaze.getMaze());
                                    if (gameState.movementRelicCollection(myMaze.getMaze(), myMaze, floatingTile)) {
                                        break;
                                    }
                                    if (gameState.ifWon()) { // if the player moved and is in a winning position, we end game
                                        StdOut.println(gameState.getCurrentTurn().getColorString() + " has won.");
                                        gameState.endGame();
                                    }

                                }
                            } else {
                                StdOut.println("Invalid input.");
                            }
                        }

                        if (input.length() == 3) {
                            if(isValidTeleportMove(input)) {
                                char[] pathFindingCords = input.toCharArray();
                                int row = Integer.parseInt(String.valueOf(pathFindingCords[2]));
                                int col = Integer.parseInt(String.valueOf(pathFindingCords[0]));
                                if (gameState.isValidPath(col, row, myMaze.getMaze())) { // we can teleport
                                    gameState.teleportPlayer(col, row, myMaze.getMaze());
                                    StdOut.println(" Moving to " + col + "," + row + ".");

                                    if (gameState.movementRelicCollection(myMaze.getMaze(), myMaze, floatingTile)) {
                                        break;
                                    }
                                    if (gameState.ifWon()) { // if the player moved and is in a winning position, we end game
                                        StdOut.println(gameState.getCurrentTurn().getColorString() + " has won.");
                                        gameState.endGame();
                                    }

                                } else {
                                    StdOut.println(" Cannot move to " + col + "," + row + ": no path.");
                                }
                            } else {
                                StdOut.println("Invalid input.");
                            }
                        }

                        if (input.equals("quit")) {
                            StdOut.println(" Game has been quit.");
                            gameState.endGame();

                        }

                        if (input.equals("done")) {
                            StdOut.print(" ");
                            gameState.nextTurn();
                            break;
                        }

                        if (input.length() != 3 && input.length() != 1) {
                            StdOut.println("Invalid input.");
                        }

                    }


                }

            } // end of text mode

            if (visualMode.equals("gui")) { // run game in gui mode

                GUI UI = new GUI(gameState, myMaze.getMaze(), floatingTile);
                UI.drawMaze();

                while (gameState.gameIsRunning) { // GUI game loop begins

                    while (true) { // loop for sliding and rotating

                        UI.drawMaze();
                        if (StdDraw.isKeyPressed(39)) { // if right arrow is pressed, rotate floating tile clockwise

                            floatingTile.setDirections(floatingTile.rotateTileClockwise());
                            UI.drawMaze();
                            StdOut.println("Pressed right");
                            Thread.sleep(200);

                        }

                        if (StdDraw.isKeyPressed(37)) { // if left arrow is pressed, rotate floating tile anti-clockwise

                            floatingTile.setDirections(floatingTile.rotateTileCounterClockwise());
                            UI.drawMaze();
                            StdOut.println("Pressed left");
                            Thread.sleep(200);

                        }

                        if (StdDraw.isMousePressed()) {

                            double xMouse = StdDraw.mouseX(); // get co-ordinates of the mouse
                            double yMouse = StdDraw.mouseY();


                            if (UI.wasSlidingPointPressed(xMouse, yMouse)) {

                                if (isValidSlide(UI.getPoint(xMouse, yMouse).getSlidingDirection(), floatingTile)) {

                                    floatingTile = myMaze.slideIntoMaze(UI.getPoint(xMouse, yMouse).getSlidingDirection(), floatingTile);
                                    gameState.wrapAroundRelicCollection(myMaze.getMaze(), myMaze, floatingTile);
                                    UI.setFloatingTile(floatingTile);
                                    UI.drawMaze();
                                    gameState.startMoving();
                                    break;
                                }

                            }
                            Thread.sleep(200);

                        }

                        if (StdDraw.isKeyPressed(81)) { // w is pressed, so check if we can move north

                            System.exit(0);

                        }


                    }

                    while (true) { // moving phase
                        UI.drawMaze();

                        // pathfinder

                        UI.drawPathfinderSquare(StdDraw.mouseX(), StdDraw.mouseY());
                        if (StdDraw.isMousePressed()) {

                            double xMouse = StdDraw.mouseX(); // get co-ordinates of the mouse
                            double yMouse = StdDraw.mouseY();

                            if (UI.canDraw(xMouse, yMouse)) {
                                if (UI.canMove(xMouse, yMouse)) {

                                    gameState.teleportPlayer(UI.getPosition(xMouse, yMouse).getCol(), UI.getPosition(xMouse, yMouse).getRow(), myMaze.getMaze());
                                    if (gameState.movementRelicCollection(myMaze.getMaze(), myMaze, floatingTile)) {
                                        UI.drawMaze();
                                        gameState.stopMoving();
                                        break;
                                    }

                                    if (gameState.ifWon()) { // if the player moved and is in a winning position, we end game
                                        UI.drawEndScreen();
                                    }
                                }
                            }
                            UI.drawMaze();
                            Thread.sleep(200);

                        }


                        // end of pathfinder
                        if (StdDraw.isKeyPressed(87)) { // w is pressed, so check if we can move north

                            if (gameState.isValidPlayerMove('n', myMaze.getMaze())) {

                                gameState.movePlayerNoText('n', myMaze.getMaze());

                                if (gameState.movementRelicCollection(myMaze.getMaze(), myMaze, floatingTile)) {
                                    UI.drawMaze();
                                    gameState.stopMoving();
                                    break;
                                }

                                if (gameState.ifWon()) { // if the player moved and is in a winning position, we end game
                                    UI.drawEndScreen();
                                }

                            }


                            UI.drawMaze();

                            Thread.sleep(200);

                        }

                        if (StdDraw.isKeyPressed(83)) { // w is pressed, so check if we can move north

                            if (gameState.isValidPlayerMove('s', myMaze.getMaze())) {

                                gameState.movePlayerNoText('s', myMaze.getMaze());

                                if (gameState.movementRelicCollection(myMaze.getMaze(), myMaze, floatingTile)) {
                                    UI.drawMaze();
                                    gameState.stopMoving();
                                    break;
                                }

                                if (gameState.ifWon()) { // if the player moved and is in a winning position, we end game
                                    UI.drawEndScreen();
                                }

                            }


                            UI.drawMaze();

                            Thread.sleep(200);

                        }

                        if (StdDraw.isKeyPressed(68)) { // w is pressed, so check if we can move north

                            if (gameState.isValidPlayerMove('e', myMaze.getMaze())) {

                                gameState.movePlayerNoText('e', myMaze.getMaze());

                                if (gameState.movementRelicCollection(myMaze.getMaze(), myMaze, floatingTile)) {
                                    UI.drawMaze();
                                    gameState.stopMoving();
                                    break;
                                }

                                if (gameState.ifWon()) { // if the player moved and is in a winning position, we end game
                                    UI.drawMaze();
                                }

                            }


                            UI.drawMaze();

                            Thread.sleep(200);


                        }

                        if (StdDraw.isKeyPressed(65)) { // w is pressed, so check if we can move north

                            if (gameState.isValidPlayerMove('w', myMaze.getMaze())) {

                                gameState.movePlayerNoText('w', myMaze.getMaze());

                                if (gameState.movementRelicCollection(myMaze.getMaze(), myMaze, floatingTile)) {
                                    UI.drawMaze();
                                    gameState.stopMoving();
                                    break;
                                }

                                if (gameState.ifWon()) { // if the player moved and is in a winning position, we end game
                                    UI.drawEndScreen();

                                }

                            }


                            UI.drawMaze();

                            Thread.sleep(200);


                        }

                        if (StdDraw.isKeyPressed(10)) { // w is pressed, so check if we can move north

                            gameState.nextTurn();
                            gameState.stopMoving();

                            UI.drawMaze();

                            Thread.sleep(200);
                            break;

                        }

                        if (StdDraw.isKeyPressed(81)) { // w is pressed, so check if we can move north

                            System.exit(0);

                        }


                    }


                }

            }

            if (!visualMode.equals("text") && !visualMode.equals("gui")) {
                StdOut.println("Unknown visual mode.");
                System.exit(0);
            }

        } catch (FileNotFoundException e) {
            StdOut.println("The game board file does not exist.");
            System.exit(0);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * A static function to check whether a valid slide can be performed by sliding the {@code floatingTile} into the position indicated by {@code slidingIndicator}. If it can, return {@code true} and if not, return {@code false}.
     *
     * @param slidingIndicator a {@code String} that indicates the position to slide the floating tile into the maze.
     * @param floatingTile     a {@code String} that is 6 characters long and holds information to create the floatingTile
     * @return {@code true} if the slide can be formed.
     */
    public static boolean isValidSlide(String slidingIndicator, Tile floatingTile) {
        char[] information = slidingIndicator.toCharArray();
        if (Integer.parseInt(String.valueOf(information[1])) % 2 != 0) { // if the number entered is odd, they cannot slide tile in
            StdOut.println(" Cannot slide into odd positions.");
            return false;

        }

        if (floatingTile.getLastExitPoint().equals(slidingIndicator)) {
            StdOut.println(" Cannot slide into last exit point.");
            return false;
        }

        return true;
    }

    /**
     * Used to determine if the sliding input is valid
     * @param input a String of length 2 that should contain the direction and row/column to slide the floating tile into.
     * @return true of the input is valid
     */
    public static boolean isValidSlidingString(String input) {

        if (input == null) {
            return false;
        } else {
            char[] info = input.toCharArray();

            if (info[0] == 'n' || info[0] == 'e' || info[0] == 's' || info[0] == 'w') { // if first part of sliding string is a valid direction continue
                switch (info[1]) {
                    case '0':
                        return true;
                    case '1':
                        return true;
                    case '2':
                        return true;
                    case '3':
                        return true;
                    case '4':
                        return true;
                    case '5':
                        return true;
                    case '6':
                        return true;
                    case '7':
                        return true;
                    case '8':
                        return true;
                    case '9':
                        return true;
                    default:
                        return false;
                }
            }
            return false;

        }

    }

    /**
     * Used to determine if the moving input is valid
     * @param input a String of length 1 that should contain the direction the player wishes to move in.
     * @return true of the input is valid
     */
    public static boolean isValidMoveInput(String input) {
        if(input == null) {
            return false;
        } else {

            switch(input) {
                case "n" : return true;
                case "s" : return true;
                case "e" : return true;
                case "w" : return true;
                default: return false;
            }

        }
    }

    /**
     * Used to determine if the pathfinding input is valid
     * @param input a String of length 3 that should contain the co-ordinates of the point to teleport to seperated by a comma
     * @return true of the input is valid
     */
    public static boolean isValidTeleportMove(String input) {
        if(input == null) {
            return false;

        } else {
            char[] info = input.toCharArray();

            if (info[1] == ',') {

                if (info[0] == '0' || info[0] == '1' || info[0] == '2' || info[0] == '3' || info[0] == '4' || info[0] == '5' || info[0] == '6' || info[0] == '7' || info[0] == '8' || info[0] == '9' ) {
                    // if the first part of string is a digit, check the last one

                    switch (info[2]) {
                        case '0':
                            return true;
                        case '1':
                            return true;
                        case '2':
                            return true;
                        case '3':
                            return true;
                        case '4':
                            return true;
                        case '5':
                            return true;
                        case '6':
                            return true;
                        case '7':
                            return true;
                        case '8':
                            return true;
                        case '9':
                            return true;
                        default:
                            return false;
                    }

                } else {
                    return false;
                }

            } else {
                return false;
            }
        }
    }
    // ==========================================================
    // Test API functions
    // ==========================================================

    // Populate these with high-level code that references your codebase.

    // ----------------------------------------------------------
    // First hand-in

    /**
     * Returns {@code true} if the tile encoded as {@code tileEncoding} is open in the direction indicated with
     * {@code dir} and {@code false} if not. As per the project specification, the {@code tileEncoding} {@code String} will be
     * six characters long. The direction {@code dir} will be one of n, e, s, and w, respectively indicating
     * north, east, south and west.
     *
     * @param tileEncoding a {@code String} that is 6 characters long and holds information to create the tile.
     * @param dir          a {@code Char} that indicates which direction to check if the tile it open to.
     * @return {@code} true of the tile is open to the direction {@code dir}
     */
    public static boolean isTileOpenToSide(String tileEncoding, char dir) {
        Tile myTile = new Tile(tileEncoding);
        return myTile.isOpenToSide(dir);
    }

    /**
     * Take the tile encoded with {@code tileEncoding} and rotate it once clockwise. Return a {@code boolean}
     * array with length 4 – each element in this {@code boolean} array must indicate whether the rotated
     * tile is open to a specific side. The order of sides is north, east, south, west.
     *
     * @param tileEncoding a {@code String} that is 6 characters long and holds information to create the tile
     * @return a {@code boolean }array with the new directions that the {@code Tile} is open to.
     */
    public static boolean[] rotateTileClockwise(String tileEncoding) {
        Tile myTile = new Tile(tileEncoding);
        return myTile.rotateTileClockwise();
    }

    /**
     * Same as rotateTileClockwise, but rotate the tile once counterclockwise instead of once
     * clockwise.
     *
     * @param tileEncoding a {@code String} that is 6 characters long and holds information to create the tile
     * @return a {@code boolean }array with the new directions that the {@code Tile} is open to.
     */
    public static boolean[] rotateTileCounterclockwise(String tileEncoding) {
        Tile myTile = new Tile(tileEncoding);
        return myTile.rotateTileCounterClockwise();
    }

    /**
     * Take the floating tile encoded with {@code floatingTileEncoding} , insert it at the sliding position {@code slidingIndicator}
     * into the maze encoded with the 2D array of strings {@code mazeTileEncodings} and return a boolean array of length 4.
     * Each element in this boolean array must indicate whether the new floating tile (after the slide is performed) is open to a specific
     * side. The order of sides is north, east, south, west.
     *
     * @param mazeTileEncodings    a 2D array of {@code String} that encode each tile's information.
     * @param floatingTileEncoding a {@code String} that encodes the floating tile's information.
     * @param slidingIndicator     a {@code String} that indicates the position to slide the floating tile into the maze.
     * @return a {@code boolean} array holding the new directions that the new floating tile is open to.
     */

    public static boolean[] slideTileIntoMaze1(String[][] mazeTileEncodings, String floatingTileEncoding, String slidingIndicator) {
        Tile floatingTile = new Tile(floatingTileEncoding);
        String[] encodedStrings = new String[mazeTileEncodings.length * mazeTileEncodings[0].length];
        int iCount = 0;

        for (int i = 0; i < mazeTileEncodings.length; i++) {

            for (int k = 0; k < mazeTileEncodings[0].length; k++) {
                encodedStrings[iCount] = mazeTileEncodings[i][k];
                iCount++;
            }

        }

        TileMaze maze = new TileMaze(mazeTileEncodings[0].length, mazeTileEncodings.length, encodedStrings);

        return maze.slideIntoMaze(slidingIndicator, floatingTile).getDirections();
    }

    /**
     * Returns {@code true} if the tile encoded as {@code newTileEncoding} can be stepped to from the tile encoded as {@code curTileEncoding} if {@code newTileEncoding} is in direction {@code dir} from {@code curTileEncoding}.
     * Returns false if not. As per the project specification, the two tile encoding strings will each be six characters long.
     * The direction {@code dir} will be one of n, e, s, and w, respectively indicating north, east, south and west.
     *
     * @param curTileEncoding a {@code String} that encodes the current tile's information.
     * @param newTileEncoding a {@code String} that encodes the destination tile's information.
     * @param dir             a {@code Char} that indicates which direction to check.
     * @return
     */
    public static boolean canMoveInDirection(String curTileEncoding, String newTileEncoding, char dir) {
        Tile currentTile = new Tile(curTileEncoding);
        Tile destinationTile = new Tile(newTileEncoding);
        return GameState.canMoveInDirection(dir, currentTile, destinationTile);
    }

    /**
     * Returns {@code true} if an {@code Adventurer} object starting in the top-left corner can successfully complete
     * the steps in the directions contained in {@code steps}. The {@code char} array {@code steps} is populated from
     * among the options n/e/s/w, which indicate the four compass directions. The maze’s tiles
     * are encoded in the 2D array {@code mazeTileEncodings}. The function must return {@code false} if any
     * step in the path cannot be taken for any reason. No other adventurers are present in the
     * maze.
     *
     * @param mazeTileEncodings a 2D array of {@code String} that encode each tile's information.
     * @param steps             a 2D array of {@code Char} that holds the directions that the adventurer must move in.
     * @return {@code true} if the {@code Adventurer} can move along the path indicated by {@code steps}.
     */
    public static boolean canMoveAlongPath(String[][] mazeTileEncodings, char[] steps) {
        String encodedStrings[] = new String[mazeTileEncodings.length * mazeTileEncodings[0].length];

        int iCount = 0;

        for (int i = 0; i < mazeTileEncodings.length; i++) {

            for (int k = 0; k < mazeTileEncodings[0].length; k++) {
                encodedStrings[iCount] = mazeTileEncodings[i][k];
                iCount++;
            }

        }
        Adventurer playerGreen = new Adventurer('G', 1, 1); // create adventurers and initial locations
        TileMaze maze = new TileMaze(mazeTileEncodings[0].length, mazeTileEncodings.length, encodedStrings);
        maze.createAdventurer(playerGreen);


        GameState gameState = new GameState(playerGreen, null, null, null, 0);


        for (int i = 0; i < steps.length; i++) {

            if (!gameState.isValidPlayerMoveWithoutText(steps[i], maze.getMaze())) {
                return false;
            }
            gameState.movePlayerNoText(steps[i], maze.getMaze());

        }
        return true;
    }

    // ----------------------------------------------------------
    // Second hand-in

    /**
     * Returns true if the tile encoded with {@code tileEncoding} has a {@code relic} on it, and returns {@code false}
     * if not.
     *
     * @param tileEncoding a {@code String} that encodes the tile's information.
     * @return {@code true} if the tile has a relic.
     */
    public static boolean tileHasRelic(String tileEncoding) {
        Tile myTile = new Tile(tileEncoding);
        return myTile.hasRelic();
    }

    /**
     * Take the floating tile encoded with {@code floatingTileEncoding}, insert it at the sliding position {@code slidingIndicator} into the maze encoded with the 2D array of strings {@code mazeTileEncodings}, and return a Java character.
     * This character represents the relic on the new floating tile — if it has a relic (regardless of its collection order number), return its lowercase
     * initial; if it has no relic, return ‘x’.
     *
     * @param mazeTileEncodings    a 2D array of {@code String} that encode each tile's information.
     * @param floatingTileEncoding a {@code String} that encodes the floating tile's information.
     * @param slidingIndicator     a {@code String} that indicates the position to slide the floating tile into the maze.
     * @return a Java character representing the color of the relic or 'x' if no relic is present.
     */
    public static char slideTileIntoMaze2(String[][] mazeTileEncodings, String floatingTileEncoding, String slidingIndicator) {
        Tile floatingTile = new Tile(floatingTileEncoding);
        String[] encodedStrings = new String[mazeTileEncodings.length * mazeTileEncodings[0].length];
        int iCount = 0;

        for (int i = 0; i < mazeTileEncodings.length; i++) {

            for (int k = 0; k < mazeTileEncodings[0].length; k++) {
                encodedStrings[iCount] = mazeTileEncodings[i][k];
                iCount++;
            }

        }

        TileMaze maze = new TileMaze(mazeTileEncodings[0].length, mazeTileEncodings.length, encodedStrings);
        floatingTile = maze.slideIntoMaze(slidingIndicator, floatingTile);

        char val = ' ';
        if (floatingTile.hasRelic()) {
            val = floatingTile.getRelic().getColor();
        } else {
            val = 'x';
        }


        return val;
    }

}
