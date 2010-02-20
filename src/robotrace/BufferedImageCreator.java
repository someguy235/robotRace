/*
 * BufferedImageCreator.java
 *
 * Created on September 8, 2006, 4:23 PM
 *
 * by Ethan Shepherd
 */

/*
 * This class takes a normal image file, like the .jpg and .png files found
 * in the "images" directory, and creates a Java BufferedImage from it.
 * This BufferedImage is able to do the types of transforms that are necessary
 * in the ImageLoader class.
 * It is also able to create an image of a subsection of a larger image. Most of 
 * the image files are actually arrays of images, and this class parses them
 * for use.
 */

package robotrace;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import javax.swing.*;
import java.awt.*;

public class BufferedImageCreator {
    
    //X's and Y's refer to the sub-area of a larger image that should be used to make the new image
    public BufferedImage toBufferedImage(Image image, int w, int h, int sX1, int sY1, int sX2, int sY2, boolean alpha) {
        // This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();
 
        int transparency = Transparency.BITMASK;
        if (alpha){
            transparency = Transparency.TRANSLUCENT;
            //System.out.println("alpha");
        }
        
        // Create a buffered image with a format that's compatible with the screen
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        
        try {
            // Create the buffered image
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(w, h, transparency);
        } catch (HeadlessException e) {
            // The system does not have a screen
        }
    
        if (bimage == null) {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_ARGB;
            bimage = new BufferedImage(w, h, type);
        }
    
        // Copy image to buffered image
        Graphics g = bimage.createGraphics();
    
        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, w, h, sX1, sY1, sX2, sY2, null);
        g.dispose();
    
        return bimage;
    }
}