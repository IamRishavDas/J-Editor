import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class TransformationFunctions {

    public static final int maxRGB = 255;
    public static final int minRGB = 0;

    private static int[] checkBoundary(int r, int g, int b){
        if(r < 0) r = minRGB;   if(r > 255) r = maxRGB;
        if(g < 0) g = minRGB;   if(g > 255) g = maxRGB;
        if(b < 0) b = minRGB;   if(b > 255) b = maxRGB;
        return new int[]{r, g, b};
    }

    public static void clearTheWindow(BufferedImage image){
        if(image == null) return;
        EditorFrame.removePreviousImageFromPanel();
    }

    static int[][] imageArray;
    public static void storeCurrentImageState(BufferedImage image){
        if(image == null) return;
        imageArray = new int[image.getWidth()][image.getHeight()];
        for(int y = 0; y<image.getHeight(); y++){
            for(int x = 0; x<image.getWidth(); x++){
                imageArray[x][y] = image.getRGB(x, y);
            }
        }
        System.out.println("The current state is stored!");
        System.out.println("Image Width: " + image.getWidth() + " Image Height: " + image.getHeight());
        System.out.println("Array Width: " + imageArray.length + " Array Height: " + imageArray[0].length);
    }

    public static void recoverToPreviousCurrentState(BufferedImage image){
        if(imageArray == null){
            return;
        }
        for(int y = 0; y<image.getHeight(); y++){
            for(int x = 0; x<image.getWidth(); x++){
                image.setRGB(x, y, imageArray[x][y]);
            }
        }
        EditorFrame.removePreviousImageFromPanel();
        EditorFrame.displayImage(image);
    }

    public static void grayScaleFilter(BufferedImage image){
        if(image == null) return;
        for(int y = 0; y<image.getHeight(); y++){
            for(int x = 0; x<image.getWidth(); x++){
                Color color = new Color(image.getRGB(x, y));
                int avg = (color.getRed() + color.getGreen() + color.getBlue())/3;
                Color gray = new Color(avg, avg, avg);
                image.setRGB(x, y, gray.getRGB());
            }
        }
        EditorFrame.removePreviousImageFromPanel();
        EditorFrame.displayImage(image);
    }

    public static void negativeFilter(BufferedImage image){
        if(image == null) return;
        for(int y = 0; y<image.getHeight(); y++){
            for(int x = 0; x<image.getWidth(); x++){
                Color color = new Color(image.getRGB(x, y));
                int nR = 255-color.getRed();
                int nG = 255-color.getGreen();
                int nB = 255-color.getBlue();
                int[] vals = checkBoundary(nR, nG, nB);
                nR = vals[0];
                nG = vals[1];
                nB = vals[2];
                Color negaiveColor = new Color(nR, nG, nB);
                image.setRGB(x, y, negaiveColor.getRGB());
            }
        }
        EditorFrame.removePreviousImageFromPanel();
        EditorFrame.displayImage(image);
    }

    public static void logTransformation(BufferedImage image, float scalingFactor, float upscalingFactor){
        if(image == null) return;
        for(int y = 0; y<image.getHeight(); y++){
            for(int x = 0; x<image.getWidth(); x++){
                Color color = new Color(image.getRGB(x, y));
                int logR = (int)(scalingFactor * Math.log1p(color.getRed() + 1) + upscalingFactor);
                int logG = (int)(scalingFactor * Math.log1p(color.getGreen() + 1) + upscalingFactor);
                int logB = (int)(scalingFactor * Math.log1p(color.getBlue() + 1) + upscalingFactor);
                int[] vals = checkBoundary(logR, logG, logB);
                logR = vals[0];
                logG = vals[1];
                logB = vals[2];
                image.setRGB(x, y, new Color(logR, logG, logB).getRGB());
            }
        }
        EditorFrame.removePreviousImageFromPanel();
        EditorFrame.displayImage(image);
    }

    public static void powerLawTransformation(BufferedImage image, float scalingFactor, float gamma){
        if(image == null) return;
        for(int y = 0; y<image.getHeight(); y++){
            for(int x = 0; x<image.getWidth(); x++){
                Color color = new Color(image.getRGB(x, y));
                int powR = (int)(scalingFactor * Math.pow(color.getRed(), gamma));
                int powG = (int)(scalingFactor * Math.pow(color.getGreen(), gamma));
                int powB = (int)(scalingFactor * Math.pow(color.getBlue(), gamma));
                int[] vals = checkBoundary(powR, powG, powB);
                powR = vals[0];
                powG = vals[1];
                powB = vals[2];
                if(powR > 255 || powG > 255 || powB > 255) System.out.println(powR + ": " + powG + ": " + powB);
                image.setRGB(x, y, new Color(powR, powG, powB).getRGB());
            }   
        }
        EditorFrame.removePreviousImageFromPanel();
        EditorFrame.displayImage(image);
    }

    public static void exponentialTransformation(BufferedImage image, float scalingFactor){
        if(image == null) return;
        for(int y = 0; y<image.getHeight(); y++){
            for(int x = 0; x<image.getWidth(); x++){
                Color color = new Color(image.getRGB(x, y));
                int expR = (int)(scalingFactor * Math.exp(color.getRed()));
                int expG = (int)(scalingFactor * Math.exp(color.getGreen()));
                int expB = (int)(scalingFactor * Math.exp(color.getBlue()));
                int[] vals = checkBoundary(expR, expG, expB);
                expR = vals[0];
                expG = vals[1];
                expB = vals[2];
                image.setRGB(x, y, new Color(expR, expG, expB).getRGB());
            }
        }
        EditorFrame.removePreviousImageFromPanel();
        EditorFrame.displayImage(image);
    }

    public static void rotateTheImage(BufferedImage image) {
        if (image == null) {
            JOptionPane.showMessageDialog(null, "The canvas is empty!");
            return;
        }
    
        image = createRotatedImage(image);
        EditorFrame.removePreviousImageFromPanel();
        EditorFrame.displayImage(image);
    }
    
    private static BufferedImage createRotatedImage(BufferedImage originalImage) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
    
        BufferedImage rotatedImage = new BufferedImage(width, height, originalImage.getType());
    
        Graphics2D g = rotatedImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.rotate(Math.toRadians(90), width / 2, height / 2);
        g.drawImage(originalImage, 0, 0, null);
        g.dispose();
    
        return rotatedImage;
    }
    

    
}
