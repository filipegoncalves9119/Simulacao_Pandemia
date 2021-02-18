package pt.ipbeja.po2.contagious.model;


import javafx.stage.FileChooser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;


/**
 * Filipe Gonçalves
 * nº 6050
 * Git: https://github.com/PO2-2019-2020/po2-tp2-6050-po2
 */


public class World {
    public static final Random rand = new Random();

    private View view;
    private boolean pause = false;
    private int nLines;
    private int nCols;
    private Cell[][] board;
    public List<CellPosition> positions;
    private Cell actualCell;
    private Cell nextCell;
    private int count = -1;
    private int nextCount = 0;
    private List<String> all = new ArrayList<>();
    private List<String> sickNoSList = new ArrayList<>();
    private List<String> sickWSList = new ArrayList<>();
    private List<String> deadList = new ArrayList<>();
    private List<String> healhyList = new ArrayList<>();
    private List<String> immuneList = new ArrayList<>();
    private int chanceToHaveSym;

    private File fileRead;
    private int sickInputText;
    private int healthyInputText;
    private int immuneInputText;
    private int chanceToSickText;
    private int chanceToBecImmuneText;
    private int minTimeToHealText;
    private int maxTimeToHealText;



    public World(){

    }


    /**
     * Class constructor
     * @param view
     * @param nLines line size
     * @param nCols cols size
     * @param sick number of sick people given by view
     * @param immune number of immune people given by view
     * @param healthy number of healthy people given by view
     * @param chanceToSick number of change to spread sickness to people given by view
     * @param chanceToImmune number chance for person become immune given by view
     * @param minTimeHeal number of min days to heal given by view
     * @param maxTimeHeal number of max days to heal given by view
     */
    public World(View view, int nLines, int nCols, int sick, int immune, int healthy, int chanceToSick, int chanceToImmune, int minTimeHeal, int maxTimeHeal, File file) {

        this.board = new Cell[nLines][nCols];
        this.sickInputText = sick;
        this.healthyInputText = healthy;
        this.immuneInputText = immune;
        this.chanceToSickText = chanceToSick;
        this.chanceToBecImmuneText = chanceToImmune;
        this.minTimeToHealText = minTimeHeal;
        this.maxTimeToHealText = maxTimeHeal;
        this.view = view;
        this.nLines = nLines;
        this.nCols = nCols;
        this.positions = new ArrayList<>();
        this.chanceToHaveSym = 20;
        this.fillBoardEmpty();
        this.fillBoardPeople();
        this.fileRead = file;

    }


    /**
     * method to clear the board, set all empty cells
     */
    public void fillBoardEmpty() {
        for (int i = 0; i < nLines; i++) {
            for (int j = 0; j < nCols; j++) {
                this.board[i][j] = new EmptyCell(new CellPosition(i, j));
            }
        }
    }

    /**
     * method to fill the board randomly with people
     */
    private void fillBoardPeople() {

        for (int i = 0; i < sickInputText; i++) {

            int r = rand.nextInt(nLines);
            int c = rand.nextInt(nCols);
            Cell cell = makeNewSick(r, c);
            cell.setDays(generateDays());
            this.board[r][c] = cell;
        }

        for (int i = 0; i < immuneInputText; i++) {
            int r = rand.nextInt(nLines);
            int c = rand.nextInt(nCols);
            this.board[r][c] = new ImmunePerson(new CellPosition(r, c));

        }

        for (int i = 0; i < healthyInputText; i++) {
            int r = rand.nextInt(nLines);
            int c = rand.nextInt(nCols);
            this.board[r][c] = new HealhyPerson(new CellPosition(r, c));

        }

    }

    /**
     * Method to start the moves
     */
    public void start() {
        new Thread(() -> {
            this.populate();
            this.simulate(200);
        }).start();

    }

    /**
     * Method to resume the thread
     */
    public void resume() {
        new Thread(() -> {
            this.simulate(200);
        }).start();

    }


    /**
     * Method to populate the board with people
     */
    private void populate() {
        for (int i = 0; i < nLines; i++) {
            for (int j = 0; j < nCols; j++) {
                this.view.populateWorld(board[i][j].cellPosition());

            }
        }
    }


