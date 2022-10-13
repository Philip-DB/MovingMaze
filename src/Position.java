public class Position {

    private double xCord;
    private double yCord;

    private int iRow;
    private int iCol;

    public Position(double xCord,double yCord,int iRow,int iCol) {
        this.xCord = xCord;
        this.yCord = yCord;
        this.iRow = iRow;
        this.iCol = iCol;
    }

    public double getyCord() {
        return yCord;
    }

    public double getxCord() {
        return xCord;
    }

    public int getCol() {
        return iCol;
    }

    public int getRow() {
        return iRow;
    }
}
