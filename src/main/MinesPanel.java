package main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class MinesPanel extends JPanel implements ActionListener {
    private Mine[] mines = new Mine[Constants.currentMines]; // array that contains all the mines
    private ArrayList<Flag> flags = new ArrayList<>(); // ArrayList that contains all the flags
    private int boardSize = Constants.currentSquares; // the length and width of the board
    private Board board = new Board(boardSize); // board object
    private Button button = null; // button clicked
    private int closed = Constants.currentSquares * Constants.currentSquares - Constants.currentMines; // number of closed buttons excluding the ones containing mines
    private JLabel lblTime, lblMinesLeft;
    private int time = 0, remainingMines = Constants.currentMines;
    private Timer timer;
    private boolean gameOver = false; // boolean to indicate whether the user has finished the game or not (to disable clicking)

    public MinesPanel(JLabel lblTime, JLabel lblMinesLeft) {
        this.lblTime = lblTime;
        this.lblMinesLeft = lblMinesLeft;
        for (int i = 0; i < mines.length; i++)
            mines[i] = generateMine(); // generate the mines array

        for (int i = 0; i < boardSize; i++)
            for (int j = 0; j < boardSize; j++) {
                Button button = board.getButton(i, j); // gets the button at the row and column i, j
                button.addMouseListener(new MouseListener()); // add MouseListener
                add(button); // add the button to the panel
            }
        timer = new Timer(Constants.DELAY, this);
        timer.start();


//        showMines();
    }

    private void showMines() { // shows all the mines in the board
        for (Mine mine : mines)
            board.getButton(mine.getRow(), mine.getColumn()).setIcon(mine.getImgIcon());
    }

    private Mine generateMine() {
        Random random = new Random();
        Mine mine;
        int x, y;
        do {
            x = random.nextInt(Constants.currentSquares); // generate random numbers bounded by the size of the board
            y = random.nextInt(Constants.currentSquares);
            mine = new Mine(x, y);
        } while (collides(mine)); // keep looping till find a non colliding location with another mine
        return mine;
    }

    private boolean collides(Mine mine) {
        for (Mine m : mines) // loop through the mine
            if (mine.equals(m)) // if the points are equal (the 2 mines share the same location)
                return true;
        return false;
    }

    public void addFlag(Flag flag) {
        flags.add(flag); // add a flag to the flags ArrayList
        remainingMines--; // decrement the remaining mines variable
        lblMinesLeft.setText("Mines Left = " + remainingMines);
    }

    public void removeFlag(Flag flag) {
        for (int i = 0; i < flags.size(); i++) // loop through the flags ArrayList
            if (flags.get(i).equals(flag)) { // if the given flag is found in the list
                flags.remove(i); // remove the flag
                remainingMines++; // increment the remaining mines variable
                lblMinesLeft.setText("Mines Left = " + remainingMines);
            }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        time++;
        lblTime.setText("Time = " + time);
    }

    private class MouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (gameOver)
                return;

            button = (Button)e.getSource(); // gets the source of the click

            try {
                if (!button.isEnabled()) // to unable the player to remove the surrounding number when displayed
                    return;

                if (e.getModifiers() == MouseEvent.BUTTON3_MASK) { // if it's right click

                    if (button.getIcon() != null) { // if it contains a flag
                        removeFlag(new Flag(button.getRow(), button.getColumn()));
                        button.setIcon(null); // remove the flag icon
                        return;
                    }
                    // it can't contain a flag if we reached this point
                    Flag flag = new Flag(button.getRow(), button.getColumn()); // we initialize a new flag
                    addFlag(flag); // we add the flag to the flags ArrayList
                    button.setIcon(flag.getImgIcon()); // the image of the flag is added to the button
                } else if (e.getModifiers() == MouseEvent.BUTTON1_MASK) { // if it's a left click
                    open();

                }
                if (closed == 0 && remainingMines == 0) { // if the closed buttons equals zero and the remaining flags equals zero, then the must must have won
                    JOptionPane.showConfirmDialog(null, "You Win!!",
                            "Congratulations", JOptionPane.DEFAULT_OPTION);
                    gameOver = true;
                    timer.stop();
                }
            } catch (NullPointerException e1) {
                System.out.println("Error");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void open() throws IOException {
        if (containsMine()) { // if the button contains a mine
            showMines(); // we show all mines in the board
            JOptionPane.showConfirmDialog(null, "Game Over", "Finish", JOptionPane.DEFAULT_OPTION);
            timer.stop(); // stop the timer
            gameOver = true;
            return;
        }
        // the button doesn't contain a mine
        Point point = new Point(button.getRow(), button.getColumn());
        int surroundings = getSurroundingsMines(point); // we get the number of the surrounding mines of that location(point)
        openButton(surroundings, button);
        if (surroundings == 0)
            openNeighbors(point, getSurroundingPoints(point));
    }

    private void openButton(int surroundings, Button b) throws IOException {
        if (b.getOpened()) // if the button was already opened, we return
            return;
        if (containsFlag(b)) // if the button contains a flag
            removeFlag(new Flag(b.getRow(), b.getColumn())); // remove the flag from the ArrayList
        closed--; // decrement the number of closed buttons
        b.setOpen(); // set the boolean field of the button to be true
        Image img = ImageIO.read(new File("res/" + surroundings + ".png")); // set the image to the number of mines surrounded by this location
        img = img.getScaledInstance(Constants.currentSquareLength - 10,
                Constants.currentSquareLength - 10, Image.SCALE_SMOOTH); // sets the size of the image
        b.setIcon(new ImageIcon(img)); //set the icon of the button
        b.setBackground(Color.YELLOW); //set the background of the color to be yellow
        b.setEnabled(false); // set enabled to false to not be able to click on it once more
    }

    private boolean containsFlag(Button button) {
        for (Flag flag : flags) // loop through the flags ArrayList
            if (button.getPoint().equals(flag.getPoint())) // if there is a flag (button location equals the flag location)
                return true;
        return false;
    }

    private void openNeighbors(Point point, ArrayList<Point> points) throws IOException {
        int surroundings = getSurroundingsMines(point); // gets the number of surrounding mines of the button location
        openButton(surroundings, board.getButton(point.getRow(), point.getColumn())); // open the selected button

        for (Point p : points) { // loop through the points ArrayList
            if (containsMine(p) || board.getButton(p.getRow(), p.getColumn()).getOpened()) // if the button at the point contains a mine or is opened, we continue to the next point
                continue;
            surroundings = getSurroundingsMines(p); // gets the surroundings of the current point
            if (surroundings != 0) { // if the surroundings doesn't equal zero
                openButton(surroundings, board.getButton(p.getRow(), p.getColumn())); // we open the button and continue
                continue;
            }
            openNeighbors(p, getSurroundingPoints(p)); // if the surroundings equals zero, we recursively open the neighbors of the button
        }

    }

    private int getSurroundingsMines(Point point) {
        int surrounding = 0;
        ArrayList<Point> points = getSurroundingPoints(point);

        for (Point p : points) // loop through all the points around the specific point
            for (Mine m : mines) // loop through all the mines in the board
                if (p.equals(m.getPoint())) // if the point and the mine share the same location
                    surrounding++; // increase the surrounding if one of the points containing a mine

        return surrounding;
    }

    private ArrayList<Point> getSurroundingPoints(Point point) {
        int row = point.getRow();
        int column = point.getColumn();
        ArrayList<Point> points = new ArrayList<>();
        ArrayList<Point> pointsToRemove = new ArrayList<>();

        // add all the surrounding points
        points.add(new Point(row - 1, column - 1));
        points.add(new Point(row - 1, column));
        points.add(new Point(row, column - 1));
        points.add(new Point(row + 1, column + 1));
        points.add(new Point(row + 1, column));
        points.add(new Point(row, column + 1));
        points.add(new Point(row + 1, column - 1));
        points.add(new Point(row - 1, column + 1));

        for (Point p : points) // you can't remove directly from the arrayList as ConcurrentModificationException is triggered
            if (p.isOutOfBounds()) // checking if all points are in valid locations
                pointsToRemove.add(p); // we add the invalid points to another arrayList to be removed later

        points.removeAll(pointsToRemove); // remove all invalid points at once after iterating

        return points;
    }

    private boolean containsMine(Point point) {
        for (int i = 0; i < mines.length; i++)
            if (mines[i].getPoint().equals(point))
                return true;
        return false;
    }

    private boolean containsMine() {
        return containsMine(new Point(button.getRow(), button.getColumn()));
    }
}
