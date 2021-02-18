package pt.ipbeja.po2.contagious.model;

import java.util.Objects;


/**
 * Filipe Gonçalves
 * nº 6050
 * Git: https://github.com/PO2-2019-2020/po2-tp2-6050-po2
 */

public abstract class Cell {
    private CellPosition cellPosition;
    private int x;
    private int y;
    private int days;
    private int chanceOfDeath;
    private int daysToFeelSym;
    private int daysLived;
    private final int maxValue;
    private final int minValue;
    private final int minDays;
    private final int maxDays;

    public Cell(CellPosition cellPosition) {
        this.cellPosition = cellPosition;
        this.maxValue = 100;
        this.minValue = 95;
        this.minDays = 4;
        this.maxDays = 6;
       // daysToHeal();
        ChanceToDie();
        daysForSym();
    }

    public CellPosition cellPosition() {
        return cellPosition;
    }



    public String cellType(){
        return "Cell";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return x == cell.x &&
                y == cell.y &&
                Objects.equals(cellPosition, cell.cellPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cellPosition, x, y);
    }

    private void ChanceToDie(){
        this.chanceOfDeath = (int) (Math.random() * (this.maxValue - this.minValue)) + this.minValue;
    }

    private void daysToHeal(){
        int upper = 14;
        int lower = 7;
        this.days = (int) (Math.random() * (upper - lower)) + lower;
    }

    private void daysForSym(){

        this.daysToFeelSym = (int) (Math.random() * (this.maxDays - this.minDays)) + this.minDays;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public void setChanceOfDeath(int chance){
        this.chanceOfDeath = chance;
    }

    public int getChanceOfDeath(){
        return this.chanceOfDeath;
    }

    public void setCellPosition(CellPosition cellPosition) {
        this.cellPosition = cellPosition;
    }


    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }

    public int getDaysToFeelSym() {
        return daysToFeelSym;
    }

    public void setDaysToFeelSym(int daysToFeelSym) {
        this.daysToFeelSym = daysToFeelSym;
    }

    public int getDaysLived() {
        return daysLived;
    }

    public void setDaysLived(int daysLived) {
        this.daysLived = daysLived;
    }
}
