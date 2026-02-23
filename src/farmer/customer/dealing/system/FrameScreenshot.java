package farmer.customer.dealing.system;

import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.imageio.ImageIO;
import java.io.File;

public class FrameScreenshot {

    public static void capture(JFrame frame, String fileName) {
        try {
            BufferedImage image = new BufferedImage(
                    frame.getWidth(),
                    frame.getHeight(),
                    BufferedImage.TYPE_INT_RGB
            );

            frame.paint(image.getGraphics());
            ImageIO.write(image, "png", new File(fileName));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
