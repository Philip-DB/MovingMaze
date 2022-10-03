/**
 * A class used by the MovingMaze class to store information specific to each adventurer in the game.
 * The class also provides methods to access the instance variables of the adventurer object, and change some of its values but not its {@code color },
 * {@code startingRow} or {@code startingCol} .
 * @author Philip de Bruyn
 * @see MovingMaze
 */
public class Adventurer {

    private final char color;
    private int currentRow;
    private int currentCol;

    private int numRelicCollected;

    private final int startingRow;
    private final int startingCol;


    /**
     * A constructor method used to create an Adventurer object. It takes in the starting {@code col } , {@code row} and {@code column} .
     * @param col The color of the adventurer as {@code Char} .
     * @param row the starting row of the adventurer as an {@code Integer} .
     * @param column the starting column of the adventurer as an {@code Integer} .
     */
    public Adventurer( char col,int row, int column) {
        color = col; // get the color of the adventurer

        startingRow = row; // get starting row , this is used to identify is a player has won or not
        startingCol = column; // get starting column

        currentRow = row;
        currentCol = column;



        numRelicCollected = 0;





    }

    /**
     * An instance method that returns the current row of the adventurers position on the board
     * @return the current row as an {@code Integer} .
     */
    public int getCurrentRow() {
        return  currentRow;
    }

    /**
     * An instance method that sets the current row of the adventurers position on the board
     * @param row the new row of the adventurers position as an {@code Integer} .
     */
    public void setCurrentRow(int row) {
        currentRow = row;
    }

    /**
     * An instance method that sets the current column of the adventurers position on the board
     * @param col the new column of the adventurers position as an {@code Integer} .
     */
    public void setCurrentCol(int col) {
        currentCol = col;
    }

    /**
     * An instance method that returns the current column of the adventurers position on the board
     * @return the current column of the adventurer as an {@code Integer} .
     */
    public int getCurrentCol() {
        return currentCol;
    }

    /**
     * An instance method that returns the adventurers color
     * @return returns the current adventurers color as a {@code Char} in Uppercase.
     */
    public char getColor() {
        return color;
    }

    /**
     * An instance method that returns the color of the adventurer as a full word
     * @return a {@code String} containing the color of the adventurer as a full word with a Capital letter
     */
    public String getColorString() {
        switch(color) {

            case 'G' : return "Green";
            case 'Y' : return "Yellow";
            case 'R' : return "Red";
            case 'B' : return "Blue";
        }
        return "No color";
    }

    /**
     * An instance method that returns the current number of collected relics by the adventurer.
     * @return an {@code Integer} that represents the amount of relics collected by the adventurer.
     * @see Relic for more information about Relics
     */
    public int getNumRelicCollected() {
        return numRelicCollected;
    }

    /**
     * An instance method that increases the number of relics collected by the adventurer by one.
     */
    public void incNumRelicCollected() {
        numRelicCollected++;
    }

    /**
     * An instance method that returns the starting Row of the adventurer.
     * @return an {@code Integer} that represents the starting row of the adventurers starting position on the board.
     */
    public int getStartingRow() {
        return startingRow;
    }

    /**
     * An instance method that returns the starting column of the adventurer.
     * @return an {@code Integer} that represents the starting Column of the adventurers starting position on the board.
     */
    public int getStartingCol() {
        return startingCol;
    }
}
