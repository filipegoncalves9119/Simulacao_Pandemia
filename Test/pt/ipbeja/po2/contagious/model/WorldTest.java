package pt.ipbeja.po2.contagious.model;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;



/**
 * Filipe Gonçalves
 * nº 6050
 * Git: https://github.com/PO2-2019-2020/po2-tp2-6050-po2
 */


class WorldTest {

    ViewTest view = new ViewTest();
    World world;
    private File file;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {

        this.world = new World(view, 10, 10,0,0,0,100,0,9,10, file);


    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {

    }


    @Test
    void Test01() {
        world.fillBoardEmpty();
        this.world.getBoard()[0][1] = new ImmunePerson(new CellPosition(0,1));

        // Pessoa em 0,1
        assertTrue(world.isPerson(0,1));

        // vazio em 0,0
        assertTrue(world.isEmpty(0,0));

        //mover pessoa 1 casa para esquerda
        this.world.moveToEmpty(0,1,0, -1,0);

        // verificar se pessoa fez movimento
        assertTrue(world.isPerson(0,0));

        // demasiado à esquerda
        assertFalse(world.isInside(0,-1));

        // demasiado em cima
        assertFalse(world.isInside(-1,0));

        // demasiado em baixo
        assertFalse(world.isInside(1000,0));

        // demasiado à direita
        assertFalse(world.isInside(0,1000));

    }

    @Test
    void Test02(){
        world.fillBoardEmpty();

        this.world.getBoard()[0][0] = new SickWithSymptom(new CellPosition(0,0));
        this.world.getBoard()[1][2] = new ImmunePerson(new CellPosition(1,2));
        this.world.getBoard()[2][3] = new HealhyPerson(new CellPosition(2,3));
        this.world.getBoard()[3][2] = new SickNoSymptom(new CellPosition(3,2));


        System.out.println(world.printBoard());
        //1º moves
        this.world.moveToEmpty(0,0, 1,0,0);
        this.world.moveToEmpty(1,2,0,-1,0);
        this.world.moveToEmpty(3,2,0,-1,0);

        this.world.moveToEmpty(2,3,0,-1,0); // healhy

        world.verifySick();

        assertEquals("HealhyPerson",world.getBoard()[2][2].cellType());
        world.positions.clear();

        //2º moves
        this.world.moveToEmpty(1,0, 1,0,0);
        this.world.moveToEmpty(1,1,0,-1,0);
        this.world.moveToEmpty(3,1,0,-1,0);

        this.world.moveToEmpty(2,2,0,-1,0); // new Sick

        world.verifySick();
        System.out.println(world.printBoard());


        assertEquals("SickNoS", world.getBoard()[2][1].cellType());



    }
}
