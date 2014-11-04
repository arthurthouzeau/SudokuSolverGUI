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
    final static int SIZE = 9;

    /**
     * Initializes a new random SudokuBoard with a unique solution.
     */
    public RandomGenerator() {
        super(SIZE);

        LinkedList<Integer> cells = new LinkedList<Integer>();
        LinkedList<Integer> cellsNotSolution = new LinkedList<Integer>();
        LinkedList<Integer> trialList = new LinkedList<Integer>();
        int currentCellRank;
        int[] currentCellCoord = new int[2];
        int currentTrialValue;
        int currentTrialIndex;

        /*
         * Part I - Generate a complete random grid respecting Sudoku rules
         */
        // In order to reduce computation difficulty for the Backtracking
        // method we first randomly fill the first row
        for (int i = 1; i < size + 1; i++)
            trialList.add(i);
        Collections.shuffle(trialList);
        for (int j = 0; j < size; j++)
            this.set(trialList.remove(), 0, j);

        // Then randomly go through each of the empty cells
        for (int i = 1; i < size + 1; i++)
            trialList.add(i);
        for (int i = size; i < size * size; i++)
            cells.add(i);
        Collections.shuffle(cells);
        while (!cells.isEmpty()) {
            currentCellRank = cells.remove();
            currentCellCoord[0] = currentCellRank / size;
            currentCellCoord[1] = currentCellRank % size;

            // For each one, randomly find a valid number to go into it
            Collections.shuffle(trialList);
            currentTrialIndex = 0;
            currentTrialValue = trialList.get(currentTrialIndex);
            while (!this.isNumValid(currentCellCoord, currentTrialValue)) {
                currentTrialIndex++;
                currentTrialValue = trialList.get(currentTrialIndex);
            }
            
            // Set the value of the cell to the valid number found
            this.set(currentTrialValue, currentCellCoord[0],
                    currentCellCoord[1]);

            // Check if the new board has a valid solution,
            // if not, remove the number added previously and
            // start the process again with a new random cell.
            RecursiveSolver rs = new RecursiveSolver(this);
            if (!rs.solve()) {
                this.set(0, currentCellCoord[0], currentCellCoord[1]);
                cellsNotSolution.add(currentCellRank);
            } else {
                while (!cellsNotSolution.isEmpty()) {
                    cells.add(cellsNotSolution.remove());
                }
                Collections.shuffle(cells);
            }
        }

        /*
         * Part II - From this random complete grid, create a board with a
         * unique solution such that it is not possible to remove any more
         * numbers without destroying the uniqueness of the solution.
         */
        for (int i = 0; i < size * size; i++) {
            cells.add(i);
        }
        Collections.shuffle(cells);
        int row;
        int col;
        int temp;
        while (!cells.isEmpty()) {
            currentCellRank = cells.remove();
            row = currentCellRank / size;
            col = currentCellRank % size;
            temp = this.get(row, col);
            this.set(0, row, col);
            RecursiveSolver rc = new RecursiveSolver(this);
            if (rc.countSolutions() > 1)
                this.set(temp, row, col);
        }
    }
}
