package model;

import java.util.Collections;
import java.util.LinkedList;

/**
 * This class is a subclass of SudokuBoard. It contains a constructor to
 * generate random Sudoku grids with a unique solution.
 * 
 * @author Arthur Thouzeau
 * @version 1.0
 */
public class RandomGenerator extends SudokuBoard {

    /**
     * Initializes a new random SudokuBoard with a unique solution.
     * 
     * @param size the size of the Sudokuboard to generate (number of rows)
     */
    @SuppressWarnings("unchecked")
    public RandomGenerator(int size) {
        super(size);
        
        LinkedList<Integer>[] available = new LinkedList[SIZE * SIZE];
        int cell = 0;
        int[] cellCoord = new int[2];
        int num;

        // Initialization of the "available" array used to keep
        // track of what numbers we can still use in each cell
        for (int i = 0; i < available.length; i++) {
            available[i] = new LinkedList<Integer>();
            for (int j = 1; j < SIZE + 1; j++) {
                available[i].add(j);
            }
        }
        
        /*
         * Part I - Generate a complete random grid respecting Sudoku rules
         */
        while (cell < SIZE * SIZE) {
            cellCoord[0] = cell / SIZE;
            cellCoord[1] = cell % SIZE;
            
            if (!available[cell].isEmpty()) {
                Collections.shuffle(available[cell]);
                num = available[cell].remove();
                if (isNumValid(cellCoord, num)) {
                    set(num, cellCoord[0], cellCoord[1]);
                    cell++;
                }
            } else {
                for (int i = 1; i < SIZE + 1; i++) {
                    available[cell].add(i);
                }
                cell--;
                set(0, cell / SIZE, cell % SIZE);
            }
        }
        
        /*
         * Part II - From this random complete grid, create a board with a
         * unique solution such that it is not possible to remove any more
         * numbers without destroying the uniqueness of the solution.
         */
        LinkedList<Integer> cells = new LinkedList<Integer>();
        for (int i = 0; i < SIZE * SIZE; i++) {
            cells.add(i);
        }
        Collections.shuffle(cells);
        int row;
        int col;
        int temp;
        while (!cells.isEmpty()) {
            cell = cells.remove();
            row = cell / SIZE;
            col = cell % SIZE;
            temp = get(row, col);
            set(0, row, col);
            RecursiveSolver rc = new RecursiveSolver(this);
            if (rc.countSolutions() > 1)
                set(temp, row, col);
        }     
    }
}