    /**
     * Method to make the simulation
     * it beging simulating the movements across the board
     * loop the entire board and move each person
     * Also stops the thread if wanted
     *
     * @param nIter iterations number
     */
    private void simulate(int nIter) {

        for (int i = count + 1; i < nIter; i++) {
            try {
                if (pause) {
                    Thread.currentThread().stop();
                } else {
                    Thread.sleep(1000);
                    count = i;
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (int j = 0; j < nLines; j++) {
                for (int k = 0; k < nCols; k++) {
                    if (isPerson(j, k)) {
                        int[] s = makeRandom();

                        if (!board[j][k].cellType().equals("DeadPerson")) {
                            moveToEmpty(j, k, s[0], s[1], count + 1);
                        }
                    }
                }
            }
            this.positions.clear();
            //  System.out.println(printBoard());
        }
    }


    /**
     * method to move the people
     * checks if moves are valid and move people to different cells
     * controls if a certain cell has already moved this round
     * Call for heal method
     *
     * @param actualL actual line
     * @param actualC actual col
     * @param line    line to be added
     * @param col     col to be added
     * @param round   actual round
     */
    public void moveToEmpty(int actualL, int actualC, int line, int col, int round) {
        verifySick();
        if (isPerson(actualL, actualC)) {
            if (isInside(line + actualL, col + actualC)) {
                if (isEmpty(actualL + line, actualC + col)) {
                    this.actualCell = this.board[actualL][actualC];
                    if (!this.positions.contains(actualCell.cellPosition())) {
                        if (isSick(actualL, actualC)) {
                            willDie(actualL, actualC);
                        }
                        this.board[actualL][actualC] = new EmptyCell(new CellPosition(actualL, actualC));
                        movePersonType(actualL + line, actualC + col);
                        makeSymAppear(actualL + line, actualC + col);
                        healOverTime(actualL + line, actualC + col);
                        this.view.updatePosition(actualL, actualC, line, col, round);
                        this.positions.add(actualCell.cellPosition());

                    }
                }
            }
        }
    }




    /**
     * Method to determinate if the person will die
     * Generates e random number and if the number is higher than the one of the object that is renerated when object is created, the person dies
     *
     * @param line
     * @param col
     */
    private void willDie(int line, int col) {

        int a = generateProb();

        //System.out.println("chance: " + a + "chance de morte da cell: " + actualCell.getChanceOfDeath() + "Dias vividos: " + actualCell.getDaysLived());
        if (isSick(actualCell.cellPosition().getLine(), actualCell.cellPosition().getCol()) && actualCell.getDaysLived() > 5 && a > actualCell.getChanceOfDeath()) {

            this.actualCell = new DeadPerson(new CellPosition(line, col));
            this.board[line][col] = actualCell;

        }
    }


    /**
     * Method to move the persons
     * Set new positions for the cells
     * Decrement days left to heal
     *
     * @param line
     * @param col
     */
    private void movePersonType(int line, int col) {
        this.actualCell.setCellPosition(new CellPosition(line, col));
        this.actualCell.setDays(actualCell.getDays() - 1);
        this.actualCell.setDaysLived(actualCell.getDaysLived() + 1);
        if (this.actualCell.cellType().equals("SickNoS")) {
            this.actualCell.setDaysToFeelSym(actualCell.getDaysToFeelSym() - 1);
        }
        this.board[line][col] = this.actualCell;

    }

    /**
     * Method to make people feel symthoms
     * Has a chance if the count of a given days reachs 0 to change to symthom person
     * @param line
     * @param col
     */
    private void makeSymAppear(int line, int col) {
        int chance = generateProb();
        Cell cell;
        int days = this.actualCell.getDaysToFeelSym();

        if (chance > this.chanceToHaveSym) {
            if (days == 0) {
                cell = new SickWithSymptom(new CellPosition(line, col));
                cell.setChanceOfDeath(actualCell.getChanceOfDeath());
                cell.setDays(actualCell.getDays());
                this.board[line][col] = cell;
            }

        }
    }

    /**
     * method to verify if there are healhy people near sick people
     * if so makes heahly people sick instead
     */
    public void verifySick() {
        for (int i = 0; i < nLines; i++) {
            for (int j = 0; j < nCols; j++) {
                if (isInside(i + 1, j) && isInside(i - 1, j) && isInside(i, j + 1) && isInside(i, j - 1)) {
                    if (this.board[i][j].cellType().equals("HealhyPerson")) {
                        if (isSick(i + 1, j) || isSick(i, j + 1) || isSick(i - 1, j) || isSick(i, j - 1)) {
                            if (this.chanceToSickText > generateProb()) {
                                Cell cell = makeNewSick(i, j);
                                cell.setDays(this.generateDays());
                                this.board[i][j] = cell;
                            }
                        }
                    }
                }
            }
        }
    }






    /**
     * Method to heal people
     * Checks for sick person and checks for cell atribute days left to heal
     *
     * @param line
     * @param col
     */
    private void healOverTime(int line, int col) {

        if (isSick(line, col)) {
            int a = this.board[line][col].getDays();
            if (a == 0) {
                if (generateProb() < this.chanceToBecImmuneText) {
                    this.board[line][col] = new ImmunePerson(new CellPosition(line, col));
                } else {
                    this.board[line][col] = new HealhyPerson(new CellPosition(line, col));
                }
            }
        }
    }

    /**
     * Method to make new sick people it has some given chance of being able to spread desease or not
     * and if spread also has a chance to generate sick people with or without symptom
     *
     * @param line
     * @param col
     * @return sick cell
     */
    private Cell makeNewSick(int line, int col) {
        Cell cell;
        cell = new SickNoSymptom(new CellPosition(line, col));
        return cell;

    }

    /**
     * Generates random numbe between 2 given values
     * controlls if the higher is the higher and lower is the lower
     *
     * @return generated number
     */
    private int generateDays() {
        int upper = maxTimeToHealText;
        int lower = minTimeToHealText;
        if (upper < lower) {
            int a = upper;
            upper = lower;
            lower = a;
        }

        return (int) (Math.random() * (upper - lower)) + lower;
    }

    /**
     * Method do generate randoms numbers which will be used to move the cells
     *
     * @return an array with 2 numbers
     */
    private int[] makeRandom() {

        int[] a = {-1, 0, 1};
        int[] b = {0, 0};

        b[0] = a[rand.nextInt(3)];
        b[1] = a[rand.nextInt(3)];

        if (b[0] == 0 && b[1] == 0) {
            b[0] = 1;
        }
        return b;
    }


    /**
     * Method to return the board size
     *
     * @return an array with 2 int values
     */
    public int[] boardSize() {
        int[] sizes = new int[2];
        String nLineNcol = "";

        this.fileRead = reading();
        read(fileRead);

        for (int j = 0; j < 2; j++) {
            nLineNcol += this.all.get(j) + " ";
        }

        String[] s = nLineNcol.split(" ");

        sizes[0] = Integer.parseInt(s[0]);
        sizes[1] = Integer.parseInt(s[1]);

        return sizes;
    }


    /**
     * Method to split the lists strings and fill the board with the previous loaded coordinates and it's respective people
     */
    public void fillFileBoard() {
        this.fillBoardEmpty();
        this.setUpLoadBoard();

        this.setSickPerson(this.sickWSList);

        this.setSickPerson(this.sickNoSList);

        this.setOtherPerson(this.healhyList);

        this.setOtherPerson(this.immuneList);

        this.setOtherPerson(this.deadList);

    }


    /**
     * Method to set sick types to the board reading positions from a list
     *
     * @param list to be read
     */
    private void setSickPerson(List<String> list) {

        for (int i = 0; i < list.size(); i++) {
            String s = list.get(i);
            String[] sicks = s.split(" ");
            int a = Integer.parseInt(sicks[0]);
            int b = Integer.parseInt(sicks[1]);
            int c = Integer.parseInt(sicks[2]);
            Cell cell;
            if (list.equals(sickNoSList)) {
                cell = new SickNoSymptom(new CellPosition(a, b));
            } else {
                cell = new SickWithSymptom(new CellPosition(a, b));
            }
            cell.setDays(c);
            this.board[a][b] = cell;


        }
    }

    /**
     * Method to set other types then sick to the board reading positions from a list
     *
     * @param list to be read
     */
    private void setOtherPerson(List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            String s = list.get(i);
            String[] healhty = s.split(" ");
            int a = Integer.parseInt(healhty[0]);
            int b = Integer.parseInt(healhty[1]);
            Cell cell;

            if (list.equals(this.healhyList)) {
                cell = new HealhyPerson(new CellPosition(a, b));
                this.board[a][b] = cell;
            }
            if (list.equals(this.deadList)) {
                cell = new DeadPerson(new CellPosition(a, b));
                this.board[a][b] = cell;
            }
            if (list.equals(this.immuneList)) {
                cell = new ImmunePerson(new CellPosition(a, b));
                this.board[a][b] = cell;
            }

        }
    }


    /**
     * Method to read the file and place the coordinates to each type of person in lists
     */
    private void setUpLoadBoard() {

        int count = 2;

        this.read(this.fileRead);

        this.count = count;
        this.setLists(count, this.all, this.healhyList, "immune");
        count = this.nextCount;

        this.setLists(count, this.all, this.immuneList, "dead");
        count = this.nextCount;

        this.setLists(count, this.all, this.deadList, "sickwsim");
        count = this.nextCount;

        this.setLists(count, this.all, this.sickWSList, "sicknos");

        count = this.nextCount;
        this.setLists(count, this.all, this.sickNoSList, "");


        //System.out.println("healthy " + healhyList + "\n" + "immune " + immuneList + "\n" + "Dead " + deadList + "\n" +" sick with sim " + sickWSList + "\n" + "sick no sim " + sickNoSList + "\n" + "total linhas e cols :" + nLines + "," + nCols);

    }

    /**
     * Method to fill each list with it's respective person and coordinates
     *
     * @param count to keep track where list has already checked
     * @param all   list to get items from
     * @param toAdd list to be saved
     * @param state used to stop at a certain line that contains the next state
     */

    private void setLists(int count, List<String> all, List<String> toAdd, String state) {

        while (count < all.size() - 1) {

            String next = all.get(count + 1);

            if (!next.equals(state)) {
                toAdd.add(next);
                count++;
                this.nextCount = count + 1;

            } else {
                this.nextCount = count + 1;
                count = all.size() - 1;
            }
        }
    }



    /**
     * Generates random number and compares to a given % to determinate if move direction will change after each iteraction
     *
     * @return
     */
    private boolean checkForDiferentMove() {
        int a = generateProb();

        if (a >= 10) {
            return true;
        }
        return false;
    }


    /**
     * Method to set up the file with all board information
     *
     * @return built string to be added to the file
     */

    private String setUpFile() {

        StringBuilder stringBuilder = new StringBuilder();

        String sws = "";
        String h = "";
        String i = "";
        String sns = "";
        String d = "";

        stringBuilder.append(nLines).append("\n").append(nCols).append("\n").append("healthy").append("\n");
        for (int j = 0; j < nLines; j++) {
            for (int k = 0; k < nCols; k++) {
                if (board[j][k].cellType().equals("HealhyPerson")) {
                    h += j + " " + k + "\n";
                }
                if (board[j][k].cellType().equals("ImmunePerson")) {
                    i += j + " " + k + "\n";
                }
                if (board[j][k].cellType().equals("DeadPerson")) {
                    d += j + " " + k + "\n";
                }

                if (board[j][k].cellType().equals("SickWS")) {
                    sws += j + " " + k + " " + board[j][k].getDays() + "\n";
                }
                if (board[j][k].cellType().equals("SickNoS")) {
                    sns += j + " " + k + " " + board[j][k].getDays() + "\n";
                }
            }
        }

        stringBuilder.append(h).append("immune").append("\n").append(i).append("dead").append("\n").append(d).append("sickwsim").append("\n").append(sws).append("sicknos").append("\n").append(sns);

        return stringBuilder.toString();

    }


    /**
     * Method to create a file and fill file with information of the board
     */
    public void writer() {

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("board.txt")));
            writer.write(setUpFile());
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to read the file
     * read line by line and add them to a string
     * @param file receives a file
     * @return a list of strings
     */
    private void read(File file){

        String s="";
        ArrayList<String> list = new ArrayList<>();
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                s = (scanner.nextLine());
                list.add(s);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
       this.all = list;
    }

