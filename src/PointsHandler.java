import java.util.LinkedList;
import java.util.List;
import java.util.function.UnaryOperator;

public class PointsHandler {
    private List<Point> points;
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

    public void addLine(double xStart, double yStart, double xEnd, double yEnd, int size) {
        for (int i = 0; i < size; i++) {
            double x = (double) i / size * (xEnd - xStart) + xStart;
            double y = (double) i / size * (yEnd - yStart) + yStart;
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
                    int x = (int) ((p.x / radius / 2 + 0.5) * (width - 1));
                    int y = (int) ((-p.y / radius / 2 + 0.5) * (height - 1));
                    plane[x][y] = true;
                }
            }
        } catch (NullPointerException e) {
            System.out.println("NullPointerException: " + e.getMessage());
        }
        return plane;
    }
}
