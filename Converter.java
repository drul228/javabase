package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

public class Converter implements TextGraphicsConverter {
    private int width;
    private int height;
    private double maxRation;
    private String text;
    private TextColorSchema schema = new Schema();

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        BufferedImage img = ImageIO.read(new URL(url));
        int newHeight = img.getHeight();
        int newWidth = img.getWidth();
        double factRatio = newHeight / newWidth;
        double invertedRatio = newWidth / newHeight;
        if (factRatio > maxRation && maxRation != 0) {
            throw new BadImageSizeException(factRatio, maxRation);
        } else if (invertedRatio > maxRation && maxRation != 0){
            throw new BadImageSizeException(invertedRatio, maxRation);
        }
        if (newHeight > height & height != 0 & width == 0) {
            int coef = newHeight / height;
            newHeight = newHeight / coef;
            newWidth = newWidth / coef;
        }
        if (newWidth > width & width != 0 & height == 0) {
            int coef = newWidth / width;
            newWidth = newWidth / coef;
            newHeight = newHeight / coef;
        }
        if (newHeight > height & newWidth > width & width != 0 & height != 0) {
            int whoIsBigger = newHeight > newWidth ? newHeight : newWidth;
            int whoisBigger2 = whoIsBigger == height ? height : width;
            int coef = whoIsBigger / whoisBigger2;
            newHeight = newHeight / coef;
            newWidth = newWidth / coef;
        }
        Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);
        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = bwImg.createGraphics();
        graphics.drawImage(scaledImage, 0, 0, null);
        WritableRaster bwRaster = bwImg.getRaster();
        StringBuilder stringBuilder = new StringBuilder();
        ImageIO.write(bwImg, "png", new File("out.png"));
        for (int i = 0; i < newHeight; i++) {
            for (int t = 0; t < newWidth; t++) {
                int color = bwRaster.getPixel(t, i, new int[3])[0];
                char c = schema.convert(color);
                stringBuilder.append(c + "" + c);
            }
            stringBuilder.append("\n");
        }
        text = String.valueOf(stringBuilder);
        return text;
    }


    @Override
    public void setMaxWidth(int width) {
        this.width = width;

    }

    @Override
    public void setMaxHeight(int height) {
        this.height = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRation = maxRatio;

    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        this.schema = schema;
    }


}
