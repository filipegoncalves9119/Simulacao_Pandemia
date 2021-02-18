package pt.ipbeja.po2.contagious.gui;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import pt.ipbeja.po2.contagious.model.CellPosition;
import pt.ipbeja.po2.contagious.model.View;
import pt.ipbeja.po2.contagious.model.World;

import java.io.File;
import java.util.Arrays;

/**
 * Filipe Gonçalves
 * nº 6050
 * Git: https://github.com/PO2-2019-2020/po2-tp2-6050-po2
 */

public class ContagiousBoard extends VBox implements View {
    public static World world;
    private GuiStart start;
    static WorldBoard pane;
    private Label counterLabel;

    private HBox paneBox;
    private HBox hBox;
    private HBox daysLabal;
    private HBox graphLabel;
    static Button startButton;
    static Button pauseButton;
    private View view = this;
    private HBox chartBox;
    private MenuBar menuBar;
    private Menu menu;
    private MenuItem save;
    private MenuItem load;
    private Label nLinesInput;
    private Label nColsInput;
    private Label sickInput;
    private Label healthyInput;
    private Label immuneInput;
    private Label chanceToSick;
    private Label chanceToBecImmune;
    private Label minTimeToHeal;
    private Label maxTimeToHeal;
    private TextField nLinesInputText;
    private TextField nColsInputText;
    private TextField sickInputText;
    private TextField healthyInputText;
    private TextField immuneInputText;
    private TextField chanceToSickText;
    private TextField chanceToBecImmuneText;
    private TextField minTimeToHealText;
    private TextField maxTimeToHealText;
    private File file;
    private HBox mainHbox;
    private World world2;
    private VBox mainBox;
    private VBox mainBox1;
    private String[] args;

    /**
     * Class Constructor
     */
    public ContagiousBoard(String[] arg) {
        this.args = arg;
        initializeObjects();
        setLabelsForInput();
        setTextAreasForInput();
        pauseHandler pauseHandler = new pauseHandler();
        startHandler startHandler = new startHandler();
        SaveHandler saveHandler = new SaveHandler();
        LoadHandler loadHandler = new LoadHandler();

        this.menu.getItems().addAll(this.save, this.load);
        this.menuBar.getMenus().add(this.menu);
        mainBox.getChildren().addAll(nLinesInput, nColsInput, sickInput, healthyInput, immuneInput, chanceToBecImmune, chanceToSick, minTimeToHeal, maxTimeToHeal);
        mainBox1.getChildren().addAll(nLinesInputText, nColsInputText, sickInputText, healthyInputText,
                immuneInputText, chanceToBecImmuneText,chanceToSickText,minTimeToHealText,maxTimeToHealText, startButton);

        startButton.setOnAction(startHandler);
        pauseButton.setOnAction(pauseHandler);
        mainBox1.setSpacing(10);
        mainBox.setSpacing(18);
        mainHbox.getChildren().addAll(mainBox, mainBox1);
        this.getChildren().addAll(menuBar, mainHbox);

        this.save.setOnAction(saveHandler);
        this.load.setOnAction(loadHandler);

        world2 = new World();
    }

    /**
     * Method to initialize objects
     */
    private void initializeObjects() {
        mainBox = new VBox();
        mainBox1 = new VBox();
        mainHbox = new HBox();
        start = new GuiStart();
        startButton = new Button("Start");
        pauseButton = new Button("Pause");
        paneBox = new HBox();
        hBox = new HBox();
        daysLabal = new HBox();
        graphLabel = new HBox();
        chartBox = new HBox();

        this.menuBar = new MenuBar();
        this.menu = new Menu("File");
        this.save = new MenuItem("Save as");
        this.load = new MenuItem("Open");

    }

