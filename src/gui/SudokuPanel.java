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
    private final int SIZE;
    private final int SQRTSIZE;
    private final Font CELL_FONT = new Font("Arial", Font.PLAIN, 20);
    private final Color SOLVED_COLOR = new Color(129, 239, 97);
    private JLabel[][] cells;

    /**
     * Initializes a new SudokuPanel. It adds all the JLabels and sets up the
     * borders.
     */
    public SudokuPanel(int size) {
        this.SIZE = size;
        this.SQRTSIZE = (int) Math.sqrt(SIZE);
        cells = new JLabel[SIZE][SIZE];
        initLabels();
        setLayout(new GridLayout(SQRTSIZE, SQRTSIZE));
        for (int k = 0; k < SIZE; k++) {
            JPanel box = new JPanel(new GridLayout(SQRTSIZE, SQRTSIZE));
            box.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            int boxRow = (k / SQRTSIZE) * SQRTSIZE;
            int boxCol = (k % SQRTSIZE) * SQRTSIZE;
            for (int i = 0; i < SQRTSIZE; i++) {
                for (int j = 0; j < SQRTSIZE; j++) {
                    box.add(cells[boxRow + i][boxCol + j]);
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
                cells[i][j] = new JLabel();
                cells[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                cells[i][j].setOpaque(true);
                cells[i][j].setBackground(Color.WHITE);
                cells[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                cells[i][j].setFont(CELL_FONT);
            }
        }
    }

    /**
     * Reset the graphical Sudoku grid when importing or generating a new
     * Sudoku.
     * 
     * @param sb the new Sudoku to solve
     */
    public void updateCellsNew(SudokuBoard sb) {
        int value;
        JLabel cell;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                value = sb.get(i, j);
                cell = cells[i][j];
                if (value == 0) {
                    cell.setText("");
                    cell.setBackground(Color.LIGHT_GRAY);
                } else {
                    cell.setText(String.valueOf(value));
                    cell.setBackground(Color.WHITE);
                }
            }
        }
    }
    
    /**
     * Repaints the graphical Sudoku grid when a solution is found.
     * 
     * @param sb the Sudoku results
     */
    public void updateCellsResults(SudokuBoard sb) {
        JLabel cell;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                cell = cells[i][j];
                if (cell.getText().equals(""))
                    cell.setBackground(SOLVED_COLOR);
                cell.setText(String.valueOf(sb.get(i, j)));
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
                String label = cells[i][j].getText();
                int value = label.equals("") ? 0 : Integer.parseInt(label);
                sb.set(value, i, j);
                ;
            }
        }
        return sb;
    }
}
