package pt.ipbeja.po2.contagious.model;


/**
 * Filipe Gonçalves
 * nº 6050
 * Git: https://github.com/PO2-2019-2020/po2-tp2-6050-po2
 */

public class ImmunePerson extends Person{
    public ImmunePerson(CellPosition cellPosition) {
        super(cellPosition);
    }

    public String cellType(){
        return "ImmunePerson";
    }
}
