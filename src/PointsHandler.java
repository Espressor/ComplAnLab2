import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.UnaryOperator;

public class PointsHandler {
    private final List<Point> points;
    private final double inf;

    public PointsHandler(double inf) {
        this.inf = inf;
        points = new LinkedList<>();
    }

    public void addPoint(double x, double y) {
        points.add(new Point(x, y));
    }

    public void removePoint(double x, double y) {
        points.removeIf(p -> p.x == x && p.y == y);
    }

    public void addHalfPlane(int size, int visibleSize) {
        addRectangle(-inf, -inf, inf, inf, size);
        addRectangle(-visibleSize, -visibleSize, visibleSize, visibleSize, size);
        int t = 2 * visibleSize * visibleSize;
        addRectangle(-t, -t, t, t, size);
        addZeroRectangle(-inf, -inf, inf, inf, size / 10);
    }

    public void addRectangle(double xStart, double yStart, double xEnd, double yEnd, int size) {
        int sizeX = Math.max((int) Math.sqrt(size * (xEnd - xStart) / (yEnd - yStart)), 1);
        int sizeY = size / sizeX;
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                double x = ((double) i / sizeX) * (xEnd - xStart) + xStart;
                double y = ((double) j / sizeY) * (yEnd - yStart) + yStart;
                addPoint(x, y);
            }
        }
    }

    public void addZeroRectangle(double xStart, double yStart, double xEnd, double yEnd, int size) {
        int sizeX = Math.max((int) Math.sqrt(size * (xEnd - xStart) / (yEnd - yStart)), 1);
        int sizeY = size / sizeX;

        ArrayList<UnaryOperator<Double>> densities = new ArrayList<>();
        densities.add(value -> 1 / (1 + Math.pow(value, 4)) + 0.5);
        densities.add(value -> Math.pow(value, 0.3) / (1 + Math.pow(value, 3)) + 0.5);
        densities.add(value -> Math.pow(value, 0.6) / (1 + Math.pow(value, 3)) + 0.5);
        densities.add(value -> Math.pow(value, 0.9) / (1 + Math.pow(value, 3)) + 0.5);
        densities.add(value -> 1 / (1 + Math.pow(value, 2)) + 0.5);
        densities.add(value -> 1 / (1 + Math.pow(value, 1.8)) + 0.5);
        densities.add(value -> 1 / (1 + Math.pow(value, 1.6)) + 0.5);
        densities.add(value -> 1 / (1 + Math.pow(value, 1.3)) + 0.5);
        densities.add(value -> 1 / (1 + Math.pow(value, 0.8)) + 0.5);

        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                for (UnaryOperator<Double> d : densities) {
                    double x = d.apply((double) i) * (xEnd - xStart) + xStart;
                    double y = d.apply((double) j) * (yEnd - yStart) + yStart;
                    addPoint(x, y);
                    addPoint(-x, y);
                    addPoint(x, -y);
                    addPoint(-x, -y);
                }
            }
        }
    }

    public void addCircle(double radius, int size) {
        int linSize = (int) Math.sqrt(4 / Math.PI * size); // area of circle is 4/pi of area of square
        for (int i = 0; i < linSize; i++)
            for (int j = 0; j < linSize; j++) {
                double x = ((double) i / linSize - 0.5) * radius * 2;
                double y = (0.5 - (double) j / linSize) * radius * 2;
                if (x * x + y * y <= radius * radius)
                    addPoint(x, y);
            }
    }


    public void addMyLine(int size, int visibleSize) {
        addLine(0, 0, inf, 0, size);
        addLine(0, 0, visibleSize * visibleSize, 0, size);
        addZeroLine(0, 0, inf, 0, size);
    }

    public void addLine(double xStart, double yStart, double xEnd, double yEnd, int size) {
        for (int i = 0; i < size; i++) {
            double x = (double) i / size * (xEnd - xStart) + xStart;
            double y = (double) i / size * (yEnd - yStart) + yStart;
            addPoint(x, y);
        }
    }

    public void addZeroLine(double xStart, double yStart, double xEnd, double yEnd, int size) {
        for (int i = 0; i < size; i++) {
            double x = (1d / (1 + i * i)) * (xEnd - xStart) + xStart;
            double y = (1d / (1 + i * i)) * (yEnd - yStart) + yStart;
            addPoint(x, y);

        }
    }

    public void transform(UnaryOperator<SimpleComplex> function) {
        points.parallelStream().forEach((p) -> {
            SimpleComplex z = new SimpleComplex(p.x, p.y);
            z = function.apply(z);
            p.x = z.real();
            p.y = z.imaginary();
            if (!p.inRadius(inf))
                p.x = p.y = 0;
        });
    }


    public boolean[][] getPlane(int width, int height, int radius) {
        boolean[][] plane = new boolean[width][height];
        try {
            for (Point p : points) {
                if (p == null)
                    System.out.println("Null point");
                else if (p.inRadius(radius)) {
                    int x = (int) ((p.x / radius / 2 + 0.5) * width);
                    int y = (int) ((0.5 - p.y / radius / 2) * height);
                    plane[x][y] = true;
                }
            }
        } catch (NullPointerException e) {
            System.out.println("NullPointerException: " + e.getMessage());
        }
        return plane;
    }
}
