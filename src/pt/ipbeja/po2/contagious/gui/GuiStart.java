package pt.ipbeja.po2.contagious.gui;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * Filipe Gonçalves
 * nº 6050
 * Git: https://github.com/PO2-2019-2020/po2-tp2-6050-po2
 */

public class GuiStart extends Application {
public Stage stage;

private static String[] arg;

    @Override
    public void start(Stage primaryStage) throws Exception {

        ContagiousBoard board = new ContagiousBoard(arg);
        Scene scene = new Scene(board);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Contagious World");
        primaryStage.setOnCloseRequest((e) -> {
            System.exit(0);
        });
        stage = primaryStage;
        primaryStage.show();
    }

    public static void main(String[] args) {
        arg = args;
        Application.launch(args);
    }
}
