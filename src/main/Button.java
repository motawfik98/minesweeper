package main;

import javax.swing.*;
import java.awt.*;

public class Button extends JButton {
    private int row, column; // co-ordinates of the button
    private boolean opened = false;

    public Button(int row, int column) {
        this.row = row;
        this.column = column;
        setPreferredSize(new Dimension(Constants.currentSquareLength, Constants.currentSquareLength)); // set size of the button
        setBackground(Color.BLUE); // set the color
    }


    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public void setOpen() {
        opened = true;
    }

    public boolean getOpened() {
        return opened;
    }

    public Point getPoint() {
        return new Point(row, column);
    }
}
