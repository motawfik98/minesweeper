package main;

public class Point extends Shape{

    public Point(int row, int column) {
        super(row, column);
    }


    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Point))
            return false;
        Point p = (Point) obj;
        return (getRow() == p.getRow() && getColumn() == p.getColumn());
    }

    public boolean isOutOfBounds() {
        if (getRow() < 0)
            return true;

        if (getRow() >= Constants.currentSquares)
            return true;

        if (getColumn() < 0)
            return true;

        if (getColumn() >= Constants.currentSquares)
            return true;

        return false;
    }
}
