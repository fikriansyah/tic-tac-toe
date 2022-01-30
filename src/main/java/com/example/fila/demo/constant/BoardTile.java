package com.example.fila.demo.constant;

public enum BoardTile {

    EMPTY(""),
    X("x"),
    O("o");

    private final String text;

    BoardTile(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
