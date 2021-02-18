package pt.ipbeja.po2.contagious.gui;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import pt.ipbeja.po2.contagious.model.CellPosition;
import pt.ipbeja.po2.contagious.model.World;



/**
 * Filipe Gonçalves
 * nº 6050
 * Git: https://github.com/PO2-2019-2020/po2-tp2-6050-po2
 */

public class WorldBoard extends Pane {

    private final int CELL_SIZE;
    private final int nLinesPane;
    private final int nColsPane;
    private World world;

    private Rectangle[][] board;


    public WorldBoard(World world, int size) {
        this.world = world;
        this.CELL_SIZE = size;
        this.nLinesPane = world.nLines() * CELL_SIZE;
        this.nColsPane = world.nCols() * CELL_SIZE;
        this.setPrefSize(this.nLinesPane, this.nColsPane);
        this.board = new Rectangle[this.nLinesPane][nColsPane];

    }

    /**
     * Method to fill the gui board with rectangles(People)
     * Reads the model Board and fills gui accordingly
     * @param position
     */
    private void fillRectangle(CellPosition position){

        int i = position.getLine();
        int j = position.getCol();
        Rectangle r;

        switch (world.getBoard()[i][j].cellType()) {
            case "ImmunePerson":
                r = new Rectangle(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                r.setFill(Color.LIGHTSKYBLUE);
                board[i][j] = r;
                break;
            case "DeadPerson":
                r = new Rectangle(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                r.setFill(Color.DARKBLUE);
                board[i][j] = r;
                break;
            case "HealhyPerson":
                r = new Rectangle(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                r.setFill(Color.GREEN);
                board[i][j] = r;
                break;
            case "EmptyCell":
                r = new Rectangle(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                r.setFill(Color.TRANSPARENT);
                board[i][j] = r;
                break;
            case "SickWS":
                r = new Rectangle(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                r.setFill(Color.ORANGERED);
                board[i][j] = r;
                break;
            case "SickNoS":
                r = new Rectangle(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                r.setFill(Color.ORANGE);
                board[i][j] = r;
                break;
        }

        Platform.runLater( () -> {
            this.getChildren().add(board[i][j]);
        });
    }

    /**
     * Method to populate the world
     * @param position
     */
    public void populateWorld(CellPosition position) {
        this.fillRectangle(position);
    }


    /**
     * Method to update and move the gui positions
     * It receives coordinates and move accordingly.
     * @param line actual line
     * @param col   actual col
     * @param x coordiante to be added
     * @param y coordiante to be added
     */
    public void updatePosition(int line, int col, int x, int y) {


            Rectangle r = board[line][col];
            board[line][col] =  board[x+line][y+col];
            board[x+line][y+col] = r;

        switch (world.getBoard()[x+line][y+col].cellType()) {
            case "ImmunePerson":
                board[x+line][y+col].setFill(Color.LIGHTSKYBLUE);
                break;
            case "HealhyPerson":
                board[x+line][y+col].setFill(Color.GREEN);
                break;
            case "EmptyCell":
                board[x+line][y+col].setFill(Color.TRANSPARENT);
                break;
            case "DeadPerson":
                board[x+line][y+col].setFill(Color.DARKBLUE);
                break;
            case "SickWS":
                board[x+line][y+col].setFill(Color.ORANGERED);
                break;
            case "SickNoS":
                board[x+line][y+col].setFill(Color.ORANGE);
                break;
        }

           TranslateTransition tt =
                    new TranslateTransition(Duration.millis(200), this.board[line+x][col+y]);
            tt.setByX(y * CELL_SIZE);
            tt.setByY(x * CELL_SIZE);
            tt.play();


    }

}
