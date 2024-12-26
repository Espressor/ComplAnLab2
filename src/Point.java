public class Point {

    public double x;
    public double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public boolean inRadius(double r) {
        return x < r && x > -r && y < r && y > -r;
    }
}
