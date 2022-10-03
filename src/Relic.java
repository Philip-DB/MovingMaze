/**
 * The Relic class handles all relic related functions and states for a relic on a specific tile.
 * @author Philip  de Bruyn
 */

public class Relic {

    private final int relicNum; // Order of the Relic
    private final char color; // Color of the Relic
    private boolean hidden; // Whether of not to hide the relic ( this is not final as it can become unhidden )

    /**
     * A constructor method that creates a relic object
     * @param num an {@code Integer} that represents the order of collection of the relic
     * @param col an {@code Char} that represents the color of the relic
     */
    public Relic(int num , char col) {
        relicNum = num;
        color = col;

        if( relicNum > 1) {
            hidden= true;
        } else {
            hidden = false;
        }

    }

    /**
     * An instance method that un-hides the relic object.
     */
    public void unHide( ) {

        hidden = false;
    }

    /**
     * An instance method that hides the current relic object.
     */
    public void Hide( ) {

        hidden = true;
    }

    /**
     * An instance method that returns if the relic is hidden or not.
     * @return {@code true} if the relic is currently hidden
     */
    public boolean isHidden() {
        return hidden;
    }

    /**
     * An instance method that returns the color of the relic
     * @return A {@code Char} that represents the relics color
     */
    public char getColor() {
        return color;
    }

    /**
     * An instance method that represents the position of the relic in collection order.
     * @return An {@code Integer} that represents the relics collection order.
     */
    public int getRelicNum() {
        return relicNum;
    }


}
