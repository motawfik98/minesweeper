package main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Flag extends Shape {

    private static Image img;
    private ImageIcon imgIcon;

    public Flag(int row, int column) {
        super(row, column);
        initImage();
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof Flag))
            return false;
        Flag f = (Flag) obj;
        return (getRow() == f.getRow() && getColumn() == f.getColumn());
    }

    private void initImage() {
        try {
            img = ImageIO.read(new File("res/flag.png"));
            img = img.getScaledInstance(Constants.currentSquareLength, Constants.currentSquareLength, Image.SCALE_SMOOTH);
            imgIcon = new ImageIcon(img);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ImageIcon getImgIcon() {
        return imgIcon;
    }

}
