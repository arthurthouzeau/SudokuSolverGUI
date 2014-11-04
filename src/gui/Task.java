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
public class Task implements Callable<SudokuBoard> {

    /**
     * Returns the new randomly generated SudokuBoard
     * 
     * @return the new randomly generated SudokuBoard
     */
    @Override
    public SudokuBoard call() throws Exception {
        RandomGenerator sg = new RandomGenerator();
        return sg;
    }
}