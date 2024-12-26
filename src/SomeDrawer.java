import java.awt.*;
import java.awt.image.BufferedImage;

public class SomeDrawer {

    public static BufferedImage getImage(boolean[][] plane, Color primeColor) {
        int width = plane.length;
        int height = plane[0].length;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, plane[x][y] ? primeColor.getRGB() : Color.WHITE.getRGB());
            }
        }
        return image;
    }

    public static BufferedImage addToImage(BufferedImage image, boolean[][] plane, Color primeColor) {
        int width = plane.length;
        int height = plane[0].length;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int xIm = x * image.getWidth() / width;
                int yIm = y * image.getHeight() / height;
                if (plane[x][y])
                    image.setRGB(xIm, yIm, primeColor.getRGB());
            }
        }
        return image;
    }
}
