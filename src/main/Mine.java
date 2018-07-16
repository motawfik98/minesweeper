package main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Mine extends Shape {
    private static Image img;
    private ImageIcon imgIcon;

    public Mine(int row, int column) {
        super(row, column);
        initImage();
    }

    private void initImage() {
        try {
            img = ImageIO.read(new File("res/mine.png"));
            img = img.getScaledInstance(Constants.currentSquareLength, Constants.currentSquareLength, Image.SCALE_SMOOTH);
            imgIcon = new ImageIcon(img);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ImageIcon getImgIcon() {
        return imgIcon;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof Mine))
            return false;
        Mine m = (Mine) obj;
        return (getRow() == m.getRow() && getColumn() == m.getColumn());
    }
}
