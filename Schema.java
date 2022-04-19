package ru.netology.graphics.image;

public class Schema implements TextColorSchema {
    @Override
    public char convert(int color) {
        char [] symbols = new char[] {'#', '$', '@', '%', '*', '+', '-', '\''};
        return symbols[(int) Math.floor(color / 256. * symbols.length)];
    }

}
