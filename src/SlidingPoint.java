public class SlidingPoint {

    private String slidingDirection;
    private int row;
    private int col;

    private double xCord;

    private double yCord;

    public SlidingPoint(int row, int col, double xCord,double yCord, String slidingDirection) {
        this.col = col;
        this.row = row;
        this.xCord = xCord;
        this.yCord = yCord;
        this.slidingDirection = slidingDirection;

    }

    public String getSlidingDirection() {
        return slidingDirection;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public double getxCord() {
        return xCord;
    }

    public double getyCord() {
        return yCord;
    }



}
