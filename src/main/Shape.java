package main;


public abstract class Shape {
    private int row, column;

    public Shape(int row, int column) {
        this.row = row;
        this.column = column;
    }


    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public Point getPoint() {
        return new Point(row, column);
    }

}