    /**
     * Method to open and choose a file from directory
     *
     * @return chosen file
     */
    private File reading() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open text File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt", ".tex"));
        File file = fileChooser.showOpenDialog(null);

        return file;
    }



    /**
     * Method to print board in console
     * @return printed board as string
     */
    public String printBoard() {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < nLines; i++) {
            for (int j = 0; j < nCols; j++) {
                switch (board[i][j].cellType()) {
                    case "EmptyCell":
                        b.append("- ");
                        break;
                    case "HealhyPerson":
                        b.append("H ");
                        break;
                    case "ImmunePerson":
                        b.append("I ");
                        break;
                    case "SickNoS":
                        b.append("SN ");
                        break;
                    case "SickWS":
                        b.append("SWS ");
                        break;
                    case "DeadPerson":
                        b.append("D ");
                        break;
                }
            }
            b.append("\n");
        }
        return b.toString();
    }

    /**
     * Method to calculate how many of each people are in the world
     *
     * @return each type of people count
     */
    public int[] people() {
        int[] number = new int[6];

        for (int j = 0; j < nLines; j++) {
            for (int k = 0; k < nCols; k++) {
                if (isPerson(j, k) && board[j][k].cellType().equals("ImmunePerson")) {
                    number[3]++;
                }
                if (isPerson(j, k) && isSick(j, k)) {
                    number[5]++;
                }
                if (isPerson(j, k) && board[j][k].cellType().equals("HealhyPerson")) {
                    number[2]++;
                }
                if (isPerson(j, k) && board[j][k].cellType().equals("DeadPerson")) {
                    number[4]++;
                }
                if (isPerson(j, k) && board[j][k].cellType().equals("SickWS")) {
                    number[0]++;
                }
                if (isPerson(j, k) && board[j][k].cellType().equals("SickNoS")) {
                    number[1]++;
                }

            }
        }

        return number;
    }

    /**
     * Method to generate random nº between 0 and 100
     *
     * @return
     */
    private int generateProb() {
        int upper = 100;
        int lower = 0;

        return (int) (Math.random() * (upper - lower)) + lower;
    }


    /**
     * Method to check if is sick person given a line and col
     *
     * @param line param
     * @param col  param
     * @return true if is sick person
     */
    private boolean isSick(int line, int col) {
        return board[line][col].cellType().equals("SickNoS") || board[line][col].cellType().equals("SickWS");
    }


    /**
     * check if coordinates are valid
     *
     * @param line param
     * @param col  param
     * @return true if valid
     */
    public boolean isInside(int line, int col) {
        return line < nLines && col < nCols && line >= 0 && col >= 0;
    }

    /**
     * method to check if the cell type is a person
     *
     * @param line line param
     * @param col  col param
     * @return true if person
     */
    public boolean isPerson(int line, int col) {
        String p = this.board[line][col].cellType();
        return p.equals("HealhyPerson") || p.equals("ImmunePerson") || p.equals("SickPerson") || p.equals("DeadPerson") || p.equals("SickNoS") || p.equals("SickWS");
    }

    /**
     * method to check if the cell is empty
     *
     * @param line param
     * @param col  param
     * @return true if cell is empty
     */
    public boolean isEmpty(int line, int col) {
        String s = this.board[line][col].cellType();
        return s.equals("EmptyCell");
    }

    /**
     * @return nLines
     */
    public int nLines() {
        return this.nLines;
    }

    /**
     * @return nCols
     */
    public int nCols() {
        return this.nCols;
    }

    /**
     * @return board variable
     */
    public Cell[][] getBoard() {
        return this.board;
    }

    /**
     * set the flag true
     * used to stop the thread from running
     */
    public void setPause(Boolean pause) {
        this.pause = pause;

    }

    /**
     * Method to return pause
     *
     * @return pause
     */
    public boolean isPause() {
        return pause;
    }

    public int getCount() {
        return count;
    }

    /**
     * Methdo to return the string used to save the file
     *
     * @return string file
     */
    public String getSetUpFile() {
        return setUpFile();
    }

    /**
     * getter for file
     * @return a file object
     */
    public File getFile(){
        return fileRead;
    }

}