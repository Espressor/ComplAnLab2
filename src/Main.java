import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.function.UnaryOperator;

public class Main {
    public static void main(String[] args) {
        int width = 400, height = 400;
        int size = 2; // size of visible square
        int inf = 800;
        String[] imageNames = {"start", "reversed", "halfed", "circled"};
        BufferedImage[] images = new BufferedImage[imageNames.length];

        SimpleComplex I = new SimpleComplex(0, 1);

        UnaryOperator<SimpleComplex> halfPlaneToCircle = (z) ->
                I.multiply(z.subtract(I).divide(z.add(I))); // i(z-i)/(z+i)
        UnaryOperator<SimpleComplex> nega = (z) -> z.multiply(-1); // -z
        UnaryOperator<SimpleComplex> square = SimpleComplex::square; // z^2
        UnaryOperator<SimpleComplex> root = (z) -> z.power(0.5); // sqrt(z)

        UnaryOperator<SimpleComplex> ourTransformationTo = (z) ->
                halfPlaneToCircle.apply(root.apply(nega.apply(z)));

        long start = System.currentTimeMillis();
        PointsHandler ph = new PointsHandler(inf);
        PointsHandler ph1 = new PointsHandler(inf);

        ph.addRectangle(-inf, -inf, inf, inf, 10 * 1000000); // plane
        ph.addRectangle(-6 * size, -6 * size, 6 * size, 6 * size, 10 * 1000000);
        ph.addZeroRectangle(-inf, -inf, inf, inf, 10 * 1000000);
        ph.addZeroRectangle(-inf / 8, -inf / 8, inf / 8, inf / 8, 10 * 1000000);

        ph1.addLine(0, 0, inf, 0, 10000); // line
        ph1.addLine(0, 0, size*4, 0, 10000); // line
        ph1.addZeroLine(0, 0, inf/4, 0, 80000);
        images[0] = SomeDrawer.addToImage(SomeDrawer.getImage(ph.getPlane(width, height, size), Color.BLACK),
                ph1.getPlane(width, height, size), Color.WHITE);

        ph.transform(nega);
        ph1.transform(nega);
        images[1] = SomeDrawer.addToImage(SomeDrawer.getImage(ph.getPlane(width, height, size), Color.BLACK),
                ph1.getPlane(width, height, size), Color.WHITE);

        ph.transform(root);
        ph1.transform(root);
        images[2] = SomeDrawer.addToImage(SomeDrawer.getImage(ph.getPlane(width, height, size), Color.BLACK),
                ph1.getPlane(width, height, size), Color.WHITE);

        ph.transform(halfPlaneToCircle);
        ph1.transform(halfPlaneToCircle);
        images[3] = SomeDrawer.addToImage(SomeDrawer.getImage(ph.getPlane(width, height, size), Color.BLACK),
                ph1.getPlane(width, height, size), Color.WHITE);

        System.out.println("Calculated in " + (System.currentTimeMillis() - start) / 1000f + " s");

        // save images
        File[] imageFiles = new File[imageNames.length];
        for (int i = 0; i < imageNames.length; i++)
            imageFiles[i] = new File("images/" + imageNames[i] + ".png");

        try {
            for (int i = 0; i < imageNames.length; i++)
                ImageIO.write(images[i], "png", imageFiles[i]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}