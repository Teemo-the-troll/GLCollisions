package models;

import org.lwjgl.opengl.GL33;

import java.util.Arrays;
import java.util.Random;

public class Maze {
    private final Square[] body;
    public Maze(String source) {
        //TODO redo this for use of squares
        String[] rawRows = source.split("\n");
        this.body = new Square[rawRows.length];
        for (int i = 0; i < rawRows.length; i++) {
            Float[] squareStuff = Arrays.stream(rawRows[i].split(";")).map(Float::valueOf).toArray(Float[]::new);
            this.body[i] = new Square(squareStuff[0],squareStuff[1],squareStuff[2]);
        }
    }

    public Square[] getBody() {
        return body;
    }


}