    /**
     * Method to initialize the labels
     */
    private void setLabelsForInput() {
        this.nLinesInput = new Label("Number of lines ");
        this.nColsInput = new Label("Number of collumns");
        this.sickInput = new Label("Number of sick people");
        this.healthyInput = new Label("Number of healthy people");
        this.immuneInput = new Label("Number of immune people");
        this.chanceToSick = new Label("Chance to spread disease");
        this.chanceToBecImmune = new Label("Chance to become immune");
        this.minTimeToHeal = new Label("Min of days to heal");
        this.maxTimeToHeal = new Label("Max of days to heal");
    }

    /**
     * Method to initialize the text areas
     */
    private void setTextAreasForInput() {
        this.nLinesInputText = new TextField();
        this.nColsInputText = new TextField();
        this.sickInputText = new TextField();
        this.healthyInputText = new TextField();
        this.immuneInputText = new TextField();
        this.chanceToSickText = new TextField();
        this.chanceToBecImmuneText = new TextField();
        this.minTimeToHealText = new TextField();
        this.maxTimeToHealText = new TextField();
    }

    /**
     * Handler for start Button
     * Which will make all the gui begin
     *
     * Set all the layout
     */
    class startHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {


            if(args.length >1 ){
                world = new World(view, Integer.parseInt(args[0]), Integer.parseInt(args[1]),
                        Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]),
                        Integer.parseInt(args[5]), Integer.parseInt(args[6]), Integer.parseInt(args[7]), Integer.parseInt(args[8]), file);

            }
            else {
                setIfEmpty();

                world = new World(view, Integer.parseInt(nLinesInputText.getText()), Integer.parseInt(nColsInputText.getText()),
                        Integer.parseInt(sickInputText.getText()), Integer.parseInt(immuneInputText.getText()), Integer.parseInt(healthyInputText.getText()),
                        Integer.parseInt(chanceToSickText.getText()), Integer.parseInt(chanceToBecImmuneText.getText()), Integer.parseInt(minTimeToHealText.getText()), Integer.parseInt(maxTimeToHealText.getText()), file);

            }
            pane = new WorldBoard(world, 20);
            setUpStartItems();
        }
    }

    /**
     * Method to prevent null values from input information
     * check if every field is empty and if so gives a default value
     */
    private void setIfEmpty() {
        String a = nLinesInputText.getText();
        String b = nColsInputText.getText();
        String c = sickInputText.getText();
        String d = immuneInputText.getText();
        String e = healthyInputText.getText();
        String f = chanceToSickText.getText();
        String g = chanceToBecImmuneText.getText();
        String h = minTimeToHealText.getText();
        String i = maxTimeToHealText.getText();

        if (a.equals("")) {
            nLinesInputText.setText("20");
        }
        if(b.equals("")){
            nColsInputText.setText("20");
        }
        if(c.equals("")){
            sickInputText.setText("5");
        }
        if(d.equals("")){
            immuneInputText.setText("0");
        }
        if(e.equals("")){
            healthyInputText.setText("15");
        }
        if(f.equals("")){
            chanceToSickText.setText("90");
        }
        if(g.equals("")){
            chanceToBecImmuneText.setText("70");
        }
        if(h.equals("")){
            minTimeToHealText.setText("10");
        }
        if(i.equals("")){
            maxTimeToHealText.setText("15");
        }
    }

    /**
     * Handler for the Load menu items
     */
    class LoadHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {


            int[] a = world2.boardSize();
            file = world2.getFile();

            world = new World(view, a[0],a[1], 0, 0, 0,0,0,0,0,file);
            pane = new WorldBoard(world, 20);
            loadBoard();

            setUpStartItems();


        }
    }


    /**
     * Method to build string used to show the number of each kind of people there are in the world
     *
     * @return built string
     */
    private String getNumbersInfected() {
        StringBuilder s = new StringBuilder();
        int[] i = world.people();

       // s.append("Total Sick people: ").append(i[5]).append("\n");
        s.append("Sick w/ sym: ").append(i[0]).append("\n");
        s.append("Sick no sym: ").append(i[1]).append("\n");
        s.append("Healhy people: ").append(i[2]).append("\n");
        s.append("Immune people: ").append(i[3]).append("\n");
        s.append("Dead people: ").append(i[4]).append("\n");


        return s.toString();

    }

    /**
     * Handler for pause button
     */
    static class pauseHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            if (!world.isPause()) {
                world.setPause(true);
                pauseButton.setText("Resume");
            } else {
                world.setPause(false);
                pauseButton.setText("Pause");
                world.resume();
            }
        }
    }


    /**
     * Handler for the save menu items
     */
    class SaveHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            world.getSetUpFile();
            world.writer();
        }
    }


    /**
     * Method to call the new loaded world
     */
    private void loadBoard() {
        world.fillFileBoard();
    }

    @Override
    public void populateWorld(CellPosition position) {
        pane.populateWorld(position);
    }


    /**
     * Method to make bar chart of people
     *
     * @return the bar chart
     */
    private BarChart graph() {

        int[] a = world.people();
        CategoryAxis xAxis = new CategoryAxis();

        xAxis.setCategories(FXCollections.<String>observableArrayList(Arrays.asList("Sick w/ sym","Sick no sym", "Healhy", "Immune", "Dead")));

        xAxis.setLabel("Type of people");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Number of people");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);

        XYChart.Series<String, Number> sickWS = new XYChart.Series<>();
        sickWS.setName("Sick w/ sym");
        sickWS.getData().add(new XYChart.Data<>("Sick w/ sym", a[0]));


        XYChart.Series<String, Number> sickNoS = new XYChart.Series<>();
        sickNoS.setName("Sick no sym");
        sickNoS.getData().add(new XYChart.Data<>("Sick no sym", a[1]));


        XYChart.Series<String, Number> healhy = new XYChart.Series<>();
        healhy.setName("Healhy");
        healhy.getData().add(new XYChart.Data<>("Healhy", a[2]));


        XYChart.Series<String, Number> dead = new XYChart.Series<>();
        dead.setName("Dead");
        dead.getData().add(new XYChart.Data<>("Dead", a[4]));


        XYChart.Series<String, Number> immune = new XYChart.Series<>();
        immune.setName("Immune");
        immune.getData().add(new XYChart.Data<>("Immune", a[3]));



        barChart.getData().addAll(sickWS, sickNoS, healhy, immune, dead);
        barChart.setBarGap(-40);

        return barChart;
    }

    /**
     * Method to set up some objects used for the start and load options
     */
    private void setUpStartItems() {

        counterLabel = new Label(("0"));
        counterLabel.setPrefHeight(100);
        counterLabel.setPrefWidth(pane.getPrefWidth());
        startButton.setPrefWidth(pane.getPrefWidth());
        getChildren().remove(startButton);
        //pane.setMaxSize(650, 650);
        chartBox.getChildren().add(graph());
        paneBox.getChildren().add(pane);
        daysLabal.getChildren().add(counterLabel);
        graphLabel.getChildren().addAll(paneBox, chartBox);
        hBox.getChildren().addAll(pauseButton);

        getChildren().addAll(hBox, daysLabal, graphLabel);
        getScene().getWindow().sizeToScene();

        world.start();
        mainBox.getChildren().removeAll(nLinesInput, nColsInput, sickInput, healthyInput, immuneInput,chanceToSick,chanceToBecImmune,minTimeToHeal,maxTimeToHeal);
        mainBox1.getChildren().removeAll(nLinesInputText, nColsInputText, sickInputText, healthyInputText, immuneInputText, startButton, chanceToSickText, chanceToBecImmuneText, minTimeToHealText, maxTimeToHealText);
    }

    /**
     * Method to call update position which is used ot update gui interface
     *
     * @param actualL
     * @param actualC
     * @param dx
     * @param dy
     * @param i
     */
    @Override
    public void updatePosition(int actualL, int actualC, int dx, int dy, int i) {

        Platform.runLater(() -> {

            pane.updatePosition(actualL, actualC, dx, dy);
            // this.graph.setText(makeGraph());
            chartBox.getChildren().set(0, graph());

            this.counterLabel.setText("Days: " + i + "\n" + getNumbersInfected());
        });
    }

}