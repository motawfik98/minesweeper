package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Frame extends JFrame implements ActionListener {
    private JPanel pnlLabels = new JPanel();
    private JLabel lblTime = new JLabel();
    private JLabel lblMinesLeft = new JLabel();
    private JMenuBar bar = new JMenuBar();
    private JMenu mnuStart = new JMenu("Start");
    private JMenuItem mniNew = new JMenuItem("New Game");
    private Object[] newGameOptions = {"Beginner", "Intermediate", "Expert"};
    private String level;


    public Frame(String level) {
        this.level = level;
        initLevel();
        init();
    }

    private void init() {
        int boardSize = Constants.currentSquares;
        int minesLeft = Constants.currentMines;
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        MinesPanel pnlMain = new MinesPanel(lblTime, lblMinesLeft);
        pnlMain.setLayout(new GridLayout(boardSize, boardSize));

        lblTime.setPreferredSize(new Dimension(150, 20));
        int time = 0;
        lblTime.setText("Time = " + time);
        lblMinesLeft.setPreferredSize(new Dimension(150, 20));
        lblMinesLeft.setText("Mines Left = " + minesLeft);

        pnlLabels.add(lblTime);
        pnlLabels.add(lblMinesLeft);
        pnlLabels.setBackground(Color.RED);

        mniNew.addActionListener(this);
        mnuStart.add(mniNew);
        bar.add(mnuStart);
        setJMenuBar(bar);

        Container c = getContentPane();
        c.add(pnlMain);
        c.add(pnlLabels, BorderLayout.SOUTH);
        setResizable(false);
        setLocation(300, 15);
        setTitle("Minesweeper");


        pack();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == mniNew) {
            int result = JOptionPane.showOptionDialog(this, "You must select a level to start the game!",
                    "Choose level", JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, newGameOptions, newGameOptions[1]);
            if (result == JOptionPane.YES_OPTION) {
                this.dispose();
                Frame frame = new Frame("beginner");
                frame.setVisible(true);
            } else if (result == JOptionPane.NO_OPTION) {
                this.dispose();
                Frame frame = new Frame("intermediate");
                frame.setVisible(true);
            } else if (result == JOptionPane.CANCEL_OPTION) {
                this.dispose();
                Frame frame = new Frame("advanced");
                frame.setVisible(true);
            }
        }
    }

    private void initLevel() {
        if (level.equalsIgnoreCase("beginner")) {
            Constants.currentSquareLength = Constants.SQUARE_LENGTH_BEGINNER;
            Constants.currentMines = Constants.MINES_BEGINNER;
            Constants.currentSquares = Constants.BOARD_BEGINNER;
        } else if (level.equalsIgnoreCase("intermediate")) {
            Constants.currentSquareLength = Constants.SQUARE_LENGTH_INTERMEDIATE;
            Constants.currentMines = Constants.MINES_INTERMEDIATE;
            Constants.currentSquares = Constants.BOARD_INTERMEDIATE;
        } else {
            Constants.currentSquareLength = Constants.SQUARE_LENGTH_ADVANCED;
            Constants.currentMines = Constants.MINES_ADVANCED;
            Constants.currentSquares = Constants.BOARD_ADVANCED;
        }
    }
}
