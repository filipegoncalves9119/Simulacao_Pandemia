package pt.ipbeja.po2.contagious.model;

import java.util.Objects;

/**
 * Filipe Gonçalves
 * nº 6050
 * Git: https://github.com/PO2-2019-2020/po2-tp2-6050-po2
 */

public class CellPosition {
    private int line;
    private int col;

    public CellPosition(int line, int col) {
        this.line = line;
        this.col = col;
    }

    public int getLine() {
        return line;
    }

    public int getCol() {
        return col;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CellPosition cellPosition = (CellPosition) o;
        return line == cellPosition.line &&
                col == cellPosition.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(line, col);
    }

}
