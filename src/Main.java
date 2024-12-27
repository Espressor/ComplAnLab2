import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

public class Main {
    public static void main(String[] args) {
        int width = 400, height = 400;
        int size = 2; // size of visible square
        int inf = 2000;
        Map<String, BufferedImage> images = new HashMap<>();

        SimpleComplex I = new SimpleComplex(0, 1);
        SimpleComplex ONE = new SimpleComplex(1, 0);

        UnaryOperator<SimpleComplex> halfPlaneToCircle = z ->
                I.multiply(z.subtract(I).divide(z.add(I))); // i(z-i)/(z+i)
        UnaryOperator<SimpleComplex> CircleToHalfPlane = z ->
                z.add(I).divide(z.multiply(I).add(ONE)); // (z+i)/(iz+1)
        UnaryOperator<SimpleComplex> nega = z -> z.multiply(-1); // -z
        UnaryOperator<SimpleComplex> square = SimpleComplex::square; // z^2
        UnaryOperator<SimpleComplex> root = z -> z.power(0.5); // sqrt(z)

        long start = System.currentTimeMillis();
        boolean[][] someSet;
        boolean[][] otherSet;

        PointsHandler plane = new PointsHandler(inf);
        PointsHandler line = new PointsHandler(inf);
        plane.addHalfPlane(600000, size);
        line.addMyLine(100000, size);

        someSet = plane.getPlane(width, height, size);
        otherSet = line.getPlane(width, height, size);
        images.put("start", SomeDrawer.addToImage(SomeDrawer.getImage(someSet, Color.BLACK), otherSet, Color.WHITE));

        plane.transform(nega);
        line.transform(nega);
        someSet = plane.getPlane(width, height, size);
        otherSet = line.getPlane(width, height, size);
        images.put("reversed", SomeDrawer.addToImage(SomeDrawer.getImage(someSet, Color.BLACK), otherSet, Color.WHITE));

        plane.transform(root);
        line.transform(root);
        someSet = plane.getPlane(width, height, size);
        otherSet = line.getPlane(width, height, size);
        images.put("halfed", SomeDrawer.addToImage(SomeDrawer.getImage(someSet, Color.BLACK), otherSet, Color.WHITE));

        plane.transform(halfPlaneToCircle);
        line.transform(halfPlaneToCircle);
        someSet = plane.getPlane(width, height, size);
        otherSet = line.getPlane(width, height, size);
        images.put("circled", SomeDrawer.addToImage(SomeDrawer.getImage(someSet, Color.BLACK), otherSet, Color.WHITE));

        System.out.println("Calculated in " + (System.currentTimeMillis() - start) / 1000f + " s");

        // save images
        images.forEach((name, image) ->
        {
            try {
                ImageIO.write(image, "png", new File("images/" + name + ".png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}