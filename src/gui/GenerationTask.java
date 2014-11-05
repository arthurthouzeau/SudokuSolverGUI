package gui;

import java.util.concurrent.Callable;

import model.SudokuBoard;
import model.RandomGenerator;

/**
 * Implementation of the Callable interface to control
 * the execution of the random generation of Sudokus.
 * 
 * @author Arthur Thouzeau
 * @version 1.0
 */
public class GenerationTask implements Callable<SudokuBoard> {
    private final int size;
    
    /**
     * Initializes the task to generate a SudokuBoard.
     * 
     * @param size the size of the Sudokuboard to generate (number of rows)
     */
    public GenerationTask (int size) {
        this.size = size;
    }
    
    /**
     * Returns the new randomly generated SudokuBoard
     * 
     * @return the new randomly generated SudokuBoard
     */
    @Override
    public SudokuBoard call() throws Exception {
        RandomGenerator sg = new RandomGenerator(size);
        return sg;
    }
}