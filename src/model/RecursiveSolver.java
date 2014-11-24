package model;

/**
 * This class contains the routines to solve a Sudoku puzzle using 
 * the Recursive Backtracking method.
 * 
 * @author Arthur Thouzeau
 * @version 1.0
 */
public class RecursiveSolver {
    private SudokuBoard sb;

    /**
     * Initializes a new Recursive Solver.
     * 
     * @param board the Sudoku to solve
     */
    public RecursiveSolver(SudokuBoard board) {
        sb = new SudokuBoard(board.SIZE);
        for (int i = 0; i < sb.SIZE; i++) {
            for (int j = 0; j < sb.SIZE; j++) {
                sb.set(board.get(i, j), i, j);
            }
        }
    }

    /**
     * Returns the Sudoku board.
     * 
     * @return the Sudoku board
     */
    public SudokuBoard getBoard() {
        return sb;
    }

    /**
     * Enumerates all unassigned cells of a Sudoku board.
     * 
     * @return the list of unassigned cells in a Sudoku board as a 2D array
     */
    private int[][] listEmptyCells() {
        int nbEmpty = 0;
        int[][] emptyCells;
        int k = 0;

        for (int i = 0; i < sb.SIZE; i++) {
            for (int j = 0; j < sb.SIZE; j++) {
                if (sb.get(i, j) == 0)
                    nbEmpty++;
            }
        }
        emptyCells = new int[nbEmpty][2];

        for (int i = 0; i < sb.SIZE; i++) {
            for (int j = 0; j < sb.SIZE; j++) {
                if (sb.get(i, j) == 0) {
                    emptyCells[k][0] = i;
                    emptyCells[k][1] = j;
                    k++;
                }
            }
        }
        return emptyCells;
    }

    /**
     * Creates the list of unassigned cells and starts the recursive procedure
     * with the first one (calls the recursive method {@link #solveBacktrack}).
     * 
     * @return <code>true</code> if the sudoku has a solution;
     *         <code>false</code> otherwise
     */
    public boolean solve() {
        int[][] emptyCells = listEmptyCells();
        return solveBacktrack(emptyCells, 0);
    }

    /**
     * Recursive Backtracking function carrying out trial and error,
     * trying to find a compatible value for each unassigned cell.
     * In case of multiple solutions, this method will stop at the 
     * first one discovered.
     * 
     * @param emptyCells the list of unassigned cells
     * @param ind the index indicating where the algorithm is at in the list of
     *            unassigned cells
     * @return <code>true</code> if the sudoku has a solution;
     *         <code>false</code> otherwise
     */
    private boolean solveBacktrack(int[][] emptyCells, int ind) {

        if (ind == emptyCells.length)
            return true;

        for (int num = 1; num <= sb.SIZE; num++) {
            if (sb.isNumValid(emptyCells[ind], num)) {

                // If "num" doesn't break any rule, insert it in the board and
                // move to the next unassigned cell.
                sb.set(num, emptyCells[ind][0], emptyCells[ind][1]);

                if (solveBacktrack(emptyCells, ind + 1))
                    return true;

                // If "num" finally doesn't lead to a solution, remove it and
                // try with "num+1".
                sb.set(0, emptyCells[ind][0], emptyCells[ind][1]);
            }
        }
        return false;
    }
    
    /**
     * Creates the list of unassigned cells and starts the recursive procedure
     * with the first one (calls the recursive method {@link #countSolutionsBacktrack}).
     * 
     * @return <code>0</code> if the sudoku has no solution;
     *         <code>1</code> if it has a unique solution;
     *         <code>2</code> if it has multiple solutions
     */
    public int countSolutions() {
        int[][] emptyCells = listEmptyCells();
        return countSolutionsBacktrack(emptyCells, 0, false);
    }
    
    /**
     * Recursive Backtracking function carrying out trial and error, trying to
     * find a compatible value for each unassigned cell. In case of multiple
     * solutions, this method will stop at the second one discovered.
     * 
     * @param emptyCells the list of unassigned cells
     * @param ind the index indicating where the algorithm is at in the list of
     *            unassigned cells
     * @param flag the parameter indicating whether a first solution has already
     *            been discovered
     * @return <code>0</code> if the sudoku has no solution; 
     *         <code>1</code> if it has a unique solution; 
     *         <code>2</code> if it has multiple solutions
     */
    private int countSolutionsBacktrack(int[][] emptyCells, int ind, boolean flag) {
        int count = 0;

        if (ind == emptyCells.length)
            return 1;

        for (int num = 1; num <= sb.SIZE; num++) {
            
            // Check if we have already found 2 solutions
            if (count > 1 || (flag && count == 1))
                return count;
            
            if (sb.isNumValid(emptyCells[ind], num)) {

                // If "trial" doesn't break any rule, insert it in the board and
                // move to the next unassigned cell.
                sb.set(num, emptyCells[ind][0], emptyCells[ind][1]);
                
                if (count == 1) { // just found first solution, turn flag on
                    count += countSolutionsBacktrack(emptyCells, ind + 1,true);
                } else {
                    count += countSolutionsBacktrack(emptyCells, ind + 1, flag);
                }

                sb.set(0, emptyCells[ind][0], emptyCells[ind][1]);
            }
        }
        return count;
    }
}