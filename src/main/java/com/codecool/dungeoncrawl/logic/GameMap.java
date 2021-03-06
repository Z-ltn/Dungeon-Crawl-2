package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.*;

import java.io.Serializable;
import java.util.*;

public class GameMap implements Serializable {
    private int width;
    private int height;
    private Cell[][] cells;
    private Cat cat;
    private Player player;
    private int grassCounter;

    public GameMap(int width, int height, CellType defaultCellType) {
        this.width = width;
        this.height = height;
        cells = new Cell[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cells[x][y] = new Cell(this, x, y, defaultCellType);
            }
        }
    }

    public Cell getCell(int x, int y) {
        return cells[x][y];
    }

    public void setCell(Player player, int x, int y) {
        cells[x][y].setActor(player);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Cat getCat() {
        return cat;
    }

    public void setCat(Cat cat) {
        this.cat = cat;
    }

    public Player getPlayer() {
        return player;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getGrassCounter(){return grassCounter;}

    public void raiseGrassCounter(){
        grassCounter += 1;
    }

}
