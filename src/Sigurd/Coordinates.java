package Sigurd;

public class Coordinates {

    private int row, col;

    Coordinates(int col, int row) {
        this.col = col;
        this.row = row;
    }

    public void add(Coordinates coordinates) {
        col = col + coordinates.getCol();
        row = row + coordinates.getRow();
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
