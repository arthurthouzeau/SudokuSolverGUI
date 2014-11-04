package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import model.SudokuBoard;

/**
 * 
 * This class represents a graphical Sudoku board as a two-dimensional array of
 * <code>JLabel</code>. It provides methods to initialize the labels and update
 * them.
 * 
 * @author Arthur Thouzeau
 * @version 1.0
 */
@SuppressWarnings("serial")
public class SudokuPanel extends JPanel {
    final int SIZE = 9;
    final int SQRTSIZE = 3;
    private static Font font = new Font("Arial", Font.PLAIN, 20);
    private Color solvedCellsColor = new Color(129, 239, 97);
    private JLabel[][] labels = new JLabel[SIZE][SIZE];

    /**
     * Initializes a new SudokuPanel. It adds all the JLabels and sets up the
     * borders.
     */
    public SudokuPanel() {
        initLabels();
        setLayout(new GridLayout(SQRTSIZE, SQRTSIZE));
        for (int k = 0; k < SIZE; k++) {
            JPanel box = new JPanel(new GridLayout(SQRTSIZE, SQRTSIZE));
            box.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            int boxRow = (k / SQRTSIZE) * SQRTSIZE;
            int boxCol = (k % SQRTSIZE) * SQRTSIZE;
            for (int i = 0; i < SQRTSIZE; i++) {
                for (int j = 0; j < SQRTSIZE; j++) {
                    box.add(labels[boxRow + i][boxCol + j]);
                }
            }
            add(box);
        }
    }

    /**
     * Initializes the JLabels. Sets the alignment, background color, font...
     */
    private void initLabels() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                labels[i][j] = new JLabel();
                labels[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                labels[i][j].setOpaque(true);
                labels[i][j].setBackground(Color.WHITE);
                labels[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                labels[i][j].setFont(font);
            }
        }
    }

    /**
     * Reset the graphical Sudoku grid when importing or generating a new
     * Sudoku.
     * 
     * @param sb the new Sudoku to solve
     */
    public void updateLabelsNew(SudokuBoard sb) {
        int value;
        JLabel l;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                value = sb.get(i, j);
                l = labels[i][j];
                if (value == 0) {
                    l.setText("");
                    l.setBackground(Color.LIGHT_GRAY);
                } else {
                    l.setText(String.valueOf(value));
                    l.setBackground(Color.WHITE);
                }
            }
        }
    }
    
    /**
     * Repaints the graphical Sudoku grid when a solution is found.
     * 
     * @param sb the Sudoku results
     */
    public void updateLabelsResults(SudokuBoard sb) {
        JLabel l;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                l = labels[i][j];
                if (l.getText().equals(""))
                    l.setBackground(solvedCellsColor);
                l.setText(String.valueOf(sb.get(i, j)));
            }
        }
    }
    
    /**
     * Returns the current Sudoku grid displayed.
     * 
     * @return the current Sudoku grid displayed
     */
    public SudokuBoard getSudokuBoard() {
        SudokuBoard sb = new SudokuBoard(SIZE);
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                String label = labels[i][j].getText();
                int value = label.equals("") ? 0 : Integer.parseInt(label);
                sb.set(value, i, j);
                ;
            }
        }
        return sb;
    }
}
