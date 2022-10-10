import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;



/**
 * Class that handles the creation and updating of the GUI using the {@code Java Swing} library.
 *
 * @author Philip de Bruyn
 */
public class GUI  {

    private Tile[][] maze;
    private GameState gameState;

    public GUI(GameState gameState, Tile[][] maze) {
    this.maze = maze;
    this.gameState = gameState;
    }


    public void drawMaze() { // draws current maze
        StdDraw.setCanvasSize(900,900);

        StdDraw.enableDoubleBuffering();
        StdDraw.setPenColor(StdDraw.BLACK);

        StdDraw.filledRectangle(1920/2,1080/2 + 100,300,300); // this will be the area that
    }

    public static void main(String[] args) {

    }






}
